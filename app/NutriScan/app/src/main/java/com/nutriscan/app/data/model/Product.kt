package com.nutriscan.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Resposta da API ao buscar produto por código de barras.
 * status = 1 significa produto encontrado, 0 significa não encontrado.
 */
data class ProductResponse(
    val code: String? = null,
    val status: Int = 0,
    @SerializedName("status_verbose") val statusVerbose: String? = null,
    val product: Product? = null
)

/**
 * Resposta da API ao buscar produtos por texto (search.pl).
 */
data class SearchResponse(
    val count: Int = 0,
    val page: Int = 1,
    @SerializedName("page_count") val pageCount: Int = 0,
    @SerializedName("page_size") val pageSize: Int = 0,
    val products: List<Product> = emptyList(),
    val skip: Int = 0
)

/**
 * Modelo principal de produto retornado pela API Open Food Facts.
 *
 * Campos comuns a todas as telas:
 * - code: código de barras (EAN)
 * - productName, brands, imageUrl: info básica exibida em cards
 *
 * Campos usados na tela de detalhe:
 * - ingredientsText: lista de ingredientes
 * - nutriscoreGrade: nota A-E do Nutri-Score (pode vir "unknown")
 * - novaGroup: nível de processamento (1-4, pode vir como Int, Double ou String)
 * - categories: categorias separadas por vírgula (usado pra buscar alternativas)
 * - nutriments: dados nutricionais por 100g
 */
data class Product(
    val code: String? = null,
    @SerializedName("product_name") val productName: String? = null,
    val brands: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("ingredients_text") val ingredientsText: String? = null,
    @SerializedName("nutriscore_grade") private val _nutriscoreGrade: String? = null,
    @SerializedName("nova_group") private val _novaGroup: Any? = null,
    val categories: String? = null,
    @SerializedName("nutrition_data_per") val nutritionDataPer: String? = null,
    val nutriments: Nutriments? = null
) {
    /**
     * Getter que filtra o valor "unknown" retornado pela API.
     * A API retorna "unknown" quando o Nutri-Score não foi calculado.
     */
    val nutriscoreGrade: String?
        get() = _nutriscoreGrade?.takeIf { it != "unknown" && it.isNotBlank() }

    /**
     * Getter que normaliza o tipo de novaGroup.
     * A API é inconsistente: pode retornar Int, Double (ex: 4.0) ou String.
     */
    val novaGroup: Int?
        get() = when (_novaGroup) {
            is Number -> _novaGroup.toInt()
            is String -> _novaGroup.toIntOrNull()
            else -> null
        }
}

/**
 * Dados nutricionais por porção (geralmente 100g).
 * Todos os valores são nullable pois a API nem sempre retorna todos os campos.
 */
data class Nutriments(
    @SerializedName("energy-kcal_100g") val energyKcal100g: Double? = null,
    @SerializedName("energy-kj_100g") val energyKj100g: Double? = null,
    @SerializedName("carbohydrates_100g") val carbohydrates100g: Double? = null,
    @SerializedName("sugars_100g") val sugars100g: Double? = null,
    @SerializedName("added-sugars_100g") val addedSugars100g: Double? = null,
    @SerializedName("fat_100g") val fat100g: Double? = null,
    @SerializedName("saturated-fat_100g") val saturatedFat100g: Double? = null,
    @SerializedName("fiber_100g") val fiber100g: Double? = null,
    @SerializedName("proteins_100g") val proteins100g: Double? = null,
    @SerializedName("sodium_100g") val sodium100g: Double? = null,
    @SerializedName("salt_100g") val salt100g: Double? = null
)
