package com.example.ungdungbanthietbi_iot.views.order_detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.OrderViewModel
import com.example.ungdungbanthietbi_iot.viewModels.OrderDetailViewModel
import com.example.ungdungbanthietbi_iot.models.Review
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/** Giao diện màn hình chi tiết đơn hàng (OrderDetailsScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 11/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: navController: NavController
 *
 * Output: Hiển thị chi tiết đơn hàng, bao gồm thông tin người nhận,
 * danh sách sản phẩm và tổng tiền đơn hàng. Người dùng có thể hủy hoặc xác nhận đơn hàng.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    navController: NavController,
    idOrder: Int,
    totalAmount: Double
) {
    val orderViewModel: OrderViewModel = viewModel()
    val orderDetailViewModel: OrderDetailViewModel = viewModel()
    val addressViewModel: AddressViewModel = viewModel()
    val deviceViewModel: DeviceViewModel = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()
    val reviewViewModel: ReviewViewModel = viewModel()

    val order = orderViewModel.order
    val address = addressViewModel.address
    val listOrderDetail = orderDetailViewModel.listOrderDetail
    val listDevice = deviceViewModel.listDeviceByOrder
    val customer = customerViewModel.customer

    // Map để lưu trạng thái đánh giá cho từng sản phẩm
    var reviewState by remember { mutableStateOf<Map<Int, Pair<Review?, Review?>>>(emptyMap()) }

    LaunchedEffect(idOrder) {
        orderViewModel.getOrderById(idOrder)
        deviceViewModel.getDeviceByIdOrder(idOrder)
    }
    if (order != null) {
        LaunchedEffect(idOrder) {
            //addressViewModel.getAddressByIdOrder(idOrder)
            customerViewModel.getCustomerByIdOrder(idOrder)
        }
    }
    LaunchedEffect(idOrder) {
        orderDetailViewModel.getOrderDetailByIdOrder(idOrder)
    }

    // Cập nhật trạng thái đánh giá cho từng sản phẩm
    LaunchedEffect(listDevice, customer) {
        if (customer != null && listDevice.isNotEmpty()) {
            val newReviewState = mutableMapOf<Int, Pair<Review?, Review?>>()
            listDevice.forEach { device ->
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

    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Thông tin đơn hàng",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (order?.status == 4) {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                ) {
                    Button(
                        onClick = {
                            val orderNew = order.copy(
                                updated_at = getCurrentTimestamp(),
                                accept_at = getCurrentTimestamp(),
                                status = 5
                            )
                            orderViewModel.updateOrder(orderNew)

                            scope.launch {
                                listOrderDetail.forEach { device ->
                                    val reviewFromApi = reviewViewModel.checkReviewDirect(
                                        idCustomer = customer!!.id,
                                        idDevice = device.idDevice,
                                        status = 1
                                    )
                                    Log.e("Review", "reviewFromApi = $reviewFromApi")

                                    if (reviewFromApi != null) {
                                        val reviewUpdate = reviewFromApi.copy(
                                            status = 2
                                        )
                                        reviewViewModel.updateReview(reviewUpdate)
                                    }
                                }
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        elevation = ButtonDefaults.buttonElevation(2.dp)
                    ) {
                        Text(
                            text = "Xác nhận đã nhận hàng",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            } else {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                ) {}
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {
            item {
                if (address != null) {
                    Text(
                        text = "Mã đơn hàng: #${idOrder}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE0F7FA)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (order?.status) {
                                1 -> "Chờ xác nhận"
                                2 -> "Đang chuẩn bị hàng"
                                3 -> "Đang giao hàng"
                                4 -> "Đã giao"
                                5 -> "Hoàn tất"
                                else -> "Đã hủy"
                            },
                            fontSize = 16.sp,
                            color = Color(0xFF00796B),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Thông tin người nhận",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Card(
                        shape = RoundedCornerShape(5.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Họ và tên: ${order!!.nameRecipient}",
                                fontSize = 16.sp
                            )
                            Text(text = "Số điện thoại: ${order.phone}", fontSize = 16.sp)
                            Text(
                                text = "Địa chỉ: ${order.address}",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            item {
                if (order != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Danh sách sản phẩm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(5.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            listDevice.forEach { device ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(4.dp),
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
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = formatGiaTien(device.sellingPrice),
                                                    fontSize = 14.sp,
                                                    color = Color.Red
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                for (detail in listOrderDetail) {
                                                    if (detail.idDevice == device.idDevice) {
                                                        Text(
                                                            text = "x${detail.stock}",
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
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
                                            val reviewToEdit = reviewSecond ?: reviewFirst
                                            // Kiểm tra thời gian từ khi tạo đánh giá
                                            val daysSinceReviewCreated = reviewToEdit?.created_at?.let { createdAt ->
                                                calculateDaysSinceReceived(createdAt)
                                            } ?: Int.MAX_VALUE
                                            if (daysSinceReviewCreated <= 10) {
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
                            Divider()
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tổng tiền hàng",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = formatGiaTien(totalAmount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tổng tiền vận chuyển",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "0 VNĐ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Thành tiền",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = formatGiaTien(totalAmount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Red
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(16.dp),
                        color = Color(0xFF5D9EFF)
                    )
                }
            }
        }
    }
}

fun calculateDaysSinceReceived(receivedDate: String?): Int {
    return try {
        if (receivedDate.isNullOrEmpty()) {
            Log.e("CalculateDays", "receivedDate is null or empty")
            0
        } else {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val pastDate = formatter.parse(receivedDate)
            val currentTime = Calendar.getInstance().time
            val diffInMillis = currentTime.time - pastDate.time
            (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
        }
    } catch (e: Exception) {
        Log.e("CalculateDays", "Invalid receivedDate format: $receivedDate", e)
        0
    }
}