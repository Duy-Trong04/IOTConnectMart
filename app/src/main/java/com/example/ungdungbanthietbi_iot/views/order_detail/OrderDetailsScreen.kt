package com.example.ungdungbanthietbi_iot.views.order_detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
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
import com.example.ungdungbanthietbi_iot.utils.base64ToBitmap
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import kotlinx.coroutines.launch
import java.net.URLDecoder
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
    idOrder: String,
    totalAmount: Double,
    idCustomer: String
) {
    val orderViewModel: OrderViewModel = viewModel()
    val listOrder by orderViewModel.listOrders.collectAsState()
    // Decode idOrder to match order.id
    val decodedId = try {
        URLDecoder.decode(idOrder, "UTF-8")
    } catch (e: Exception) {
        Log.e("OrderDetailsScreen", "Error decoding idOrder: $idOrder", e)
        idOrder
    }
    // Load orders for customer
    LaunchedEffect(idCustomer) {
        Log.d("OrderDetailsScreen", "Loading orders for customer: $idCustomer")
        orderViewModel.getOrdersByCustomer(idCustomer) // Replace with actual customer ID
    }
    // Tìm đơn hàng từ listOrder dựa trên idOrder
    val order = listOrder?.data?.data?.find { it.id == decodedId }
    // Debug log
    LaunchedEffect(idOrder, listOrder) {
        Log.d(
            "OrderDetailsScreen",
            "idOrder: $idOrder, decodedId: $decodedId, order: ${order?.id}, " +
                    "listOrder size: ${listOrder?.data?.data?.size ?: 0}, " +
                    "listOrder ids: ${listOrder?.data?.data?.map { it.id } ?: "empty"}, " +
                    "idCustomer: $idCustomer"
        )
    }

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
            BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier.fillMaxWidth().height(100.dp)
            ) {
                if (order != null) {
                    Button(
                        onClick = {
                            // Vô hiệu hóa logic API, chỉ log hành động
                            Log.d("OrderDetailsScreen", "Xác nhận đã nhận hàng: ${order.id}")
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        enabled = order.status == 3,
                        elevation = ButtonDefaults.buttonElevation(2.dp)
                    ) {
                        Text(
                            text = "Xác nhận đã nhận hàng",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (order == null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5D9EFF))
            }
        } else if(listOrder?.data?.data?.isEmpty() == true){
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không tìm thấy đơn hàng",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.White)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(10.dp)
                        .background(Color.White)
                ) {
                    item {
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
                                text = when (order.status) {
                                    0 -> "Chờ xác nhận"
                                    1 -> "Đang chuẩn bị hàng"
                                    2 -> "Đang giao hàng"
                                    3 -> "Đã giao"
                                    4 -> "Hoàn tất"
                                    else -> "Đã hủy"
                                },
                                fontSize = 16.sp,
                                color = Color(0xFF00796B),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Thông tin người nhận",
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
                                Text(
                                    text = "Họ và tên: ${order.nameRecipient ?: "Không có thông tin"}",
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Số điện thoại: ${order.phone ?: "Không có thông tin"}",
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Địa chỉ: ${order.address}",
                                    fontSize = 16.sp
                                )
                            }
                        }
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
                                order.details.forEach { detail ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Hiển thị hình ảnh từ Base64
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
                                }
                                Divider()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
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
                    }
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