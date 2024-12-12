package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


//Nguoi Viet: Duy Trọng
//Ngay Viet: 09/12
//Input:
//Output:
//Thuat toan xu ly: Hiển thị danh sách địa chỉ nhận hàng, cho phép chọn địa chỉ mặc định và thêm địa chỉ mới.
//-------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectionScreen(navController: NavController){
    // Danh sách địa chỉ mẫu
    var addressList by remember {
        mutableStateOf(
            listOf(
                Address("Trương Duy Trọng", "09xxxxxxxx", "Thông tin địa chỉ nhận hàng của người dùng", true),
                Address("Văn Nam Cao", "09xxxxxxxx", "Thông tin địa chỉ nhận hàng của người dùng", false),
                Address("Nguyễn Mạnh Cường", "09xxxxxxxx", "Thông tin địa chỉ nhận hàng của người dùng", false)
            )
        )
    }

    // Lưu trữ địa chỉ được chọn
    var selectedAddress by remember { mutableStateOf(addressList.firstOrNull { it.isDefault }) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Địa chỉ nhận hàng", fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ){
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF6F6F6)).padding(it)) {
            LazyColumn(modifier = Modifier.weight(1f).padding(5.dp)) {
                // Tiêu đề danh sách
                item {
                    Text(
                        text = "Địa chỉ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                // Hiển thị các địa chỉ
                items(addressList.size) { index ->
                    val address = addressList[index]
                    AddressItem(
                        address = address,
                        isSelected = address == selectedAddress,
                        onSelectClick = { selectedAddress = address }// Xử lý chọn địa chỉ
                    )
                }
                // Nút thêm địa chỉ mới
                item{
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = {
                            navController.navigate(Screen.Add_Address.route)
                        }) {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                contentDescription = null,
                                tint = Color(0xFF448AFF)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Thêm Địa Chỉ Mới",
                                fontSize = 16.sp,
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

//Nguoi Viet: Duy Trọng
//Ngay Viet: 09/12
//Input:
//Output:
//Thuat toan xu ly: Hiển thị chi tiết một địa chỉ, hỗ trợ chọn và chỉnh sửa địa chỉ.
//-------------------------
@Composable
fun AddressItem(
    address: Address,
    isSelected: Boolean,// Trạng thái được chọn
    onSelectClick: () -> Unit// Hàm xử lý khi chọn địa chỉ
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable { onSelectClick() }// Xử lý khi nhấn vào item
            .padding(8.dp)
    ) {
        // Hiển thị thông tin tên, số điện thoại và nút radio chọn
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onSelectClick() }, // Xử lý khi nhấn vào nút radio
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF5D9EFF))
            )
            Text(
                text = "${address.name} | ${address.phone}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = {/* TODO: Edit address logic */}) {
                Text(text = "Sửa", color = Color(0xFF5D9EFF))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Hiển thị thông tin chi tiết địa chỉ
        Text(text = address.details, fontSize = 14.sp, color = Color.Gray)

        // Hiển thị nhãn "MẶC ĐỊNH" nếu là địa chỉ mặc định
        if (address.isDefault) {
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(text = "MẶC ĐỊNH", fontSize = 12.sp, color = Color(0xFF5D9EFF))
            }
        }
    }
}

// Dữ liệu địa chỉ
data class Address(
    val name: String,
    val phone: String,
    val details: String,
    val isDefault: Boolean// Xác định địa chỉ mặc định
)

//@Preview(showBackground = true)
//@Composable
//fun AddressSelectionPreview() {
//    AddressSelectionScreen()
//}