package com.example.ungdungbanthietbi_iot.screen.rating

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Lớp dữ liệu đại diện cho một đánh giá(fake)
data class Review(
    val productName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

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
@Composable
fun RatingHistoryScreen(navController: NavController) {
    // Dữ liệu giả định để hiển thị
    val dummyReviews = listOf(
        Review("Đèn thông minh", 5, "Sản phẩm tốt, giao hàng nhanh.", "2024-12-01"),
        Review("Cảm biến báo cháy", 4, "Âm thanh hay, nhưng giao hơi chậm.", "2024-11-28"),
        Review("Cảm biến ánh sáng", 3, "Hàng ổn nhưng có vết trầy.", "2024-11-20"),
    )
    // Gọi hàm để hiển thị danh sách đánh giá
    ReviewHistoryScreen(reviews = dummyReviews, navController = navController)
}


/** Giao diện màn hình lịch sử đánh giá (ReviewHistoryScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình chính của lịch sử đánh giá.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewHistoryScreen(reviews: List<Review>, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Đánh giá của tôi", fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
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
    ) { paddingValues ->
        // Gọi hàm hiển thị danh sách đánh giá
        ReviewList(reviews = reviews, modifier = Modifier.padding(paddingValues))
    }
}

/** danh sách các đánh giá hoặc thông báo khi danh sách rỗng. (ReviewList)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị danh sách các đánh giá hoặc thông báo khi danh sách rỗng.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun ReviewList(reviews: List<Review>, modifier: Modifier = Modifier) {
    if (reviews.isEmpty()) {
        // Hiển thị thông báo nếu không có đánh giá nào
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                "Bạn chưa có đánh giá nào!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        // Hiển thị danh sách các đánh giá bằng LazyColumn
        LazyColumn(modifier = modifier.padding(8.dp)) {
            items(reviews) { review ->
                ReviewItem(review = review)
                Spacer(modifier = Modifier.height(8.dp))// Khoảng cách giữa các mục
            }
        }
    }
}

/** Hiển thị từng đánh giá dưới dạng một Card (ReviewItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị từng đánh giá
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun ReviewItem(review: Review) {
    // Card hiển thị từng đánh giá
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = review.productName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Hiển thị thanh đánh giá
            RatingBar(rating = review.rating)
            Spacer(modifier = Modifier.height(8.dp))
            // Bình luận
            Text(
                text = review.comment,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Ngày đánh giá
            Text(
                text = review.date,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
