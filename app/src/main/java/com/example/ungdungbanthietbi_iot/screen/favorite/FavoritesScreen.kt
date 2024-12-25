package com.example.ungdungbanthietbi_iot.screen.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.Product

/** Giao diện màn hình yêu thích (FavoritesScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 05/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình sản phẩm yêu thích, có xử lý check chọn nhiều sản phẩm để thêm vào giỏ hàng
 * hoặc xóa khỏi danh sách yêu thích.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    // Danh sách sản phẩm trong giỏ hàng (dữ liệu giả)
    var products by remember {
        mutableStateOf(listOf(
            Product(1, "Product 1", 10000.0, 1, "placeholder", false),
            Product(2, "Product 2", 20000.0, 2, "placeholder", false),
            Product(3, "Product 3", 15000.0, 1, "placeholder", false),
            Product(4, "Product 4", 10000.0, 1, "placeholder", false),
            Product(5, "Product 5", 20000.0, 2, "placeholder", false),
            Product(6, "Product 6", 15000.0, 1, "placeholder", false),
            Product(7, "Product 7", 10000.0, 1, "placeholder", false),
        ))
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
                title = {
                    Text(
                        "Sản phẩm yêu thích",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                },
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
                // Thanh hiển thị tổng giá và nút mua hàng
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Checkbox chọn tất cả
                        Checkbox(
                            checked = isAllSelected,
                            onCheckedChange = { selected ->
                                products = products.map { it.copy(isSelected = selected) }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF5D9EFF),
                                uncheckedColor = Color.Gray
                            )
                        )
                        Text("Tất cả", modifier = Modifier.padding(top = 0.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Nút mua hàng
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { /* Checkout logic */ },
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.buttonElevation(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        )
                    ) {
                        Text("THÊM VÀO GIỎ HÀNG ($totalSelected)")
                    }
                }
            }
        }
    ) { padding ->
        // Danh sách sản phẩm
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(products) { product ->
                FavoritesItem(
                    product = product,
                    onRemove = {
                        products = products.filterNot { it.id == product.id }
                    },
                    onQuantityChange = { newQuantity ->
                        products = products.map {
                            if (it.id == product.id) it.copy(quantity = newQuantity.coerceAtLeast(1))
                            else it
                        }
                    },
                    onSelectChange = { selected ->
                        products = products.map {
                            if (it.id == product.id) it.copy(isSelected = selected)
                            else it
                        }
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

/** Card chứa thông tin sản phẩm yêu thích (FavoritesItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 05/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: product: ProductState,
 * onRemove: () -> Unit,
 * onQuantityChange: (Int) -> Unit,
 * onSelectChange: (Boolean) -> Unit
 *
 * Output: Hiển thị danh sách sản phẩm yêu thích gồm tên sản phẩm, giá, hình ảnh, icon Delete và 1 ô checkbox để người dùng có
 * thể thêm vào giỏ hàng nhiều hoặc xóa nhiều sản phẩm khỏi danh sách yêu thích cùng 1 lúc.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun FavoritesItem(
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
            onCheckedChange = onSelectChange,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                // Tên sản phẩm
                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "",
                    tint = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Giá sản phẩm
            Text(text = "Price: ${"$%.2f".format(product.price)}", color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
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
//fun CartScreenPreview() {
//    UngDungBanThietBi_IOTTheme {
//        FavoritesScreen()
//    }
//}