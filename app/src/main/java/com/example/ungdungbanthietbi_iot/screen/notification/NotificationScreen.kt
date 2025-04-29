package com.example.ungdungbanthietbi_iot.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.notice.Notice
import com.example.ungdungbanthietbi_iot.data.notice.NoticeViewModel
import com.example.ungdungbanthietbi_iot.data.order.Order
import com.example.ungdungbanthietbi_iot.data.order.OrderViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatDate
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, idUser: String?) {
    val noticeViewModel: NoticeViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val listNotice by remember { mutableStateOf(noticeViewModel.listNotice) }
    val listOrders by orderViewModel.listAllOrderOfCustomer.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Biến để làm mới danh sách thông báo khi quay lại
    var refreshKey by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // Khởi tạo SharedPreferences cho OrderViewModel
        orderViewModel.initialize(context)
        noticeViewModel.getNoticeByIdCustomer(idUser)
        orderViewModel.getAllOrderByCustomer(idUser ?: "")
        orderViewModel.startOrderStatusCheck(idUser ?: "")
    }
    // Lắng nghe sự kiện quay lại từ màn hình chi tiết
    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("refresh")
            ?.observe(navController.currentBackStackEntry!!) {
                refreshKey++ // Tăng refreshKey để làm mới dữ liệu
            }
    }
    // Gom thông báo theo đơn hàng
    val groupedNotices = listNotice
        .filter { it.type == "order_status_change" }
        .groupBy { notice ->
            listOrders.find { order -> notice.text.contains("#${order.id}") }?.id ?: 0
        }
        .filterKeys { it != 0 }

    // Lấy các thông báo không liên quan đến đơn hàng
    val otherNotices = listNotice.filter { it.type != "order_status_change" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar (
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 16.dp) // Dịch chuyển BottomAppBar xuống 16dp
            ){

            }
        }
    ) { paddingValues ->
        if (groupedNotices.isEmpty() && otherNotices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có thông báo nào",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(groupedNotices.entries.toList()) { (orderId, notices) ->
                    val order = listOrders.find { it.id == orderId }
                    if (order != null) {
                        NotificationGroupItem(
                            navController = navController,
                            order = order,
                            notices = notices,
                            noticeViewModel = noticeViewModel,
                            onNoticesUpdated = {
                                coroutineScope.launch {
                                    noticeViewModel.getNoticeByIdCustomer(idUser) // Làm mới danh sách thông báo
                                }
                            }
                        )
                    }
                }
                // Hiển thị các thông báo không liên quan đến đơn hàng (riêng lẻ)
                items(otherNotices) { notice ->
                    NotificationItem(
                        navController = navController,
                        notice = notice,
                        orderViewModel = orderViewModel,
                        noticeViewModel = noticeViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationGroupItem(
    navController: NavController,
    order: Order,
    notices: List<Notice>,
    noticeViewModel: NoticeViewModel,
    onNoticesUpdated: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val hasUnread = notices.any { it.status == 1 }
    val latestNotice = notices.maxByOrNull { it.created_at } // Thông báo mới nhất
    val coroutineScope = rememberCoroutineScope()

    // Xác định tiêu đề dựa trên trạng thái đơn hàng
    val title = when (order.status) {
        1 -> "Chờ xác nhận"
        2 -> "Đang xử lý"
        3 -> "Đang vận chuyển"
        4 -> "Xác nhận đã nhận hàng"
        5 -> "Giao hàng thành công"
        6 -> "Đã hủy"
        else -> "Đơn hàng #${order.id} - Cập nhật trạng thái"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Cập nhật tất cả thông báo thành đã đọc
                coroutineScope.launch {
                    notices.forEach { notice ->
                        if (notice.status == 1) {
                            val noticeUpdate = notice.copy(status = 0)
                            noticeViewModel.updateNotice(noticeUpdate)
                        }
                    }
                    // Làm mới danh sách thông báo
                    onNoticesUpdated()
                    // Chuyển hướng đến màn hình chi tiết đơn hàng
                    navController.navigate("${Screen.Order_Detail.route}?id=${order.id}&totalAmount=${order.totalAmount}") {
                        // Gửi sự kiện làm mới khi quay lại
                        navController.currentBackStackEntry?.savedStateHandle?.set("refresh", "true")
                    }
                }
            },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasUnread) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = title,
                            fontWeight = if (hasUnread) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (hasUnread) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = latestNotice?.text ?: "Cập nhật trạng thái đơn hàng",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDate(latestNotice?.created_at ?: order.created_at),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Toggle dropdown",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            expanded = !expanded
                            // Cập nhật tất cả thông báo thành đã đọc khi nhấn dropdown
                            coroutineScope.launch {
                                notices.forEach { notice ->
                                    if (notice.status == 1) {
                                        val noticeUpdate = notice.copy(status = 0)
                                        noticeViewModel.updateNotice(noticeUpdate)
                                    }
                                }
                                // Làm mới danh sách thông báo
                                onNoticesUpdated()
                            }
                        }
                )
            }
            // Dropdown hiển thị danh sách thông báo
            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    notices.sortedByDescending { it.created_at }.forEach { notice ->
                        Text(
                            text = notice.text,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = formatDate(notice.created_at),
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    navController: NavController,
    notice: Notice,
    orderViewModel: OrderViewModel,
    noticeViewModel: NoticeViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Cập nhật trạng thái thông báo thành đã đọc
                coroutineScope.launch {
                    if (notice.status == 1) {
                        val noticeUpdate = notice.copy(status = 0)
                        noticeViewModel.updateNotice(noticeUpdate)
                        noticeViewModel.getNoticeByIdCustomer(notice.idUser)
                    }
                }
                // Nếu thông báo liên quan đến đơn hàng, điều hướng đến chi tiết đơn hàng
                if (notice.type == "order_status_change") {
                    val orderId = notice.text.substringAfter("#").substringBefore(" ")
                    navController.navigate("${Screen.Order_Detail.route}?id=${orderId}&totalAmount=0")
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notice.status == 1) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notice.type,
                    fontWeight = if (notice.status == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notice.text,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatDate(notice.created_at),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            if (notice.status == 1) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
        }
    }
}