package com.example.ungdungbanthietbi_iot.views.personal

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import com.example.ungdungbanthietbi_iot.models.Order
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.viewModels.OrderViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.base64ToBitmap
import com.example.ungdungbanthietbi_iot.utils.formatDateTimeZone
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestampEX
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.views.order_detail.calculateDaysSinceReceived
import java.net.URLEncoder
import java.time.OffsetDateTime

enum class OrderStatus(val value: Int, val displayName: String) {
    CHO_XAC_NHAN(0, "Chờ xác nhận"),
    CHO_LAY_HANG(1, "Đang chuẩn bị hàng"),
    CHO_GIAO_HANG(2, "Đang giao hàng"),
    DA_GIAO(3, "Đã giao"),
    HOAN_TAT(4, "Hoàn tất"),
    DA_HUY(-1, "Đã hủy")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(navController: NavController, idCustomer: String?) {
    var selectedTabIndexItem by rememberSaveable { mutableIntStateOf(0) }
    val tabs = OrderStatus.entries.map { it.displayName }
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
                    Text(
                        text = "Đơn đã mua",
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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndexItem,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth(),
                contentColor = Color(0xFF5D9EFF),
                containerColor = Color.White,
                indicator = { tabPositions ->
                    SecondaryIndicator(
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
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W400
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
                    0 -> ChoXacNhanScreen(navController, idCustomer)
                    1 -> ChoLayHangScreen(navController, idCustomer)
                    2 -> ChoGiaoHangScreen(navController, idCustomer)
                    3 -> DaGiaoHangScreen(navController, idCustomer)
                    4 -> HoanTatScreen(navController, idCustomer)
                    5 -> HuyDonHangScreen(navController, idCustomer)
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun DaGiaoHangScreen(navController: NavController, idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()

    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true
            errorMessage.value = null
            try {
                //tìm order có status bằng 1
                orderViewModel.getOrdersByCustomer(idCustomer)
                Log.d("ChoXacNhanScreen", "Successfully triggered order fetch for customer: $idCustomer")
            } catch (e: Exception) {
                val userFriendlyError = when (e) {
                    is java.net.UnknownHostException -> "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."
                    is retrofit2.HttpException -> {
                        val httpCode = e.code()
                        Log.e("ChoXacNhanScreen", "HTTP error $httpCode: ${e.message()}")
                        when (httpCode) {
                            400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại."
                            401 -> "Không có quyền truy cập. Vui lòng đăng nhập lại."
                            404 -> "Không tìm thấy đơn hàng cho khách hàng này."
                            500 -> "Lỗi server. Vui lòng thử lại sau."
                            else -> "Lỗi server: ${e.message}"
                        }
                    }
                    else -> "Lỗi không xác định: ${e.message}"
                }
                errorMessage.value = userFriendlyError
            } finally {
                isLoading.value = false
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

            listOrder?.data?.data?.isEmpty() == true -> {
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
                    listOrder?.data?.let { orderData ->
                        // Filter orders with status == 1
                        val pendingOrders = orderData.data
                            .filter { it.status == OrderStatus.DA_GIAO.value  }
                            .sortedByDescending { OffsetDateTime.parse(it.created_at) }
                        Log.d("ChoXacNhanScreen", "Filtered ${pendingOrders.size} orders with status == 3")
                        if (pendingOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có hóa đơn nào đã giao.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(pendingOrders) { order ->
                                if (idCustomer != null) {
                                    OrderItem(
                                        order,
                                        navController,
                                        false,
                                        idCustomer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun HoanTatScreen(navController: NavController, idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()

    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true
            errorMessage.value = null
            try {
                //tìm order có status bằng 1
                orderViewModel.getOrdersByCustomer(idCustomer)
                Log.d("ChoXacNhanScreen", "Successfully triggered order fetch for customer: $idCustomer")
            } catch (e: Exception) {
                val userFriendlyError = when (e) {
                    is java.net.UnknownHostException -> "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."
                    is retrofit2.HttpException -> {
                        val httpCode = e.code()
                        Log.e("ChoXacNhanScreen", "HTTP error $httpCode: ${e.message()}")
                        when (httpCode) {
                            400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại."
                            401 -> "Không có quyền truy cập. Vui lòng đăng nhập lại."
                            404 -> "Không tìm thấy đơn hàng cho khách hàng này."
                            500 -> "Lỗi server. Vui lòng thử lại sau."
                            else -> "Lỗi server: ${e.message}"
                        }
                    }
                    else -> "Lỗi không xác định: ${e.message}"
                }
                errorMessage.value = userFriendlyError
            } finally {
                isLoading.value = false
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

            listOrder?.data?.data?.isEmpty() == true -> {
                Text(
                    text = "Không có hóa đơn nào đã hoàn tất.",
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
                    listOrder?.data?.let { orderData ->
                        // Filter orders with status == 1
                        val pendingOrders = orderData.data
                            .filter { it.status == OrderStatus.HOAN_TAT.value  }
                            .sortedByDescending { OffsetDateTime.parse(it.created_at) }
                        Log.d("ChoXacNhanScreen", "Filtered ${pendingOrders.size} orders with status == 4")
                        if (pendingOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có hóa đơn nào đã hoàn tất.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(pendingOrders) { order ->
                                if (idCustomer != null) {
                                    OrderItem(
                                        order,
                                        navController,
                                        false,
                                        idCustomer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun ChoGiaoHangScreen(navController: NavController, idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()

    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true
            errorMessage.value = null
            try {
                //tìm order có status bằng 1
                orderViewModel.getOrdersByCustomer(idCustomer)
                Log.d("ChoXacNhanScreen", "Successfully triggered order fetch for customer: $idCustomer")
            } catch (e: Exception) {
                val userFriendlyError = when (e) {
                    is java.net.UnknownHostException -> "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."
                    is retrofit2.HttpException -> {
                        val httpCode = e.code()
                        Log.e("ChoXacNhanScreen", "HTTP error $httpCode: ${e.message()}")
                        when (httpCode) {
                            400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại."
                            401 -> "Không có quyền truy cập. Vui lòng đăng nhập lại."
                            404 -> "Không tìm thấy đơn hàng cho khách hàng này."
                            500 -> "Lỗi server. Vui lòng thử lại sau."
                            else -> "Lỗi server: ${e.message}"
                        }
                    }
                    else -> "Lỗi không xác định: ${e.message}"
                }
                errorMessage.value = userFriendlyError
            } finally {
                isLoading.value = false
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

            listOrder?.data?.data?.isEmpty() == true -> {
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
                    listOrder?.data?.let { orderData ->
                        // Filter orders with status == 1
                        val pendingOrders = orderData.data
                            .filter { it.status == OrderStatus.CHO_GIAO_HANG.value  }
                            .sortedByDescending { OffsetDateTime.parse(it.created_at) }
                        Log.d("ChoXacNhanScreen", "Filtered ${pendingOrders.size} orders with status == 3")
                        if (pendingOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có hóa đơn nào đang được giao hàng.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(pendingOrders) { order ->
                                if (idCustomer != null) {
                                    OrderItem(
                                        order,
                                        navController,
                                        false,
                                        idCustomer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun HuyDonHangScreen(navController: NavController, idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()

    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true
            errorMessage.value = null
            try {
                //tìm order có status bằng 1
                orderViewModel.getOrdersByCustomer(idCustomer)
                Log.d("ChoXacNhanScreen", "Successfully triggered order fetch for customer: $idCustomer")
            } catch (e: Exception) {
                val userFriendlyError = when (e) {
                    is java.net.UnknownHostException -> "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."
                    is retrofit2.HttpException -> {
                        val httpCode = e.code()
                        Log.e("ChoXacNhanScreen", "HTTP error $httpCode: ${e.message()}")
                        when (httpCode) {
                            400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại."
                            401 -> "Không có quyền truy cập. Vui lòng đăng nhập lại."
                            404 -> "Không tìm thấy đơn hàng cho khách hàng này."
                            500 -> "Lỗi server. Vui lòng thử lại sau."
                            else -> "Lỗi server: ${e.message}"
                        }
                    }
                    else -> "Lỗi không xác định: ${e.message}"
                }
                Log.e("ChoXacNhanScreen", "Error fetching orders: ${e.stackTraceToString()}")
                errorMessage.value = userFriendlyError
            } finally {
                isLoading.value = false
                Log.i("ChoXacNhanScreen", "Order fetch completed, isLoading set to false")
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

            listOrder?.data?.data?.isEmpty() == true -> {
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
                    listOrder?.data?.let { orderData ->
                        // Filter orders with status == 1
                        val pendingOrders = orderData.data
                            .filter { it.status == OrderStatus.DA_HUY.value  }
                            .sortedByDescending { OffsetDateTime.parse(it.created_at) }
                        Log.d("ChoXacNhanScreen", "Filtered ${pendingOrders.size} orders with status == -1")
                        if (pendingOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có hóa đơn đã hủy.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(pendingOrders) { order ->
                                if (idCustomer != null) {
                                    OrderItem(
                                        order,
                                        navController,
                                        false,
                                        idCustomer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun ChoLayHangScreen(navController: NavController, idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()

    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true
            errorMessage.value = null
            try {
                //tìm order có status bằng 1
                orderViewModel.getOrdersByCustomer(idCustomer)
                Log.d("ChoXacNhanScreen", "Successfully triggered order fetch for customer: $idCustomer")
            } catch (e: Exception) {
                val userFriendlyError = when (e) {
                    is java.net.UnknownHostException -> "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."
                    is retrofit2.HttpException -> {
                        val httpCode = e.code()
                        Log.e("ChoXacNhanScreen", "HTTP error $httpCode: ${e.message()}")
                        when (httpCode) {
                            400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại."
                            401 -> "Không có quyền truy cập. Vui lòng đăng nhập lại."
                            404 -> "Không tìm thấy đơn hàng cho khách hàng này."
                            500 -> "Lỗi server. Vui lòng thử lại sau."
                            else -> "Lỗi server: ${e.message}"
                        }
                    }
                    else -> "Lỗi không xác định: ${e.message}"
                }
                Log.e("ChoXacNhanScreen", "Error fetching orders: ${e.stackTraceToString()}")
                errorMessage.value = userFriendlyError
            } finally {
                isLoading.value = false
                Log.i("ChoXacNhanScreen", "Order fetch completed, isLoading set to false")
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

            listOrder?.data?.data?.isEmpty() == true -> {
                Text(
                    text = "Không có hóa đơn nào đang chuẩn bị hàng",
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
                    listOrder?.data?.let { orderData ->
                        // Filter orders with status == 1
                        val pendingOrders = orderData.data
                            .filter { it.status == OrderStatus.CHO_LAY_HANG.value  }
                            .sortedByDescending { OffsetDateTime.parse(it.created_at) }
                        Log.d("ChoXacNhanScreen", "Filtered ${pendingOrders.size} orders with status == 2")
                        if (pendingOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có hóa đơn nào đang chuẩn bị hàng.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(pendingOrders) { order ->
                                if (idCustomer != null) {
                                    OrderItem(
                                        order,
                                        navController,
                                        false,
                                        idCustomer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun ChoXacNhanScreen(navController: NavController, idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()

    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = idCustomer) {
        if (idCustomer != null) {
            isLoading.value = true
            errorMessage.value = null
            try {
                //tìm order có status bằng 1
                orderViewModel.getOrdersByCustomer(idCustomer)
                Log.d("ChoXacNhanScreen", "Successfully triggered order fetch for customer: $idCustomer")
            } catch (e: Exception) {
                val userFriendlyError = when (e) {
                    is java.net.UnknownHostException -> "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."
                    is retrofit2.HttpException -> {
                        val httpCode = e.code()
                        Log.e("ChoXacNhanScreen", "HTTP error $httpCode: ${e.message()}")
                        when (httpCode) {
                            400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại."
                            401 -> "Không có quyền truy cập. Vui lòng đăng nhập lại."
                            404 -> "Không tìm thấy đơn hàng cho khách hàng này."
                            500 -> "Lỗi server. Vui lòng thử lại sau."
                            else -> "Lỗi server: ${e.message}"
                        }
                    }
                    else -> "Lỗi không xác định: ${e.message}"
                }
                Log.e("ChoXacNhanScreen", "Error fetching orders: ${e.stackTraceToString()}")
                errorMessage.value = userFriendlyError
            } finally {
                isLoading.value = false
                Log.i("ChoXacNhanScreen", "Order fetch completed, isLoading set to false")
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
                Log.w("ChoXacNhanScreen", "Displaying error message: ${errorMessage.value}")
            }

            listOrder?.data?.data?.isEmpty() == true -> {
                Text(
                    text = "Không có hóa đơn nào đang chờ xác nhận.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                Log.i("ChoXacNhanScreen", "No orders found for customer: $idCustomer")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    listOrder?.data?.let { orderData ->
                        // Filter orders with status == 1
                        val pendingOrders = orderData.data
                            .filter { it.status == OrderStatus.CHO_XAC_NHAN.value }
                            .sortedByDescending { OffsetDateTime.parse(it.created_at) }
                        Log.d("ChoXacNhanScreen", "Filtered ${pendingOrders.size} orders with status == 1")
                        if (pendingOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có hóa đơn nào đang chờ xác nhận.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Log.i("ChoXacNhanScreen", "No pending orders after filtering")
                        } else {
                            items(pendingOrders) { order ->
                                if (idCustomer != null) {
                                    OrderItem(
                                        order,
                                        navController,
                                        true,
                                        idCustomer,
                                    )
                                }
                            }
                            Log.d("ChoXacNhanScreen", "Displaying ${pendingOrders.size} pending orders")
                        }
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
    idCustomer: String,
) {
    val orderViewModel: OrderViewModel = viewModel()
    val reviewViewModel: ReviewViewModel = viewModel()
    val listDetail = order.details // Lấy từ JSON của order
    val listReviews by reviewViewModel.listAllReviews.collectAsState()
    val encodedOrderId = order.id.let { URLEncoder.encode(it, "UTF-8") } ?: ""
    val shouldRefresh by orderViewModel.shouldRefresh
    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {

            orderViewModel.getOrdersByCustomer(idCustomer) // Làm mới danh sách từ server
            orderViewModel.resetRefresh() // Reset state
        }
    }
    LaunchedEffect (Unit){
        reviewViewModel.getAllReviews()
    }
    // Lọc các mục trùng product_id, giữ mục có quantity lớn nhất
    val filteredDetails = listDetail.groupBy { it.product_id }
        .map { (_, details) -> details.maxByOrNull { it.quantity }!! }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        onClick = {
            navController.navigate("${Screen.Order_Detail.route}?id=${encodedOrderId}&totalAmount=${order.totalAmount}&idCustomer=$idCustomer")
        }
    ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Mã đơn hàng: #HD${encodedOrderId}",
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    filteredDetails.forEach { detail ->
                        val existingReview = listReviews.find { review ->
                            review.idCustomer == idCustomer && review.idDevice == detail.product_id
                        }
                        val isReviewed = existingReview != null
                        // Tính số ngày kể từ created_at hoặc updated_at
                        val daysSinceReview = if (existingReview != null) {
                            val reviewDate = existingReview.updated_at?.takeIf { it.isNotBlank() } ?: existingReview.created_at
                            calculateDaysSinceReceived(reviewDate)
                        } else {
                            Int.MAX_VALUE
                        }
                        val canEditReview = daysSinceReview <= 7
                        Log.d("Debug", "$existingReview, $isReviewed và $canEditReview")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val bitmap = base64ToBitmap(detail.image)
                                if (bitmap != null) {
                                    Image(
                                        painter = BitmapPainter(bitmap.asImageBitmap()),
                                        contentDescription = "Hình ảnh sản phẩm",
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(end = 8.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Column {
                                    Text(
                                        text = detail.product_name,
                                        fontSize = 16.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = formatGiaTien(detail.price),
                                                fontSize = 14.sp,
                                                color = Color.Red
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "x${detail.quantity}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                            if (order.status == 4) {
                                if (isReviewed && existingReview != null) {
                                    if (canEditReview) {
                                        Button(
                                            onClick = {
                                                navController.navigate(Screen.Update_Rating_Screen.route+"?idReview=${existingReview.idReview}&idCustomer=$idCustomer")
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF5D9EFF),
                                                contentColor = Color.White
                                            ),
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = "Chỉnh sửa",
                                                fontSize = 14.sp
                                            )
                                        }
                                    }

                                }
                                else {
                                    Button(
                                        onClick = { navController.navigate(Screen.Rating_Screen.route + "?idCustomer=$idCustomer&idDevice=${detail.product_id}") },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF5D9EFF),
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(
                                            text = "Đánh giá",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(top = 3.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tổng Tiền: ${formatGiaTien(order.totalAmount)}",
                    fontSize = 15.sp,
                    color = Color.Red
                )
                Text(
                    text = "Ngày đặt hàng: ${formatDateTimeZone(order.created_at)}",
                    fontSize = 14.sp
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ){
                    if (isCancel) {
                        Button(
                            modifier = Modifier
                                .height(40.dp) // Đặt chiều cao cố định
                                .padding(end = 5.dp), // Padding giữa các nút
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5D9EFF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(5.dp),
                            onClick = {
                                orderViewModel.cancelOrder(order.id)
                            },
                        ) {
                            Text(text = "Hủy", fontSize = 13.sp)
                        }
                    }
                    Button(
                        onClick = {
                            // Thêm hành động khi nhấn nút Xem chi tiết
                            navController.navigate("${Screen.Order_Detail.route}?id=${order.id}&totalAmount=${order.totalAmount}&idCustomer=$idCustomer")
                        },
                        modifier = Modifier
                            .height(40.dp), // Đặt chiều cao cố định
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF5D9EFF)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF5D9EFF)),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = "Xem chi tiết",
                            fontSize = 13.sp
                        )
                    }
                }
            }

    }
}