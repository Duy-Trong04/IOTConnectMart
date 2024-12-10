package com.example.ungdungbanthietbi_iot

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//Nguoi Viet: Duy Trọng
//Ngay Viet: 10/12
//Input: Họ tên, SĐT, Địa chỉ
//Output:
//Thuat toan xu ly: Hiển thị giao diện thêm địa chỉ mới, bao gồm các TextField để nhập
// thông tin và một Switch để đặt làm địa chỉ mặc định.
// Có nút "THÊM ĐỊA CHỈ" để thêm địa chỉ vào danh sách.
//-------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(){
    // Biến trạng thái lưu thông tin nhập vào
    var fullName by remember { mutableStateOf("") }// Tên đầy đủ
    var phoneNumber by remember { mutableStateOf("") }// Số điện thoại
    var province by remember { mutableStateOf("") }// Tỉnh/Thành phố
    var district by remember { mutableStateOf("") }// Quận/Huyện
    var ward by remember { mutableStateOf("") }// Phường/Xã
    var detailedAddress by remember { mutableStateOf("") }// Địa chỉ chi tiết
    var isDefaultAddress by remember { mutableStateOf(false) } // Trạng thái của Switch đặt làm địa chỉ mặc định

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Thêm địa chỉ", textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    // Nút quay lại
                    IconButton(onClick = { /* Back action */ }) {
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
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = isDefaultAddress,
                        onCheckedChange = { isDefaultAddress = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF00C3FF),// Màu khi bật
                            uncheckedThumbColor = Color.Gray, // Màu khi tắt
                            checkedTrackColor = Color(0xFF5D9EFF)// Màu đường chạy khi bật
                        ),
                        modifier = Modifier.scale(0.8f)// Thu nhỏ kích thước Switch
                    )
                }
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                // Nút thêm địa chỉ
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* Thêm logic thêm địa chỉ */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    ),
                    shape = RoundedCornerShape(10.dp),// Bo góc nút
                    elevation = ButtonDefaults.buttonElevation(4.dp)// Tạo độ nổi
                ) {
                    Text("THÊM ĐỊA CHỈ", fontSize = 18.sp)
                }
            }
        }

    ){
        // Nội dung chính (LazyColumn) hiển thị danh sách các trường nhập liệu
        LazyColumn (
            modifier = Modifier.fillMaxWidth().padding(it).background(Color.White)
                .padding(10.dp)
        ){
            // Nhóm trường "Liên hệ"
            item {
                Text(
                    text = "Liên hệ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Họ và tên") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone) // Bàn phím số
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Nhóm trường "Địa chỉ"
            item {
                Text(
                    text = "Địa chỉ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Tỉnh/Thành phố") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )
                TextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("Quận/Huyện") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                )
                TextField(
                    value = ward,
                    onValueChange = { ward = it },
                    label = { Text("Phường/Xã") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                )
                TextField(
                    value = detailedAddress,
                    onValueChange = { detailedAddress = it },
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

@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    AddAddressScreen()
}