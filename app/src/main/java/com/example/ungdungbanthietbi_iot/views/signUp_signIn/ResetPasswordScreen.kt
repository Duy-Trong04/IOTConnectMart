package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.views.components.CustomerTextField

/** Giao diện màn hình cập nhật mật khẩu (ResetPasswordScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 09/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình cập nhật mật khẩu
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//
//fun ResetPasswordScreen(navController: NavController,
//                        accountViewModel: AccountViewModel,
//                        email: String?) {
//    // Biến nhận dữ liệu mật khẩu mới từ người dùng
//    var Password by remember { mutableStateOf("") }
//    // Biến nhận dữ liệu nhập lại mật khầu từ người dùng
//    var comfirmPassword by remember { mutableStateOf("") }
//    // Biến kiểm tra trạng thái hiển thị password
//    var isPasswordVisible by remember { mutableStateOf(false) }
//    // Biến kiểm tra trạng thái hiển thị comfirmPassword
//    var isComfirmPasswordVisible by remember { mutableStateOf(false) }
//    // Khởi tạo FocusRequester cho các trường nhập liệu
//    val focusRequesterNewPassword = remember { FocusRequester() }
//    val focusRequesterComfirmPassword = remember { FocusRequester() }
//
//    Scaffold(
//        modifier = Modifier.fillMaxWidth(),
//        content = { padding ->
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//                    .background(Color.White),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                item {
//                    Spacer(modifier = Modifier.height(50.dp))
//
//                    Text(
//                        text = "TẠO MẬT KHẨU MỚI",
//                        fontSize = 27.sp,
//                        color = Color(0xFF085979),
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    // Hiển thị logo từ file drawable
//                    Image(
//                        // Thay "logo" bằng tên file ảnh
//                        painter = painterResource(id = R.drawable.logo),
//                        contentDescription = "Logo",
//                        modifier = Modifier.size(300.dp)
//                    )
//
//                    Text(
//                        text = "IOT Connect Mart",
//                        fontSize = 27.sp,
//                        color = Color(0xFF085979),
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                    //New Password
//                    TextField(
//                        value = Password,
//                        onValueChange = {Password = it},
//                        modifier = Modifier.width(350.dp).padding(4.dp),
//                        placeholder = { Text(text = "Password") },
////                        leadingIcon = {
////                            Icon(imageVector = Icons.Default.Lock,
////                                contentDescription = "Password"
////                            )
////                        },
//                        trailingIcon = {
//                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                                Icon(imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                                    contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
//                                )
//                            }
//                        },
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.White,
//                            unfocusedContainerColor = Color.White,
//                            focusedIndicatorColor = Color(0xFF00C3FF)
//                        ),
//                        singleLine = true,
//                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
//                            imeAction = ImeAction.Next
//                        )
//                    )
//
//                    //Comfirm Password
//                    TextField(
//                        value = comfirmPassword,
//                        onValueChange = {comfirmPassword = it},
//                        modifier = Modifier.width(350.dp).padding(4.dp),
//                        placeholder = { Text(text = "Comfirm Password") },
////                        leadingIcon = {
////                            Icon(imageVector = Icons.Default.Lock,
////                                contentDescription = "Comfirm Password"
////                            )
////                        },
//                        trailingIcon = {
//                            IconButton(onClick = { isComfirmPasswordVisible = !isComfirmPasswordVisible }) {
//                                Icon(imageVector = if (isComfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                                    contentDescription = if (isComfirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
//                                )
//                            }
//                        },
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.White,
//                            unfocusedContainerColor = Color.White,
//                            focusedIndicatorColor = Color(0xFF00C3FF)
//                        ),
//                        singleLine = true,
//                        visualTransformation = if (isComfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
//                            imeAction = ImeAction.Done
//                        )
//                    )
//                    //Button
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        onClick = { /* Chuyển sang màn hình đăng nhập(LoginScreen) */
//                            accountViewModel.resetPassword(email!!, Password, comfirmPassword)
//                            navController.navigate(Screen.LoginScreen.route)
//                        },
//                        modifier = Modifier
//                            .width(350.dp)
//                            .padding(bottom = 10.dp)
//                            .height(45.dp),
//                        shape = MaterialTheme.shapes.small,
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C3FF))
//                    ) {
//                        Text(text = "XÁC NHẬN", fontSize = 23.sp, fontWeight = FontWeight.Bold)
//                    }
//                }
//            }
//
//        }
//    )
//
//}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    email: String
) {
    val focusManager = LocalFocusManager.current

    var password by remember { mutableStateOf("") }
    var passwordObscure by remember { mutableStateOf(true) }

    var passwordConfirm by remember { mutableStateOf("") }
    var passwordConfirmObscure by remember { mutableStateOf(true) }

    var openDialog_Success by remember { mutableStateOf(false) }
    var openDialog_Error by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color(0xFF5D9EFF)
                ),
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Box(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.ill_reset_password),
                contentDescription = "Reset Password Illustration",
                modifier = Modifier
                    .weight(4.5f)
                    .padding(horizontal = 32.dp),
                contentScale = ContentScale.Fit,
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(5.5f)
            ) {
                Text(
                    text = "Đặt lại\nMật khẩu ?", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D9EFF)
                    )
                )
                Box(modifier = Modifier.height(16.dp))
                CustomerTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    hint = "Mật khẩu mới",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "New Password Field",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordObscure) painterResource(id = R.drawable.ic_outline_visibility) else painterResource(
                            id = R.drawable.ic_outline_visibility_off
                        ), contentDescription = "Show New Password", modifier = Modifier.clickable {
                            passwordObscure = !passwordObscure
                        })
                    },
                    obscure = passwordObscure,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                Box(modifier = Modifier.height(16.dp))
                CustomerTextField(
                    value = passwordConfirm,
                    onValueChange = {
                        passwordConfirm = it
                    },
                    hint = "Nhập lại mật khẩu mới",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = " Re-enter New Password Field",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordConfirmObscure) painterResource(id = R.drawable.ic_outline_visibility) else painterResource(
                            id = R.drawable.ic_outline_visibility_off
                        ), contentDescription = "Show Re-entered New Password", modifier = Modifier.clickable {
                            passwordConfirmObscure = !passwordConfirmObscure
                        })
                    },
                    obscure = passwordConfirmObscure,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                Box(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        /* Chuyển sang màn hình đăng nhập(LoginScreen) */
                        if(password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                            accountViewModel.resetPassword(email, password, passwordConfirm)
                            openDialog_Success = true
                        }
                        else{
                            openDialog_Error = true
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    )
                ) {
                    Text(
                        text = "Xác nhận",
                        fontSize = 18.sp
                    )
                }
                Box(modifier = Modifier.height(16.dp))
            }
        }
        if (openDialog_Error) {
            AlertDialog(
                onDismissRequest = { openDialog_Error = false }, // Đóng khi nhấn ngoài dialog
                text = {
                    if (password == "" || passwordConfirm == "") {
                        Text("Vui lòng nhập đầy đủ thông tin!")
                    } else if (password != passwordConfirm) {
                        Text("Mật khẩu và Xác nhận mật khẩu không trùng khớp!")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog_Error = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5F9EFF)
                        )
                    ) {
                        Text("Xác nhận")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog_Error = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text("Hủy")
                    }
                }
            )
        }
        if (openDialog_Success) {
            AlertDialog(
                onDismissRequest = {
                    openDialog_Success = false
                    navController.navigate(Screen.LoginScreen.route)
                }, // Đóng khi nhấn ngoài dialog
                icon = { Icon(Icons.Filled.Done, contentDescription = "") },
                iconContentColor =  Color.Green,
                title = {
                    Text("Thông báo")
                },
                titleContentColor = Color.Green,
                text = {
                    Text("Đặt lại mật khẩu thành công!")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog_Success = false
                            navController.navigate(Screen.LoginScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5F9EFF)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Đăng nhập")
                    }
                },
            )
        }
    }
}