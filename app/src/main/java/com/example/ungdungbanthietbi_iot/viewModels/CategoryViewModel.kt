package com.example.ungdungbanthietbi_iot.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Category
import com.example.ungdungbanthietbi_iot.models.CategoryResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoryViewModel: ViewModel() {
    // Private MutableStateFlow to hold the list of categories
    private val _listCategories = MutableStateFlow<List<Category>>(emptyList())
    // Public StateFlow for UI to observe
    val listCategories: StateFlow<List<Category>> = _listCategories.asStateFlow()
    // Optional: StateFlow for error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    fun getCategories() {
        viewModelScope.launch {
            try {
                val response: Response<CategoryResponse> = RetrofitClient.categoryAPIService.getCategories()
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.status_code == 200) {
                        _listCategories.value = apiResponse.data.categories
                        _errorMessage.value = null // Xóa thông báo lỗi nếu thành công
                    } else {
                        _listCategories.value = emptyList()
                        _errorMessage.value = "API error: Invalid response data"
                    }
                } else {
                    _listCategories.value = emptyList()
                    _errorMessage.value = "API error: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                _listCategories.value = emptyList()
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }
}