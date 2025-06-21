package com.example.ungdungbanthietbi_iot.views.favorite

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.viewModels.LikedViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTienInt
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel

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
    username:String,
    password: String,
) {
    val likedViewModel: LikedViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    val listLiked by likedViewModel.listLiked.collectAsState()
    val isLoading by likedViewModel.isLoading.collectAsState()
    // Lấy dữ liệu và tính tổng tiền ban đầu
    LaunchedEffect(idCustomer) {
        likedViewModel.getLikedByIdCustomer(idCustomer)
    }
    //Đọc hình ảnh base64
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Yêu thích (${listLiked.size})",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
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
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if(isLoading){
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF5D9EFF)
                )
            }
        } else if(listLiked.isNotEmpty()) {
            // Danh sách sản phẩm
            LazyColumn(
                modifier = Modifier.padding(padding)
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                items(listLiked) { liked ->
                    // Tải hình ảnh
                    LaunchedEffect(liked) {
                        bitmap = liked.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(170.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            navController.navigate(Screen.ProductDetailsScreen.route + "?id=${liked.product_id}&idCustomer=${idCustomer}&username=${username}&password=$password")
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
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = liked.name.ifEmpty { "Hình ảnh sản phẩm" },
                                    modifier = Modifier
                                        .size(140.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } ?: run {
                                Image(
                                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                    contentDescription = "Product Image",
                                    modifier = Modifier
                                        .size(150.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                // Tên sản phẩm
                                Text(
                                    text = liked.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Giá sản phẩm
                                Text(
                                    text = "Giá: ${formatGiaTienInt(liked.selling_price)}",
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                            }
                            // Nút xóa sản phẩm
                            IconButton(onClick = {
                                likedViewModel.deleteLiked(idCustomer, liked.product_id)
                                likedViewModel.getLikedByIdCustomer(idCustomer)
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
        else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = "Danh sách yêu thích đang trống !",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
