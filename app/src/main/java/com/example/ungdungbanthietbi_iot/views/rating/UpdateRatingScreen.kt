package com.example.ungdungbanthietbi_iot.views.rating

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.api.ReviewRequestUpdate
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import kotlinx.coroutines.delay

/** Giao diện màn hình đánh giá, bình luận (RatingScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị  đánh giá sản phẩm bằng cách chọn số sao, nhập nhận xét, thêm ảnh, và gửi đánh giá.
 * Nó bao gồm các tính năng như chế độ ẩn danh, thanh điều hướng, và giao diện cuộn để tổ chức các thành phần một cách hợp lý.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRatingScreen(navController: NavController, idReview: Int, idCustomer: String?){

    val reviewViewModel: ReviewViewModel = viewModel()
    val review = reviewViewModel.review
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    // Launcher để chọn ảnh từ thư viện
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImage = it
        }
    }

    var rating by remember { mutableIntStateOf(0) } // Lưu trạng thái số sao được đánh giá
    var comment by remember { mutableStateOf("") } // Lưu nội dung bình luận
    val error by reviewViewModel.error.collectAsState()
    val isLoading by reviewViewModel.isLoading.collectAsState()
    LaunchedEffect (idReview){
        reviewViewModel.getReviewById(idReview)
    }
    LaunchedEffect(review) {
        review?.let {
            rating = it.rating
            comment = it.comment ?: ""
            // If your model has an anonymous flag, populate it here
        }
    }
    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá sản phẩm",
                    fontWeight = FontWeight.Bold
                )},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                if (showSnackbar.value) {
                    LaunchedEffect(Unit) {
                        delay(3000) // Chờ 3000ms (3 giây)
                        showSnackbar.value = false // Đặt giá trị để tắt Snackbar
                    }
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        containerColor = Color.White,
                        contentColor = Color.Gray
                    ) {
                        Text(snackbarMessage.value)
                    }
                }
                if (error != null) {
                    Text(
                        text = "Lỗi: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Nút Gửi
                Button(
                    onClick = {
                        if (!isLoading) {
                            if (idCustomer != null) {
                                review?.let {
                                    val updatedReview = ReviewRequestUpdate(
                                        id = idReview,
                                        customer_id = idCustomer,
                                        comment = comment,
                                        image = "images",
                                        rating = rating
                                    )
                                    reviewViewModel.updateReview(updatedReview)
                                }
                                showSnackbar.value = true
                                snackbarMessage.value =
                                    "Đánh giá của bạn đã được gửi thành công!"
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("needRefreshReviews", true)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(1.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (isLoading) "Đang gửi..." else "Gửi đánh giá", fontSize = 20.sp)
                }
            }
        }
    ) { paddingValues ->
        if(review == null){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5D9EFF))
            }
        }
        else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(10.dp)
            ) {
                item {
                    // Tiêu đề
                    Text(
                        text = "Bạn đánh giá sản phẩm này như thế nào?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Đánh giá sao
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Star $i",
                                tint = if (i <= rating) Color(0xFFFFD700) else Color(0xFFBDBDBD),
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { rating = i } // Cập nhật số sao khi người dùng nhấn
                                    .padding(4.dp)
                            )
                        }
                    }

                    // Ô nhập bình luận
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Viết bình luận của bạn...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF5D9EFF),
                            focusedLabelColor = Color(0xFF5D9EFF),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            cursorColor = Color(0xFF5D9EFF)
                        ),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Chế độ ảnh đánh giá
                    Text(
                        text = "Thêm ảnh đánh giá",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Thay thế LazyRow bằng một Box đơn lẻ và phóng to kích thước
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Phóng to chiều cao ảnh
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImage == null) {
                            // Hiển thị icon thêm nếu chưa có ảnh
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Thêm ảnh",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Thêm ảnh",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            // Hiển thị ảnh đã chọn, lấp đầy toàn bộ không gian
                            AsyncImage(
                                model = selectedImage,
                                contentDescription = "Ảnh đánh giá",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit  // Đảm bảo ảnh lắp đầy ô chứa
                            )
                        }
                    }
                }
            }
        }
    }
}