package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//Nguoi Viet: Duy Trọng
//Ngay Viet: 06/12
//Input:
//Output:
//Thuat toan xu ly:
// Hiển thị màn hình thanh toán, có xu lý chọn phương thức thanh toán hoặc hiện thị 1 hoặc nhiều sản phẩm,
// tổng thanh toán
//-------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen() {
    // Danh sách sản phẩm (fake)
    val products = remember {
        mutableStateListOf(
            ProductState(1, "Sản phẩm 1", 100000.0, 1, "placeholder", true),
            ProductState(2, "Sản phẩm 2", 150000.0, 2, "placeholder", true),
            ProductState(3, "Sản phẩm 1", 100000.0, 1, "placeholder", true),
            ProductState(4, "Sản phẩm 2", 150000.0, 2, "placeholder", true),
            ProductState(5, "Sản phẩm 1", 100000.0, 1, "placeholder", true),
            ProductState(6, "Sản phẩm 2", 150000.0, 2, "placeholder", true)
        )
    }

    // Địa chỉ giao hàng
    var address by remember { mutableStateOf("123 Đường ABC, Phường 1, Quận 1, TP.HCM") }

    // Tính tổng tiền
    val totalAmount by remember(products) {
        derivedStateOf {
            products.filter { it.isSelected }.sumOf { it.price * it.quantity }
        }
    }

    // Phương thức thanh toán
    var selectedPaymentMethod by remember { mutableStateOf("Thanh toán khi nhận hàng (COD)") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán", modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                ) },
                navigationIcon = {
                    IconButton(onClick = { /* Quay lại */ }) {
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
                modifier = Modifier.fillMaxWidth().height(180.dp)
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
                        "Tổng thanh toán: ${"%,.0f".format(totalAmount)}đ",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Button(
                        onClick = { /* Xử lý thanh toán */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF)),
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.buttonElevation(5.dp),
                    ) {
                        Text("Đặt hàng", color = Color.White)
                    }
                }
            }
        }
    ) {
        LazyColumn (
            modifier = Modifier.padding(it)
        ){
            item {
                // Địa chỉ giao hàng
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F8F8))
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "",
                                    tint = Color.Red
                                )
                                Text("Trương Duy Trọng", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(address, color = Color.Gray)
                        }
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(imageVector = Icons.Filled.ChevronRight,
                                contentDescription = ""
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Danh sách sản phẩm
            items(products) { product ->
                CheckoutItem(
                    product = product
                )
            }

            item {
                Spacer(modifier = Modifier.height(5.dp))

                // Phương thức thanh toán
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text("Phương thức thanh toán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(0.dp))

                    val paymentMethods = listOf("Thanh toán khi nhận hàng (COD)", "Momo")
                    paymentMethods.forEach { method ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = method }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == method,
                                onClick = { selectedPaymentMethod = method }
                            )
                            Text(text = method, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            }
        }
    }
}


//Nguoi Viet: Duy Trọng
//Ngay Viet: 06/12
//Input:
//Output:
//Thuat toan xu ly:
// Hiển thị thông tin của 1 sản phẩm gồm tên, giá, số lượng và tổng tiền của phẩm đó
//-------------------------
@Composable
fun CheckoutItem(
    product: ProductState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = "Product Image",
            modifier = Modifier.size(80.dp)
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)) {
            Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Giá: ${"%,.0f".format(product.price)}đ", color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Số lượng: ${product.quantity}")
        }
        Text(
            text = "Tổng: ${"%,.0f".format(product.price * product.quantity)}đ",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CheckoutScreenPreview() {
//    CheckoutScreen()
//}
