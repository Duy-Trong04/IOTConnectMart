package com.example.ungdungbanthietbi_iot.views.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.api.UpdateAddressRequest
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel

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
fun UpdateAddress(
    navController: NavController,
    idCustomer:String,
    idAddress:Int
){

    val addressViewModel: AddressViewModel = viewModel()
    val address = addressViewModel.address
    LaunchedEffect (idCustomer){
        addressViewModel.getAddressById(idAddress)
    }
    // Biến trạng thái lưu thông tin nhập vào
    var hoten by remember { mutableStateOf("") }// Tên đầy đủ
    var phone by remember { mutableStateOf("") }// Số điện thoại
    var district by remember { mutableStateOf("") }// Tỉnh/Thành phố
    var city by remember { mutableStateOf("") }// Quận/Huyện
    var ward by remember { mutableStateOf("") }// Phường/Xã
    var street by remember { mutableStateOf("") }// Địa chỉ chi tiết
    var detail by remember { mutableStateOf("") }// Địa chỉ chi tiết
    var isDefault by remember { mutableStateOf(false) } // Trạng thái của Switch đặt làm địa chỉ mặc định
    var validatePhone by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var stateSwitch by remember { mutableStateOf(true) }
    if(address != null){
        hoten = address.receiver_name
        phone = address.phone
        district = address.district
        city = address.city
        ward = address.ward
        street = address.street
        detail = address.detail
        isDefault = address.is_default
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Cập nhật địa chỉ", textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        // Thanh điều hướng hoặc nút hành động ở dưới cùng (bottomBar)
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 10.dp).padding(bottom = 10.dp)
            ) {
                // Nút Switch: Đặt làm địa chỉ mặc định
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Đặt làm địa chỉ mặc định",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    if(address != null){
                        stateSwitch = address.is_default == false
                    }
                    Switch(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,// Màu khi bật
                            uncheckedThumbColor = Color.Gray, // Màu khi tắt
                            checkedTrackColor = Color(0xFF5D9EFF),// Màu đường chạy khi bật
                            disabledCheckedTrackColor = Color(0xFF5D9EFF),
                            disabledCheckedThumbColor = Color.White
                        ),
                        enabled = stateSwitch,
                        modifier = Modifier.scale(0.6f)// Thu nhỏ kích thước Switch
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        onClick = {
                            if(address != null){
                                openDialog = if(address.is_default){
                                    true
                                } else{
                                    true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        shape = RoundedCornerShape(10.dp),// Bo góc nút
                        elevation = ButtonDefaults.buttonElevation(1.dp)// Tạo độ nổi
                    ) {
                        Text(text = "Xóa địa chỉ", fontSize = 16.sp)
                    }
                    Button(
                        onClick = {
                            if(phone.length != 10){
                                validatePhone = true
                            }
                            val updateAddress = UpdateAddressRequest(
                                customer_id = idCustomer,
                                id = idAddress,
                                receiver_name = hoten,
                                phone = phone,
                                district = district,
                                city = city,
                                ward = ward,
                                street = street,
                                detail = detail,
                                is_default = isDefault
                            )
                            addressViewModel.updateAddress(updateAddress)
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        shape = RoundedCornerShape(10.dp),// Bo góc nút
                        elevation = ButtonDefaults.buttonElevation(1.dp)// Tạo độ nổi
                    ) {
                        Text(text = "Lưu địa chỉ", fontSize = 16.sp)
                    }
                }
            }
        }
    ) {
        //Popup
        if (openDialog) {
            AlertDialog(
                containerColor = Color.White,
                modifier = Modifier.padding(10.dp),
                onDismissRequest = { openDialog = false },
                text = {
                    if (address != null) {
                        if (address.is_default) {
                            Text(
                                "Bạn không thể xóa địa chỉ mặc định!",
                                fontSize = 17.sp
                            )
                        }
                        else{
                            Text(
                                "Bạn muốn xóa địa chỉ?",
                                fontSize = 17.sp,
                            )
                        }
                    }
                },
                title = {
                    Text(text = "Thông Báo")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (address != null) {
                                if (address.is_default) {
                                    openDialog = false
                                } else {
                                    openDialog = false
                                    addressViewModel.deleteAddress(
                                        idCustomer,
                                        idAddress
                                    )
                                    navController.popBackStack()
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        )
                    ) {
                        Text(
                            text = "Xác nhận",
                            fontSize = 16.sp
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            if (address != null) {
                                if (address.is_default) {
                                    openDialog = false
                                } else {
                                    openDialog = false
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text(
                            text = "Hủy",
                            fontSize = 16.sp
                        )
                    }
                }
            )
        }
        // Nội dung chính (LazyColumn) hiển thị danh sách các trường nhập liệu
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(it).background(Color.White)
                .padding(10.dp)
        ){
            item{
                Text(
                    text = "Thông tin người nhận",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 6.dp, top = 10.dp)
                )
                TextField(
                    value = hoten,
                    onValueChange = { hoten = it },
                    label = { Text("Họ và tên") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                if(validatePhone){
                    Text(
                        text = "Số điện thoại phải đủ 10 số",
                        color = Color.Red
                    )
                }
            }
            // Nhóm trường "Địa chỉ"
            item {
                Text(
                    text = "Địa chỉ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 6.dp, top = 10.dp)
                )
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Tỉnh/Thành phố") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
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
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
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
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                )
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Đường") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)// Bàn phím văn bản
                )
                TextField(
                    value = detail,
                    onValueChange = { detail = it },
                    label = { Text("Địa chỉ chi tiết") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)// Bàn phím văn bản
                )
            }
        }
    }
}