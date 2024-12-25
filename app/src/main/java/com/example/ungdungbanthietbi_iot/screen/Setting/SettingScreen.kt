package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ungdungbanthietbi_iot.navigation.Screen


//Người thực hiện: NGUYỄN MẠNH CƯỜNG
//Ngày viết: 12/7/2024
//Input:
//Output:Màn hình thiết lập tài khoản của người dùng
//Chinh sua ngay 14/12/2024
//chinh sua kich thuoc Button bang 1/2 man hinh hien tai

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun AccountSettingsScreen(navController: NavHostController, onBack: () -> Unit = {}) {
    //Bien lay kich thuoc man hinh hien tai
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Thiết lập tài khoản",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                item{ Text(text = "Tài khoản", fontWeight = FontWeight.Bold,fontSize = 16.sp) }
                item { SettingItem(title = "Đổi mật khẩu", onClick = { /* Thêm chức năng đổi mật khẩu */navController.navigate(Screen.ChangePassword.route) }) }
                item { SettingItem(title = "Địa chỉ", onClick = {
                        navController.navigate(Screen.Address_Selection.route) })
                }
                item { SettingItem(title = "Tài khoản / Thẻ ngân hàng", onClick = { /* Thêm chức năng cho tài khoản / thẻ ngân hàng */ }) }

                item{ Text(text = "Cài đặt", fontWeight = FontWeight.Bold,fontSize = 16.sp) }
                item { SettingItem(title = "Chế độ Sáng / Tối", onClick = { /* Thêm chức năng cho chế độ sáng / tối */ }) }
                item { SettingItem(title = "Cài đặt riêng tư", onClick = { /* Thêm chức năng cho cài đặt riêng tư */ }) }

                item{ Text(text = "Hỗ trợ", fontWeight = FontWeight.Bold,fontSize = 16.sp) }
                item { SettingItem(title = "Liên hệ", onClick = { /* Thêm chức năng cho liên hệ */navController.navigate(
                    Screen.ContactScreen.route) }) }
                item { SettingItem(title = "Điều khoản IOT Connect Mart", onClick = { /* Thêm chức năng cho điều khoản IOT ConnectSmart */ }) }
                item { SettingItem(title = "Giới thiệu về ứng dụng", onClick = { /* Thêm chức năng cho giới thiệu về ứng dụng */ }) }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            onClick = { /* Thêm chức năng đăng xuất */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .width(screenWidth/2)
                                .padding(vertical = 16.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFF5F9EFF),
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Đăng xuất",
                                tint = Color(0xFF5F9EFF)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "ĐĂNG XUẤT", color = Color(0xFF5F9EFF))
                        }
                    }
                }

                item {
                    Text(
                        text = "IOT Connect Mart v1.0.0",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}


//HàmSettingItem
//Input:
//title: String: Chuỗi văn bản hiển thị tiêu đề của mục cài đặt.
//onClick: () -> Unit: Hàm được gọi khi người dùng nhấp vào mục cài đặt này
//Output
//Hiển thị một khối (Box) chứa tiêu đề (Text) của mục cài đặt.
//Khi người dùng nhấp vào mục, hàm onClick sẽ được gọi.
@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = title, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }
}
