package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungbanthietbi_iot.data.order.Order
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.order.OrderViewModel
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetailViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.screen.order_detail.calculateDaysSinceReceived
import com.example.ungdungbanthietbi_iot.utils.formatDate
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import java.text.DecimalFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(navController: NavController, idCustomer: String?) {
    var selectedTabIndexItem by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao", "Hoàn tất", "Đã hủy")
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(text = "Đơn đã mua",
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
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(3.dp),
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndexItem,
                edgePadding = 0.dp,
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
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .clip(shape = RectangleShape)
                            .clickable { selectedTabIndexItem = index }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                }
            }

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (selectedTabIndexItem) {
                    0 -> ChoXacNhanScreen(navController,idCustomer)
                    1 -> ChoLayHangScreen(navController,idCustomer)
                    2 -> ChoGiaoHangScreen(navController, idCustomer)
                    3 -> DaGiaoHangScreen(navController, idCustomer)
                    4 -> HoanTatScreen(navController,idCustomer)
                    5 -> HuyDonHangScreen(navController,idCustomer)
                }
            }
        }
    }
}

@Composable
fun DaGiaoHangScreen(navController: NavController, idCustomer: String?){
    val orderViewModel:OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    val isLoading =  remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true // Bắt đầu tải dữ liệu
            errorMessage.value = null
            try {
                orderViewModel.getOrderByCustomer(
                    idCustomer,
                    4
                )
            } catch (e: Exception) {
                errorMessage.value = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading.value = false // Kết thúc tải dữ liệu
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Đã xảy ra lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            listOrder.isEmpty() -> {
                Text(
                    text = "Không có hóa đơn nào đã giao.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(listOrder) { order ->
                        OrderItem(order,navController, false, orderViewModel, orderDetailViewModel, deviceViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HoanTatScreen(navController: NavController, idCustomer: String?){
    val orderViewModel:OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    val isLoading =  remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true // Bắt đầu tải dữ liệu
            errorMessage.value = null
            try {
                orderViewModel.getOrderByCustomer(
                    idCustomer,
                    5
                )
            } catch (e: Exception) {
                errorMessage.value = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading.value = false // Kết thúc tải dữ liệu
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Đã xảy ra lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            listOrder.isEmpty() -> {
                Text(
                    text = "Không có hóa đơn nào đã giao.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(listOrder) { order ->
                        OrderItem(order,navController, false, orderViewModel, orderDetailViewModel, deviceViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ChoGiaoHangScreen(navController: NavController, idCustomer: String?){
    val orderViewModel:OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    val isLoading =  remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true // Bắt đầu tải dữ liệu
            errorMessage.value = null
            try {
                orderViewModel.getOrderByCustomer(
                    idCustomer,
                    3
                )
            } catch (e: Exception) {
                errorMessage.value = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading.value = false // Kết thúc tải dữ liệu
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Đã xảy ra lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            listOrder.isEmpty() -> {
                Text(
                    text = "Không có hóa đơn nào đang chờ giao hàng.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(listOrder) { order ->
                        OrderItem(order,navController, false, orderViewModel, orderDetailViewModel, deviceViewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun HuyDonHangScreen(navController: NavController,idCustomer: String?) {
    // Lấy ViewModel
    val orderViewModel: OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    // Quan sát danh sách hóa đơn thông qua StateFlow
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    // Trạng thái đang tải
    val isLoading = remember { mutableStateOf(false) }

    // Trạng thái lỗi (nếu có)
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Hàm gọi API để lấy danh sách hóa đơn
    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true // Bắt đầu tải dữ liệu
            errorMessage.value = null
            try {
                orderViewModel.getOrderByCustomer(
                    idCustomer,
                    6
                )
            } catch (e: Exception) {
                errorMessage.value = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading.value = false // Kết thúc tải dữ liệu
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Đã xảy ra lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            listOrder.isEmpty() -> {
                Text(
                    text = "Không có hóa đơn đã hủy.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(listOrder) { order ->
                        OrderItem(order,navController, false, orderViewModel, orderDetailViewModel, deviceViewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun ChoLayHangScreen(navController: NavController,idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    // Quan sát danh sách hóa đơn thông qua StateFlow
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    // Trạng thái đang tải
    val isLoading = remember { mutableStateOf(false) }

    // Trạng thái lỗi (nếu có)
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Hàm gọi API để lấy danh sách hóa đơn
    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true // Bắt đầu tải dữ liệu
            errorMessage.value = null
            try {
                orderViewModel.getOrderByCustomer(
                    idCustomer,
                    2
                )
            } catch (e: Exception) {
                errorMessage.value = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading.value = false // Kết thúc tải dữ liệu
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Đã xảy ra lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            listOrder.isEmpty() -> {
                Text(
                    text = "Không có hóa đơn nào đang chờ lấy hàng.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(listOrder) { order ->
                        OrderItem(order,navController, false, orderViewModel, orderDetailViewModel, deviceViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ChoXacNhanScreen(navController: NavController,idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    // Quan sát danh sách hóa đơn thông qua StateFlow
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    // Trạng thái đang tải
    val isLoading = remember { mutableStateOf(false) }

    // Trạng thái lỗi (nếu có)
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Hàm gọi API để lấy danh sách hóa đơn
    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true // Bắt đầu tải dữ liệu
            errorMessage.value = null
            try {
                orderViewModel.getOrderByCustomer(
                    idCustomer,
                    1 // Trạng thái "Chờ xác nhận"
                )
            } catch (e: Exception) {
                errorMessage.value = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading.value = false // Kết thúc tải dữ liệu
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Đã xảy ra lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            listOrder.isEmpty() -> {
                Text(
                    text = "Không có hóa đơn nào đang chờ xác nhận.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(listOrder) { order ->
                        OrderItem(order,navController, true, orderViewModel, orderDetailViewModel, deviceViewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun OrderItem(
    order: Order,
    navController: NavController,
    isCancel: Boolean,
    orderViewModel:OrderViewModel,
    orderDetailViewModel: OrderDetailViewModel,
    deviceViewModel: DeviceViewModel
) {

    val reviewViewModel: ReviewViewModel = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()
    val customer = customerViewModel.customer

    LaunchedEffect(key1 = order.id) {
        deviceViewModel.getDeviceByIdOrder2(order.id)
        orderDetailViewModel.getOrderDetailByIdOrder2(order.id)
        customerViewModel.getCustomerByIdOrder(order.id)
    }

    val listDevice by remember(order.id) {
        derivedStateOf { deviceViewModel.devicesByOrder[order.id] ?: emptyList() }
    }

    val listDetail by remember(order.id) {
        derivedStateOf { orderDetailViewModel.orderDetailsByOrder[order.id] ?: emptyList() }
    }
    // Map để lưu trạng thái đánh giá cho từng sản phẩm
    var reviewState by remember { mutableStateOf<Map<Int, Pair<Review?, Review?>>>(emptyMap()) }
    // Chạy lại bất cứ khi nào `listDevice` hoặc `customer` thay đổi
    LaunchedEffect(listDevice, customer) {
        if (customer != null && listDevice.isNotEmpty()) {
            val newReviewState = mutableMapOf<Int, Pair<Review?, Review?>>()
            listDevice.forEach { device ->
                // Khởi tạo null để Compose hiển thị loading (hoặc tránh miss key)
                reviewViewModel.initReviewCheck(device.idDevice)
                reviewViewModel.checkReview(customer.id, device.idDevice)
                reviewViewModel.checkReview2(customer.id, device.idDevice)
                val reviewFirst = reviewViewModel.checkReviewDirect(customer.id, device.idDevice, 2)
                val reviewSecond = reviewViewModel.checkReviewDirect(customer.id, device.idDevice, 1)
                newReviewState[device.idDevice] = Pair(reviewFirst, reviewSecond)
            }
            reviewState = newReviewState
        }
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = {
            navController.navigate("${Screen.Order_Detail.route}?id=${order.id}&totalAmount=${order.totalAmount}")
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f) // Cột chiếm không gian linh hoạt
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Mã đơn hàng: #HD${order.id}",
                    )
                    // Nút Hủy
                    if (isCancel) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 8.dp)
                        ) {
                            Button(
                                modifier = Modifier.fillMaxHeight(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5D9EFF)
                                ),
                                shape = RoundedCornerShape(5.dp),
                                onClick =  {
                                    val orderNew = Order(
                                        order.id,
                                        order.idCustomer,
                                        order.totalAmount,
                                        order.paymentMethod,
                                        order.address,
                                        order.accountNumber,
                                        order.phone,
                                        order.nameRecipient,
                                        order.note,
                                        order.platformOrder,
                                        order.created_at,
                                        order.updated_at,
                                        order.accept_at,
                                        order.idEmployee,
                                        6)
                                    orderViewModel.updateOrder(orderNew)
                                }
                            ) {
                                Text("Hủy")
                            }
                        }
                    }
                }

                Column() {
                    listDevice.forEach { device ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = device.image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Column {
                                    Text(
                                        text = device.name,
                                        fontSize = 16.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = formatGiaTien(device.sellingPrice),
                                                fontSize = 14.sp,
                                                color = Color.Red
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            for (detail in listDetail) {
                                                if (detail.idDevice == device.idDevice) {
                                                    Text(
                                                        text = "x${detail.stock}",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                        if (order.status == 5) {
                                            val (reviewFirst, reviewSecond) = reviewState[device.idDevice] ?: Pair(null, null)
                                            val isSecondOrLaterPurchase = reviewFirst != null
                                            val hasReview = if (isSecondOrLaterPurchase) {
                                                reviewSecond != null // Chỉ coi là có đánh giá nếu đã có reviewSecond
                                            } else {
                                                reviewFirst != null || reviewSecond != null // Mua lần đầu thì kiểm tra cả hai
                                            }
                                            if (!hasReview) {
                                                Button(
                                                    onClick = {
                                                        if (isSecondOrLaterPurchase && reviewFirst != null) {
                                                            // Mua lần thứ hai hoặc tiếp theo: điều hướng đến Update_Rating_Screen
                                                            navController.navigate(
                                                                Screen.Update_Rating_Screen.route +
                                                                        "?idReview=${reviewFirst.idReview}&idCustomer=${customer!!.id}"
                                                            )
                                                        } else {
                                                            // Mua lần đầu: điều hướng đến Rating_Screen
                                                            navController.navigate(
                                                                Screen.Rating_Screen.route +
                                                                        "?idCustomer=${customer!!.id}&idDevice=${device.idDevice}"
                                                            )
                                                        }
                                                    },
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF5D9EFF),
                                                        contentColor = Color.White
                                                    )
                                                ) {
                                                    Text("Đánh giá")
                                                }
                                            } else {
                                                val daysSinceReceived = calculateDaysSinceReceived(order.accept_at)
                                                if (daysSinceReceived <= 10) {
                                                    Button(
                                                        onClick = {
                                                            val reviewToEdit = reviewSecond ?: reviewFirst
                                                            navController.navigate(
                                                                Screen.Update_Rating_Screen.route +
                                                                        "?idReview=${reviewToEdit!!.idReview}&idCustomer=${customer!!.id}"
                                                            )
                                                        },
                                                        shape = RoundedCornerShape(8.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = Color(0xFF5D9EFF),
                                                            contentColor = Color.White
                                                        )
                                                    ) {
                                                        Text("Chỉnh sửa")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tổng Tiền: ${formatGiaTien(order.totalAmount)}", color = Color.Red)
                Text(text = "Ngày Đặt Hàng: ${formatDate(order.created_at)}")
            }
        }
    }
}
