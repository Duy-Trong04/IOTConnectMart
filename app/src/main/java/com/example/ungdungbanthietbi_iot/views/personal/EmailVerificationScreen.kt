package com.example.ungdungbanthietbi_iot.views.personal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerState
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    navController: NavController,
    idCustomer: String,
    email: String,
    username: String,
    token: String
) {
    val customerViewModel: CustomerViewModel = viewModel()

    val customerState by customerViewModel.customerState.collectAsState()
    val accountViewModel: AccountViewModel = viewModel()
    val isLoading by accountViewModel.isLoading.collectAsState()
    val sendOtpResult by accountViewModel.sendOtpResult.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    LaunchedEffect(idCustomer) {
        if (idCustomer.isBlank()) {
            customerViewModel.setErrorState("Lỗi: ID khách hàng không hợp lệ")
        }
        else{
            customerViewModel.getCustomerById(idCustomer)
        }
    }
    // Theo dõi kết quả gửi OTP
    LaunchedEffect(sendOtpResult) {
        sendOtpResult?.let { response ->
            if (response.isSuccessful) {
                showSuccessDialog = true
            } else {
                errorMessage = "Không tìm thấy email\nVui lòng nhập lại !"
                showErrorDialog = true
            }
            // Reset trạng thái trong ViewModel để tránh lặp lại
            accountViewModel.resetSendOtpResult()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Xác thực Email",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        when (val state = customerState) {
            is CustomerState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(600.dp).background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF5F9EFF)
                    )
                }
            }
            is CustomerState.Success -> {
                val customer = state.customer
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { /* Không cho phép chỉnh sửa */ },
                        label = { Text("Email") },
                        trailingIcon = {
                            Icon(
                                imageVector = if(customer.email_verified) Icons.Default.Done else Icons.Default.Close,
                                contentDescription = "",
                                tint = if( customer.email_verified) Color(0xFF02C92C) else Color.Red
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = if(customer.email_verified) Color(0xFF02C92C) else Color.Red,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false, // Vô hiệu hóa chỉnh sửa
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            accountViewModel.sendOtp(email)
                        },
                        shape = RoundedCornerShape(10.dp),
                        enabled = !customer.email_verified,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF),
                            disabledContentColor = Color(0xFF5D9EFF)
                        ),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        }
                        else {
                            if(customer.email_verified){
                                Text("Email đã xác thực", fontSize = 18.sp)
                                return@Button
                            }
                            else {
                                Text("Gửi yêu cầu xác thực", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
            is CustomerState.Error -> {}
        }
    }
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Thông báo") },
            containerColor = Color.White,
            text = { Text(text = "Mã OTP đã được gửi đến email của bạn.\nVui lòng kiểm tra và nhập mã OTP") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate(Screen.VerifiedEmailScreen.route + "?id=$idCustomer&email=$email&username=$username&token=$token")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Tiếp tục")
                }
            }
        )
    }
}