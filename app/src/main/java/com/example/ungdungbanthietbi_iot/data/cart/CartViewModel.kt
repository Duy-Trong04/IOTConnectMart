
package com.example.ungdungbanthietbi_iot.data.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel : ViewModel() {
    private val cartDao = MyApplication.database.cartDao()
    var listCart by mutableStateOf<List<CartEntity>>(emptyList())
    var cartUpdateResult by mutableStateOf("")
    var cartAddResult by mutableStateOf("")

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
                getCartByIdCustomer(cart.idCustomer)
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
                getCartByIdCustomer(cart.idCustomer)
            } catch (e: Exception) {
                cartAddResult = "Error adding to cart: ${e.message}"
                Log.e("Cart Error", "Error adding to cart: ${e.message}")
            }
        }
    }
}