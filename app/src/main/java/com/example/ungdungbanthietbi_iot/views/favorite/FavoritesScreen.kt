package com.example.ungdungbanthietbi_iot.views.favorite

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.LikedViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/** Giao diện màn hình yêu thích (FavoritesScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 05/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình sản phẩm yêu thích, có xử lý check chọn nhiều sản phẩm để thêm vào giỏ hàng
 * hoặc xóa khỏi danh sách yêu thích.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    idCustomer: String,
    username: String
) {
    val likedViewModel: LikedViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val listLiked by remember { derivedStateOf { likedViewModel.listLiked } }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Hàm format tiền
    fun formatGiaTien(gia: Long): String {
        val formatter = DecimalFormat("#,###,###")
        return "${formatter.format(gia)}đ"
    }

    // Lấy dữ liệu ban đầu
    LaunchedEffect(idCustomer) {
        isLoading = true
        likedViewModel.getLikedProducts(idCustomer).onSuccess { response ->
            if (response.status_code == 200) {
                // listLiked được cập nhật trong ViewModel
            } else {
                errorMessage = "Lỗi khi tải danh sách yêu thích: Mã trạng thái ${response.status_code}"
            }
            isLoading = false
        }.onFailure { exception ->
            errorMessage = exception.message
            isLoading = false
        }
    }

    LaunchedEffect(listLiked) {
        Log.d("FavoritesScreen", "List Liked: $listLiked")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Yêu thích(${listLiked.size})",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                item {
                    Text(
                        text = "Đang tải...",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            } else if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage ?: "Đã xảy ra lỗi",
                        fontSize = 20.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            } else if (listLiked.isNotEmpty()) {
                items(listLiked) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(190.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            navController.navigate(
                                Screen.ProductDetailsScreen.route + "?id=${product.id}"
                            )
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            AsyncImage(
                                model = product.image ?: "",
                                contentDescription = product.name,
                                modifier = Modifier.size(150.dp),
                                contentScale = ContentScale.Fit,
//                                placeholder = painterResource(R.drawable.placeholder_image),
//                                error = painterResource(R.drawable.placeholder_image)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Giá: ${formatGiaTien(product.selling_price)}",
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    likedViewModel.deleteLikedProduct(idCustomer, product.id.toString())
                                        .onSuccess {
                                            // listLiked được cập nhật trong ViewModel
                                        }.onFailure { e ->
                                            errorMessage = "Lỗi khi xóa sản phẩm: ${e.message}"
                                        }
                                    isLoading = false
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Item"
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "Danh sách yêu thích đang trống!",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}