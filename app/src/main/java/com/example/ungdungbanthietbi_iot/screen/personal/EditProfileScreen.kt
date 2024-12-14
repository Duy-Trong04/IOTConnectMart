package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.annotation.ExperimentalCoilApi
import com.example.ungdungbanthietbi_iot.navigation.Screen
import java.time.format.DateTimeFormatter


/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Hồ sơ cá nhân của người dùng
*/

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
//@Preview(showBackground = true)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    var userName by remember { mutableStateOf("Nguyen Van A") }
    var gender by remember { mutableStateOf("Nam") }
    var birthDate by remember { mutableStateOf("01/01/1990") }
    var phoneNumber by remember { mutableStateOf("0123456789") }
    var email by remember { mutableStateOf("email@example.com") }
    var profileImage by remember { mutableStateOf("https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/anh-dep-thien-nhien-thump.jpg") }

    // Biến trạng thái để điều khiển hiển thị dialog
    var showGenderDialog by remember { mutableStateOf(false) }
    var showCalendarDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sửa hồ sơ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Chức năng lưu lại thông tin đã sửa */ }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clickable { /* Thêm chức năng sửa hình ảnh */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberImagePainter(profileImage),
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                        Text(
                            text = "Sửa",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color.Black.copy(alpha = 0.7f))
                                .padding(8.dp)
                        )
                    }
                }
                item {
                    BoxEditProfile(label = "Tên người dùng", value = userName, onClick = { /*Chuyển trang đổi user name*/navController.navigate(
                        Screen.EditUsernamScreen.route) })
                }
                item {
                    BoxEditProfile(label = "Giới tính", value = gender, onClick = { /*Chuyển trang đổi giới tính*/ showGenderDialog=true})
                }
                item {
                    BoxEditProfile(label = "Ngày sinh", value = birthDate, onClick = { /*Chuyển trang đổi ngày sinh*/ showCalendarDialog = true})
                }
                item {
                    BoxEditProfile(label = "Số điện thoại", value = phoneNumber, onClick = { /*Chuyển trang đổi số điện thoại*/navController.navigate(Screen.EditPhoneScreen.route) })
                }
                item {
                    BoxEditProfile(label = "Email", value = email, onClick = { /*Chuyển trang đổi email*/navController.navigate(Screen.EditEmailScreen.route) })
                }
            }
            // Hiển thị dialog chọn giới tính nếu trạng thái showGenderDialog là true
            if (showGenderDialog) {
                GenderSelectionDialog(
                    onDismiss = { showGenderDialog = false },
                    onGenderSelected = { selectedGender ->
                        gender = selectedGender
                        showGenderDialog = false
                    }
                )
            }
            if (showCalendarDialog) {
                CalendarDialog(
                    onDismiss = { showCalendarDialog = false },
                    onDateSelected = { selectedDate ->
                birthDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                showCalendarDialog = false
                    }
                )
            }

        }
    )
}


//Hàm BoxEditProfile()
//Hiển thị một hàng thông tin với nhãn và giá trị tương ứng.
//Cho phép nhấp vào để thực hiện hành động thông qua onClick
@Composable
fun BoxEditProfile(label: String, value: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Bold)
            Text(value, color = if (value.isEmpty()) Color.Gray else Color.Black)
        }
    }
}
