package com.example.ungdungbanthietbi_iot.screen.order_detail

import androidx.compose.foundation.clickable
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
import com.example.ungdungbanthietbi_iot.data.address_book.AddressViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.order.Order
import com.example.ungdungbanthietbi_iot.data.order.OrderViewModel
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetailViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import java.text.DecimalFormat

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
    idOrder:Int,
    totalAmount:Double
) {
    val orderViewModel:OrderViewModel = viewModel()
    val orderDetailViewModel:OrderDetailViewModel = viewModel()
    val addressViewModel:AddressViewModel = viewModel()
    val deviceViewModel:DeviceViewModel = viewModel()
    val customerViewModel:CustomerViewModel = viewModel()
    val reviewViewModel:ReviewViewModel = viewModel()


    var order = orderViewModel.order
    var address = addressViewModel.address
    var listOrderDetail = orderDetailViewModel.listOrderDetail
    var listDevice = deviceViewModel.listDeviceByOrder
    var customer = customerViewModel.customer

    LaunchedEffect(idOrder) {
        orderViewModel.getOrderById(idOrder)
        deviceViewModel.getDeviceByIdOrder(idOrder)
    }
    if (order != null) {
        LaunchedEffect(idOrder) {
            addressViewModel.getAddressByIdOrder(idOrder)
            customerViewModel.getCustomerByIdOrder(idOrder)
        }
    }
    LaunchedEffect(idOrder) {
        orderDetailViewModel.getOrderDetailByIdOrder(idOrder)
    }

    //format giá sản phẩm
    val formatter = DecimalFormat("##,###,###")
    val formattedPrice = formatter.format(totalAmount)

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
                    // Nút quay lại
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
            if(order?.status == 4){
                BottomAppBar (
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                ){
                    // Nút "Xác nhận đã nhận hàng"
                    Button(
                        onClick = {
                            var orderNew = Order(
                                order!!.id,
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
                                5)
                            orderViewModel.updateOrder(orderNew)

                            // Sau khi cập nhật đơn hàng, tạo đánh giá cho từng sản phẩm trong order_detail
                            listOrderDetail?.forEach { device ->
                                val review = Review(
                                    idReview = 0, // Giả sử id sẽ được tự sinh khi lưu vào database
                                    idCustomer = customer?.id ?: "",
                                    idEmployee = "Null", // Nếu có idEmployee cụ thể, cập nhật tương ứng
                                    idDevice = device.idDevice,
                                    comment = "", // Để trống, người dùng có thể cập nhật sau
                                    rating = 0,   // Giá trị mặc định, có thể thay đổi sau khi người dùng đánh giá
                                    response = "Null",
                                    note = "Null",
                                    created_at = getCurrentTimestamp(), // Hàm lấy thời gian hiện tại
                                    updated_at = getCurrentTimestamp(),
                                    status = 0 // Ví dụ: 1 là trạng thái "chưa đánh giá" hoặc trạng thái mặc định
                                )
                                reviewViewModel.addReview(review)
                            }
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),

                        elevation = ButtonDefaults.buttonElevation(2.dp)
                    ) {
                        Text(
                            text = "Xác nhận đã nhận hàng", color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            else {
                BottomAppBar (
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                ){}
            }
        }
    ) { padding ->
        LazyColumn (
            modifier = Modifier.padding(padding).padding(16.dp)
        ){
            item {
                if (address != null) {
                    // Mã vận đơn
                    Text(
                        text = "Mã đơn hàng: #${idOrder}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Trạng thái đơn hàng
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE0F7FA)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (order?.status == 1) {
                                "Chờ xác nhận"
                            } else if (order?.status == 2) {
                                "Chờ lấy hàng"
                            } else if (order?.status == 3) {
                                "Chờ giao hàng"
                            } else if (order?.status == 4) {
                                "Đã giao"
                            } else if (order?.status == 5) {
                                "Hoàn tất"
                            } else {
                                "Đã hủy"
                            },
                            fontSize = 16.sp,
                            color = Color(0xFF00796B),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Thông tin người nhận
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
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Họ và tên: ${customer?.surname} ${customer?.lastName}",
                                fontSize = 16.sp
                            )
                            Text(text = "Số điện thoại: ${customer?.phone}", fontSize = 16.sp)
                            Text(
                                text = "Địa chỉ: ${address.street}, ${address.ward}, ${address.district}, ${address.city}",
                                fontSize = 16.sp
                            )
                        }
                    }
                }

            }
            item {
                if(listDevice != null){
                    Spacer(modifier = Modifier.height(16.dp))
                    // Chi tiết sản phẩm
                    Text(
                        text = "Danh sách sản phẩm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
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
                                        Text(
                                            text = device.name,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Text(
                                        text = "${formatter.format(device.sellingPrice)}đ",
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    for (detail in listOrderDetail) {
                                        if (detail.idDevice == device.idDevice) {
                                            Text(
                                                text = "x${detail.stock}",
                                                fontSize = 16.sp
                                            )
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
                                // Tổng tiền
                                Text(
                                    text = "${formattedPrice}đ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
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
                                // Tổng tiền
                                Text(
                                    text = "0đ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
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
                                // Tổng tiền
                                Text(
                                    text = "${formattedPrice}đ",
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

// Hàm giả lập lấy thời gian hiện tại, bạn có thể thay thế bằng cách lấy thời gian theo chuẩn của hệ thống
fun getCurrentTimestamp(): String {
    // Ví dụ: trả về thời gian hiện tại theo định dạng "yyyy-MM-dd HH:mm:ss"
    val current = java.util.Calendar.getInstance().time
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return formatter.format(current)
}