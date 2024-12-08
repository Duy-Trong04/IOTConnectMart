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
    Scaffold(
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
                        // Search input field
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text(text = "Produc 1")},
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start
                            ),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )
                        // Search icon
                        IconButton(onClick = {/* Perform search */}) {
                            Icon(imageVector = Icons.Filled.FilterAlt, contentDescription = "",
                                tint = Color.White
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
    ) { padding ->
        var selectedTabIndex by remember { mutableStateOf(0) }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            // Tab row
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
            // Tab content
            when (selectedTabIndex) {
                0 -> {
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

@Preview(showBackground = true)
@Composable
fun SearchResultsScreenPreview() {
    UngDungBanThietBi_IOTTheme {
        SearchResultsScreen()
    }
}