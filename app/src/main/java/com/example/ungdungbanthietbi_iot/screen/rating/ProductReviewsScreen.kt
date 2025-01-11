package com.example.ungdungbanthietbi_iot.screen.rating

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review

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
    val listReview = reviewViewModel.listReview
    LaunchedEffect(id) {
        reviewViewModel.getReviewByIdDevice(id)
    }
    // Biến trạng thái để sản phẩm yêu thích không
    var isUseful by remember { mutableStateOf(false) }
    // Hiển thị màn hình danh sách đánh giá với các đánh giá mẫu
    ReviewListScreen(reviews = listReview, navController = navController, isUseful = isUseful)
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
fun ReviewListScreen(reviews: List<Review>, navController: NavController, isUseful: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá ",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                ) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { index ->
                ReviewCard(review = index, isUseful = isUseful)// Hiển thị từng bài đánh giá
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
fun ReviewCard(review: Review, isUseful:Boolean) {
    var currentisUseful by remember { mutableStateOf(isUseful) } // Trạng thái đánh dấu hữu ích
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Hàng chứa ảnh sản phẩm và tên người dùng
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                //hình ảnh tạm
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(50.dp).padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
                // Tên người dùng
                Text(
                    text = review.idCustomer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            // Nút "Hữu ích"
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = {
                    currentisUseful = !currentisUseful
                }) {
                    Icon(imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "Like",
                        tint = if(isUseful) Color(0xFFFBC02D) else Color.Gray
                    )
                }
                Text("Hữu ích", modifier = Modifier.padding(end = 5.dp))
            }
        }
        // Thanh đánh giá sao
        Row{
            for (i in 1..5) {
                Text(
                    text = if (i <= review.rating) "★" else "☆",
                    fontSize = 18.sp,
                    color = if (i <= review.rating) Color(0xFFFBC02D) else Color(0xFFFBC02D)
                )
            }
        }
        // Nội dung bình luận
        Text(
            text = review.comment,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        // Ngày đánh giá
        Text(
            text = review.created_at,
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}


