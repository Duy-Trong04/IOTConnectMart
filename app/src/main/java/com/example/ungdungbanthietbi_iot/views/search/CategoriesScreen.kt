package com.example.ungdungbanthietbi_iot.views.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val listAllDevice: List<Device> = deviceViewModel.listAllDevice
    val filteredDevices = listAllDevice.filter { it.categories == category } // Lọc sản phẩm theo danh mục

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = category ?: "Tìm kiếm",
                    fontWeight = FontWeight.Bold,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (filteredDevices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có sản phẩm trong danh mục ${category ?: "này"}",
                    color = Color(0xFF616161),
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                val pairedDevices = filteredDevices.chunked(2)
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
                                    isFavorite = false, // Có thể kiểm tra trạng thái yêu thích
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