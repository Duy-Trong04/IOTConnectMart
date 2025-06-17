package com.example.ungdungbanthietbi_iot.views.rating

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.models.Reviews
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.views.order_detail.calculateDaysSinceReceived
import com.example.ungdungbanthietbi_iot.utils.formatDateTimeZone

/** Giao diện màn hình lịch sử đánh giá (RatingHistoryScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình lịch sử đánh giá, sử dụng danh sách đánh giá mẫu (dummy data)
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingHistoryScreen(navController: NavController, idCustomer: String, username: String) {

    val reviewViewModel: ReviewViewModel = viewModel()
    val listReviews by reviewViewModel.listAllReviews.collectAsState()
    val isLoading by reviewViewModel.isLoading.collectAsState()
    LaunchedEffect (Unit){
        Log.d("RatingHistoryScreen", "Fetching reviews for idCustomer: $idCustomer")
        reviewViewModel.getAllReviews()
    }

    val reviewsOfCustomer = listReviews
        .filter { it.idCustomer == idCustomer }
    Log.d("RatingHistoryScreen", "Filtered reviews count: ${reviewsOfCustomer.size}")

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(text = "Đánh giá của tôi (${reviewsOfCustomer.size})",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .padding(it)
                .padding(3.dp),
        ) {
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                when {
                    isLoading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    reviewsOfCustomer.isEmpty() -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "Bạn chưa có đánh giá nào!",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.padding(8.dp)) {
                            items(reviewsOfCustomer) { review ->
                                ReviewItem(
                                    review = review,
                                    idCustomer = idCustomer,
                                    idDevice = review.idDevice,
                                    username = username,
                                    navController = navController
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Reviews, idCustomer: String, idDevice: String, username:String, navController: NavController) {

    val deviceViewModel: DeviceViewModel = viewModel()
    val device = deviceViewModel.deviceMap[idDevice] // Lấy thiết bị theo ID
    val customerName = "${review.surname} ${review.lastname}".trim()
    // Gọi API lấy device khi idDevice thay đổi
    LaunchedEffect(idDevice) {
        if (device == null) {
            deviceViewModel.getDeviceBySlug2(idDevice)
        }
    }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    // Tải hình ảnh
    LaunchedEffect(review) {
        bitmap = review.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }
    // Tính số ngày kể từ khi tạo đánh giá
    val daysSinceReviewCreated = remember(review.created_at) {
        calculateDaysSinceReceived(review.created_at)
    }

    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "${review.surname} ${review.lastname}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if(daysSinceReviewCreated <= 7){
                    Button(
                        onClick = {
                            navController.navigate(Screen.Update_Rating_Screen.route + "?idReview=${review.idReview}&idCustomer=${idCustomer}")
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFF5D9EFF)
                        )
                    ) {
                        Text(
                            text = "Chỉnh sửa",
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            // Hiển thị thanh đánh giá
            RatingBar(rating = review.rating)
            Spacer(modifier = Modifier.height(8.dp))
            // Bình luận
            review.comment?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Ngày đánh giá
            Text(
                text = formatDateTimeZone(review.created_at),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate(Screen.ProductDetailsScreen.route + "?id=${idDevice}&idCustomer=${idCustomer}&username=${username}")
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = customerName.ifEmpty { "Hình ảnh sản phẩm" },
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp),
                            contentScale = ContentScale.Fit
                        )
                    } ?: run {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    if (device != null) {
                        Text(
                            text = device.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

        }
    }
}

//Hiển thị thanh đánh giá sao dựa trên điểm đánh giá (rating) từ 1 đến 5.
@Composable
fun RatingBar(rating: Int) {
    // Hiển thị thanh đánh giá với các ngôi sao
    Row {
        for (i in 1..5) {
            Text(
                text = if (i <= rating) "★" else "☆",
                fontSize = 18.sp,
                color = if (i <= rating) Color(0xFFFBC02D) else Color(0xFFFBC02D)
            )
        }
    }
}
