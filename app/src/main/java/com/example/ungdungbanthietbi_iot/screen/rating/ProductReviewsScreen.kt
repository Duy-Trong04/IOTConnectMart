package com.example.ungdungbanthietbi_iot.screen.rating

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

// Định nghĩa dữ liệu cho một bài đánh giá của người dùng (fake)
data class UserReview(
    val userName: String,
    val userAvatarUrl: String,
    val rating: Int,
    val comment: String,
    val date: String,
    var usefulCount: Int = 0 // Đếm số lượng hữu ích
)

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
fun ProductReviewsScreen(navController: NavController) {
    // Danh sách các bài đánh giá mẫu
    val sampleReviews = listOf(
        UserReview(
            userName = "Nguyen Van A",
            userAvatarUrl = "https://example.com/user1.jpg",
            rating = 4,
            comment = "Sản phẩm rất tốt, chất lượng vượt mong đợi!",
            date = "2024-12-18",
            usefulCount = 8
        ),
        UserReview(
            userName = "Le Thi B",
            userAvatarUrl = "https://example.com/user2.jpg",
            rating = 3,
            comment = "Sản phẩm ổn, giao hàng hơi chậm.",
            date = "2024-12-17",
            usefulCount = 5
        ),
        UserReview(
            userName = "Le Thi C",
            userAvatarUrl = "https://example.com/user2.jpg",
            rating = 1,
            comment = "Sản phẩm ổn, giao hàng hơi chậm.",
            date = "2024-12-17",
            usefulCount = 2
        )
    )
    // Hiển thị màn hình danh sách đánh giá với các đánh giá mẫu
    ReviewListScreen(reviews = sampleReviews, navController = navController)
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
fun ReviewListScreen(reviews: List<UserReview>, navController: NavController) {
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
            items(reviews.size) { index ->
                ReviewCard(review = reviews[index])// Hiển thị từng bài đánh giá
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
fun ReviewCard(review: UserReview) {
    var isUseful by remember { mutableStateOf(false) } // Trạng thái đánh dấu hữu ích
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
                    text = review.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            // Nút "Hữu ích"
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = {
                    isUseful = !isUseful
                    if (isUseful) review.usefulCount++ else review.usefulCount--
                }) {
                    Icon(imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "Like",
                        tint = if(isUseful) Color(0xFFFBC02D) else Color.Gray
                    )
                }
                Text("Hữu ích (${review.usefulCount})", modifier = Modifier.padding(end = 5.dp))
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
            text = review.date,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}


