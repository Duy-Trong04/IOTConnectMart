package com.example.ungdungbanthietbi_iot.views.search

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.views.home.CardDevice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    deviceViewModel: DeviceViewModel = viewModel(),
    category: String?,
    username: String?,
    idCustomer: String?,
    password: String?
) {
    val listAllDevice by deviceViewModel.listAllDevice.collectAsState()
    val isLoading by deviceViewModel.isLoadingAll.collectAsState()
    // Trạng thái tab được chọn
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    // Trạng thái sắp xếp giá
    var isPriceAscending by remember { mutableStateOf(false) }
    val filteredDevices = listAllDevice.filter { it.categories == category } // Lọc sản phẩm theo danh mục
    // Trạng thái cho hộp thoại lọc
    var showFilterDialog by remember { mutableStateOf(false) }
    // Trạng thái cho khoảng giá lọc
    var priceRange by remember { mutableStateOf(0f..10000000f) } // Mặc định: 0 đến 10 triệu
    // Lọc và sắp xếp danh sách sản phẩm
    val filteredAndSortedDevices = filteredDevices
        .filter { it.sellingPrice in priceRange.start..priceRange.endInclusive }
        .let { devices ->
            when (selectedTabIndex) {
                0 -> devices // Liên quan
                1 -> devices.sortedByDescending { it.created_at } // Mới nhất
                2 -> devices.sortedByDescending { it.totalReview } // Bán chạy
                3 -> if (isPriceAscending) {
                    devices.sortedBy { it.sellingPrice } // Giá tăng dần
                } else {
                    devices.sortedByDescending { it.sellingPrice } // Giá giảm dần
                }
                else -> devices
            }
        }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = category ?: "Tìm kiếm",
                    fontSize = 20.sp,
                    color = Color.White
                )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Nút biểu tượng lọc
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "Lọc",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF5D9EFF),
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
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
                        },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Giá",
                                    color = if (selectedTabIndex == 3) Color(0xFF5D9EFF) else Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (isPriceAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isPriceAscending) "Sắp xếp giá tăng dần" else "Sắp xếp giá giảm dần",
                                    modifier = Modifier.size(19.dp),
                                    tint = if (selectedTabIndex == 3) Color(0xFF5D9EFF) else Color.Gray
                                )
                            }
                        }
                    )
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

                // Hiển thị danh sách sản phẩm
                if (filteredAndSortedDevices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không có sản phẩm trong danh mục ${category ?: "này"}",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        val pairedDevices = filteredAndSortedDevices.chunked(2)
                        items(pairedDevices) { pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                pair.forEach { device ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                    ) {
                                        CardDevice(
                                            device = device,
                                            isFavorite = false,
                                            idCustomer = idCustomer,
                                            username = username,
                                            password = password,
                                            deviceViewModel = deviceViewModel,
                                            navController = navController
                                        )
                                    }
                                    if (pair.size == 1) {
                                        Box(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(40.dp)) }
                    }
                }
            }
        }
    }
}