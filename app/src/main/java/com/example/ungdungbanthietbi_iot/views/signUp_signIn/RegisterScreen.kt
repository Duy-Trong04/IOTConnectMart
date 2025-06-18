package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import com.example.ungdungbanthietbi_iot.api.RegisterRequest
import com.example.ungdungbanthietbi_iot.viewModels.RegisterUiState
import com.example.ungdungbanthietbi_iot.views.components.CustomerTextField


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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    customerViewModel: CustomerViewModel
) {
    val focusManager = LocalFocusManager.current
    val registerUiState by accountViewModel.registerUiState.collectAsState()
    val isLoading by accountViewModel.isLoading.collectAsState()

    var surname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var comFirmPassword by remember { mutableStateOf("") }
    var passwordObscure by remember { mutableStateOf(true) }
    var comFirmPasswordObscure by remember { mutableStateOf(true) }
    var openDialog by remember { mutableStateOf(false) }
    var openDialog_Success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }

    // Xử lý phản hồi từ API
    LaunchedEffect(registerUiState) {
        when (registerUiState) {
            is RegisterUiState.Success -> {
                openDialog_Success = true
                errorMessage = "Đăng ký thành công\nBạn có thể đăng nhập vào ứng dụng"
            }
            is RegisterUiState.Error -> {
                openDialog = true
                errorMessage = (registerUiState as RegisterUiState.Error).message
            }
            else -> {
                // Không làm gì khi Idle hoặc Loading
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                )
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
                painter = painterResource(id = R.drawable.ill_signup),
                contentDescription = "Sign up Illustration",
                modifier = Modifier
                    .weight(2.7f)
                    .padding(
                        horizontal = 32.dp,
                    ),
                contentScale = ContentScale.Fit,
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(7.3f),
            ) {
                Text(
                    text = "Đăng ký", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D9EFF)
                    )
                )
                CustomerTextField(
                    value = surname,
                    onValueChange = {
                        surname = it
                    },
                    hint = "Họ",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Họ",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                CustomerTextField(
                    value = lastname,
                    onValueChange = {
                        lastname = it
                    },
                    hint = "Tên",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person2,
                            contentDescription = "Tên",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                CustomerTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        if (it.matches(Regex("\\d*"))) {
                            phone = it
                        }
                        phoneError = if (it.length == 10) "" else "Số điện thoại phải đúng 10 số"
                    },
                    hint = "Số điện thoại",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.PhoneAndroid,
                            contentDescription = "Số điện thoại",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                CustomerTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    hint = "Email ",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email Field",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                CustomerTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    hint = "Tài khoản",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Tài khoản",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                CustomerTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    hint = "Mật khẩu",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Password,
                            contentDescription = "Password Field",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordObscure) painterResource(id = R.drawable.ic_outline_visibility_off) else painterResource(
                            id = R.drawable.ic_outline_visibility
                        ), contentDescription = "Show Password", modifier = Modifier.clickable {
                            passwordObscure = !passwordObscure
                        })
                    },
                    obscure = passwordObscure,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                CustomerTextField(
                    value = comFirmPassword,
                    onValueChange = {
                        comFirmPassword = it
                    },
                    hint = "Nhập lại mật khẩu",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Password,
                            contentDescription = "Password Field",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (comFirmPasswordObscure) painterResource(id = R.drawable.ic_outline_visibility_off) else painterResource(
                            id = R.drawable.ic_outline_visibility
                        ), contentDescription = "Show Password", modifier = Modifier.clickable {
                            comFirmPasswordObscure = !comFirmPasswordObscure
                        })
                    },
                    obscure = comFirmPasswordObscure,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Button(
                    onClick = {
                        when {
                            surname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                                    username.isEmpty() || password.isEmpty() || comFirmPassword.isEmpty() -> {
                                errorMessage = "Vui lòng nhập đầy đủ thông tin !"
                                openDialog = true
                            }
                            phoneError.isNotEmpty() -> {
                                errorMessage = phoneError
                                openDialog = true
                            }
                            password != comFirmPassword -> {
                                errorMessage = "Mật khẩu và Xác nhận mật khẩu không trùng khớp !"
                                openDialog = true
                            }
                            else -> {
                                val accountNew = RegisterRequest(
                                    username = username,
                                    password = password,
                                    confirm_password = comFirmPassword,
                                    surname = surname,
                                    lastname = lastname,
                                    phone = phone,
                                    email = email,
                                    gender = true
                                )
                                accountViewModel.register(accountNew)
                            }
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
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "Đăng ký",
                            fontSize = 18.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Text(
                        "Bạn đã có tài khoản ?",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Box(modifier = Modifier.width(8.dp))
                    Text(
                        "Đăng nhập !",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF5D9EFF),
                        ),
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }
                Box(modifier = Modifier.height(24.dp))
            }
        }
        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                title = { Text(text = "Thông báo") },
                containerColor = Color.White,
                text = { Text(errorMessage) },
                confirmButton = {
                    Button(
                        onClick = { openDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Xác nhận")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { openDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        shape = RoundedCornerShape(10.dp)
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
                title = {
                    Text("Thông báo")
                },
                containerColor = Color.White,
                text = {
                    Text(errorMessage)
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Đăng nhập")
                    }
                },
            )
        }
    }
}
