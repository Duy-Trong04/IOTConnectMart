@file:OptIn(ExperimentalPagerApi::class)

package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import com.example.ungdungbanthietbi_iot.data.Order
import kotlinx.coroutines.launch


/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Lịch sử mua hàng của người dùng
*/

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun OrderListScreen(onBack: () -> Unit = {},initialPage: Int = 0) {
    //Mục đích: Khởi tạo trạng thái của Pager và thiết lập trang ban
    // đầu dựa trên giá trị initialPage(vị trí tab)
    //Chức năng: Ghi nhớ và quản lý trạng thái của Pager
    // bao gồm trang hiện tại và hoạt động cuộn.
    val tabTitles = listOf("CHỜ XÁC NHẬN", "CHỜ LẤY HÀNG", "CHỜ GIAO HÀNG", "ĐÃ GIAO", "ĐÃ HỦY")
    val pagerState = rememberPagerState(initialPage)
    val scope = rememberCoroutineScope()
    //rememberCoroutineScope()
    // Mục đích: Tạo và ghi nhớ một phạm vi coroutine.
    //Chức năng: Cho phép khởi chạy và quản lý các coroutine trong phạm vi của composable
    // ví dụ như khi cần chuyển trang trong Pager.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Lịch sử mua hàng", fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(title) },
                            selectedContentColor = Color(0xFF5F9EFF),
                            unselectedContentColor = Color.Black,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalPager(
                    state = pagerState,
                    count = tabTitles.size,
                    //modifier = Modifier
                ) { page ->
                    val orders = getOrdersForTab(tabTitles[page])
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(orders) { order ->
                            OrderItem(order)
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderItem(order: Order) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDEFF0)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(order.Image),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(120.dp)
                        .padding(16.dp)
                )
                Column {
                    Text("Đơn hàng #${order.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tên Sản phẩm: ${order.nameproduct}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ngày: ${order.date}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tổng tiền: ${order.total}VND", fontSize = 16.sp, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Trạng thái: ${order.status}", fontSize = 16.sp)
                }
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = { /* Thực hiện chức năng Hoàn tiền/Trả hàng */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5F9EFF),
                        contentColor = Color.White
                    )
                ) {
                    Text("Hoàn tiền/Trả hàng")
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = { /* Thực hiện chức năng Mua lại */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5F9EFF),
                        contentColor = Color.White
                    )
                ) {
                    Text("Mua lại")
                }
            }
        }
    }
}

fun getOrdersForTab(status: String): List<Order> {
    return sampleOrders.filter { it.status.equals(status, ignoreCase = true) }
}

val sampleOrders = listOf(
    Order(1, "Bóng đèn", "01/12/2024", 150000.0, "Đã giao","https://paragon.com.vn/wp-content/uploads/2022/04/Bong-den-led-bulb-11w-E27-PBCB1130E27L-1.jpeg"),
    Order(2, "Cảm biến", "02/12/2024", 20000.0, "Chờ giao hàng","https://via.placeholder.com/150"),
    Order(3, "Camera", "03/12/2024", 300000.0, "Đã hủy","https://camerawifi.com.vn/wp-content/uploads/2020/03/camera-ezviz-C6N-1.jpg"),
    Order(4, "Đèn led", "04/12/2024", 450000.0, "Đã giao","https://via.placeholder.com/150"),
    Order(5, "Cảm biến", "05/12/2024", 500000.0, "Chờ xác nhận","https://via.placeholder.com/150"),
    Order(6, "Camera", "06/12/2024", 60000.0, "Chờ lấy hàng","https://via.placeholder.com/150"),
    Order(7, "Đèn", "07/12/2024", 70000.0, "Chờ giao hàng","https://via.placeholder.com/150"),
    Order(8, "Đèn LED", "08/12/2024", 80000.0, "Đã giao","https://via.placeholder.com/150")
)
