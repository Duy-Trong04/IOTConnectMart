package com.example.ungdungbanthietbi_iot.screen.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.data.cart.CartViewModel
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import java.text.DecimalFormat


/** Giao diện màn hình giỏ hàng (CartScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 05/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình giỏ hàng, có xu lý check chọn nhiều sản phẩm để mua hoặc xóa khỏi giỏ hàng và hiển thị tổng tiền.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    idCustomer:String,
    username:String
) {

    val cartViewModel:CartViewModel = viewModel()
    val deviceViewModel:DeviceViewModel = viewModel()

    // Lấy danh sách giỏ hàng và sản phẩm
    val listCart = cartViewModel.listCart

    // Biến lưu tổng tiền
    var totalPrice by remember { mutableStateOf(0.0) }

    // Biến lưu trạng thái checkbox của từng sản phẩm
    val selectedItems = remember { mutableStateMapOf<Int, Boolean>() }
    //Lưu thông tin sản phẩm để truyền qua màn hình thanh toán
    val selectedProducts = remember { mutableListOf<Triple<Int, Int, Int>>() }

    var showDialog by remember { mutableStateOf(false) }
    // Hàm tính tổng tiền
    fun calculateTotalPrice() {
        if (listCart.isEmpty()) {
            totalPrice = 0.0
        } else {
            totalPrice = listCart.filter { selectedItems[it.id] == true }.sumOf { giohang ->
                val device = deviceViewModel.listDeviceOfCustomer.find { it.idDevice == giohang.idDevice }
                val gia = device?.sellingPrice ?: 0.0
                gia * giohang.stock
            }
        }
    }
    //Hàm format tiền
    fun formatGiaTien(gia: Double): String {
        val formatter = DecimalFormat("#,###,###")
        return "${formatter.format(gia)}đ"
    }
    // Lấy dữ liệu và tính tổng tiền ban đầu
    LaunchedEffect(idCustomer) {
        cartViewModel.getCartByIdCustomer(idCustomer)
        deviceViewModel.getDeviceByCart(idCustomer)
        listCart.forEach { selectedItems[it.id] = false } // Mặc định không chọn sản phẩm nào
    }

    // Tính tổng tiền khi dữ liệu giỏ hàng hoặc sản phẩm thay đổi
    LaunchedEffect(listCart, deviceViewModel.listDeviceOfCustomer) {
        calculateTotalPrice() // Tính tổng tiền khi dữ liệu thay đổi
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Giỏ hàng(${listCart.size})", textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    // Nút quay lại
                    IconButton(onClick = {
                        cartViewModel.updateAllCart()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    // Hiển thị tổng giá thanh toán
                    Text(
                        "Tổng thanh toán: ${formatGiaTien(totalPrice)}",
                        style = TextStyle(color = Color.Red, fontSize = 18.sp)
                    )
                    val selectedProductsString = selectedProducts.joinToString(",") { "${it.first}:${it.second}:${it.third}" }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Nút mua hàng
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                           if(selectedProducts.isEmpty()){
                               showDialog = true // Hiển thị dialog nếu không có sản phẩm nào được chọn
                           }
                           else{
                                navController.navigate(Screen.Check_Out.route + "?selectedProducts=${selectedProductsString}&tongtien=${totalPrice}&username=${username}")
                           }
                        },
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.buttonElevation(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        )
                    ) {
                        Text("MUA HÀNG",
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Thông báo") },
                text = { Text("Vui lòng chọn sản phẩm để mua.") },
                confirmButton = {
                    Button(onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        )) {
                        Text("OK")
                    }
                }
            )
        }
        // Danh sách sản phẩm
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(listCart) { cart ->
                var soLuong by remember { mutableStateOf(cart.stock) }
                val sanPham = deviceViewModel.listDeviceOfCustomer.find { it.idDevice == cart.idDevice }

                var chieucaocard by remember { mutableStateOf(190) }
                if(sanPham != null){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(chieucaocard.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            navController.navigate(Screen.ProductDetailsScreen.route + "?id=${sanPham.idDevice}")
                        }
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            // Checkbox chọn sản phẩm
                            Checkbox(
                                checked = selectedItems[cart.id] == true,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF5D9EFF),
                                    uncheckedColor = Color.Gray,
                                ),
                                onCheckedChange = { isChecked ->
                                    selectedItems[cart.id] = isChecked

                                    if (isChecked) {
                                        // Thêm sản phẩm vào danh sách selectedProducts cùng với MaGioHang
                                        selectedProducts.add(Triple(cart.idDevice, cart.stock, cart.id))
                                    } else {
                                        // Loại bỏ sản phẩm khỏi danh sách
                                        selectedProducts.removeAll { it.first == cart.idDevice }
                                    }
                                    // Tính lại tổng tiền sau khi thay đổi trạng thái checkbox
                                    calculateTotalPrice()
                                },
                            )
                            // Hình ảnh sản phẩm (chỉ là giả)
                            AsyncImage(
                                model = sanPham.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp),
                                contentScale = ContentScale.Fit
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                // Tên sản phẩm
                                Text(text = sanPham.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                // Giá sản phẩm
                                Text(text = "Giá: ${formatGiaTien(sanPham.sellingPrice)}", color = Color.Red)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Nút giảm số lượng
                                    IconButton(onClick = {
                                        if (soLuong > 1) {
                                            soLuong-- // Giảm số lượng
                                            cart.stock = soLuong
                                            cartViewModel.updateAllCart()


                                            // Cập nhật lại số lượng trong selectedProducts
                                            val index = selectedProducts.indexOfFirst { it.first == cart.idDevice }
                                            if (index != -1) {
                                                selectedProducts[index] = Triple(cart.idDevice, soLuong, cart.id)
                                            }

                                            calculateTotalPrice() // Tính lại tổng tiền
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Remove,
                                            contentDescription = "Decrease Quantity"
                                        )
                                    }
                                    // Hiển thị số lượng
                                    Text(
                                        text = soLuong.toString(),
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        fontSize = 16.sp
                                    )
                                    // Nút tăng số lượng
                                    IconButton(onClick = {
                                        if (soLuong < 500 /* 500 là số lượng tồn kho*/) {
                                            soLuong++ // Tăng số lượng
                                            cart.stock = soLuong
                                            cartViewModel.updateAllCart()

                                            // Cập nhật lại số lượng trong selectedProducts
                                            val index = selectedProducts.indexOfFirst { it.first == cart.idDevice }
                                            if (index != -1) {
                                                selectedProducts[index] = Triple(cart.idDevice, soLuong, cart.id)
                                            }

                                            calculateTotalPrice() // Tính lại tổng tiền
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Increase Quantity"
                                        )
                                    }
                                }
                            }
                            // Nút xóa sản phẩm
                            IconButton(onClick = {
                                cartViewModel.deleteCart(cart.id)
                                cartViewModel.listCart = cartViewModel.listCart.filter { it.id != cart.id }
                                calculateTotalPrice()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Item"
                                )
                            }
                        }
                    }
                }

