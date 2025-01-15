package com.example.ungdungbanthietbi_iot.screen.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
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
import com.example.ungdungbanthietbi_iot.data.Product
import com.example.ungdungbanthietbi_iot.data.cart.CartViewModel
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.liked.Liked
import com.example.ungdungbanthietbi_iot.data.liked.LikedViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
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
    idCustomer:String,
    username:String
) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val likedViewModel:LikedViewModel = viewModel()

    val listLiked = likedViewModel.listLiked
    //Hàm format tiền
    fun formatGiaTien(gia: Double): String {
        val formatter = DecimalFormat("#,###,###")
        return "${formatter.format(gia)}đ"
    }
    // Lấy dữ liệu và tính tổng tiền ban đầu
    LaunchedEffect(idCustomer) {
        likedViewModel.getLikedByIdCustomer(idCustomer)
        deviceViewModel.getDeviceByLiked(idCustomer)
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
                    // Nút quay lại
                    IconButton(onClick = {
                        likedViewModel.updateAllLiked()
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
        // Danh sách sản phẩm
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(listLiked) { liked ->
                val device = deviceViewModel.listDeviceOfCustomer.find { it.idDevice == liked.idDevice }
                if(device != null){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(190.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.idDevice}")
                        }
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            AsyncImage(
                                model = device.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp),
                                contentScale = ContentScale.Fit
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                // Tên sản phẩm
                                Text(text = device.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                // Giá sản phẩm
                                Text(text = "Giá: ${formatGiaTien(device.sellingPrice)}", color = Color.Red)
                                Spacer(modifier = Modifier.height(8.dp))

                            }
                            // Nút xóa sản phẩm
                            IconButton(onClick = {
                                likedViewModel.deleteLiked(liked.id)
                                likedViewModel.listLiked = likedViewModel.listLiked.filter { it.id != liked.id }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Item"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
