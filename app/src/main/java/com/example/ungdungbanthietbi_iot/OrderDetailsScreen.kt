package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme

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
fun OrderDetailsScreen(navController: NavController) {
    // Data class representing information about the receiver
    data class ReceiverInfo(val name: String, val phone: String, val address: String) //Lưu trữ thông tin của người nhận đơn hàng (tên, số điện thoại, địa chỉ).

    // Mã vận đơn và danh sách trạng thái đơn hàng (giả)
    val orderCode = "123456789"
    // Danh sách trạng thái đơn hàng
    val orderStatus = listOf("Đang xử lý", "Chờ lấy hàng", "Đang giao hàng", "Đã giao hàng")
    // Thông tin người nhận
    val receiver = ReceiverInfo("Trần Thị B", "0987654321", "456 Đường XYZ, Quận 1, TP. HCM")
    // Lưu trạng thái các sản phẩm trong đơn hàng
    var products by remember {
        mutableStateOf(listOf(
            ProductState(1, "Sản phẩm 1", 10000.0, 1, "placeholder", false),
            ProductState(2, "Sản phẩm 2", 20000.0, 2, "placeholder", false),
            ProductState(3, "Sản phẩm 3", 30000.0, 1, "placeholder", false),
        ))
    }
    // Tính tổng tiền của các sản phẩm trong đơn hàng
    val totalAmount = products.sumOf { it.quantity * it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Thông tin đơn hàng",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
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
            BottomAppBar (
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nút "Hủy đơn hàng"
                        Button(
                            onClick = { /* Xử lý hủy đơn hàng */ },
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(end = 4.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5D9EFF)
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp)
                        ) {
                            Text(
                                text = "Hủy đơn hàng", color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        // Nút "Xác nhận đã nhận hàng"
                        Button(
                            onClick = { /* Xử lý xác nhận */ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5D9EFF)
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp)
                        ) {
                            Text(
                                text = "Xác nhận đã nhận hàng", color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) {
        LazyColumn (
            modifier = Modifier.padding(it).padding(16.dp)
        ){
            item{
                // Mã vận đơn
                Text(
                    text = "Mã vận đơn: ${orderCode}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
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
                        text = orderStatus[1],
                        fontSize = 14.sp,
                        color = Color(0xFF00796B),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Thông tin người nhận
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Thông tin người nhận",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextButton(onClick = {} ){
                        Text(text = "Sửa", fontSize = 16.sp,
                            color = Color(0xFF5D9EFF),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Tên: ${receiver.name}", fontSize = 14.sp)
                        Text(text = "Số điện thoại: ${receiver.phone}", fontSize = 14.sp)
                        Text(text = "Địa chỉ: ${receiver.address}", fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Chi tiết sản phẩm
                Text(
                    text = "Chi tiết sản phẩm",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        products.forEach { product ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Thêm hình ảnh sản phẩm
//                                Image(
//                                   painter = painterResource(id = R.drawable.ic_placeholder),
//                                   contentDescription = product.name,
//                                   modifier = Modifier
//                                       .height(40.dp)
//                                       .padding(end = 8.dp)
//                               )
                                    Image(
                                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                        contentDescription = "Product Image",
                                        modifier = Modifier.size(80.dp)
                                    )
                                    Text(
                                        text = product.name,
                                        fontSize = 16.sp
                                    )
                                }
                                Text(
                                    text = "${product.quantity}x  ${product.price}",
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Divider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tổng cộng",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            // Tổng tiền
                            Text(
                                text = "${totalAmount}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun OrderDetailsScreenPreview() {
//    UngDungBanThietBi_IOTTheme {
//        OrderDetailsScreen()
//    }
//}