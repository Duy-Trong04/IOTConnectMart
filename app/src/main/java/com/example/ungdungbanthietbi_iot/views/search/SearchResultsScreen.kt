package com.example.ungdungbanthietbi_iot.views.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import com.example.ungdungbanthietbi_iot.views.home.CardDevice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    query: String,
    username: String?,
    idCustomer:String?,
    password: String?,
) {
    val deviceViewModel: DeviceViewModel = viewModel()

    val devices by deviceViewModel.listDeviceSearch.collectAsState()
    val searchQuery by deviceViewModel.searchQuery.collectAsState()
    // Trạng thái tải
    val isLoading by deviceViewModel.isLoading.collectAsState()
    // Trạng thái cho hộp thoại lọc
    var showFilterDialog by remember { mutableStateOf(false) }
    // Trạng thái cho khoảng giá lọc
    var priceRange by remember { mutableStateOf(0f..10000000f) } // Mặc định: 0 đến 10 triệu
    // Trạng thái tab được chọn
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    // Trạng thái sắp xếp giá
    var isPriceAscending by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.isNotEmpty() && searchQuery != devices.firstOrNull()?.name) {
            deviceViewModel.updateSearchQuery(query)
            deviceViewModel.searchDevice(query)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(1f)) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { /* Không cho phép nhập trực tiếp */ },
                            modifier = Modifier
                                .fillMaxWidth(1f),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color(0xFF5D9EFF)
                            ),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start,
                            ),
                            shape = RoundedCornerShape(25.dp),
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
                },
                actions = {
                    // Nút biểu tượng lọc
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .background(Color.White)
                .padding(padding)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5D9EFF))
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

                // Sắp xếp và lọc danh sách sản phẩm
                val filteredDevices = devices.filter {
                    it.sellingPrice in priceRange.start..priceRange.endInclusive
                }
                val sortedDevices = when (selectedTabIndex) {
                    0 -> filteredDevices
                    1 -> filteredDevices.sortedByDescending { it.created_at }
                    2 -> filteredDevices.sortedByDescending { it.totalReview }
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
                        modifier = Modifier.fillMaxSize()
                            .background(Color.White),
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
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        columns = GridCells.Fixed(2),
                    ) {
                        items(sortedDevices) { device ->
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
                    text = "Bộ lọc",
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
//                    Button(
//                        onClick = { tempPriceRange = 0f..8000000f },
//                        modifier = Modifier.height(40.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = if (tempPriceRange.endInclusive <= 8000000f && tempPriceRange.endInclusive > 2000000f) Color(0xFF5D9EFF) else Color.LightGray,
//                            contentColor = if (tempPriceRange.endInclusive <= 8000000f && tempPriceRange.endInclusive > 2000000f) Color.White else Color.Black
//                        ),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text("Dưới 8 triệu", fontSize = 14.sp)
//                    }
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
                CustomRangeSlider(
                    giaTri = tempPriceRange,
                    onGiaTriThayDoi = { newRange ->
                        tempPriceRange = newRange
                    },
                    phamViGiaTri = 0f..10000000f,
                    modifier = Modifier.height(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
fun CustomRangeSlider(
    giaTri: ClosedFloatingPointRange<Float>,
    onGiaTriThayDoi: (ClosedFloatingPointRange<Float>) -> Unit,
    phamViGiaTri: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    val minGia = phamViGiaTri.start
    val maxGia = phamViGiaTri.endInclusive
    val khoangGia = maxGia - minGia

    // Tính toán tỉ lệ vị trí của thumb
    var targetStartOffset by remember { mutableFloatStateOf((giaTri.start - minGia) / khoangGia) }
    var targetEndOffset by remember { mutableFloatStateOf((giaTri.endInclusive - minGia) / khoangGia) }

    // Làm mượt chuyển động của thumb
    val startOffset by animateFloatAsState(targetValue = targetStartOffset, animationSpec = tween(durationMillis = 100),
        label = ""
    )
    val endOffset by animateFloatAsState(targetValue = targetEndOffset, animationSpec = tween(durationMillis = 100),
        label = ""
    )

    // Hệ số giảm tốc để làm chậm chuyển động kéo
    val dragSensitivity = 0.1f // Giảm tốc độ kéo hơn nữa (chậm hơn)

    // Cập nhật giá trị khi thumb di chuyển
    fun capNhatGiaTri() {
        val newStart = minGia + (startOffset * khoangGia)
        val newEnd = minGia + (endOffset * khoangGia)
        onGiaTriThayDoi(newStart.coerceIn(minGia, newEnd)..newEnd.coerceIn(newStart, maxGia))
    }
    // Gọi cập nhật giá trị khi offset thay đổi
    LaunchedEffect(startOffset, endOffset) {
        capNhatGiaTri()
    }

    Layout(
        modifier = modifier
            .height(10.dp)
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(4.dp)),
        content = {
            // Thanh active giữa hai thumb
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFF5D9EFF), RoundedCornerShape(10.dp))
            )
            // Thumb trái
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color(0xFF5D9EFF), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val newOffset = (targetStartOffset + (dragAmount.x / size.width) * dragSensitivity)
                                .coerceIn(0f, targetEndOffset)
                            targetStartOffset = newOffset
                        }
                    }
            )
            // Thumb phải
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color(0xFF5D9EFF), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val newOffset = (targetEndOffset + (dragAmount.x / size.width) * dragSensitivity)
                                .coerceIn(targetStartOffset, 1f)
                            targetEndOffset = newOffset
                        }
                    }
            )
        }
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val thumbSize = 18.dp.toPx().toInt()

        // Đo các thành phần
        val activeTrack = measurables[0].measure(
            Constraints.fixed(
                width = ((endOffset - startOffset).coerceAtLeast(0f) * width).toInt(),
                height = height
            )
        )
        val leftThumb = measurables[1].measure(Constraints.fixed(thumbSize, thumbSize))
        val rightThumb = measurables[2].measure(Constraints.fixed(thumbSize, thumbSize))

        // Tính toán vị trí
        val leftThumbX = (startOffset * width - thumbSize / 2).toInt().coerceIn(0, width - thumbSize)
        val rightThumbX = (endOffset * width - thumbSize / 2).toInt().coerceIn(0, width - thumbSize)
        val activeTrackX = (startOffset * width).toInt().coerceIn(0, width - activeTrack.width)

        layout(width, height) {
            activeTrack.place(activeTrackX, 0)
            leftThumb.place(leftThumbX, (height - thumbSize) / 2)
            rightThumb.place(rightThumbX, (height - thumbSize) / 2)
        }
    }
}