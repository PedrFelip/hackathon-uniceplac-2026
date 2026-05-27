package com.nutriscan.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriscan.app.data.model.Product
import com.nutriscan.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado da tela de busca por texto.
 */
data class SearchUiState(
    val query: String = "",
    val results: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel da tela de pesquisa de produtos.
 * Gerencia o texto da busca e dispara a requisição quando o usuário confirma.
 */
class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    /** Atualiza o texto digitado pelo usuário. */
    fun updateQuery(query: String) {
        _searchState.value = _searchState.value.copy(query = query)
    }

    /** Dispara a busca na API pelo texto digitado. */
    fun searchProducts() {
        val query = _searchState.value.query.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(isLoading = true, errorMessage = null)
            repository.searchProducts(query)
                .onSuccess { products ->
                    _searchState.value = _searchState.value.copy(
                        results = products,
                        isLoading = false,
                        errorMessage = if (products.isEmpty()) "Nenhum produto encontrado" else null
                    )
                }
                .onFailure { e ->
                    _searchState.value = _searchState.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erro ao buscar produtos"
                    )
                }
        }
    }
}
