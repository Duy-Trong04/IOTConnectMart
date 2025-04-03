package com.example.ungdungbanthietbi_iot.screen.rating

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetailViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen

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
    val listReviewChuaDanhGia = reviewViewModel.listReviewChuaDanhGia
    val listReviewDaDanhGia = reviewViewModel.listReviewDaDanhGia

    LaunchedEffect (idCustomer){
        reviewViewModel.getReviewByIdCustomerChuaDanhGia(idCustomer.toString())
        reviewViewModel.getReviewByIdCustomerDaDanhGia(idCustomer.toString())
    }

    var selectedTabIndexItem by remember { mutableStateOf(1) }
    val tabs = listOf("Chưa đánh giá (${listReviewChuaDanhGia.size})", "Đã đánh giá (${listReviewDaDanhGia.size})")
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
                    Text(text = "Đánh giá của tôi",
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
            TabRow(
                selectedTabIndex = selectedTabIndexItem,
                modifier = Modifier.fillMaxWidth(),
                contentColor = Color(0xFF5D9EFF),
                containerColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndexItem]),
                        color = Color(0xFF5D9EFF)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndexItem == index,
                        onClick = { selectedTabIndexItem = index },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W600,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (selectedTabIndexItem) {
                    0 -> ChuaDanhGiaScreen(navController,idCustomer)
                    1 -> DaDanhGiaScreen(navController,idCustomer)
                }
            }
        }
    }

}

@Composable
fun ChuaDanhGiaScreen(navController: NavController, idCustomer: String?){

    val reviewViewModel: ReviewViewModel = viewModel()
    val listReviewChuaDanhGia = reviewViewModel.listReviewChuaDanhGia

    LaunchedEffect (idCustomer){
        reviewViewModel.getReviewByIdCustomerChuaDanhGia(idCustomer.toString())

    }

    if(listReviewChuaDanhGia.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Bạn không có sản phẩm nào cần đánh giá!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    else{

        LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(listReviewChuaDanhGia) { review ->
                    ReviewCard(
                        review = review,
                        idCustomer = idCustomer,
                        idDevice = review.idDevice,
                        navController
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các mục
                }
        }
    }

}

@Composable
fun ReviewCard(review: Review, idCustomer: String?, idDevice: Int?, navController: NavController) {

    val deviceViewModel: DeviceViewModel = viewModel()
    val device = deviceViewModel.deviceMap[idDevice.toString()] // Lấy thiết bị theo ID
    // Gọi API lấy device khi idDevice thay đổi
    LaunchedEffect(idDevice) {
        if (idDevice != null && device == null) {
            deviceViewModel.getDeviceBySlug2(idDevice.toString())
        }
    }

    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    var listOrderDetail = orderDetailViewModel.listOrderDetailByStatus

    LaunchedEffect (idCustomer){
        orderDetailViewModel.getOrderDetailByStatus(idCustomer.toString(), 0)
    }


    // Lọc ra đơn hàng liên quan với review hiện tại.
    val orderDetail = listOrderDetail.firstOrNull { it.idDevice == idDevice }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            device?.let {
                Row(
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.ProductDetailsScreen.route + "?id=${idDevice}")
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
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                // Phần thông tin chi tiết đơn hàng
                orderDetail?.let { detail ->
                    Column(
                        modifier = Modifier
//                        .clickable {
//                            navController.navigate(Screen.OrderDetailScreen.route + "?id=${detail.id}")
//                        }
                    ) {
                        Text(
                            text = "Mã đơn hàng: ${detail.idOrder}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Số lượng: ${detail.stock}",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Giá: ${detail.price}",
                            fontSize = 14.sp
                        )
                    }
                }
                Button(
                    onClick = {
                    navController.navigate(Screen.Rating_Screen.route + "?idReview=${review.idReview}&idCustomer=${idCustomer}&idDevice=${idDevice}")
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Đánh giá",
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun DaDanhGiaScreen(navController: NavController, idCustomer: String?){

    val reviewViewModel: ReviewViewModel = viewModel()
    val listReviewDaDanhGia = reviewViewModel.listReviewDaDanhGia

    LaunchedEffect (idCustomer){
        reviewViewModel.getReviewByIdCustomerDaDanhGia(idCustomer.toString())
    }

    if(listReviewDaDanhGia.isEmpty()) {
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
        val groupedReviews = listReviewDaDanhGia.groupBy { it.idDevice }
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            groupedReviews.forEach { (idDevice, reviews) ->
                items(reviews) { review ->
                    ReviewItem(review = review, idCustomer = idCustomer, idDevice = idDevice, navController)
                    Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các mục
                }
            }
        }
    }

}

@Composable
fun ReviewItem(review: Review, idCustomer: String?, idDevice: Int?, navController: NavController) {
    val customerViewModel: CustomerViewModel = viewModel()
    val custmer = customerViewModel.customer

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
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${custmer?.surname} ${custmer?.lastName}",
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
                text = review.created_at,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            device?.let {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate(Screen.ProductDetailsScreen.route + "?id=${idDevice}")
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
