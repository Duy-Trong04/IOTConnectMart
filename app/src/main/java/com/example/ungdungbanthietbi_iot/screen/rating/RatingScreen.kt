package com.example.ungdungbanthietbi_iot.screen.rating

import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.screen.order_detail.getCurrentTimestamp
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(navController: NavController, idReview: Int, idCustomer: String?, idDevice: Int){

    val reviewViewModel:ReviewViewModel = viewModel()

    var rating by remember { mutableStateOf(0) } // Lưu trạng thái số sao được đánh giá
    var comment by remember { mutableStateOf("") } // Lưu nội dung bình luận
    var images by remember { mutableStateOf(mutableListOf<Uri>()) } // Lưu danh sách ảnh
    var isAnonymous by remember { mutableStateOf(false) } // Trạng thái ẩn danh

    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá sản phẩm ${idReview} ${idCustomer} ${idDevice}",
                    fontWeight = FontWeight.Bold
                )},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            BottomAppBar (
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth().height(170.dp)
            ){
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 1.dp)
                        ) {
                            Checkbox(
                                checked = isAnonymous,
                                onCheckedChange = { isAnonymous = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF5D9EFF),
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(
                                text = "Đánh giá ẩn danh",
                                fontSize = 16.sp,
                                modifier = Modifier.clickable { isAnonymous = !isAnonymous }
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Nút Gửi
                    Button(
                        onClick = {
                            if(isAnonymous){
                                val updateReview = Review(
                                    idReview = idReview,
                                    idCustomer = "Ẩn danh",
                                    idEmployee = "Null",
                                    idDevice = idDevice,
                                    comment = comment,
                                    rating = rating,
                                    response = "",
                                    note = "",
                                    created_at = getCurrentTimestamp(),
                                    updated_at = getCurrentTimestamp(),
                                    status = 1
                                )
                                reviewViewModel.updateReview(updateReview)
                            }
                            else{
                                if(idCustomer != null){
                                    val updateReview = Review(
                                        idReview = idReview,
                                        idCustomer = idCustomer,
                                        idEmployee = "Null",
                                        idDevice = idDevice,
                                        comment = comment,
                                        rating = rating,
                                        response = "",
                                        note = "",
                                        created_at = getCurrentTimestamp(),
                                        updated_at = getCurrentTimestamp(),
                                        status = 1
                                    )
                                    reviewViewModel.updateReview(updateReview)
                                }
                            }

                            showSnackbar.value = true
                            snackbarMessage.value = "Đánh giá của bạn đã được gửi thành công!"

                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.buttonElevation(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Gửi đánh giá", fontSize = 20.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item{
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF5D9EFF),
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
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Nút thêm ảnh
                    item {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .clickable {
                                    // Thêm logic chọn ảnh ở đây (giả lập bằng mã bên dưới)
                                    images.add(Uri.parse("android.resource://com.example.app/drawable/sample_image"))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Thêm ảnh",
                                tint = Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    // Hiển thị danh sách ảnh
                    items(images) { image ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                        }
                    }
                }
            }
        }
    }
}
// Hàm giả lập lấy thời gian hiện tại, bạn có thể thay thế bằng cách lấy thời gian theo chuẩn của hệ thống
fun getCurrentTimestamp(): String {
    // Ví dụ: trả về thời gian hiện tại theo định dạng "yyyy-MM-dd HH:mm:ss"
    val current = java.util.Calendar.getInstance().time
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return formatter.format(current)
}