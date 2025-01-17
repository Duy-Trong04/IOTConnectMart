package com.example.ungdungbanthietbi_iot.screen.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.data.address_book.Address
import com.example.ungdungbanthietbi_iot.data.address_book.AddressViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel

/** Giao diện màn hình thêm địa chỉ (AddAddressScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Hiển thị giao diện thêm địa chỉ mới, bao gồm các TextField để nhập
 * thông tin và một Switch để đặt làm địa chỉ mặc định. Có nút "THÊM ĐỊA CHỈ" để thêm địa chỉ vào danh sách.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    navController: NavController,
    idCustomer:String
){

    var showDialog by remember { mutableStateOf(false) }

    val addressViewModel:AddressViewModel = viewModel()
    var listAddress = addressViewModel.listAddress
    addressViewModel.getAddressByIdCustomer(idCustomer)

    val customerViewModel: CustomerViewModel = viewModel()
    val customer = customerViewModel.customer
    if(idCustomer != null){
        LaunchedEffect (idCustomer){
            customerViewModel.getCustomerById(idCustomer)
        }
    }
    // Biến trạng thái lưu thông tin nhập vào
    var hoten by remember { mutableStateOf("${customer?.surname} ${customer?.lastName}") }// Tên đầy đủ
    var phone by remember { mutableStateOf("${customer?.phone}") }// Số điện thoại
    var district by remember { mutableStateOf("") }// Tỉnh/Thành phố
    var city by remember { mutableStateOf("") }// Quận/Huyện
    var ward by remember { mutableStateOf("") }// Phường/Xã
    var street by remember { mutableStateOf("") }// Địa chỉ chi tiết
    var isDefault by remember { mutableStateOf(false) } // Trạng thái của Switch đặt làm địa chỉ mặc định
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Thêm địa chỉ", textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    // Nút quay lại
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        // Thanh điều hướng hoặc nút hành động ở dưới cùng (bottomBar)
        bottomBar = {
            BottomAppBar (
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth().height(175.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    // Nút Switch: Đặt làm địa chỉ mặc định
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Đặt làm địa chỉ mặc định",
                            modifier = Modifier.weight(1f),
                            fontSize = 18.sp
                        )
                        Switch(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,// Màu khi bật
                                uncheckedThumbColor = Color.Gray, // Màu khi tắt
                                checkedTrackColor = Color(0xFF5D9EFF)// Màu đường chạy khi bật
                            ),
                            modifier = Modifier.scale(0.8f)// Thu nhỏ kích thước Switch
                        )
                    }
                    // Nút thêm địa chỉ
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if(city.trim() != "" || district.trim() != "" || ward != "" || street != ""){
                                /* Thêm logic thêm địa chỉ */
                                if(isDefault){
                                    for(address in listAddress){
                                        if(address.isDefault == 1){
                                            var address = Address(
                                                address.id,
                                                address.idCustomer,
                                                address.district,
                                                address.city,
                                                address.ward,
                                                address.street,
                                                0
                                            )
                                            addressViewModel.updateAddress(address)
                                        }
                                    }
                                }
                                if(idCustomer != null){
                                    var address = Address(
                                        0,
                                        idCustomer,
                                        district,
                                        city,
                                        ward,
                                        street,
                                        if(isDefault) 1 else 0
                                    )
                                    addressViewModel.addAddress(address)
                                }
                                navController.popBackStack()
                            }
                            else{
                                showDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        shape = RoundedCornerShape(5.dp),// Bo góc nút
                        elevation = ButtonDefaults.buttonElevation(4.dp)// Tạo độ nổi
                    ) {
                        Text("Thêm địa chỉ", fontSize = 20.sp)
                    }
                    if(showDialog){
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(text = "Thông báo") },
                            text = { Text(text = "Vui lòng nhập đầy đủ thông tin địa chỉ.") },
                            confirmButton = {
                                Button(onClick = { showDialog = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF5D9EFF)
                                    )) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        // Nội dung chính (LazyColumn) hiển thị danh sách các trường nhập liệu
        LazyColumn (
            modifier = Modifier.fillMaxWidth().padding(it).background(Color.White)
                .padding(10.dp)
        ){
            // Nhóm trường "Địa chỉ"
            item {
                Text(
                    text = "Địa chỉ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Tỉnh/Thành phố") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF)
                    )
                )
                TextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("Quận/Huyện") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF)
                    ),
                )
                TextField(
                    value = ward,
                    onValueChange = { ward = it },
                    label = { Text("Phường/Xã") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF)
                    ),
                )
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Địa chỉ chi tiết") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)// Bàn phím văn bản
                )
            }
        }
    }
}