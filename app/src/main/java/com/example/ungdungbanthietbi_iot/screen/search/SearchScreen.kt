package com.example.ungdungbanthietbi_iot.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.data.Product
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/** Giao diện màn hình tìm kiếm (SearchScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 07/12/2024
 * Lần cập nhật cuối cùng: 08/01/2025
 * -------------------------------------------
 * Input:
 *  deviceViewModel: ViewModel quản lý dữ liệu về thiết bị, bao gồm kết quả tìm kiếm và lịch sử tìm kiếm.
 *
 * Output: Hiển thị giao diện tìm kiếm sản phẩm với các tính năng:
 * - Tìm kiếm theo từ khóa.
 * - Quản lý lịch sử tìm kiếm.
 * - Gợi ý từ khóa.
 * - Hiển thị kết quả tìm kiếm dựa trên từ khóa nhập vào.
 * ------------------------------------------------------------
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 08/01/2025
 * ------------------------------------------------------------
 * Nội dung cập nhật:  Cập nhật UI cho màn hình tìm kiếm, xử lý logic tìm kiếm và hiển thị kết quả.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, deviceViewModel: DeviceViewModel){
    // Lấy danh sách thiết bị tìm được và lịch sử tìm kiếm từ ViewModel
    val devices = deviceViewModel.searchResult
    val history = deviceViewModel.searchHistory
    LaunchedEffect(Unit) {
        deviceViewModel.getAllDevice()
    }
    // Biến lưu giá trị từ ô tìm kiếm
    var coroutineScope = rememberCoroutineScope()
    // Scaffold để chứa nội dung màn hình
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
                            value = deviceViewModel.searchQuery,
                            onValueChange = { deviceViewModel.searchQuery = it},
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            trailingIcon = {
                                // Dấu X để xóa từ khóa tìm kiếm
                                IconButton(
                                    onClick = {deviceViewModel.searchQuery = ""
                                    deviceViewModel.searchResult = emptyList()
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp) // Kích thước của hình tròn
                                            .background(Color.Red, shape = CircleShape) // Hình tròn với màu đỏ
                                            .padding(3.dp) // Khoảng cách giữa biểu tượng và viền của hình tròn
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Close",
                                            tint = Color.White, // Màu của dấu X
                                            modifier = Modifier.size(20.dp) // Kích thước của dấu X
                                        )
                                    }
                                }
                            },
                            placeholder = { Text(text = "Tìm kiếm ...", color = Color.LightGray) },
                            textStyle = TextStyle(
                                fontSize = 16.sp, // Kích thước chữ phù hợp
                                textAlign = TextAlign.Start // Canh chữ bắt đầu từ bên trái
                            ),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true,
                        )

                    }

                },
                actions = {
                    Row (
                        //modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        // Icon tìm kiếm
                        IconButton(onClick = {
                            coroutineScope.launch {
                                deviceViewModel.searchDevice(deviceViewModel.searchQuery)
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "",
                                tint = Color.White,
                            )
                        }
                        if(deviceViewModel.searchQuery.isNotEmpty()){
                            IconButton(onClick = {
                                //lọc giá sản phẩm
                            }) {
                                Icon(imageVector = Icons.Filled.FilterAlt, contentDescription = "FilterAlt",
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
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
            if (deviceViewModel.searchQuery.isEmpty()) {
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
                            // Xóa toàn bộ lịch sử
                            TextButton(
                                onClick = {
                                    deviceViewModel.clearSearchHistory() },
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
                    items(history) { keyword ->
                        Divider()
                        HistorySearchItem(keyword, deviceViewModel)
                    }
                }
            } else {
                // Kết quả tìm kiếm
                LazyColumn() {
                    items(devices) { device ->
                        CardDeviceSearch(device = device,
                            onClick = {
                                //Chuyển đến màn hình chi tiết sản phẩm
                                navController.navigate(Screen.ProductDetailsScreen.route+"?id=${device.idDevice}")
                            }
                        )
                    }
                }
            }
        }
    }
}
/** Hiển thị lịch sử tìm kiếm (HistorySearchItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 07/12/2024
 * Lần cập nhật cuối cùng: 08/01/2025
 * -------------------------------------------
 * Input: keyword: String, viewModel: DeviceViewModel
 *
 * Output: hiện thị lịch sử tìm kiếm của người dùng
 * ------------------------------------------------------------
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 08/01/2025
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 * Cập nhật UI, xử lý logic khi ấn vào lịch sử sẽ hiện thị kết quả
 */
@Composable
fun HistorySearchItem(keyword: String, viewModel: DeviceViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.searchQuery = keyword
                viewModel.searchDevice(keyword)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = "History",
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = keyword)
        }
        IconButton(onClick = { viewModel.removeSearchHistory(keyword) }) {
            Icon(imageVector = Icons.Default.Close,
                contentDescription = "Xóa lịch sử",
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
        }
    }
}
/** Card chứa thông tin của 1 sản phẩm (CardDeviceSearch)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 07/12/2024
 * Lần cập nhật cuối cùng: 08/01/2025
 * -------------------------------------------
 * Input: device: Device, onClick: () -> Unit
 *
 * Output: Card chứa kết quả khi người dùng thực hiện tìm kiếm
 * ------------------------------------------------------------
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 08/01/2025
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 * Cập nhật UI cho Card, xử lý logic chuyển màn hình chi tiết.
 */
@Composable
fun CardDeviceSearch(device: Device, onClick: () -> Unit) {
    //format giá sản phẩm
    val formatter = DecimalFormat("#,###,###")
    val formattedPrice = formatter.format(device.sellingPrice)
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp) // Giảm padding để Card nhỏ hơn
            .fillMaxWidth(), // Giảm chiều rộng của Card để làm nó nhỏ lại
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp) // Giảm padding bên trong Row
        ) {
            // Load hình ảnh từ API
            AsyncImage(
                model = device.image,
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp).height(100.dp)
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = device.name,
                        fontSize = 18.sp, // Giảm kích thước chữ
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${formattedPrice} VNĐ",
                    color = Color.Red,
                    fontSize = 16.sp, // Giảm kích thước chữ
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = device.descriptionNormal,
                    fontSize = 14.sp, // Giảm kích thước chữ
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}