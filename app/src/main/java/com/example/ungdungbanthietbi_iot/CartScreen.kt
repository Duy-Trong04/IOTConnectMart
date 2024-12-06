package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//Nguoi Viet: Duy Trọng
//Ngay Viet: 05/12
//Input:
//Output:
//Thuat toan xu ly:
// Hiển thị màn hình giỏ hàng, có xu lý check chọn nhiều sản phẩm để mua hoặc xóa khỏi giỏ hàng và hiển thị tổng tiền.
//-------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {

    // Danh sách sản phẩm trong giỏ hàng (dữ liệu giả)
    val products = remember {
        mutableStateListOf(
            ProductState(1, "Product 1", 10000.0, mutableStateOf(1), "placeholder", mutableStateOf(false)),
            ProductState(2, "Product 2", 20000.0, mutableStateOf(2), "placeholder", mutableStateOf(false)),
            ProductState(3, "Product 3", 15000.0, mutableStateOf(1), "placeholder", mutableStateOf(false))
        )
    }

    // Tổng giá trị các sản phẩm được chọn
    val totalPrice by remember(products) {
        derivedStateOf {
            products
                .filter { it.isSelected.value }// Lọc các sản phẩm được chọn
                .sumOf { it.price * it.quantity.value }// Tính tổng giá
        }
    }

    // Số lượng sản phẩm được chọn
    val totalSelected by remember(products) {
        derivedStateOf {
            products.count { it.isSelected.value } // Đếm các sản phẩm có trạng thái được chọn
        }
    }

    // Trạng thái chọn tất cả sản phẩm - tự động cập nhật
    val isAllSelected by remember(products) {
        derivedStateOf {
            products.isNotEmpty() && products.all { it.isSelected.value }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Giỏ hàng", textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    // Nút xóa các sản phẩm được chọn
                    TextButton(
                        onClick = {
                            products.removeAll { it.isSelected.value }// Xóa sản phẩm được chọn khỏi danh sách
                        }
                    ) {
                        Text("Xóa", color = Color.White, fontSize = 16.sp)
                    }
                },
                navigationIcon = {
                    // Nút quay lại
                    IconButton(onClick = { /* Back action */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Thanh hiển thị tổng giá và nút mua hàng
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Checkbox chọn tất cả
                        Checkbox(
                            checked = isAllSelected,
                            onCheckedChange = { selected ->
                                products.forEach { it.isSelected.value = selected  }// Cập nhật trạng thái từng sản phẩm
                            }
                        )
                        Text("Tất cả", modifier = Modifier.padding(top = 13.dp))
                    }
                    // Hiển thị tổng giá thanh toán
                    Text(
                        "Tổng thanh toán: \$%.2f".format(totalPrice),
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Nút mua hàng
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* Checkout logic */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF) // Màu nền nút
                    )
                ) {
                    Text("MUA HÀNG ($totalSelected)") // Tên nút kèm theo số lượng sản phẩm đã chọn
                }
            }
        }
    ) { padding ->
        // Danh sách sản phẩm
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(products) { product ->
                CartItem(
                    product = product,
                    onRemove = { products.remove(product) },// Xóa sản phẩm khỏi danh sách
                    onQuantityChange = { newQuantity ->
                        product.quantity.value = newQuantity.coerceAtLeast(1)// Cập nhật số lượng
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}


//Nguoi Viet: Duy Trọng
//Ngay Viet: 05/12
//Input:
//Output:
//Thuat toan xu ly:
// Hiển thị danh sách sản phẩm có trong giỏ hàng gồm tên sản phẩm, giá, số lượng, hình ảnh, icon Delete và 1 ô checkbox để người dùng có
// thể mua nhiều hoặc xóa nhiều sản phẩm cùng 1 lúc.
//-------------------------
@Composable
fun CartItem(
    product: ProductState,// Dữ liệu sản phẩm
    onRemove: () -> Unit,// Callback để xóa sản phẩm
    onQuantityChange: (Int) -> Unit// Callback để thay đổi số lượng
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox chọn sản phẩm
        Checkbox(
            checked = product.isSelected.value,
            onCheckedChange = { selected ->
                product.isSelected.value = selected // Cập nhật trạng thái chọn
            }
        )
        // Hình ảnh sản phẩm (chỉ là giả)
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = "Product Image",
            modifier = Modifier.size(80.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            // Tên sản phẩm
            Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            // Giá sản phẩm
            Text(text = "Price: ${"$%.2f".format(product.price)}", color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Nút giảm số lượng
                IconButton(onClick = {
                    if (product.quantity.value > 1)
                        onQuantityChange(product.quantity.value - 1)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Decrease Quantity"
                    )
                }
                // Hiển thị số lượng
                Text(
                    text = product.quantity.value.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 16.sp
                )
                // Nút tăng số lượng
                IconButton(onClick = {
                    onQuantityChange(product.quantity.value + 1)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Increase Quantity"
                    )
                }
            }
        }
        // Nút xóa sản phẩm
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = "Remove Item"
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CartScreenPreview() {
//    UngDungBanThietBi_IOTTheme {
//        CartScreen()
//    }
//}