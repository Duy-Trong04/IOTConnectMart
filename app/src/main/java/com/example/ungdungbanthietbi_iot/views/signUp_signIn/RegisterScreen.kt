package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.models.AddAccount
import com.example.ungdungbanthietbi_iot.models.AddCustomer
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.ungdungbanthietbi_iot.api.RegisterRequest


/** Giao diện màn hình đăng ký (RegisterScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình đăng ký
 * ------------------------------------------------------------
 *
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 21/12/2024
 * ------------------------------------------------------------
 * Nội dung cập nhật: chỉnh sửa lại các TextField, layout
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RegisterScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    customerViewModel: CustomerViewModel
) {


    val accountCheckResult by accountViewModel.accountCheckResult
    val customerCheckResult by customerViewModel.customerCheckResult
    Log.d("AccountViewModel", "accountCheckResult: $accountCheckResult")
    Log.d("AccountViewModel", "customerCheckResult: $customerCheckResult")

    // Biến nhận dữ liệu họ từ người dùng
    var ho by remember { mutableStateOf("") }
    // Biến nhận dữ liệu tên từ người dùng
    var ten by remember { mutableStateOf("") }
    // Biến nhận dữ liệu sdt từ người dùng
    var sdt by remember { mutableStateOf("") }
    // Biến nhận dữ liệu email từ người dùng
    var email by remember { mutableStateOf("") }
    // Biến nhận dữ liệu username từ người dùng
    var username by remember { mutableStateOf("") }
    // Biến nhận dữ liệu password từ người dùng
    var password by remember { mutableStateOf("") }
    // Biến nhận dữ liệu comfirmPassword từ người dùng
    var comfirmPassword by remember { mutableStateOf("") }
    // Biến kiểm tra trạng thái hiển thị password
    var isPasswordVisible by remember { mutableStateOf(false) }
    // Biến kiểm tra trạng thái hiển thị comfirmPassword
    var isComfirmPasswordVisible by remember { mutableStateOf(false) }
    // Biến kiểm tra kết quả đăng nhập
    var openDialog by remember { mutableStateOf(false) }
    var openDialog_Dk by remember { mutableStateOf(false) }

    // Gửi dữ liệu lên server
    //val accountNew = AddAccount(username, username, password)
    val customerNew = AddCustomer(username, ho, ten, sdt)
    //accountViewModel.check_Dk(accountNew)
    customerViewModel.check_Dk(customerNew)

    var phoneError by remember { mutableStateOf("") }

//    // Xử lý kết quả từ API
//    LaunchedEffect(registerResult) {
//        val context = LocalContext.current
//        registerResult?.let { response ->
//            if (response.isSuccessful && response.body()?.status_code == 200) {
//                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
//                openDialog_Dk = true
//            } else {
//                val errorMessage = response.errorBody()?.string() ?: "Đăng ký thất bại"
//                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                openDialog = true
//            }
//        }
//    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "ĐĂNG KÝ",
                        fontSize = 27.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Hiển thị logo từ file drawable
                    Image(
                        // Thay "logo" bằng tên file ảnh của bạn
                        painter = painterResource(id = R.drawable.logo9),
                        contentDescription = "Logo",
                        modifier = Modifier.size(240.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    //Họ
                    TextField(
                        value = ho,
                        onValueChange = { ho = it },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Họ") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Họ"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )

                    //Tên
                    TextField(
                        value = ten,
                        onValueChange = { ten = it },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Tên") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = "Tên"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    //SDT
                    TextField(
                        value = sdt,
                        onValueChange = {
                            sdt = it
                            if (it.matches(Regex("\\d*"))) { // Chỉ cho phép nhập số
                                sdt = it
                            }
                            phoneError = if (it.length == 10) {
                                ""
                            } else {
                                "Số điện thoại phải đúng 10 số"
                            } },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "SĐT") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "SĐT"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                        )
                    )
                    if (phoneError.isNotEmpty()) {
                        Text(
                            text = phoneError,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }

                    //email
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "email"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    //username
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Username") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Username"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    // Password
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )

                    // Comfirm Password
                    TextField(
                        value = comfirmPassword,
                        onValueChange = { comfirmPassword = it },
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Comfirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Comfirm Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                isComfirmPasswordVisible = !isComfirmPasswordVisible
                            }) {
                                Icon(
                                    imageVector = if (isComfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isComfirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        visualTransformation = if (isComfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row() {
                        Text(
                            text = "Bạn đã có tài khoản? ",
                            fontSize = 15.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Đăng nhập",
                            fontSize = 15.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { /* Chuyển sang màn hình đăng nhập(LoginScreen) */
                                navController.navigate(Screen.LoginScreen.route)
                            }
                        )
                    }

                    //Button
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {

                            val accountNew = RegisterRequest(
                                username = username,
                                password = password,
                                confirm_password = comfirmPassword,
                                surname = ho,
                                lastname = ten,
                                phone = sdt,
                                email = email,
                                gender = true
                            )
                            if (username.isNotEmpty() && password.isNotEmpty() && comfirmPassword.isNotEmpty() && sdt.isNotEmpty() && ho.isNotEmpty() && ten.isNotEmpty()) {
                                accountViewModel.register(accountNew)
                                navController.navigate(Screen.LoginScreen.route)
                                Log.d("AccountViewModel", "Đăng ký thành công")
                                openDialog_Dk = true
                            }
                            else {
                                openDialog = true
                            }
                        },
                        modifier = Modifier
                            .width(350.dp)
                            .padding(bottom = 10.dp)
                            .height(45.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C3FF))
                    ) {
                        Text(text = "ĐĂNG KÝ", fontSize = 23.sp, fontWeight = FontWeight.Bold)
                    }

                }
            }
        }
    )
    if (openDialog == true) {
        AlertDialog(
            onDismissRequest = { openDialog = false }, // Đóng khi nhấn ngoài dialog
            text = {
                if (username == "" || password == "" || comfirmPassword == "" || sdt == "" || ho == "" || ten == "") {
                    Text("Vui lòng nhập đầy đủ thông tin")
                } else if (password != comfirmPassword) {
                    Text("Password và Comfirm Password không khớp")
                }
                else {
                    Text("Tài khoản đã tồn tại")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog = false
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
    if (openDialog_Dk == true) {
        AlertDialog(
            onDismissRequest = { openDialog_Dk = false }, // Đóng khi nhấn ngoài dialog
            text = {
                Text("Đăng ký thành công")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog_Dk = false
                        navController.navigate(Screen.LoginScreen.route)
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