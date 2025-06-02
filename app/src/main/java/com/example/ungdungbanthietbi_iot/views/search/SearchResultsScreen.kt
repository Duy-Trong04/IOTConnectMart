package com.example.ungdungbanthietbi_iot.views.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    query: String?,
    username: String?
) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val accountViewModel: AccountViewModel = viewModel()

    val devices by deviceViewModel.listDeviceSearch.collectAsState()
    val searchQuery by deviceViewModel.searchQuery.collectAsState()
    val account = accountViewModel.account

    // Trạng thái cho hộp thoại lọc
    var showFilterDialog by remember { mutableStateOf(false) }
    // Trạng thái cho khoảng giá lọc
    var priceRange by remember { mutableStateOf(0f..10000000f) } // Mặc định: 0 đến 10 triệu
    // Trạng thái tab được chọn
    var selectedTabIndex by remember { mutableStateOf(0) }
    // Trạng thái sắp xếp giá
    var isPriceAscending by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (!query.isNullOrEmpty()) {
            deviceViewModel.updateSearchQuery(query)
            deviceViewModel.searchDevice("", query)
            deviceViewModel.searchDevice(query, "")
        }
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
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { /* Không cho phép nhập trực tiếp */ },
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
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable {
                                        navController.popBackStack()
                                    }
                            )
                        }
                        // Nút biểu tượng lọc
                        IconButton(onClick = { showFilterDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = "Filter",
                                tint = Color.White
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            // TabRow cho các loại lọc
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                contentColor = Color(0xFF5D9EFF),
                containerColor = Color.White,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier
                            .zIndex(-1f)
                            .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFF5D9EFF)
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            "Liên quan",
                            color = if (selectedTabIndex == 0) Color(0xFF5D9EFF) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            "Mới nhất",
                            color = if (selectedTabIndex == 1) Color(0xFF5D9EFF) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = {
                        Text(
                            "Bán chạy",
                            color = if (selectedTabIndex == 2) Color(0xFF5D9EFF) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 3,
                    onClick = {
                        if (selectedTabIndex == 3) {
                            isPriceAscending = !isPriceAscending
                        } else {
                            selectedTabIndex = 3
                            isPriceAscending = false
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Giá",
                            color = if (selectedTabIndex == 3) Color(0xFF5D9EFF) else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isPriceAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isPriceAscending) "Sắp xếp giá tăng dần" else "Sắp xếp giá giảm dần",
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTabIndex == 3) Color(0xFF5D9EFF) else Color.Gray
                        )
                    }
                }
            }

            // Hộp thoại lọc
            if (showFilterDialog) {
                FilterDialog(
                    priceRange = priceRange,
                    onDismiss = { showFilterDialog = false },
                    onApply = { newPriceRange ->
                        priceRange = newPriceRange
                        showFilterDialog = false
                    },
                    onReset = {
                        priceRange = 0f..10000000f // Reset khoảng giá về mặc định
                        showFilterDialog = false
                    }
                )
            }

            // Sắp xếp và lọc danh sách sản phẩm
            val filteredDevices = devices.filter {
                it.sellingPrice in priceRange.start..priceRange.endInclusive
            }
            val sortedDevices = when (selectedTabIndex) {
                0 -> filteredDevices
                1 -> filteredDevices.sortedByDescending { it.created_at }
                2 -> filteredDevices.sortedByDescending { it.isHide }
                3 -> if (isPriceAscending) {
                    filteredDevices.sortedBy { it.sellingPrice }
                } else {
                    filteredDevices.sortedByDescending { it.sellingPrice }
                }
                else -> filteredDevices
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
fun FilterDialog(
    priceRange: ClosedFloatingPointRange<Float>,
    onDismiss: () -> Unit,
    onApply: (ClosedFloatingPointRange<Float>) -> Unit,
    onReset: () -> Unit // Thêm callback để xử lý nút "Bỏ chọn"
) {
    var tempPriceRange by remember { mutableStateOf(priceRange) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Tiêu đề
                Text(
                    text = "Lọc danh mục",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Phần chọn nhanh giá
                Text(
                    text = "Giá",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(
                        onClick = { tempPriceRange = 0f..2000000f },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tempPriceRange.endInclusive <= 2000000f) Color(0xFF5D9EFF) else Color.LightGray,
                            contentColor = if (tempPriceRange.endInclusive <= 2000000f) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Dưới 2 triệu", fontSize = 14.sp)
                    }
                    Button(
                        onClick = { tempPriceRange = 0f..5000000f },
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tempPriceRange.endInclusive <= 5000000f && tempPriceRange.endInclusive > 2000000f) Color(0xFF5D9EFF) else Color.LightGray,
                            contentColor = if (tempPriceRange.endInclusive <= 5000000f && tempPriceRange.endInclusive > 2000000f) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Dưới 5 triệu", fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Phần chọn giá phù hợp
                Text(
                    text = "Chọn giá phù hợp",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = formatGiaTien(tempPriceRange.start.toDouble()),
                        onValueChange = { /* Read-only */ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = Color.Gray
                        )
                    )
                    OutlinedTextField(
                        value = formatGiaTien(tempPriceRange.endInclusive.toDouble()),
                        onValueChange = { /* Read-only */ },
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = Color.Gray
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                RangeSlider(
                    value = tempPriceRange,
                    onValueChange = { newRange ->
                        tempPriceRange = newRange
                    },
                    valueRange = 0f..10000000f, // Phạm vi giá tối đa
                    steps = 100,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF5D9EFF), // Màu chấm tròn giống hình
                        activeTrackColor = Color(0xFF5D9EFF), // Màu thanh trượt đã chọn
                        inactiveTrackColor = Color.LightGray, // Màu thanh trượt chưa chọn
                        activeTickColor = Color.Transparent, // Ẩn dấu tick
                        inactiveTickColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .height(40.dp) // Điều chỉnh chiều cao để chấm tròn nổi bật
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            tempPriceRange = 0f..10000000f // Reset về giá trị mặc định
                            onReset() // Gọi callback để reset bộ lọc
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4444), // Màu đỏ
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Bỏ chọn", fontSize = 14.sp)
                    }
                    Button(
                        onClick = { onApply(tempPriceRange) },
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF), // Màu xanh
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Xem kết quả", fontSize = 14.sp)
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