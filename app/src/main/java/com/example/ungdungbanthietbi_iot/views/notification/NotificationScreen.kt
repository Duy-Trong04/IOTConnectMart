package com.example.ungdungbanthietbi_iot.views.notification

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
import com.example.ungdungbanthietbi_iot.models.Notice
import com.example.ungdungbanthietbi_iot.viewModels.NoticeViewModel
import com.example.ungdungbanthietbi_iot.models.Order
import com.example.ungdungbanthietbi_iot.viewModels.OrderViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatDate
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestampEX
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

// Mock data for notices
val mockNotices = listOf(
    Notice(
        id = 1,
        idUser = "user123",
        type = "admin_order_confirmation",
        text = "Đơn hàng #1 đang chờ xác nhận",
        created_at = "2025-06-10T10:05:00Z",
        status = 1
    ),
    Notice(
        id = 2,
        idUser = "user123",
        type = "order_status_change",
        text = "Đơn hàng #1 đã được xác nhận",
        created_at = "2025-06-10T10:10:00Z",
        status = 0
    ),
    Notice(
        id = 3,
        idUser = "user123",
        type = "order_status_change",
        text = "Đơn hàng #2 đang xử lý",
        created_at = "2025-06-09T15:35:00Z",
        status = 1
    ),
    Notice(
        id = 4,
        idUser = "user123",
        type = "promotion",
        text = "Khuyến mãi 20% cho đơn hàng tiếp theo!",
        created_at = "2025-06-09T08:00:00Z",
        status = 1
    )
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, idUser: String?) {
    val coroutineScope = rememberCoroutineScope()
    var notices by remember { mutableStateOf(mockNotices) }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Tôi", "Admin")

    // Filter notices for each tab
    val userNotices = notices.filter { it.type == "order_status_change" }
    val adminNotices = notices.filter { it.type != "order_status_change" }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF5D9EFF)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        selectedContentColor = Color(0xFF5D9EFF),
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> { // Tôi (User) Tab
                    if (idUser == "" || userNotices.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không có thông báo đơn hàng",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(userNotices) { notice ->
                                NotificationItem(
                                    navController = navController,
                                    notice = notice,
                                    isAdminTab = false,
                                    onNoticeUpdated = { updatedNotice ->
                                        notices = notices.map { if (it.id == updatedNotice.id) updatedNotice else it }
                                    },
                                    onNoticeRemoved = { noticeId ->
                                        notices = notices.filter { it.id != noticeId }
                                    }
                                )
                            }
                        }
                    }
                }
                1 -> { // Admin Tab
                    if (adminNotices.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không có thông báo từ admin",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(adminNotices) { notice ->
                                NotificationItem(
                                    navController = navController,
                                    notice = notice,
                                    isAdminTab = true,
                                    onNoticeUpdated = { updatedNotice ->
                                        notices = notices.map { if (it.id == updatedNotice.id) updatedNotice else it }
                                    },
                                    onNoticeRemoved = { noticeId ->
                                        notices = notices.filter { it.id != noticeId }
                                    }
                                )
                            }
                        }
                    }
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
    isAdminTab: Boolean,
    onNoticeUpdated: (Notice) -> Unit,
    onNoticeRemoved: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
//                    if (notice.status == 1) {
//                        val updatedNotice = notice.copy(status = 0)
//                        onNoticeUpdated(updatedNotice)
//                    }
//                    if (notice.type == "order_status_change") {
//                        val orderId = notice.text.substringAfter("#").substringBefore(" ")
//                        navController.navigate("${Screen.Order_Detail.route}?id=${orderId}&totalAmount=0")
//                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notice.status == 1) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (notice.type == "order_status_change") "Đơn hàng #${notice.text.substringAfter("#").substringBefore(" ")}" else notice.type,
                            fontWeight = if (notice.status == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (notice.status == 1) {
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
                        text = notice.text,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = if (expanded) Int.MAX_VALUE else 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDate(notice.created_at),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    // Confirmation button only for admin tab with "đang chờ xác nhận"
                    if (isAdminTab && notice.type == "admin_order_confirmation" && notice.text.contains("đang chờ xác nhận")) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (notice.status == 1) {
                                        val updatedNotice = notice.copy(status = 0)
                                        onNoticeUpdated(updatedNotice)
                                    }
                                    onNoticeRemoved(notice.id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5D9EFF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Xác nhận đơn hàng")
                        }
                    }
                }
                // Dropdown icon only for user tab (order_status_change)
                if (notice.type == "order_status_change" && !isAdminTab) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle dropdown",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                expanded = !expanded
                                coroutineScope.launch {
                                    if (notice.status == 1) {
                                        val updatedNotice = notice.copy(status = 0)
                                        onNoticeUpdated(updatedNotice)
                                    }
                                }
                            }
                    )
                }
            }
            // Dropdown content only for user tab
            if (expanded && notice.type == "order_status_change" && !isAdminTab) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = notice.text,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = formatDate(notice.created_at),
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}