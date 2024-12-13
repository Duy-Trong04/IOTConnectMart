package com.example.ungdungbanthietbi_iot.screen.personal

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview



/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Chọn(Chỉnh sửa) giới tính

 ---
 Cập nhật lại Sửa từ column thành LazyColumn
*/
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GenderSelectionScreen(onBack: () -> Unit = {}) {
    var selectedGender by remember { mutableStateOf("Nam") }

    val configuration = LocalConfiguration.current

    // Kiểm tra xem thiết bị có phải là máy tính bảng hay không
    val isTablet = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE

    // Đặt kích thước font chữ dựa trên loại thiết bị
    val fontSize = if (isTablet) 24.sp else 18.sp
    val radioFontSize = if (isTablet) 20.sp else 16.sp
    val buttonFontSize = if (isTablet) 20.sp else 16.sp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chọn giới tính", fontSize = fontSize, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Giới tính", fontSize = fontSize, fontWeight = FontWeight.Bold)

                        val genderOptions = listOf("Nam", "Nữ", "Khác")

                        genderOptions.forEach { gender ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (gender == selectedGender),
                                        onClick = { selectedGender = gender },
                                        role = Role.RadioButton
                                    )
                                    .padding(8.dp)
                            ) {
                                RadioButton(
                                    selected = (gender == selectedGender),
                                    onClick = { selectedGender = gender }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = gender, fontSize = radioFontSize)
                            }
                        }

                        Button(
                            onClick = { /* Thêm chức năng xác nhận(Lưu thông tin) giới tính */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF))
                        ) {
                            Text("Xác nhận", color = Color.White, fontSize = buttonFontSize)
                        }
                    }
                }
            }
        }
    )
}
