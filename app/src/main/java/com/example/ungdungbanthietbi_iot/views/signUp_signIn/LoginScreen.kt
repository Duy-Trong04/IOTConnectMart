package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch
import androidx.compose.material3.TextButton
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.views.components.CustomerTextField

/** Giao diện màn hình đăng nhập (LoginScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình đăng nhập
 * ------------------------------------------------------------
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 21/12/2024
 * ------------------------------------------------------------
 * Nội dung cập nhật:Chỉnh sửa lại TextField, layout
 *
 */

//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@ExperimentalComposeUiApi
//@Composable
//fun LoginScreen(navController: NavController, accountViewModel: AccountViewModel) {
//    val context = LocalContext.current
////    val snackBarHostState = remember {
////        SnackbarHostState()
////    }
//    // Biến nhận dữ liệu email từ người dùng
//    var username by remember { mutableStateOf("") }
//    // Biến nhận dữ liệu password từ người dùng
//    var password by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//    var openDialog by remember { mutableStateOf(false) }
//    // Biến kiểm tra trạng thái hiển thị mật khẩu
//    var isPasswordVisible by remember { mutableStateOf(false) }
//    val (focusUsername,focusPassword) = remember { FocusRequester.createRefs()}
//    val keyboardController =  LocalSoftwareKeyboardController.current
//
//    // Key cho DataStore
//    val usernameKey = stringPreferencesKey("username")
//    val passwordKey = stringPreferencesKey("password")
//
//    Scaffold {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(fraction = 0.30f),
//                Alignment.TopEnd,
//            ){
//                Image(painter = painterResource(id = R.drawable.ic_shape), contentDescription = "",
//                    modifier = Modifier.fillMaxSize(),contentScale = ContentScale.FillBounds
//                )
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight()
//                        .padding(horizontal = 20.dp, vertical = 50.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Image(painter = painterResource(id = R.drawable.ecom), contentDescription = "Logo App",
//                        modifier = Modifier
//                            .weight(1f)
//                            .size(500.dp),
//                        colorFilter = ColorFilter.tint(Color.White)
//                    )
//                }
//
//            }
//            Column(modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp)) {
//                Text(text = "Đăng nhập", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
//                Spacer(modifier = Modifier.height(16.dp))
//                OutlinedTextField(value = username, onValueChange = {username = it},
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusUsername),
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                    keyboardActions = KeyboardActions(onNext = {focusPassword.requestFocus()}),
//                    singleLine = true,
//                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color(0xFF5D9EFF),
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedLabelColor = Color(0xFF5D9EFF),
//                        cursorColor = Color(0xFF5D9EFF)
//                    ),
//                    label = {Text(text = "Username")}
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                OutlinedTextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusPassword),
//                    value = password,
//                    onValueChange ={password = it},
//                    label = { Text(text = "Password")},
//                    singleLine = true,
//                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color(0xFF5D9EFF),
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedLabelColor = Color(0xFF5D9EFF),
//                        cursorColor = Color(0xFF5D9EFF)
//                    ),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions (onDone = {keyboardController?.hide()}),
//                    visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
//                            Icon(imageVector = if(isPasswordVisible)Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
//                                contentDescription ="Password Toggle" )
//
//                        }
//                    }
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth(),Arrangement.End
//                ){
//                    TextButton(onClick = {
//                        /* Chuyển sang màn hình quên mật khẩu(ForgotPasswordScreen)*/
//                        navController.navigate(Screen.ForgotPasswordScreen.route)
//                    }) {
//                        Text(text = "Quên mật khẩu?",fontSize = 14.sp, color = Color.Black)
//                    }
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = {
//                    /* Chuyển sang màn hình trang chủ(HomeScreen) */
//                    if(username.isEmpty() || password.isEmpty()){
//                        openDialog = true
//                    }
//                    else {
//                        accountViewModel.checkLogin(username, password)
//                        scope.launch {
//                            // Lắng nghe kết quả từ loginResult
//                            accountViewModel.loginUiState.collect { state ->
//                                if (state.isLoading) return@collect
//                                if (state.result == true) {
//                                    context.dataStore.edit { preferences ->
//                                        preferences[usernameKey] = username
//                                        preferences[passwordKey] = password
//                                    }
//                                    navController.navigate(Screen.HomeScreen.route + "?username=$username&id=${state.customer_id}&password=$password") {
//                                        popUpTo(0) { inclusive = true }
//                                    }
//                                } else if (state.result == false) {
//                                    openDialog = true
//                                }
//                                return@collect
//                            }
//                        }
//                    }
//                },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF5D9EFF)
//                    )
//                ) {
//                    Text(text = "Đăng nhập", fontSize = 18.sp)
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                Row(modifier = Modifier.fillMaxWidth(),Arrangement.Center,verticalAlignment = Alignment.CenterVertically) {
//                    Text(text = "Bạn chưa có tài khoản?",fontSize = 14.sp)
//                    TextButton(onClick = {
//                        /* Chuyển sang màn hình đăng ký(RegisterScreen) */
//                        navController.navigate(Screen.RegisterScreen.route)
//                    }) {
//                        Text(
//                            text = "Đăng ký",
//                            textDecoration = TextDecoration.Underline,
//                            color = Color(0xFF5D9EFF)
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//        if (openDialog) {
//            AlertDialog(
//                onDismissRequest = { openDialog = false },
//                title = {
//                    Text(text = "Thông báo")
//                },
//                text = {
//                    if (username.isEmpty() || password.isEmpty()) {
//                        Text("Vui lòng nhập đầy đủ thông tin")
//                    } else {
//                        Text("Tài khoản hoặc mật khẩu không chính xác")
//                    }
//                },
//                shape = RoundedCornerShape(15.dp),
//                confirmButton = {
//                    Button(
//                        onClick = { openDialog = false },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF5D9EFF)
//                        ),
//                        shape = RoundedCornerShape(10.dp)
//                    ) {
//                        Text("Xác nhận")
//                    }
//                }
//            )
//        }
//    }
//}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, accountViewModel: AccountViewModel){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Biến nhận dữ liệu email từ người dùng
    var username by remember { mutableStateOf("") }
    // Biến nhận dữ liệu password từ người dùng
    var password by remember { mutableStateOf("") }
    var passwordObscure by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    var openDialog by remember { mutableStateOf(false) }
    // Key cho DataStore
    val usernameKey = stringPreferencesKey("username")
    val passwordKey = stringPreferencesKey("password")

    Scaffold {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Box(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.ill_signin),
                contentDescription = "Sign in Illustration",
                modifier = Modifier
                    .weight(3f)
                    .padding(
                        horizontal = 32.dp,
                    ),
                contentScale = ContentScale.Fit,
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(7f)
            ) {
                Text(
                    text = "Đăng nhập",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D9EFF)
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
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
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
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Mật khẩu",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordObscure) painterResource(id = R.drawable.ic_outline_visibility) else painterResource(
                            id = R.drawable.ic_outline_visibility_off
                        ), contentDescription = "Show Password", modifier = Modifier.clickable {
                            passwordObscure = !passwordObscure
                        }
                        )
                    },
                    obscure = passwordObscure,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                TextButton(
                    onClick = {
                        /* Chuyển sang màn hình quên mật khẩu(ForgotPasswordScreen)*/
                        navController.navigate(Screen.ForgotPasswordScreen.route)
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF5D9EFF)
                    )
                ) {
                    Text("Quên mật khẩu ?")
                }
                Button(
                    onClick = {
                        /* Chuyển sang màn hình trang chủ(HomeScreen) */
                        if(username.isEmpty() || password.isEmpty()){
                            openDialog = true
                        }
                        else {
                            accountViewModel.checkLogin(username, password)
                            scope.launch {
                                // Lắng nghe kết quả từ loginResult
                                accountViewModel.loginUiState.collect { state ->
                                    if (state.isLoading) return@collect
                                    if (state.result == true) {
                                        context.dataStore.edit { preferences ->
                                            preferences[usernameKey] = username
                                            preferences[passwordKey] = password
                                        }
                                        navController.navigate(Screen.HomeScreen.route + "?username=$username&id=${state.customer_id}&password=$password") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else if (state.result == false) {
                                        openDialog = true
                                    }
                                    return@collect
                                }
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
                    Text(
                        text = "Đăng nhập",
                        fontSize = 18.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Divider(
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "HOẶC",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Box(modifier = Modifier.width(32.dp))
                    Text(
                        text = "Đăng nhập với Google",
                        style = MaterialTheme.typography.bodyLarge.copy(Color.Black)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "Bạn chưa có tài khoản?",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Box(modifier = Modifier.width(8.dp))
                    Text(
                        "Đăng ký !",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF5D9EFF),
                        ),
                        modifier = Modifier.clickable {
                            /* Chuyển sang màn hình đăng ký(RegisterScreen) */
                            navController.navigate(Screen.RegisterScreen.route)
                        }
                    )
                }
            }
        }
    }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(text = "Thông báo")
            },
            text = {
                if (username.isEmpty() || password.isEmpty()) {
                    Text("Vui lòng nhập đầy đủ thông tin")
                } else {
                    Text("Tài khoản hoặc mật khẩu không chính xác")
                }
            },
            shape = RoundedCornerShape(15.dp),
            confirmButton = {
                Button(
                    onClick = { openDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Xác nhận")
                }
            }
        )
    }
}