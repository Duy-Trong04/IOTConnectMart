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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.order.OrderViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import java.text.DecimalFormat
import java.util.Locale


/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Lịch sử mua hàng của người dùng
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(navController: NavController, idCustomer: String?) {
    var selectedTabIndexItem by remember { mutableStateOf(0) }
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

    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    val isLoading =  remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }


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
                        OrderItem(order,navController, false)
                    }
                }
            }
        }
    }
}

@Composable
fun HoanTatScreen(navController: NavController, idCustomer: String?){
    val orderViewModel:OrderViewModel = viewModel()

    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    val isLoading =  remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }


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
                        OrderItem(order,navController, false)
                    }
                }
            }
        }
    }
}

@Composable
fun ChoGiaoHangScreen(navController: NavController, idCustomer: String?){
    val orderViewModel:OrderViewModel = viewModel()

    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    val isLoading =  remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }

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
                        OrderItem(order,navController, false)
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

    // Quan sát danh sách hóa đơn thông qua StateFlow
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    // Trạng thái đang tải
    val isLoading = remember { mutableStateOf(false) }

    // Trạng thái lỗi (nếu có)
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Hàm gọi API để lấy danh sách hóa đơn

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
                        OrderItem(order,navController, false)
                    }
                }
            }
        }
    }
}


@Composable
fun ChoLayHangScreen(navController: NavController,idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()

    // Quan sát danh sách hóa đơn thông qua StateFlow
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    // Trạng thái đang tải
    val isLoading = remember { mutableStateOf(false) }

    // Trạng thái lỗi (nếu có)
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Hàm gọi API để lấy danh sách hóa đơn
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
                        OrderItem(order,navController, false)
                    }
                }
            }
        }
    }
}

@Composable
fun ChoXacNhanScreen(navController: NavController,idCustomer: String?) {
    val orderViewModel: OrderViewModel = viewModel()

    // Quan sát danh sách hóa đơn thông qua StateFlow
    val listOrder by orderViewModel.listOrderOfCustomer.collectAsState()

    // Trạng thái đang tải
    val isLoading = remember { mutableStateOf(false) }

    // Trạng thái lỗi (nếu có)
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Hàm gọi API để lấy danh sách hóa đơn

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
                        OrderItem(order,navController, true)
                    }
                }
            }
        }
    }
}


@Composable
fun formatDate(inputDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng từ API
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Định dạng đầu ra
        val date = inputFormat.parse(inputDate)
        date?.let { outputFormat.format(it) } ?: "Ngày không hợp lệ"
    } catch (e: Exception) {
        "Ngày không hợp lệ"
    }
}


@Composable
fun OrderItem(order: Order, navController: NavController, isCancel: Boolean) {

    var orderViewModel:OrderViewModel = viewModel()

    //format giá sản phẩm
    val formatter = DecimalFormat("#,###,###")
    val formattedPrice = formatter.format(order.totalAmount)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
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
            // Cột chứa thông tin hóa đơn
            Column(
                modifier = Modifier.weight(1f) // Cột chiếm không gian linh hoạt
            ) {
                Text(
                    text = "Mã đơn hàng: ${order.id}",
                )
                Text(text = "Ngày Đặt Hàng: ${formatDate(order.created_at)}")
                Text(text = "Tổng Tiền: ${formattedPrice}đ")
            }

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
                            var orderNew = Order(
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
    }
}
