package com.example.ungdungbanthietbi_iot.screen.rating

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetailViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.screen.order_detail.calculateDaysSinceReceived

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
fun RatingHistoryScreen(navController: NavController, idCustomer: String?) {

    val reviewViewModel: ReviewViewModel = viewModel()
    val listReviewDaDanhGia = reviewViewModel.listReviewDaDanhGia
    val listReviewDanhGiaLan2 = reviewViewModel.listReviewDanhGiaLan2
    val list1 by remember { derivedStateOf { reviewViewModel.listReviewDaDanhGia } }
    val list2 by remember { derivedStateOf { reviewViewModel.listReviewDanhGiaLan2 } }
    // 1) Lần đầu load
    LaunchedEffect(idCustomer) {
        idCustomer?.let {
            reviewViewModel.getReviewByIdCustomerDaDanhGia(it)
            reviewViewModel.getReviewByIdCustomerDanhGiaLan2(it)
        }
    }
    // 2) Reload mỗi khi screen quay lại (ON_RESUME)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                idCustomer?.let {
                    reviewViewModel.getReviewByIdCustomerDaDanhGia(it)
                    reviewViewModel.getReviewByIdCustomerDanhGiaLan2(it)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val all = (reviewViewModel.listReviewDanhGiaLan2 + reviewViewModel.listReviewDaDanhGia)
        .distinctBy { it.idReview }
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
                    Text(text = "Đánh giá của tôi (${all.size})",
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
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
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

                if(all.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "Bạn chưa có đánh giá nào!",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else{
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(all) { review ->
                            ReviewItem(review = review, idCustomer = idCustomer, idDevice = review.idDevice, navController)
                            Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các mục
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ReviewItem(review: Review, idCustomer: String?, idDevice: Int?, navController: NavController) {
    val customerViewModel: CustomerViewModel = viewModel()
    val accountViewModel: AccountViewModel = viewModel()
    val customer = customerViewModel.customer
    val account = accountViewModel.accountById

    val reviewViewModel: ReviewViewModel = viewModel()

    // 1. State giữ kết quả API (null = chưa review, non-null = đã review)
    var reviewInfo by remember { mutableStateOf<Review?>(null) }

    // 2. Khi idCustomer hoặc idDevice thay đổi, gọi API
    LaunchedEffect(idCustomer, idDevice) {
        if (!idCustomer.isNullOrBlank() && idDevice != null) {
            reviewInfo = reviewViewModel.checkReviewDirect(idCustomer, idDevice, 2)
        }
    }

    val deviceViewModel: DeviceViewModel = viewModel()
    val device = deviceViewModel.deviceMap[idDevice.toString()] // Lấy thiết bị theo ID
    // Gọi API lấy device khi idDevice thay đổi
    LaunchedEffect(idDevice) {
        if (idDevice != null && device == null) {
            deviceViewModel.getDeviceBySlug2(idDevice.toString())
        }
    }

    LaunchedEffect (idCustomer){
        customerViewModel.getCustomerById(idCustomer.toString())
        accountViewModel.getAccountById(idCustomer.toString())
    }
    // Tính số ngày kể từ khi tạo đánh giá
    val daysSinceReviewCreated = remember(review.created_at) {
        review.created_at?.let { createdAt ->
            calculateDaysSinceReceived(createdAt)
        } ?: Int.MAX_VALUE
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
                    text = "${customer?.surname} ${customer?.lastName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if(daysSinceReviewCreated <= 10){
                    Button(
                        onClick = {
                            navController.navigate(Screen.Update_Rating_Screen.route + "?idReview=${reviewInfo!!.idReview}&idCustomer=${idCustomer}")
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
            Text(
                text = review.comment,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Ngày đánh giá
            Text(
                text = formatDate(review.created_at),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            device?.let {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate(Screen.ProductDetailsScreen.route + "?id=${idDevice}&idCustomer=${idCustomer}&username=${account!!.username}")
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = it.image,
                        contentDescription = null,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = it.name,
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
