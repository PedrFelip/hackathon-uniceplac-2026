package com.nutriscan.app.data.api

import com.nutriscan.app.data.model.ProductResponse
import com.nutriscan.app.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface Retrofit para a API Open Food Facts.
 * URL base definida em RetrofitClient.
 */
interface OpenFoodFactsApi {

    companion object {
        /**
         * Campos retornados na busca por barcode.
         * Limitamos os campos para reduzir o tamanho da resposta JSON.
         */
        private const val FILTROS = "product_name,brands,image_url," +
                "ingredients_text,nutriscore_grade,nova_group,categories," +
                "nutrition_data_per,nutriments"
    }

    /**
     * Busca produto pelo código de barras (EAN).
     * Usa o endpoint v2 que retorna dados completos com os campos definidos em FILTROS.
     */
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = FILTROS
    ): ProductResponse

    /**
     * Busca produtos por texto livre usando o endpoint legado search.pl (v1).
     * Usado tanto para a tela de pesquisa quanto para buscar alternativas.
     *
     * @param query Termo de busca (nome do produto ou palavras-chave)
     * @param fields Campos retornados (inclui nutriscore_grade e nova_group para recomendações)
     * @param lc Idioma (pt = português)
     */
    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") query: String,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
        @Query("fields") fields: String = "code,product_name,brands,image_url,nutriscore_grade,nova_group",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
        @Query("lc") lc: String = "pt",
    ): SearchResponse
}
