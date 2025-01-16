package com.example.ungdungbanthietbi_iot.screen.rating

import android.icu.text.SimpleDateFormat
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import java.util.Locale

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
    ReviewListScreen(reviews = listReview, navController = navController, isUseful = isUseful, id.toInt())
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
fun ReviewListScreen(reviews: List<Review>, navController: NavController, isUseful: Boolean, id:Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
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
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { index ->
                ReviewCard(review = index, isUseful = isUseful, id)// Hiển thị từng bài đánh giá
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
fun ReviewCard(review: Review, isUseful:Boolean, id:Int) {
    var currentisUseful by remember { mutableStateOf(isUseful) } // Trạng thái đánh dấu hữu ích
    val customerViewModel: CustomerViewModel = viewModel()
    val listCustomer = customerViewModel.listCustomerReviewDevice

    LaunchedEffect(id) {
        customerViewModel.getCustomerReviewDeviceByIdDevice(id)
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
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
                //hình ảnh tạm
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(50.dp).padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
                for (customer in listCustomer){
                    if(customer.id == review.idCustomer){
                        Text(
                            text = "${customer.surname} ${customer.lastName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
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
        // Nội dung bình luận
        Text(
            text = review.comment,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
        // Ngày đánh giá
        Text(
            text = formatDate(review.created_at),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
    }
}


@Composable
fun formatDate(inputDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng từ API
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Định dạng đầu ra
        val date = inputFormat.parse(inputDate)
        date?.let { outputFormat.format(it) } ?: "Ngày không hợp lệ"
    } catch (e: Exception) {
        "Ngày không hợp lệ"
    }
}
