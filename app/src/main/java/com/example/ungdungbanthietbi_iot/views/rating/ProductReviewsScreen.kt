package com.example.ungdungbanthietbi_iot.views.rating

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.models.Reviews
import com.example.ungdungbanthietbi_iot.utils.formatDateTimeZone
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel

/** Giao diện màn hình danh sách đánh giá của sản phẩm (ProductReviewsScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 08/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình danh sách các bài đánh giá cho 1 sản phẩm
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun ProductReviewsScreen(
    navController: NavController,
    id:String,
    reviewViewModel: ReviewViewModel
) {
    val listReview by reviewViewModel.listReviews.collectAsState()
    val isLoading by reviewViewModel.isLoading.collectAsState()
    LaunchedEffect(id) {
        reviewViewModel.getReviewByIdDevice(id)
    }
    if(isLoading){
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF5D9EFF)
            )
        }
    } else if(listReview.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sản phẩm hiện chưa có đánh giá nào !",
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
    else {
        ReviewListScreen(reviews = listReview, navController = navController)
    }
}

/** Giao diện màn hình danh sách đánh giá của sản phẩm (ReviewListScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 08/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình danh sách các bài đánh giá cho 1 sản phẩm
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewListScreen(reviews: List<Reviews>, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                ) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { index ->
                ReviewCard(review = index)// Hiển thị từng bài đánh giá
            }
        }
    }
}

/** Giao diện hiển thị thông tin từng bài đánh giá (ReviewCard)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 08/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị thông tin từng bài đánh giá
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun ReviewCard(review: Reviews) {
    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var bitmapReviewImage by remember { mutableStateOf<Bitmap?>(null) }

    // Tải hình ảnh
    LaunchedEffect(review.customer_image) {
        bitmap = review.customer_image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }
    LaunchedEffect(review.image) {
        bitmapReviewImage = review.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }
    val customerName = "${review.surname} ${review.lastname}".trim()
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        // Hàng chứa ảnh sản phẩm và tên người dùng
        Row (
            modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5D9EFF)) // Màu nền xanh
                ) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = customerName.ifEmpty { "Hình ảnh sản phẩm" },
                            modifier = Modifier
                                .size(48.dp),
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .size(48.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${review.surname} ${review.lastname}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            }
        }
        // Thanh đánh giá sao
        Row(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        ){
            for (i in 1..5) {
                Text(
                    text = if (i <= review.rating) "★" else "☆",
                    fontSize = 18.sp,
                    color = if (i <= review.rating) Color(0xFFFBC02D) else Color(0xFFFBC02D)
                )
            }
        }
        if(review.comment != null) {
            // Nội dung bình luận
            Text(
                text = review.comment,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
        }
        if(review.image != null) {
            // Hình ảnh bình luận
            bitmapReviewImage?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 5.dp)
                ) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Hình ảnh bình luận",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        // Ngày đánh giá
        Text(
            text = formatDateTimeZone(review.created_at),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
    }
}
