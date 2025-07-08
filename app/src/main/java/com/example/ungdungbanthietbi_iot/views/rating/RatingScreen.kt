package com.example.ungdungbanthietbi_iot.views.rating

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.api.ReviewRequestCreate
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.views.components.compressImage
import com.example.ungdungbanthietbi_iot.views.components.uriToByteArray
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(navController: NavController, idCustomer: String, idDevice: String) {
    val context = LocalContext.current
    val reviewViewModel: ReviewViewModel = viewModel()

    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64String by remember { mutableStateOf<String?>(null) }

    val error by reviewViewModel.error.collectAsState()
    val isLoading by reviewViewModel.isLoading.collectAsState()
    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            try {
                val byteArray = uriToByteArray(context, it)
                byteArray?.let { bytes ->
                    val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val compressedBytes = compressImage(bytes, 90, 1024)
                    base64String = compressedBytes?.let { bitmapToBase64(BitmapFactory.decodeByteArray(it, 0, it.size)) }
                    Log.d("ImagePicker", "Uri: $it")
                    Log.d("ImagePicker", "ByteArray size: ${bytes.size}")
                    Log.d("ImagePicker", "Base64String: ${base64String?.take(100)?.plus("...") ?: "null"}")
                    Log.d("ImagePicker", "Base64String Length: ${base64String?.length ?: 0}")
                } ?: run {
                    Log.e("ImagePicker", "Failed to convert URI to byte array")
                    snackbarMessage.value = "Không thể tải ảnh, vui lòng thử lại."
                    showSnackbar.value = true
                }
            } catch (e: Exception) {
                Log.e("ImagePicker", "Error processing image: ${e.message}")
                snackbarMessage.value = "Lỗi khi xử lý ảnh: ${e.message}"
                showSnackbar.value = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá sản phẩm") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 10.dp).padding(bottom = 10.dp)
            ) {
                if (showSnackbar.value) {
                    LaunchedEffect(Unit) {
                        delay(3000)
                        showSnackbar.value = false
                    }
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        containerColor = Color.White,
                        contentColor = Color.Gray
                    ) {
                        Text(snackbarMessage.value)
                    }
                }
                if (error != null) {
                    Text(
                        text = "Lỗi: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (!isLoading) {
                            val createReview = ReviewRequestCreate(
                                customer_id = idCustomer,
                                product_id = idDevice,
                                comment = comment,
                                image = base64String,
                                rating = rating
                            )
                            reviewViewModel.addReview(createReview)
                            showSnackbar.value = true
                            snackbarMessage.value = "Đánh giá của bạn đã được gửi thành công!"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (isLoading) "Đang gửi..." else "Gửi đánh giá", fontSize = 20.sp)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Bạn đánh giá sản phẩm này như thế nào?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star $i",
                            tint = if (i <= rating) Color(0xFFFFD700) else Color(0xFFBDBDBD),
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { rating = i }
                                .padding(4.dp)
                        )
                    }
                }
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Viết bình luận của bạn...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Thêm ảnh đánh giá",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri == null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Thêm ảnh",
                                tint = Color.Gray,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Thêm ảnh",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Ảnh đánh giá",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                            error = painterResource(android.R.drawable.ic_menu_gallery)
                        )
                    }
                }
            }
        }
    }
}