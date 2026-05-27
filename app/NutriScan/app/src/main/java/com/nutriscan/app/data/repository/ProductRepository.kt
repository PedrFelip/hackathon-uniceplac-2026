package com.nutriscan.app.data.repository

import com.nutriscan.app.data.api.RetrofitClient
import com.nutriscan.app.data.model.Product
import com.nutriscan.app.data.model.ProductResponse

/**
 * Repositório central de acesso aos dados de produtos.
 * Encapsula as chamadas à API e transforma as respostas em Result<T>.
 */
class ProductRepository {
    private val api = RetrofitClient.api

    /**
     * Busca produtos por texto livre.
     * Usado na tela de pesquisa.
     */
    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = api.searchProducts(query)
            Result.success(response.products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca produto pelo código de barras.
     * Retorna dados completos (nutricionais, ingredientes, etc).
     * status = 1 na resposta significa produto encontrado.
     */
    suspend fun getProductByBarcode(barcode: String): Result<Product> {
        return try {
            val response: ProductResponse = api.getProductByBarcode(barcode)
            if (response.status == 1 && response.product != null) {
                val product = response.product
                // Garante que o code nunca seja nulo — usa o barcode da requisição como fallback
                if (product.code.isNullOrBlank()) {
                    val fixedProduct = product.copy(code = barcode)
                    Result.success(fixedProduct)
                } else {
                    Result.success(product)
                }
            } else {
                Result.failure(Exception("Produto não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca alternativas mais saudáveis para o produto dado.
     *
     * Estratégia:
     * 1. Se o produto tem categoria, usa a primeira categoria como termo de busca
     * 2. Se não tem categoria, extrai palavras-chave do nome do produto
     * 3. Filtra resultados com Nutri-Score melhor que o produto atual
     * 4. Ordena do melhor (A) para o pior e limita a 5 resultados
     *
     * @param product Produto atual para comparar
     * @return Lista de até 5 produtos com Nutri-Score melhor
     */
    suspend fun getHealthierAlternatives(product: Product): Result<List<Product>> {
        val currentGrade = product.nutriscoreGrade

        // Define o termo de busca: prioridade para categoria, senão usa palavras-chave do nome
        val query = if (!product.categories.isNullOrBlank()) {
            val category = product.categories.split(",").firstOrNull()?.trim() ?: ""
            if (category.isNotBlank()) category else extractKeywords(product.productName)
        } else {
            extractKeywords(product.productName)
        }

        if (query.isBlank()) return Result.success(emptyList())

        return try {
            val response = api.searchProducts(query = query, pageSize = 20)
            val alternatives = response.products
                .filter { it.code != product.code && it.code != null }
                .filter { isHealthier(it.nutriscoreGrade, currentGrade) }
                .sortedBy { nutriscoreRank(it.nutriscoreGrade) }
                .take(5)
            Result.success(alternatives)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Extrai as 3 palavras mais relevantes do nome do produto.
     * Remove stop words (preposições, artigos) e palavras muito curtas.
     */
    private fun extractKeywords(name: String?): String {
        if (name.isNullOrBlank()) return ""
        val stopWords = setOf("em","de","do","da","dos","das","no","na","com","para","por","sem","e","ou","en","la","el","le")
        return name.lowercase()
            .replace(",", " ")
            .split(Regex("\\s+"))
            .filter { it.length > 2 && it !in stopWords }
            .take(3)
            .joinToString(" ")
    }

    /** Converte a letra do Nutri-Score em número para comparação. A=0 (melhor), E=4 (pior). */
    private fun nutriscoreRank(grade: String?): Int = when (grade?.lowercase()) {
        "a" -> 0; "b" -> 1; "c" -> 2; "d" -> 3; "e" -> 4; else -> 5
    }

    /** Verifica se o candidato tem Nutri-Score melhor que o atual. */
    private fun isHealthier(candidate: String?, current: String?): Boolean {
        if (candidate == null) return false
        if (current == null) return true
        return nutriscoreRank(candidate) < nutriscoreRank(current)
    }
}
