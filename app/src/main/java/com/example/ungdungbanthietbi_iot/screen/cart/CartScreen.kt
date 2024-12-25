package com.example.ungdungbanthietbi_iot.screen.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.Product
import com.example.ungdungbanthietbi_iot.navigation.Screen


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
fun CartScreen(navController: NavController) {
    // Danh sách sản phẩm trong giỏ hàng (dữ liệu giả)
    var products by remember {
        mutableStateOf(
            listOf(
                Product(1, "Product 1", 10000.0, 1, "placeholder", false),
                Product(2, "Product 2", 20000.0, 2, "placeholder", false),
                Product(3, "Product 3", 15000.0, 1, "placeholder", false)
            )
        )
    }

    // Tổng giá trị các sản phẩm được chọn
    val totalPrice by remember(products) {
        derivedStateOf {
            products
                .filter { it.isSelected }
                .sumOf { it.price * it.quantity }
        }
    }

    // Số lượng sản phẩm được chọn
    val totalSelected by remember(products) {
        derivedStateOf {
            products.count { it.isSelected }
        }
    }

    // Trạng thái chọn tất cả sản phẩm - tự động cập nhật
    val isAllSelected by remember(products) {
        derivedStateOf {
            products.isNotEmpty() && products.all { it.isSelected }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Giỏ hàng", textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
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
                            products = products.filterNot { it.isSelected }
                        }
                    ) {
                        Text("Xóa", color = Color.White, fontSize = 16.sp)
                    }
                },
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
                modifier = Modifier.fillMaxWidth().height(165.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Checkbox chọn tất cả
                            Checkbox(
                                checked = isAllSelected,
                                onCheckedChange = { selected ->
                                    products = products.map { it.copy(isSelected = selected) }.toMutableList()
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF5D9EFF),
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(text = "Tất cả")
                        }
                        // Hiển thị tổng giá thanh toán
                        Text(
                            "Tổng thanh toán: ${totalPrice} VNĐ",
                            style = TextStyle(color = Color.Black, fontSize = 16.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Nút mua hàng
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if(totalSelected > 0){
                                navController.navigate(Screen.Check_Out.route)
                            }
                        },
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.buttonElevation(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        )
                    ) {
                        Text("MUA HÀNG ($totalSelected)")
                    }
                }
            }
        }
    ) { padding ->
        // Danh sách sản phẩm
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(products) { product ->
                CartItem(
                    product = product,
                    onRemove = {
                        products = products.filter { it.id != product.id }.toMutableList()
                    },
                    onQuantityChange = { newQuantity ->
                        products = products.map {
                            if (it.id == product.id) it.copy(quantity = newQuantity.coerceAtLeast(1))
                            else it
                        }.toMutableList()
                    },
                    onSelectChange = { selected ->
                        products = products.map {
                            if (it.id == product.id) it.copy(isSelected = selected)
                            else it
                        }.toMutableList()
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

/** Card chứa thông tin từng của sản phẩm trong giỏ hàng (CartItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 05/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *  product: ProductState,
 *  onRemove: () -> Unit,
 *  onQuantityChange: (Int) -> Unit,
 *  onSelectChange: (Boolean) -> Unit
 *
 * Output: Hiển thị danh sách sản phẩm có trong giỏ hàng gồm tên sản phẩm, giá, số lượng, hình ảnh,
 * icon Delete và 1 ô checkbox để người dùng có thể mua nhiều hoặc xóa nhiều sản phẩm cùng 1 lúc.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun CartItem(
    product: Product,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    onSelectChange: (Boolean) -> Unit
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
            checked = product.isSelected,
            onCheckedChange = { selected ->
                onSelectChange(selected)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF5D9EFF),
                uncheckedColor = Color.Gray
            )
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
            Text(text = "Giá: ${product.price} VNĐ", color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Nút giảm số lượng
                IconButton(onClick = {
                    if (product.quantity > 1)
                        onQuantityChange(product.quantity - 1)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Decrease Quantity"
                    )
                }
                // Hiển thị số lượng
                Text(
                    text = product.quantity.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 16.sp
                )
                // Nút tăng số lượng
                IconButton(onClick = {
                    onQuantityChange(product.quantity + 1)
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
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove Item"
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CartScreenPreview1() {
//    UngDungBanThietBi_IOTTheme {
//        CartScreen()
//    }
//}