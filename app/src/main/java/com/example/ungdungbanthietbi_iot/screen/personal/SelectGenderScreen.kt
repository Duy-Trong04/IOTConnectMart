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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.customer.Gender


/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Chọn(Chỉnh sửa) giới tính

 ---
 Cập nhật lại Sửa từ column thành LazyColumn
=======
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionDialog(customerId:String,onDismiss: () -> Unit, onGenderSelected: (String) -> Unit) {
    var selectedGender by remember { mutableStateOf("Nam") }
    var gender by remember { mutableStateOf(1) }
    val configuration = LocalConfiguration.current

    var genderModel:CustomerViewModel = viewModel()

    // Kiểm tra xem thiết bị có phải là máy tính bảng hay không
    val isTablet = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE

    // Đặt kích thước font chữ dựa trên loại thiết bị
    val fontSize = if (isTablet) 24.sp else 18.sp
    val radioFontSize = if (isTablet) 20.sp else 16.sp
    val buttonFontSize = if (isTablet) 20.sp else 16.sp

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onGenderSelected(selectedGender)
                    if(selectedGender == "Nam"){
                        gender = 0
                    }else if(selectedGender == "Nữ"){
                        gender = 1
                    }else{
                        gender = 2
                    }
                    val g: Gender
                    g = Gender(customerId,gender)
                    genderModel.updateGender(g)
                    onDismiss()
                }
            )
            {
                Text("Xác nhận", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Đóng", color = Color.White)
            }
        },
        title = {
            Text("Chọn giới tính", fontSize = fontSize, fontWeight = FontWeight.Bold)
        },
        text = {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Text("Giới tính", fontSize = fontSize, fontWeight = FontWeight.Bold)

                item {
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
                }
            }
        }
    )
}
