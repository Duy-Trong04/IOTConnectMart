package com.example.ungdungbanthietbi_iot.views.notification

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.ungdungbanthietbi_iot.utils.formatDate
import com.example.ungdungbanthietbi_iot.utils.formatDateTimeZone
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, idCustomer: String?, token: String?) {
    val noticeViewModel: NoticeViewModel = viewModel()

    val listNotices by noticeViewModel.listNotice.collectAsState()
    val isLoading by noticeViewModel.isLoading.collectAsState()

    LaunchedEffect (idCustomer){
        if (token != null) {
            noticeViewModel.getNotifications(token, "order")
        }
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
        Box(
            modifier = Modifier
                .padding(paddingValues) // Áp dụng padding từ Scaffold
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5D9EFF))
                }
            } else if (listNotices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp) // Padding ngang để tránh sát mép
                ) {
                    items(listNotices) { notice ->
                        NotificationItem(navController, notice, token, noticeViewModel)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun NotificationItem(
    navController: NavController,
    notice: Notice,
    token: String?,
    noticeViewModel: NoticeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var isReadUpdated by remember { mutableStateOf(false) }

    // Theo dõi trạng thái cập nhật is_read và reload danh sách
    LaunchedEffect(isReadUpdated) {
        if (isReadUpdated && token != null) {
            noticeViewModel.getNotifications(token, "order") // Load lại danh sách
            isReadUpdated = false // Reset trạng thái
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (notice.is_read == false && token != null) {
                    coroutineScope.launch {
                        noticeViewModel.readNotification(token, notice.id)
                        isReadUpdated = true // Đánh dấu đã cập nhật để trigger reload
                    }
                }
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notice.is_read == false) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                            text = if(notice.type == "order") "Đơn hàng" else "Khuyến mãi",
                            fontWeight = if (notice.is_read == true) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (notice.is_read == false) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    notice.text?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDateTimeZone(notice.created_at!!),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}