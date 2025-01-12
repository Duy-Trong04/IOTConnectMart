package com.example.ungdungbanthietbi_iot.data.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel:ViewModel() {
    var listCart by mutableStateOf<List<Cart>>(emptyList())

    var cartUpdateResult by mutableStateOf("")
    var cartAddResult by mutableStateOf("")

    fun getCartByIdCustomer(idCustomer: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.cartAPIService.getCartByIdCustomer(idCustomer)
                }
                listCart = response.cart
            } catch (e: Exception) {
                Log.e("Cart Error", "Lỗi khi lấy giỏ hàng: ${e.message}")
            }
        }
    }

    // Cập nhật giỏ hàng đơn lẻ
    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.cartAPIService.updateCart(cart)
                }
                cartUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                cartUpdateResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("Cart Error", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    // Cập nhật tất cả giỏ hàng
    fun updateAllCart() {
        viewModelScope.launch {
            try {
                listCart.forEach { cart ->
                    val response = RetrofitClient.cartAPIService.updateCart(cart)
                    if (!response.success) {
                        // Nếu cập nhật thất bại, xử lý lỗi (hoặc thông báo lỗi)
                        Log.e("Cart Error", "Cập nhật thất bại cho sản phẩm: ${cart.idDevice}")
                    }
                }
                cartUpdateResult = "Cập nhật giỏ hàng thành công"
            } catch (e: Exception) {
                cartUpdateResult = "Lỗi khi cập nhật toàn bộ giỏ hàng: ${e.message}"
                Log.e("Cart Error", "Lỗi khi cập nhật toàn bộ giỏ hàng: ${e.message}")
            }
        }
    }

    //Xóa khỏi giỏ hàng
    fun deleteCart(id: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteRequest(id)
                val response = RetrofitClient.cartAPIService.deleteCart(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Gio Hang Deleted") {
                        // Cập nhật lại giỏ hàng trong ViewModel
                        listCart = listCart.filter { it.id != id }
                        Log.d("CartViewModel", "Giỏ hàng đã được xóa")
                    } else {
                        Log.e("CartViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("CartViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception: ${e.message}")
            }
        }
    }

    //Them vào giỏ hàng
    fun addToCart(cart:Cart) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.cartAPIService.addToCart(cart)
                cartAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddToCart", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}