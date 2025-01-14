package com.example.ungdungbanthietbi_iot.screen.check_out

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.address_book.AddressViewModel
import com.example.ungdungbanthietbi_iot.data.cart.CartViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.order.Order
import com.example.ungdungbanthietbi_iot.data.order.OrderViewModel
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetail
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetailViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/** Giao diện màn hình thanh toán (CheckoutScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 06/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Hiển thị màn hình thanh toán, có xu lý chọn phương thức thanh toán hoặc hiện thị 1 hoặc nhiều sản phẩm, tổng thanh toán
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController:NavController,
    selectedProducts: List<Triple<Int, Int, Int>>, // Thay đổi kiểu thành danh sách
    tongtien: Double,
    username:String
) {

    val ngayHienTai = LocalDate.now() // Lấy ngày hiện tại
    val formattedDate = ngayHienTai.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

    val deviceViewModel:DeviceViewModel = viewModel()
    val cartViewModel:CartViewModel = viewModel()
    val accountViewModel:AccountViewModel = viewModel()
    val addressViewModel:AddressViewModel = viewModel()
    val orderViewModel:OrderViewModel = viewModel()
    val orderDetailViewModel:OrderDetailViewModel = viewModel()
    val customerViewModel:CustomerViewModel = viewModel()


    // State để lưu danh sách sản phẩm đã lấy thông tin
    val listDevice by deviceViewModel.listDevice.collectAsState(initial = emptyList())

    // State để lưu trữ lựa chọn phương thức thanh toán
    var selectedPaymentMethod by remember { mutableStateOf("Thanh toán khi nhận hàng (COD)") }

    val addressDefault = addressViewModel.address
    val account = accountViewModel.account
    val customer = customerViewModel.customer

    LaunchedEffect(username) {
        accountViewModel.getUserByUsername(username)
    }

    if (account != null) {
        LaunchedEffect(account) {
            addressViewModel.getAddressDefault(account.idPerson, 1)

        }
    }

    if(addressDefault != null){
        LaunchedEffect (addressDefault){
            customerViewModel.getCustomerById(addressDefault.idCustomer)
        }
    }

    // Lấy thông tin sản phẩm khi màn hình được tạo
    LaunchedEffect(selectedProducts) {
        selectedProducts.forEach { triple ->
            deviceViewModel.getdeviceById2(triple.first.toString())
        }
    }

    //Hàm format tiền
    fun formatGiaTien(gia: Double): String {
        val formatter = DecimalFormat("#,###,###")
        return "${formatter.format(gia)}đ"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán", modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                ) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar (
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ){
                // Thanh hiển thị tổng giá và nút mua hàng
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tổng thanh toán: ${formatGiaTien(tongtien)}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        color = Color.Red
                    )
                    Button(
                        onClick = {
                            if(account != null && addressDefault != null){
                                // Lấy mã khách hàng và địa chỉ
                                val idPerson = account.idPerson ?: ""
                                val idAddress = "${addressDefault.street}, ${addressDefault.ward}, ${addressDefault.district}, ${addressDefault.city}, Việt Nam"
                                val phone = customer?.phone ?: ""
                                // Tạo đối tượng HoaDonBan
                                val order = Order(
                                    0, // id sẽ được tự động tạo khi insert vào DB
                                    idPerson,
                                    tongtien,
                                    selectedPaymentMethod,
                                    idAddress,
                                    "NULL",
                                    phone,
                                    "NULL",
                                    "NULL",
                                    "Mobile",
                                    formattedDate,
                                    formattedDate,
                                    formattedDate,
                                    "EMP000001",
                                    1 // Trạng thái thanh toán
                                )

                                // Thêm Order trước
                               orderViewModel.addOrder(order)

                                // Sau khi Order đã được thêm, tiếp tục thêm OrderDetail
                                selectedProducts.forEach{triple ->
                                    listDevice.forEach { device ->
                                        if(device.idDevice == triple.first){
                                            // Tạo đối tượng OrderDetail
                                            val orderDetail = OrderDetail(
                                                0,// id sẽ được tự động tạo khi insert vào DB
                                                0, // idOrder cần phải lấy từ bảng order sau khi insert
                                                device.idDevice,
                                                device.sellingPrice,
                                                triple.second,
                                                device.sellingPrice,
                                                0
                                            )

                                            //Thêm OrderDetail vào db
                                            orderDetailViewModel.addOrderDetail(orderDetail)
                                        }
                                    }
                                }
                                // Xóa các sản phẩm khỏi giỏ hàng
                                selectedProducts.forEach { triple ->
                                    cartViewModel.deleteCart(triple.third)
                                }
                            }
                            navController.navigate(Screen.CheckOutSuccess.route +"?username=${username}"){
                                popUpTo(0) { inclusive = true }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF)),
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                    ) {
                        Text("Đặt hàng", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(10.dp)
        ){
            item {
                if(addressDefault != null){
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column (
                                modifier = Modifier.fillMaxHeight().padding(10.dp),
                                verticalArrangement = Arrangement.Top
                            ){
                                Icon(
                                    imageVector = Icons.Filled.LocationOn, contentDescription = "",
                                    tint = Color.Red
                                )
                            }
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${customer?.surname} ${customer?.lastName}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Thay đổi",
                                        color = Color(0xFF5D9EFF),
                                        modifier = Modifier.clickable {

                                        }
                                    )
                                }

                                Text(
                                    text = customer?.phone.toString()
                                )

                                Text(
                                    text = "${addressDefault.street}, ${addressDefault.ward}, ${addressDefault.district}, ${addressDefault.city}, Việt Nam"
                                )
                            }
                        }
                    }
                }

            }
            items(listDevice) { device ->
                selectedProducts.forEach { triple ->
                    if (device.idDevice == triple.first)
                        DeviceItem(device, triple.second) // triple.second là số lượng
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(1.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Text(
                        text = "Phương thức thanh toán",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 5.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Thanh toán khi nhận hàng (COD)",
                                modifier = Modifier.padding(start = 25.dp)
                            )
                            RadioButton(
                                selected = selectedPaymentMethod == "Thanh toán khi nhận hàng (COD)",
                                onClick = {
                                    selectedPaymentMethod = "Thanh toán khi nhận hàng (COD)"
                                },
                                colors = RadioButtonDefaults.colors(
                                    unselectedColor = Color(0xFF5D9EFF),
                                    selectedColor = Color(0xFF5D9EFF)
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Chuyển khoản ngân hàng",
                                modifier = Modifier.padding(start = 25.dp)
                            )
                            RadioButton(
                                selected = selectedPaymentMethod == "Chuyển khoản ngân hàng",
                                onClick = {
                                    selectedPaymentMethod = "Chuyển khoản ngân hàng"
                                },
                                colors = RadioButtonDefaults.colors(
                                    unselectedColor = Color(0xFF5D9EFF),
                                    selectedColor = Color(0xFF5D9EFF)
                                )
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Momo",
                                modifier = Modifier.padding(start = 25.dp)
                            )
                            RadioButton(
                                selected = selectedPaymentMethod == "Momo",
                                onClick = {
                                    selectedPaymentMethod = "Momo"
                                },
                                colors = RadioButtonDefaults.colors(
                                    unselectedColor = Color(0xFF5D9EFF),
                                    selectedColor = Color(0xFF5D9EFF)
                                )
                            )
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .height(140.dp),
                    elevation = CardDefaults.cardElevation(1.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Chi tiết thanh toán",
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng tiền hàng")
                            Text(text = "${formatGiaTien(tongtien)}")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng tiền vận chuyển")
                            Text("0đ")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng thanh toán")
                            Text(text = formatGiaTien(tongtien))
                        }
                    }
                }
            }
        }
    }
}


/** Card chứa thông tin sản phẩm của màn hình thanh toán (CheckoutItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 06/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: device: Device, stock:Int
 *
 * Output: Hiển thị thông tin của 1 sản phẩm gồm tên, giá, số lượng và tổng tiền của phẩm đó
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun DeviceItem(
    device: Device,
    stock:Int
) {
    //Hàm format tiền
    fun formatGiaTien(gia: Double): String {
        val formatter = DecimalFormat("#,###,###")
        return "${formatter.format(gia)}đ"
    }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = device.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        device.name,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatGiaTien(device.sellingPrice),
                        )
                        Text(
                            "x$stock"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp, start = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng số tiền (${stock} sản phẩm)",
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = formatGiaTien(device.sellingPrice *stock),
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

