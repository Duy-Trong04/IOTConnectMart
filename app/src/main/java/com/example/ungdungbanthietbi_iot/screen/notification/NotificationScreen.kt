package com.example.ungdungbanthietbi_iot.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.notice.Notice
import com.example.ungdungbanthietbi_iot.data.notice.NoticeViewModel
import com.example.ungdungbanthietbi_iot.data.order.OrderViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatDate
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, idUser: String?) {
    val noticeViewModel: NoticeViewModel = viewModel()
    val listNotice = noticeViewModel.listNotice

    LaunchedEffect(Unit) {
        noticeViewModel.getNoticeByIdCustomer(idUser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (listNotice.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có thông báo nào ${idUser}",
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
                items(listNotice) { notification ->
                    NotificationItem(navController, notification)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(navController: NavController, notice: Notice) {
    val orderViewModel:OrderViewModel = viewModel()
    val noticeViewModel:NoticeViewModel = viewModel()

    val listOrders by orderViewModel.listAllOrderOfCustomer.collectAsState()
    LaunchedEffect (Unit) {
        orderViewModel.getAllOrderByCustomer(notice.idUser)
    }
    val matchingOrder = listOrders.find { order ->
        order.created_at == notice.created_at // Điều kiện so sánh
    }
    val idOrder = matchingOrder?.id
    val totalAmount = matchingOrder?.totalAmount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                val noticeUpdate = notice.copy(
                    idRole = "NULL",
                    status = 0
                )
                noticeViewModel.updateNotice(noticeUpdate)
                navController.navigate("${Screen.Order_Detail.route}?id=${idOrder}&totalAmount=${totalAmount}")
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
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