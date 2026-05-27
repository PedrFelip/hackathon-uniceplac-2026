package com.nutriscan.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriscan.app.data.model.Product
import com.nutriscan.app.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado da tela de detalhe do produto.
 * Carrega o produto pelo barcode e, em seguida, busca alternativas mais saudáveis.
 */
data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val alternatives: List<Product> = emptyList(),
    val isLoadingAlternatives: Boolean = false
)

/**
 * ViewModel da tela de detalhe do produto.
 *
 * Fluxo:
 * 1. loadProduct(barcode) → busca produto na API
 * 2. Após carregar, dispara loadAlternatives() automaticamente
 * 3. loadAlternatives() espera 1 segundo (delay) antes de buscar
 *    para evitar rate limit da API Open Food Facts
 */
class ProductDetailViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _state = MutableStateFlow(ProductDetailUiState())
    val state: StateFlow<ProductDetailUiState> = _state.asStateFlow()

    /** Carrega produto pelo código de barras e dispara busca de alternativas. */
    fun loadProduct(barcode: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, alternatives = emptyList())
            repository.getProductByBarcode(barcode)
                .onSuccess { product ->
                    _state.value = _state.value.copy(product = product, isLoading = false)
                    loadAlternatives(product)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar produto"
                    )
                }
        }
    }

    /**
     * Busca alternativas mais saudáveis com delay de 1s.
     * O delay evita rate limit ao fazer 2 requisições seguidas na API.
     */
    private fun loadAlternatives(product: Product) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingAlternatives = true)
            delay(1000)
            repository.getHealthierAlternatives(product)
                .onSuccess { alternatives ->
                    _state.value = _state.value.copy(alternatives = alternatives, isLoadingAlternatives = false)
                }
                .onFailure {
                    _state.value = _state.value.copy(isLoadingAlternatives = false)
                }
        }
    }
}
