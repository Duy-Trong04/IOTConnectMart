package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.customer.Email


/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: In ra màn hình chỉnh sửa email
*/

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
//@Preview(showBackground = true)
@Composable
fun EditEmailScreen(id: String,onBack: () -> Unit = {}, email: String) {
    //var email by remember { mutableStateOf("nmc@gmail.com") }
    var email1 by remember { mutableStateOf(email) }
    var id by remember { mutableStateOf(id) }
    var emailModel: CustomerViewModel= viewModel()

    var emailError by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sửa Email",
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
                actions = {
                    IconButton(onClick = {
                    /* Chức năng lưu lại thông tin đã sửa */
                        val email: Email
                        email = Email(id,email1)
                        emailModel.updateEmail(email)
                        onBack()
                    },
                        enabled = emailError.isEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save",
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
                        value = email1,
                        onValueChange = {
                            email1 = it
                            emailError = if (!it.endsWith("@gmail.com")) {
                                "Email phải có đuôi '@gmail.com'"
                            } else {
                                ""
                            }},
                        label = { Text("Email") },
                        isError = emailError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (emailError.isNotEmpty()) {
                        Text(
                            text = emailError,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    )
}