//                if(sanPham != null){
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .height(chieucaocard.dp),
//                        elevation = CardDefaults.cardElevation(4.dp),
//                        colors = CardDefaults.cardColors(containerColor = Color.White),
//                        onClick = {
//                            navController.navigate(Screen.ProductDetailsScreen.route + "?id=${sanPham.idDevice}")
//                        }
//                    ) {
//                        Row (
//                            modifier = Modifier
//                                .padding(5.dp)
//                                .fillMaxSize(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Start
//                        ){
//                            // Thêm checkbox
//                            Checkbox(
//
//                            )
//
//                            Column(
//                                horizontalAlignment = Alignment.Start,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Text(
//                                    sanPham.name,
//                                    fontWeight = FontWeight.Bold,
//                                    lineHeight = 25.sp,
//                                )
//                                Row (
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ){
//                                    Text(
//                                        "${formatGiaTien(sanPham.sellingPrice)}",
//                                        color = Color.Red,
//                                        fontSize = 17.sp
//                                    )
//
//                                    Row (
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.Start
//                                    ){
//                                        IconButton(onClick = {
//
//                                        }) {
//                                            Icon(Icons.Filled.RemoveCircleOutline, contentDescription = null)
//                                        }
//                                        Text(soLuong.toString())
//                                        IconButton(
//                                            onClick = {
//
//                                            }
//                                        ) {
//                                            Icon(Icons.Filled.AddCircleOutline, contentDescription = null)
//                                        }
//                                    }
//                                }
//                                Row (
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ){
//                                    Button(
//                                        onClick = {
//                                            cartViewModel.deleteCart(cart.id)
//                                            cartViewModel.listCart = cartViewModel.listCart.filter { it.id != cart.id }
//                                            calculateTotalPrice()
//                                        },
//                                        modifier = Modifier
//                                            .padding(11.dp)
//                                            .width(65.dp)
//                                            .height(35.dp),
//                                        colors = ButtonDefaults.buttonColors(Color.Red),
//                                        shape = RoundedCornerShape(12.dp)
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Filled.Delete,
//                                            contentDescription = "",
//                                            tint = Color.White,
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}