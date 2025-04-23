package com.example.ungdungbanthietbi_iot.screen.address

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.address_book.Address
import com.example.ungdungbanthietbi_iot.data.address_book.AddressViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen

/** Giao diện màn hình chọn địa chỉ (AddressSelectionScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 09/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Hiển thị danh sách địa chỉ nhận hàng, cho phép chọn địa chỉ mặc định và thêm địa chỉ mới.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectionScreen(
    navController: NavController,
    idCustomer: String?,
    selectedAddressId: Int? // Thêm tham số này
) {
    val addressViewModel: AddressViewModel = viewModel()
    val listAddress by addressViewModel::listAddress
    // Khởi tạo trạng thái chọn với selectedAddressId từ CheckoutScreen
    var currentSelectedAddressId by remember { mutableStateOf(selectedAddressId) }

    LaunchedEffect(Unit) {
        addressViewModel.getAddressByIdCustomer(idCustomer)
    }

    // Hàm xử lý khi chọn địa chỉ
    fun selectAddress(addressId: Int) {
        currentSelectedAddressId = addressId // Cập nhật địa chỉ được chọn
        navController.previousBackStackEntry?.savedStateHandle?.set("selectedAddressId", addressId)
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Địa chỉ nhận hàng",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.weight(1f).padding(5.dp)) {
                items(listAddress) { address ->
                    AddressItem(
                        address = address,
                        navController = navController,
                        selectedAddressId = currentSelectedAddressId,
                        onSelectClick = { addressId -> selectAddress(addressId) }
                    )
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = {
                            navController.navigate("${Screen.Add_Address.route}?idCustomer=${idCustomer}")
                        }) {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                contentDescription = null,
                                tint = Color(0xFF448AFF)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Thêm Địa Chỉ Mới",
                                fontSize = 18.sp,
                                color = Color(0xFF448AFF),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Card chứa thông tin của từng địa chỉ (AddressItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 09/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: address: Address, isSelected: Boolean, onSelectClick: () -> Unit
 *
 * Output: Hiển thị chi tiết một địa chỉ, hỗ trợ chọn và chỉnh sửa địa chỉ.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun AddressItem(
    address: Address,
    navController: NavController,
    selectedAddressId: Int?, // ID của địa chỉ đang được chọn
    onSelectClick: (Int) -> Unit // Callback khi chọn địa chỉ
) {
    val customerViewModel: CustomerViewModel = viewModel()
    val customer = customerViewModel.customer

    LaunchedEffect(address) {
        customerViewModel.getCustomerById(address.idCustomer)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { if (selectedAddressId != null) onSelectClick(address.id)
                       else { navController.navigate("${Screen.Update_Address.route}?idCustomer=${address.idCustomer}&id=${address.id}") }}, // Nhấn vào Card để chọn
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hiển thị RadioButton chỉ khi selectedAddressId != null
            if (selectedAddressId != null) {
                RadioButton(
                    selected = selectedAddressId == address.id,
                    onClick = { onSelectClick(address.id) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF5D9EFF),
                        unselectedColor = Color.Gray
                    )
                )
            } else {
                Spacer(modifier = Modifier.width(8.dp)) // Giữ khoảng cách khi không có RadioButton
            }
            // Nội dung địa chỉ
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Người nhận: ${customer?.surname} ${customer?.lastName}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (selectedAddressId != null) {
                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                navController.navigate("${Screen.Update_Address.route}?idCustomer=${address.idCustomer}&id=${address.id}")
                            }
                        ) {
                            Text(
                                text = "Sửa",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = Color(0xFF5D9EFF)
                            )
                        }
                    }
                    else {
                        Spacer(modifier = Modifier.width(8.dp)) // Giữ khoảng cách khi không có RadioButton
                    }
                }
                Text(
                    text = "Số điện thoại: ${customer?.phone}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Địa chỉ: ${address.street}, ${address.ward}, ${address.district}, ${address.city}",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 18.sp
                )
                if (address.isDefault == 1) {
                    Text(
                        text = "Mặc định",
                        fontSize = 13.sp,
                        modifier = Modifier
                            .border(1.dp, Color(0xFF5D9EFF), shape = RoundedCornerShape(10.dp))
                            .padding(3.dp),
                        color = Color(0xFF5D9EFF)
                    )
                }
            }
        }
    }
}