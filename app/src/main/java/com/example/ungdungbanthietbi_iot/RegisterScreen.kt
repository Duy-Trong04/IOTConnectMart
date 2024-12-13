package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RegisterScreen(navController: NavController) {
    // Biến nhận dữ liệu họ từ người dùng
    var ho by remember { mutableStateOf("") }
    // Biến nhận dữ liệu tên từ người dùng
    var ten by remember { mutableStateOf("") }
    // Biến nhận dữ liệu sdt từ người dùng
    var sdt by remember { mutableStateOf("") }
    // Biến nhận dữ liệu email từ người dùng
    var email by remember { mutableStateOf("") }
    // Biến nhận dữ liệu password từ người dùng
    var password by remember { mutableStateOf("") }
    // Biến nhận dữ liệu comfirmPassword từ người dùng
    var comfirmPassword by remember { mutableStateOf("") }
    // Biến kiểm tra trạng thái hiển thị password
    var isPasswordVisible by remember { mutableStateOf(false) }
    // Biến kiểm tra trạng thái hiển thị comfirmPassword
    var isComfirmPasswordVisible by remember { mutableStateOf(false) }
    // Khởi tạo FocusRequester cho các trường nhập liệu
    val focusRequesterHo = remember { FocusRequester() }
    val focusRequesterTen = remember { FocusRequester() }
    val focusRequesterSdt = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterComfirmPassword = remember { FocusRequester() }
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
                    Spacer(modifier = Modifier.height(70.dp))

                    Text(
                        text = "ĐĂNG KÝ",
                        fontSize = 37.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )

                    // Hiển thị logo từ file drawable
                    Image(
                        // Thay "logo" bằng tên file ảnh của bạn
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(320.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "IOT ConnectSmart",
                        fontSize = 27.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    //Họ
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .border(1.dp, Color(0xFF085979), shape = MaterialTheme.shapes.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = ho,
                                onValueChange = { ho = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .focusRequester(focusRequesterHo)
                                    .padding(8.dp),
                                // khi người dùng chưa nhập thì hiện nội dung mặc định trong Text( cụ thể trong đây là Họ )
                                decorationBox = { innerTextField ->
                                    if (ho.isEmpty()) {
                                        Text(text = "Họ", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    }

                    //Tên
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .border(1.dp, Color(0xFF085979), shape = MaterialTheme.shapes.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = ten,
                                onValueChange = { ten = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .focusRequester(focusRequesterTen)
                                    .padding(8.dp),
                                // khi người dùng chưa nhập thì hiện nội dung mặc định trong Text( cụ thể trong đây là Tên )
                                decorationBox = { innerTextField ->
                                    if (ten.isEmpty()) {
                                        Text(text = "Tên", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    }

                    //SDT
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .border(1.dp, Color(0xFF085979), shape = MaterialTheme.shapes.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = sdt,
                                onValueChange = { sdt = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .focusRequester(focusRequesterSdt)
                                    .padding(8.dp),
                                // khi người dùng chưa nhập thì hiện nội dung mặc định trong Text( cụ thể trong đây là SDT )
                                decorationBox = { innerTextField ->
                                    if (sdt.isEmpty()) {
                                        Text(text = "SDT", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                // Sử dụng bàn phím số
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                    , imeAction = ImeAction.Next
                                )
                            )
                        }
                    }

                    //email
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .border(1.dp, Color(0xFF085979), shape = MaterialTheme.shapes.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .focusRequester(focusRequesterEmail)
                                    .padding(8.dp),
                                // khi người dùng chưa nhập thì hiện nội dung mặc định trong Text( cụ thể trong đây là Email )
                                decorationBox = { innerTextField ->
                                    if (email.isEmpty()) {
                                        Text(text = "Email", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    }

                    // Password
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .border(1.dp, Color(0xFF085979), shape = MaterialTheme.shapes.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .focusRequester(focusRequesterPassword)
                                    .padding(8.dp),
                                // Ẩn/hiện nội dung của Password
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                // khi người dùng chưa nhập thì hiện nội dung mặc định trong Text( cụ thể trong đây là Password )
                                decorationBox = { innerTextField ->
                                    if (password.isEmpty()) {
                                        Text(text = "Password", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next
                                )
                            )
                            Image(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp)
                                    // Đổi trạng thái Password khi nhấn
                                    .clickable{
                                        isPasswordVisible=!isPasswordVisible
                                    },
                            )
                        }
                    }

                    // Comfirm Password
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .focusRequester(focusRequesterComfirmPassword)
                            .border(1.dp, Color(0xFF085979), shape = MaterialTheme.shapes.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = comfirmPassword,
                                onValueChange = { comfirmPassword = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .padding(8.dp),
                                // Ẩn/hiện nội dung của Comfirm Password
                                visualTransformation = if (isComfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                // khi người dùng chưa nhập thì hiện nội dung mặc định trong Text( cụ thể trong đây là Comfirm Password )
                                decorationBox = { innerTextField ->
                                    if (comfirmPassword.isEmpty()) {
                                        Text(text = "Comfirm Password", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                )
                            )
                            Image(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription ="",
                                modifier = Modifier
                                    .size(20.dp)
                                    // Đổi trang thái Comfirm Password khi nhấn
                                    .clickable{
                                      isComfirmPasswordVisible=!isComfirmPasswordVisible
                                    },
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    Row() {
                        Text(
                            text = "Bạn đã có tài khoản? ",
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Đăng nhập",
                            fontSize = 14.sp,
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
                        onClick = { /* Chuyển sang màn hình đăng nhập(LoginScreen) */
                            navController.navigate(Screen.LoginScreen.route)
                        },
                        modifier = Modifier
                            .width(370.dp)
                            .padding(bottom = 10.dp)
                            .height(50.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C3FF))
                    ) {
                        Text(text = "ĐĂNG KÝ", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    }


                }
            }
        }
    )

}