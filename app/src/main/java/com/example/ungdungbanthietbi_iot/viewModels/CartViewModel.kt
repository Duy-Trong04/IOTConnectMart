
package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.MyApplication
import com.example.ungdungbanthietbi_iot.api.AddCartRequest
import com.example.ungdungbanthietbi_iot.api.AddToCartResponse
import com.example.ungdungbanthietbi_iot.api.CartResponse
import com.example.ungdungbanthietbi_iot.api.ProductInCart
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.CartEntity
import com.example.ungdungbanthietbi_iot.models.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel : ViewModel() {
    private val cartDao = MyApplication.database.cartDao()
    var listCart by mutableStateOf<List<CartEntity>>(emptyList())
    private var cartUpdateResult by mutableStateOf("")
    private var cartAddResult by mutableStateOf("")

    fun getCartByIdCustomer(idCustomer: String) {
        viewModelScope.launch {
            try {
                listCart = withContext(Dispatchers.IO) {
                    cartDao.getCartByIdCustomer(idCustomer)
                }
            } catch (e: Exception) {
                listCart = emptyList()
                Log.e("Cart Error", "Error fetching cart: ${e.message}")
            }
        }
    }

    fun updateCart(cart: CartEntity) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    cartDao.updateCart(cart)
                }
                cartUpdateResult = "Update successful"
                getCartByIdCustomer(cart.customer_id)
            } catch (e: Exception) {
                cartUpdateResult = "Error updating cart: ${e.message}"
                Log.e("Cart Error", "Error updating cart: ${e.message}")
            }
        }
    }

    fun updateAllCart(idCustomer: String) {
        viewModelScope.launch {
            try {
                listCart.forEach { cart ->
                    withContext(Dispatchers.IO) {
                        cartDao.updateCart(cart)
                    }
                }
                cartUpdateResult = "All carts updated successfully"
                getCartByIdCustomer(idCustomer)
            } catch (e: Exception) {
                cartUpdateResult = "Error updating all carts: ${e.message}"
                Log.e("Cart Error", "Error updating all carts: ${e.message}")
            }
        }
    }

    fun deleteCart(id: Int, idCustomer: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    cartDao.deleteCartById(id)
                }
                getCartByIdCustomer(idCustomer)
            } catch (e: Exception) {
                Log.e("Cart Error", "Error deleting cart: ${e.message}")
            }
        }
    }

    fun deleteAllSelectedCarts(selectedIds: List<Int>, idCustomer: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    selectedIds.forEach { id ->
                        cartDao.deleteCartById(id)
                    }
                }
                getCartByIdCustomer(idCustomer)
            } catch (e: Exception) {
                Log.e("Cart Error", "Error deleting selected carts: ${e.message}")
            }
        }
    }

    fun addToCart(cart: CartEntity) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    cartDao.insertCart(cart)
                }
                cartAddResult = "Added successfully"
                getCartByIdCustomer(cart.customer_id)
            } catch (e: Exception) {
                cartAddResult = "Error adding to cart: ${e.message}"
                Log.e("Cart Error", "Error adding to cart: ${e.message}")
            }
        }
    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState

    private val _listProductCart = MutableStateFlow<List<ProductInCart>>(emptyList())
    val listProductCart: StateFlow<List<ProductInCart>> get() = _listProductCart.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    fun addCart(request: AddCartRequest) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitClient.cartAPIService.addToCart(request)
                _uiState.value = UiState.SuccessAdd(response)
                getCartProducts(request.customer_id)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun getCartProducts(customerId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.cartAPIService.getCartProducts(customerId)
                _uiState.value = UiState.SuccessGet(response)
                _listProductCart.value = response.data?: emptyList()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                _listProductCart.value = emptyList()
            }
        }
    }

    fun updateQuantity(request: AddCartRequest) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitClient.cartAPIService.updateQuantity(request)
                _uiState.value = UiState.SuccessAdd(response)
                getCartProducts(request.customer_id)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun removeCart(customer_id: String, product_id: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                try {
                    val response = RetrofitClient.cartAPIService.removeCart(customer_id, product_id)
                    getCartProducts(customer_id)
                    Log.d("Cart Remove", "Xóa thành công 1 sản phẩm")
                    _uiState.value = UiState.SuccessAdd(response)
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("Cart Error", "Error deleting cart: ${e.message}")
            }
        }
    }
    fun removeAllSelectedCarts(selectedIds: List<Int>, customer_id: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                try {
                    selectedIds.forEach { productId ->
                        val response = RetrofitClient.cartAPIService.removeCart(customer_id, productId)
                        _uiState.value = UiState.SuccessAdd(response)
                    }
                    getCartProducts(customer_id) // Cập nhật lại danh sách giỏ hàng sau khi xóa
                    Log.d("Cart Remove", "Xóa thành công nhiều sản phẩm")
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("Cart Error", "Error deleting selected carts: ${e.message}")
            }
        }
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class SuccessAdd(val response: AddToCartResponse) : UiState()
        data class SuccessGet(val response: CartResponse) : UiState()
        data class Error(val message: String) : UiState()
    }
}