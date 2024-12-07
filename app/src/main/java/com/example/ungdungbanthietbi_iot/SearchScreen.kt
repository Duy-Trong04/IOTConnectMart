package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//Nguoi Viet: Duy Trọng
//Ngay Viet: 07/12
//Input: tên sản phẩm cần tìm
//Output: hiển từ khóa trùng với từ khóa mà người dùng vừa nhập
//Thuat toan xu ly:
// hiển thị giao diện tìm kiếm sản phẩm, quản lý lịch sử tìm kiếm,
// gợi ý từ khóa, và hiển thị kết quả tìm kiếm dựa trên từ khóa nhập vào
//-------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(){
    // Biến lưu giá trị từ ô tìm kiếm
    var searchQuery by remember { mutableStateOf("") }

    // Lịch sử tìm kiếm được lưu trữ trong danh sách
    val searchHistory = remember { mutableStateListOf("Sản phẩm A", "Sản phẩm B", "Sản phẩm C") }

    // Kết quả tìm kiếm (fake)
    val searchResults = listOf(
        ProductState(1, "Sản phẩm A", 100000.0, 1, "placeholder",true),
        ProductState(2, "Sản phẩm B", 1500000.0, 1, "placeholder", true),
        ProductState(3, "Sản phẩm C", 500000.0, 1, "placeholder", true)
    )

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

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        // Ô nhập liệu tìm kiếm
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {searchQuery = it},
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text(text = "Tìm kiếm", color = Color.LightGray) },
                            textStyle = TextStyle(
                                fontSize = 16.sp, // Kích thước chữ phù hợp
                                textAlign = TextAlign.Start // Canh chữ bắt đầu từ bên trái
                            ),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )
                        // Icon tìm kiếm
                        IconButton(onClick = {/* Thực hiện tìm kiếm */}) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "",
                                tint = Color.White,
                            )
                        }
                    }

                },
                navigationIcon = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ){
        // Nội dung màn hình chính
        Column(modifier = Modifier.padding(it).fillMaxSize()) {
            // Nếu không có từ khóa tìm kiếm, hiển thị gợi ý
            if (searchQuery.isEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Lịch sử tìm kiếm
                    item {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                "Lịch sử tìm kiếm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                            TextButton(
                                onClick = { searchHistory.clear() }, // Xóa toàn bộ lịch sử
                                content = {
                                    Text(
                                        text = "Xóa lịch sử tìm kiếm",
                                        color = Color.Red,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )
                        }
                    }
                    // Hiển thị từng mục trong lịch sử tìm kiếm
                    items(searchHistory) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { searchQuery = item }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = "History",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(item, fontSize = 14.sp)
                        }
                    }
                    item {
                        Divider(modifier = Modifier.weight(10f))
                        Text(
                            "Gợi ý tìm kiếm",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    // Gợi ý sản phẩm
                    items(products) { products ->
                        SearchSuggestion(products)
                    }
                }
            } else {
                // Kết quả tìm kiếm
                Text(
                    "Kết quả cho \"$searchQuery\"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(searchResults) { product ->
                        ProductItem(product)
                    }
                }
            }
        }
    }
}

//Nguoi Viet: Duy Trọng
//Ngay Viet: 07/12
//Input:
//Output:
//Thuat toan xu ly:
// hiển thị một mục sản phẩm trong danh sách, bao gồm tên sản phẩm và một đường kẻ phân cách.
// Mục này có thể được nhấn để mở chi tiết sản phẩm (hiện chưa triển khai)
//-------------------------
@Composable
fun ProductItem(product: ProductState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Mở chi tiết sản phẩm */ }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
        }
    }
}

//Nguoi Viet: Duy Trọng
//Ngay Viet: 07/12
//Input:
//Output:
//Thuat toan xu ly:
// hiển thị các gợi ý tìm kiếm cho người dùng. Mỗi gợi ý bao gồm một hình ảnh đại diện của sản phẩm và tên sản phẩm.
// Các gợi ý này có thể được nhấn để thực hiện tìm kiếm chi tiết hơn
//-------------------------
@Composable
fun SearchSuggestion(product: ProductState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.name,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.name,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}