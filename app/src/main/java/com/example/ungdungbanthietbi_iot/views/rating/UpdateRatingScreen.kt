package com.example.ungdungbanthietbi_iot.views.rating

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ungdungbanthietbi_iot.api.ReviewRequestUpdate
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

/** Hàm chuyển Bitmap thành chuỗi Base64 */
fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream) // Tăng chất lượng lên 90
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

/** Hàm chuyển URI thành byte array */
fun uriToByteArray(context: android.content.Context, uri: Uri): ByteArray? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        }
    } catch (e: Exception) {
        Log.e("UriToByteArray", "Error: ${e.message}")
        null
    }
}

/** Hàm nén ảnh */
fun compressImage(byteArray: ByteArray, quality: Int, maxSize: Int): ByteArray? {
    try {
        var bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val scale = if (bitmap.width > maxSize || bitmap.height > maxSize) {
            maxSize.toFloat() / maxOf(bitmap.width, bitmap.height)
        } else {
            1f
        }
        bitmap = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scale).toInt(),
            (bitmap.height * scale).toInt(),
            true
        )
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    } catch (e: Exception) {
        Log.e("CompressImage", "Error: ${e.message}")
        return null
    }
}

/** Giao diện màn hình chỉnh sửa đánh giá */
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRatingScreen(navController: NavController, idReview: Int, idCustomer: String?) {
    val context = LocalContext.current
    val reviewViewModel: ReviewViewModel = viewModel()
    val review = reviewViewModel.review
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64Image by remember { mutableStateOf<String?>(null) }
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }
    val error by reviewViewModel.error.collectAsState()
    val isLoading by reviewViewModel.isLoading.collectAsState()
    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    // Launcher để chọn ảnh từ thư viện
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            try {
                val byteArray = uriToByteArray(context, it)
                byteArray?.let { bytes ->
                    val compressedBytes = compressImage(bytes, 90, 1024)
                    base64Image = compressedBytes?.let { compressed ->
                        BitmapFactory.decodeByteArray(compressed, 0, compressed.size)
                    }?.let { bitmap ->
                        bitmapToBase64(bitmap)
                    }
                    Log.d("ImagePicker", "Uri: $it")
                    Log.d("ImagePicker", "ByteArray size: ${bytes.size}")
                    Log.d("ImagePicker", "Base64Image: ${base64Image?.take(100)?.plus("...") ?: "null"}")
                } ?: run {
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

    // Tải đánh giá hiện tại
    LaunchedEffect(idReview) {
        reviewViewModel.getReviewById(idReview)
    }

    // Cập nhật giao diện với dữ liệu từ đánh giá
    LaunchedEffect(review) {
        review?.let {
            rating = it.rating
            comment = it.comment ?: ""
            base64Image = it.image // Giả sử review.image là chuỗi Base64
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chỉnh sửa đánh giá") },
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
            if(review != null){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
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
                            if (!isLoading && idCustomer != null) {
                                review.let {
                                    val updatedReview = ReviewRequestUpdate(
                                        id = idReview,
                                        customer_id = idCustomer,
                                        comment = comment,
                                        image = base64Image,
                                        rating = rating
                                    )
                                    reviewViewModel.updateReview(updatedReview)
                                    showSnackbar.value = true
                                    snackbarMessage.value =
                                        "Đánh giá của bạn đã được cập nhật thành công!"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF),
                            contentColor = Color.White
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
                                text = "Cập nhật đánh giá",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (review == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5D9EFF))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(10.dp)
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
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
                        var errorMessage by remember { mutableStateOf<String?>(null) }

                        // Giải mã Base64 bên ngoài composable invocation
                        LaunchedEffect(base64Image) {
                            base64Image?.let { base64 ->
                                try {
                                    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
                                    bitmapState = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                    errorMessage = null // Xóa lỗi nếu thành công
                                } catch (e: Exception) {
                                    Log.e("ImageDisplay", "Error decoding Base64 image: ${e.message}")
                                    errorMessage = "Lỗi giải mã ảnh: ${e.message}"
                                    bitmapState = null
                                }
                            } ?: run {
                                bitmapState = null
                                errorMessage = null
                            }
                        }
                        when {
                            selectedImageUri != null -> {
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
                            bitmapState != null -> {
                                Image(
                                    bitmap = bitmapState!!.asImageBitmap(),
                                    contentDescription = "Ảnh đánh giá từ server",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            else -> {
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
                            }
                        }
                    }
                }
            }
        }
    }
}