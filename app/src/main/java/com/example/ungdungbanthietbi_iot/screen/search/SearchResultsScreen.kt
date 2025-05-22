package com.example.ungdungbanthietbi_iot.screen.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    query: String?, // Từ khóa tìm kiếm được truyền từ SearchScreen
    username: String?
) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val accountViewModel: AccountViewModel = viewModel()

    // Lấy danh sách thiết bị và từ khóa từ ViewModel
    val devices by deviceViewModel.listDeviceSearch.collectAsState()
    val searchQuery by deviceViewModel.searchQuery.collectAsState()
    val account = accountViewModel.account

    LaunchedEffect(username) {
        if (!username.isNullOrEmpty()) {
            accountViewModel.getUserByUsername(username)
        }
    }

    // Gọi API tìm kiếm khi màn hình được tải
    LaunchedEffect(query) {
        if (!query.isNullOrEmpty()) {
            deviceViewModel.updateSearchQuery(query)
            deviceViewModel.searchDevice("", query) // Tìm kiếm theo name hoặc des
            deviceViewModel.searchDevice(query, "")
        }
    }

    // Trạng thái tab được chọn
    var selectedTabIndex by remember { mutableStateOf(0) }
    // Trạng thái sắp xếp giá (true: tăng dần, false: giảm dần)
    var isPriceAscending by remember { mutableStateOf(false) }

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
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { /* Không cho phép nhập trực tiếp, chỉ để hiển thị */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                placeholder = { Text(text = "Tìm kiếm ...") },
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start
                                ),
                                shape = RoundedCornerShape(20.dp),
                                singleLine = true,
                                readOnly = true
                            )

                            // Invisible clickable overlay
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable {
                                        navController.popBackStack()
                                    }
                            )
                        }

                        // Nút biểu tượng lọc sản phẩm (chưa xử lý logic lọc)
                        IconButton(onClick = { /* TODO: Thêm logic lọc */ }) {
                            Icon(
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = "Filter",
                                tint = Color.White
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Hàng tab cho các loại lọc sản phẩm
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                contentColor = Color(0xFF5D9EFF), // Màu của tab khi được chọn (văn bản, biểu tượng)
                containerColor = Color.White, // Màu nền của TabRow
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .zIndex(-1f)
                            .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFF5D9EFF) // Màu của đường kẻ dưới
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            "Liên quan",
                            color = if (selectedTabIndex == 0) Color(0xFF5D9EFF) else Color.Gray // Màu văn bản khi được chọn/không được chọn
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            "Mới nhất",
                            color = if (selectedTabIndex == 1) Color(0xFF5D9EFF) else Color.Gray // Màu văn bản khi được chọn/không được chọn
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = {
                        Text(
                            "Bán chạy",
                            color = if (selectedTabIndex == 2) Color(0xFF5D9EFF) else Color.Gray // Màu văn bản khi được chọn/không được chọn
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 3,
                    onClick = {
                        if (selectedTabIndex == 3) {
                            // Nếu đã ở tab "Giá", chuyển đổi giữa tăng và giảm
                            isPriceAscending = !isPriceAscending
                        } else {
                            // Nếu chuyển từ tab khác sang tab "Giá", reset về giảm dần
                            selectedTabIndex = 3
                            isPriceAscending = false // Reset về giảm dần
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Giá",
                            color = if (selectedTabIndex == 3) Color(0xFF5D9EFF) else Color.Gray // Màu văn bản khi được chọn/không được chọn
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isPriceAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isPriceAscending) "Sắp xếp giá tăng dần" else "Sắp xếp giá giảm dần",
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTabIndex == 3) Color(0xFF5D9EFF) else Color.Gray // Màu biểu tượng khi được chọn/không được chọn
                        )
                    }
                }
            }

            // Sắp xếp danh sách sản phẩm theo tab được chọn
            val sortedDevices = when (selectedTabIndex) {
                0 -> devices // Liên quan (giữ nguyên)
                1 -> devices.sortedByDescending { it.created_at } // Mới nhất
                2 -> devices.sortedByDescending { it.isHide } // Bán chạy (dựa trên isHide)
                3 -> if (isPriceAscending) {
                    devices.sortedBy { it.sellingPrice } // Giá tăng dần
                } else {
                    devices.sortedByDescending { it.sellingPrice } // Giá giảm dần
                }
                else -> devices
            }

            // Hiển thị danh sách sản phẩm
            if (sortedDevices.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Không tìm thấy sản phẩm",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                ) {
                    items(sortedDevices) { device ->
                        ProductCard(
                            device = device,
                            onClick = {
                                if (account != null) {
                                    navController.navigate(
                                        Screen.ProductDetailsScreen.route +
                                                "?id=${device.idDevice}&idCustomer=${account.idPerson}&username=${username}"
                                    )
                                } else {
                                    navController.navigate(
                                        Screen.ProductDetailsScreen.route +
                                                "?id=${device.idDevice}"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    device: Device,
    onClick: () -> Unit
) {
    val formatter = DecimalFormat("#,###,###")
    val formattedPrice = formatter.format(device.sellingPrice)

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hình ảnh sản phẩm
            AsyncImage(
                model = device.image,
                contentDescription = device.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(130.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = device.name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatGiaTien(device.sellingPrice),
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}