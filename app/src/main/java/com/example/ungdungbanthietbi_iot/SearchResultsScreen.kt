package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme

/** Giao diện màn hình kết qua tìm kiếm (SearchResultsScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 08/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình tìm kiếm sản phẩm với thanh tìm kiếm, các tab lọc (Liên quan, Mới nhất, Bán chạy, Giá),
 * và danh sách sản phẩm dạng lưới.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchResultsScreen() {

    // Danh sách các sản phẩm (giả lập)
    var products by remember {
        mutableStateOf(listOf(
            ProductState(1, "Product 1", 10000.0, 1, "placeholder", false),
            ProductState(2, "Product 2", 20000.0, 2, "placeholder", false),
            ProductState(3, "Product 3", 15000.0, 1, "placeholder", false),
            ProductState(4, "Product 4", 10000.0, 1, "placeholder", false),
            ProductState(5, "Product 5", 20000.0, 2, "placeholder", false),
            ProductState(6, "Product 6", 15000.0, 1, "placeholder", false),
            ProductState(7, "Product 7", 10000.0, 1, "placeholder", false),
            ProductState(8, "Product 8", 20000.0, 2, "placeholder", false),
            ProductState(9, "Product 9", 15000.0, 1, "placeholder", false)
        ))
    }

    // Scaffold cung cấp bố cục cơ bản với TopAppBar
    Scaffold(

        // Thanh tiêu đề
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Ô nhập để tìm kiếm sản phẩm
                        OutlinedTextField(
                            value = "",// Giá trị trong ô nhập
                            onValueChange = {},// Hàm xử lý khi thay đổi văn bản
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text(text = "Produc 1")},// Gợi ý trong ô nhập
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start
                            ),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )

                        // Nút biểu tượng lọc sản phẩm
                        IconButton(onClick = {/* Perform search */}) {
                            Icon(imageVector = Icons.Filled.FilterAlt, contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                },

                // Nút quay lại
                navigationIcon = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),// Màu nền
                    titleContentColor = Color.White,// Màu chữ
                    navigationIconContentColor = Color.White// Màu biểu tượng
                )
            )
        }
    ) { padding ->
        var selectedTabIndex by remember { mutableStateOf(0) }// Lưu trạng thái tab được chọn

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            // Hàng tab cho các loại lọc sản phẩm
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    text = { Text("Liên quan") },
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                )
                Tab(
                    text = { Text("Mới nhất") },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                )
                Tab(
                    text = { Text("Bán chạy") },
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 }
                )
                Tab(
                    text = { Text("Giá") },
                    selected = selectedTabIndex == 3,
                    onClick = { selectedTabIndex = 3 }
                )
            }

            // Nội dung theo từng tab
            when (selectedTabIndex) {
                0 -> {
                    // Hiển thị danh sách sản phẩm
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),// Hiển thị 2 cột
                        contentPadding = PaddingValues(horizontal = 16.dp)// Padding hai bên
                    ) {
                        items(products.chunked(2)) { productPair ->// Chia nhóm 2 sản phẩm
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                productPair.forEach { product ->
                                    ProductCard(productState = product) // Hiển thị mỗi sản phẩm
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(products.chunked(2)) { productPair ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                productPair.forEach { product ->
                                    ProductCard(productState = product)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(products.chunked(2)) { productPair ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                productPair.forEach { product ->
                                    ProductCard(productState = product)
                                }
                            }
                        }
                    }
                }
                3 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(products.chunked(2)) { productPair ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                productPair.forEach { product ->
                                    ProductCard(productState = product)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/** Card chứa thông tin sản phẩm của giao diện tìm kiếm (ProductCard)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 08/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: productState: ProductState
 *
 * Output: Hiển thị chi tiết sản phẩm gồm: ảnh, tên, và giá trong một thẻ giao diện.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun ProductCard(productState: ProductState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Hình ảnh sản phẩm
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxWidth().size(150.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = productState.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${productState.price}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SearchResultsScreenPreview() {
//    UngDungBanThietBi_IOTTheme {
//        SearchResultsScreen()
//    }
//}