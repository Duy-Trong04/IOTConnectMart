package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    email: String?
) {
    val focusManager = LocalFocusManager.current

    var password by remember { mutableStateOf("") }
    var passwordObscure by remember { mutableStateOf(true) }

    var passwordConfirm by remember { mutableStateOf("") }
    var passwordConfirmObscure by remember { mutableStateOf(true) }

    var openDialog_Success by remember { mutableStateOf(false) }
    var openDialog_Error by remember { mutableStateOf(false) }

    Scaffold {
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
                            imageVector = Icons.Outlined.Password,
                            contentDescription = "New Password Field",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordObscure) painterResource(id = R.drawable.ic_outline_visibility_off) else painterResource(
                            id = R.drawable.ic_outline_visibility
                        ), contentDescription = "Show New Password", modifier = Modifier.clickable {
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
                Box(modifier = Modifier.height(16.dp))
                CustomerTextField(
                    value = passwordConfirm,
                    onValueChange = {
                        passwordConfirm = it
                    },
                    hint = "Nhập lại mật khẩu mới",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Password,
                            contentDescription = " Re-enter New Password Field",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordConfirmObscure) painterResource(id = R.drawable.ic_outline_visibility_off) else painterResource(
                            id = R.drawable.ic_outline_visibility
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
                            accountViewModel.resetPassword(email!!, password, passwordConfirm)
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
                        ),
                        shape = RoundedCornerShape(10.dp)
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
                        ),
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
                containerColor = Color.White,
                title = {
                    Text("Thông báo")
                },
                text = {
                    Text("Đặt lại mật khẩu thành công!\nVui lòng đăng nhập lại.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog_Success = false
                            navController.navigate(Screen.LoginScreen.route){
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
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