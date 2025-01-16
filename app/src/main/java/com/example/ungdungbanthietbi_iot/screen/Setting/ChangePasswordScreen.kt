package com.example.ungdungbanthietbi_iot.screen.Setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.account.UpdatePassword


//Người thực hiện: NGUYỄN MẠNH CƯỜNG
//Ngày Viết: 12/7/2024
//Input:
//Output:Hiển thị Màn hình đổi mật khẩu của người dùng

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun ChangePassword(onBack: () -> Unit = {}, id: String, password: String) {
    //Bien lay kich thuoc man hinh hien tai
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var id by remember { mutableStateOf(id) }
    var password by remember { mutableStateOf(password) }
    var matkhau = remember { mutableStateOf("") }
    var matkhaumoi = remember { mutableStateOf("") }
    var matkhaumoi2 = remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }
    var updatePass: AccountViewModel = viewModel()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordVisible1 by remember { mutableStateOf(false) }
    var isPasswordVisible2 by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Đổi mật khẩu",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
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
                    OutlinedTextField(
                        value = matkhau.value,
                        onValueChange = { newText ->
                            matkhau.value = newText
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                )
                            }
                        },
                        singleLine = true,
                        label = { Text("Nhập mật khẩu cũ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = matkhaumoi.value,
                        onValueChange = { newText ->
                            matkhaumoi.value = newText
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible1 = !isPasswordVisible1 }) {
                                Icon(
                                    imageVector = if (isPasswordVisible1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isPasswordVisible1) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                )
                            }
                        },
                        singleLine = true,
                        label = { Text("Nhập mật khẩu mới") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (isPasswordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = matkhaumoi2.value,
                        onValueChange = { newText ->
                            matkhaumoi2.value = newText
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible2 = !isPasswordVisible2 }) {
                                Icon(
                                    imageVector = if (isPasswordVisible2) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isPasswordVisible2) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                )
                            }
                        },
                        singleLine = true,
                        label = { Text("Nhập lại mật khẩu") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (isPasswordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            modifier = Modifier.width(screenWidth / 2),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF)),
                            onClick = {
                                /*Chuc nang xac nhan doi mat khau*/
                                openDialog = true
                            }
                        ) {
                            Text("Xác nhận")
                        }
                    }
                }
            }
        }
    )
    if (openDialog == true) {
        AlertDialog(
            onDismissRequest = { openDialog = false }, // Đóng khi nhấn ngoài dialog
            text = {
                if (matkhau.value == password) {
                    if (matkhaumoi.value == "" && matkhaumoi2.value == "") {
                        Text("Vui lòng nhập đủ thông tin")
                    }
                    else if(matkhaumoi.value == password){
                        Text("Mật khẩu mới không được trùng với mật khẩu cũ")
                    }
                    else if (matkhaumoi.value != matkhaumoi2.value) {
                        Text("Mật khẩu mới và xác nhận mật khẩu không trùng khớp")
                    } else {
                        Text("Đổi mật khẩu thành công. Vui lòng thực hiện đăng nhập lại!")
                        var account: UpdatePassword
                        account = UpdatePassword(id, matkhaumoi2.value)
                        updatePass.updatePassword(account)
                    }
                } else {
                    Text("Mật khẩu cũ không đúng")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog = false
                        onBack() // chuyển về trang login
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C3FF)
                    )
                ) {
                    Text("OK")
                }
            },
        )
    }
}
