package com.example.ungdungbanthietbi_iot.views.personal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.api.ChangePasswordRequest
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CartViewModel
import com.example.ungdungbanthietbi_iot.models.Customer
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import com.example.ungdungbanthietbi_iot.viewModels.CustomerState
import com.example.ungdungbanthietbi_iot.views.components.base64ToBitmap
import com.example.ungdungbanthietbi_iot.views.components.bitmapToBase64
import com.example.ungdungbanthietbi_iot.views.components.compressImage
import com.example.ungdungbanthietbi_iot.views.components.isValidBase64
import com.example.ungdungbanthietbi_iot.views.components.uriToByteArray
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalScreen(
    navController: NavController,
    username: String,
    id: String,
    token: String
) {
    val navdrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val cartViewModel: CartViewModel = viewModel()
    val listCart by cartViewModel.listProductCart.collectAsState()
    LaunchedEffect(username) {
        cartViewModel.getCartProducts(id)
    }

    var currentTab by remember { mutableStateOf("accountInfo") }
    val snackbarHostState = remember { SnackbarHostState() } // Khai báo SnackbarHostState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hồ sơ cá nhân", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            navdrawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "List"
                        )
                    } },
                actions = {
                    Box(
                        modifier = Modifier.size(48.dp)
                    ) {
                        IconButton(onClick = {
                            navController.navigate(
                                Screen.Cart_Screen.route +
                                        "?idCustomer=${id}&username=${username}"
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Giỏ hàng",
                                tint = Color.White
                            )
                        }
                        if (listCart.isNotEmpty()) {
                            Text(
                                text = "${listCart.size}",
                                color = Color(0xFF5D9EFF),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .offset(x = (-2).dp, y = (-2).dp)
                                    .background(color = Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .wrapContentSize(align = Alignment.Center)
                            )
                        }
                    } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            ) },
        bottomBar = {
            BottomAppBar (
                containerColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .offset(y = 16.dp) // Dịch chuyển BottomAppBar ring 16dp
            ){}
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { snackbarHostState.currentSnackbarData?.dismiss() }) {
                            Text(text = "Đóng", color = Color.Black)
                        }
                    },
                    containerColor = Color.White,
                    contentColor = Color.Black
                ) {
                    Text(data.visuals.message)
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ){
                    when (currentTab) {
                        "accountInfo" -> AccountInfoSection(id, snackbarHostState, navController, username, token)
                        "changePassword" -> ChangePasswordSection(
                            username = username,
                            token = token,
                            snackbarHostState = snackbarHostState,
                            onPasswordChanged = { currentTab = "accountInfo" }
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                    AccountOptionsSection(
                        onOptionSelected = { selectedTab -> currentTab = selectedTab },
                        currentTab = currentTab,
                        navController = navController,
                        username = username,
                        idCustomer = id,
                        token = token
                    )

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountInfoSection(
    id: String?,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    username: String,
    token: String,
    context: Context = LocalContext.current
){
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var compressedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var base64String by remember { mutableStateOf<String?>(null) }
    val maxLength = 10
    val customerViewModel: CustomerViewModel = viewModel()

    val customerState by customerViewModel.customerState.collectAsState()

    var isFocused by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) } // Trạng thái loading

    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (id.isNullOrBlank()) {
            customerViewModel.setErrorState("Lỗi: ID khách hàng không hợp lệ")
        }
        else{
            customerViewModel.getCustomerById(id)
        }
    }
    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            when (val state = customerState) {
                is CustomerState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(610.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = Color(0xFF5F9EFF)
                        )
                    }
                }

                is CustomerState.Success -> {
                    val customer = state.customer

                    Spacer(modifier = Modifier.height(8.dp))
                    //Ảnh đại diện
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        LaunchedEffect(customer) {
                            selectedImageUri = null // Reset để ưu tiên customer.image
                            if (customer.image != null && isValidBase64(customer.image)) {
                                originalBitmap = base64ToBitmap(customer.image)
                                Log.d("ImagePicker", "Bitmap from customer.image: ${originalBitmap != null}")
                            } else {
                                Log.d("ImagePicker", "Invalid or null Base64 string: ${customer.image?.take(100)}")
                            }
                        }

                        val imagePickerLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri: Uri? ->
                            uri?.let {
                                selectedImageUri = it // Thêm dòng này để cập nhật selectedImageUri
                                val byteArray = uriToByteArray(context, it)
                                byteArray?.let {
                                    originalBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    compressedBitmap = compressImage(it, 80, 200)?.let { compressedBytes ->
                                        BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.size)
                                    }
                                    base64String = originalBitmap?.let { bitmapToBase64(it) }
                                }
                                Log.d("ImagePicker", "Uri: $uri")
                                Log.d("ImagePicker", "ByteArray: ${byteArray?.size ?: "null"}")
                                base64String = originalBitmap?.let { bitmapToBase64(it) }
                                Log.d("ImagePicker", "Base64String: ${base64String?.take(100)?.plus("...") ?: "null"}")
                                Log.d("ImagePicker", "Base64String Length: ${base64String?.length ?: 0}")
                            }
                        }



                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .clickable { imagePickerLauncher.launch("image/*") }
                        ) {
                            // Hiển thị ảnh được chọn hoặc ảnh mặc định
                            //val cleanedBase64 = customer.image?.let { cleanBase64(it) }
                            when {
                                selectedImageUri != null -> {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = "Avatar",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                                        error = painterResource(android.R.drawable.ic_menu_gallery)
                                    )
                                }
                                originalBitmap != null -> {
                                    originalBitmap?.let { bitmap ->
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = "Avatar",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        //Log.d("ImagePicker", "data:image/jpeg;base64,$cleanedBase64")
                                    }
                                }
                                else -> {
                                    Image(
                                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                        contentDescription = "Product Image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Log.d("ImagePicker", "Customer Image Base64 Length: ${customer.image?.length ?: 0}")
                            Log.d("ImagePicker", "Customer Image Base64 Preview: ${customer.image?.take(100)?.plus("...") ?: "null"}")
                            LaunchedEffect(customer) {
                                if (customer.image != null) {
                                    Log.d("ImagePicker", "Is Valid Base64: ${isValidBase64(customer.image)}")
                                }
                            }
                            // Chữ "Sửa" mờ nhạt nằm bên dưới
                            Text(
                                text = "Sửa",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Xử lý ngày sinh mặc định (18 năm trước) nếu birthdate null
                    val defaultDate = remember {
                        LocalDate.now().minusYears(18)
                    }

                    val birthdate = customer.birthdate.takeIf { !it.isNullOrBlank() }
                        ?.substring(0, 10)
                        ?: defaultDate.toString()

                    // Khởi tạo các giá trị ngày sinh
                    var initialDay: String
                    var initialMonth: String
                    var initialYear: String
                    try {
                        val parts = birthdate.split("-")
                        initialYear = parts[0]
                        initialMonth = parts[1].padStart(2, '0')
                        initialDay = parts[2]
                    } catch (e: Exception) {
                        initialYear = defaultDate.year.toString()
                        initialMonth = defaultDate.monthValue.toString().padStart(2, '0')
                        initialDay = defaultDate.dayOfMonth.toString()
                    }

                    val phone = remember { mutableStateOf(customer.phone) }
                    val email = remember { mutableStateOf(customer.email) }
                    val gender = remember { mutableStateOf(customer.gender) }
                    val selectedDay = remember { mutableStateOf(initialDay) }
                    val selectedMonth = remember { mutableStateOf(initialMonth) }
                    val selectedYear = remember { mutableStateOf(initialYear) }

                    val initialPhone = remember { mutableStateOf(customer.phone) }
                    val initialEmail = remember { mutableStateOf(customer.email) }
                    val initialGender = remember { mutableStateOf(customer.gender) }
                    val initialBirthdate = remember { mutableStateOf(birthdate) }

                    val fullName = remember { mutableStateOf("${customer.surname} ${customer.lastname}".trim()) }
                    val initialFullName = remember { mutableStateOf("${customer.surname} ${customer.lastname}".trim()) }

                    fun checkIfChanged(): Boolean {
                        return fullName.value != initialFullName.value ||
                                phone.value != initialPhone.value ||
                                email.value != initialEmail.value ||
                                gender.value != initialGender.value ||
                                selectedDay.value != initialBirthdate.value.split("-")[2] ||
                                selectedMonth.value != initialBirthdate.value.split("-")[1].padStart(2, '0') ||
                                selectedYear.value != initialBirthdate.value.split("-")[0] ||
                                selectedImageUri != null // Kiểm tra nếu ảnh thay đổi
                    }

                    LaunchedEffect(fullName.value, phone.value, email.value, gender.value, selectedDay.value, selectedMonth.value, selectedYear.value) {
                        isButtonEnabled = checkIfChanged()
                    }



                    // Họ tên
                    Text("Họ và Tên: ", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = fullName.value,
                        onValueChange = {
                            fullName.value = it.trimStart()
                            isButtonEnabled = checkIfChanged()
                        },
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            if (it.isFocused) isFocused = true
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5F9EFF),
                            unfocusedBorderColor = Color(0xFF5F9EFF),
                            focusedLabelColor = Color(0xFF5F9EFF)
                        ),
                        shape = RoundedCornerShape(17.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Define the gender options as pairs of label and Boolean value
                    val genderOptions = listOf(
                        "Nam" to true,
                        "Nữ" to false
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Giới tính: ", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            genderOptions.forEach { (label, value) ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = gender.value == value,
                                        onClick = { gender.value = value },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF5F9EFF)
                                        )
                                    )
                                    Text(text = label)
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }
                    }

                    // Số điện thoại
                    Text("Số điện thoại: ", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = phone.value,
                        onValueChange = {
                            if(it.length <=maxLength){
                                phone.value = it
                                isButtonEnabled = checkIfChanged()
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            if (it.isFocused) isFocused = true
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5F9EFF),
                            unfocusedBorderColor = Color(0xFF5F9EFF),
                            focusedLabelColor = Color(0xFF5F9EFF)
                        ),
                        shape = RoundedCornerShape(17.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        // Email
                        Text("Email: ", fontWeight = FontWeight.Bold)
                        if (!customer.email_verified) {
                            Text(
                                text = "Ấn xác thực",
                                color = Color.Red,
                                modifier = Modifier
                                    .clickable {
                                        //accountViewModel.sendOtp(email.value)
                                        navController.navigate(Screen.EmailVerificationScreen.route + "?id=$id&email=${email.value}&username=$username&token=$token")
                                    }
                            )
                        }
                        else{
                            Text(text = "Đã xác thực !", color = Color(0xFF02C92C))
                        }
                    }
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                            isButtonEnabled = checkIfChanged()
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (it.isFocused) isFocused = true },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (!customer.email_verified) Color.Red else Color(0xFF5F9EFF),
                            unfocusedBorderColor = if (!customer.email_verified) Color.Red else Color(0xFF5F9EFF),
                            focusedLabelColor = Color(0xFF5F9EFF)
                        ),
                        readOnly = true,
                        shape = RoundedCornerShape(17.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Ngày sinh
                    Text("Ngày sinh: ", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DropdownMenuField(
                            label = "Ngày",
                            items = (1..31).map { it.toString().padStart(2, '0') },
                            selectedValue = selectedDay.value.padStart(2, '0'),
                            onValueChange = { selectedDay.value = it },
                            modifier = Modifier
                                .weight(1.15f)
                                .padding(end = 0.5.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        DropdownMenuField(
                            label = "Tháng",
                            items = (1..12).map { it.toString().padStart(2, '0') },
                            selectedValue = selectedMonth.value,
                            onValueChange = { selectedMonth.value = it },
                            modifier = Modifier
                                .weight(1.2f)
                                .padding(horizontal = 0.5.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        DropdownMenuField(
                            label = "Năm",
                            items = (1900..2025).map { it.toString() }.reversed(),
                            selectedValue = selectedYear.value,
                            onValueChange = { selectedYear.value = it },
                            modifier = Modifier
                                .weight(1.4f)
                                .padding(start = 0.5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                // Kiểm tra ngày sinh hợp lệ
                                if (!isValidDate(
                                        selectedDay.value,
                                        selectedMonth.value,
                                        selectedYear.value
                                    )
                                ) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Ngày sinh không hợp lệ!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                }

                                // Xử lý lưu dữ liệu
                                val regexName = "^[a-zA-Z\\p{L} ]+$"
                                val regexPhone = "^\\d{10}$".toRegex()

                                if (fullName.value.isBlank() || !fullName.value.matches(
                                        Regex(
                                            regexName
                                        )
                                    )
                                ) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Họ và tên không hợp lệ",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                } else if (!regexPhone.matches(phone.value)) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Số điện thoại phải có 10 số."
                                        )
                                    }
                                    return@Button
                                } else if (email.value.isBlank()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Email không được để trống.",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                } else if (!email.value.contains("@")) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Email phải chứa ký tự '@'.",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                } else {
                                    // Tách họ và tên từ fullName
                                    val nameParts = fullName.value.trim().split("\\s+".toRegex())
                                    val surnameValue: String
                                    val lastnameValue: String

                                    when (nameParts.size) {
                                        1 -> {
                                            surnameValue = ""
                                            lastnameValue = nameParts[0]
                                        }

                                        2 -> {
                                            surnameValue = nameParts[0]
                                            lastnameValue = nameParts[1]
                                        }

                                        else -> { // 3 chữ trở lên
                                            surnameValue = nameParts.take(2).joinToString(" ")
                                            lastnameValue = nameParts.drop(2).joinToString(" ")
                                        }
                                    }

                                    val khachHang = Customer(
                                        id = customer.id,
                                        surname = surnameValue,
                                        lastname = lastnameValue,
                                        image = base64String,
                                        email = email.value,
                                        email_verified = customer.email_verified,
                                        phone = phone.value,
                                        birthdate = "${selectedYear.value}-${selectedMonth.value}-${selectedDay.value}",
                                        gender = gender.value,
                                        created_at = "",
                                        update_at = getCurrentTimestamp(),
                                        delete_at = "",
                                        account = customer.account,
                                        fullname = customer.fullname
                                    )
                                    // Gọi API và chờ kết quả
                                    scope.launch {
                                        isUpdating = true
                                        customerViewModel.updateCustomer(khachHang)
                                        // Theo dõi trạng thái cập nhật
                                        customerViewModel.updateCustomerUiState.collect { state ->
                                            if (!state.isLoading) {
                                                isUpdating = false
                                                if (state.isSuccess) {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Cập nhật thông tin thành công",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                    selectedImageUri = null
                                                    if (id != null) {
                                                        customerViewModel.getCustomerById(id)
                                                    }
                                                }
                                                customerViewModel.resetUpdateState()
                                                return@collect // Thoát collect sau khi xử lý
                                            }
                                        }
                                    }
//                                selectedImageUri?.let {
//                                    // TODO: Gọi hàm trong ViewModel để lưu ảnh, ví dụ:
//                                    // customerViewModel.updateAvatar(uri)
//                                }
//                                selectedImageUri = null // Reset ảnh sau khi lưu
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),// Bo góc nút
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF)),
                            enabled = isButtonEnabled && !isUpdating// Chỉ bật nút khi có thay đổi
                        ) {
                            Text("LƯU THAY ĐỔI", color = Color.White, fontSize = 16.sp)
                        }
                        if (isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                                    .padding(4.dp),
                                color = Color(0xFF5F9EFF),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
                is CustomerState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (id != null) {
                                    customerViewModel.getCustomerById(id)

                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF))
                        ) {
                            Text("Thử lại", color = Color.White)
                        }
                    }
                }

            }

        }

    }
}



@Composable
fun DropdownMenuField(
    label: String,
    items: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) } // Trạng thái menu

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Text(
                    text = "▼",
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .padding(8.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF5F9EFF),
                unfocusedBorderColor = Color(0xFF5F9EFF),
                focusedLabelColor = Color(0xFF5F9EFF)
            ),
            shape = RoundedCornerShape(17.dp),
        )

        DropdownMenu(
            //containerColor = Color.White,
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .heightIn(max = 250.dp)
                .widthIn(300.dp)
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
                    onClick = {
                        onValueChange(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun isValidDate(day: String, month: String, year: String): Boolean {
    return try {
        val date = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
        // Kiểm tra thêm nếu ngày sinh không được lớn hơn ngày hiện tại
        date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())
    } catch (e: Exception) {
        false
    }
}
@Composable
fun AccountOptionsSection(
    onOptionSelected: (String) -> Unit,
    currentTab: String,
    navController: NavController,
    username:String,
    idCustomer: String,
    token: String
) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    // Lấy ViewModel
    val accountViewModel: AccountViewModel = viewModel()
    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {


        Column(modifier = Modifier.padding(8.dp)) {


            AccountOptionItem(
                iconRes = Icons.Filled.Person,
                label = "Thông tin tài khoản",
                isSelected = currentTab == "accountInfo",
                onClick = { onOptionSelected("accountInfo") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.LocationOn,
                label = "Sổ địa chỉ",
                isSelected = currentTab == "addresses",
                onClick = { navController.navigate(Screen.Address_Selection.route + "?idCustomer=${idCustomer}") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.ShoppingCart,
                label = "Theo dõi đơn hàng",
                isSelected = currentTab == "cartManagement",
                onClick = { navController.navigate(Screen.OrderListScreen.route + "?idCustomer=${idCustomer}") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.Star,
                label = "Đánh giá",
                isSelected = currentTab == "rating",
                onClick = { navController.navigate(Screen.Rating_History.route + "?idCustomer=${idCustomer}&username=$username&token=$token") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.Lock,
                label = "Đổi mật khẩu",
                isSelected = currentTab == "changePassword",
                onClick = { onOptionSelected("changePassword") }
            )
            AccountOptionLogOut(
                iconRes = Icons.AutoMirrored.Filled.ExitToApp,
                label = "Đăng xuất",
                onClick = {
                    openDialog.value = true
                }
            )
        }
    }

    if (openDialog.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { openDialog.value = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn chắc chắn muốn đăng xuất?", fontSize = 17.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                // Gọi hàm logout để xóa dữ liệu trong DataStore
                                accountViewModel.logout(context)
                                // Đóng dialog
                                openDialog.value = false
                                // Điều hướng về IntroScreen sau khi đăng xuất
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                                //Log.d("AccountOptions", "Navigated to IntroScreen after logout")
                            } catch (e: Exception) {
                                //Log.e("AccountOptions", "Error during logout: ${e.message}", e)
                                openDialog.value = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Đăng xuất", fontSize = 14.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Hủy", fontSize = 14.sp)
                }
            }
        )
    }
}

@Composable
fun AccountOptionItem(
    iconRes: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF5F9EFF) else Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = if (isSelected) Color(0xFF5F9EFF) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun AccountOptionLogOut(
    iconRes: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = label,
            tint = Color.Red
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = Color.Red,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ChangePasswordSection(
    username: String,
    token: String,
    snackbarHostState: SnackbarHostState, // Thêm tham số SnackbarHostState
    onPasswordChanged: () -> Unit // Callback để chuyển tab
) {
    val scope = rememberCoroutineScope()

    var matkhaucu by remember { mutableStateOf("") }
    var matkhaumoi by remember { mutableStateOf("") }
    var kiemtramkmoi by remember { mutableStateOf("") }

    val accountViewModel: AccountViewModel = viewModel()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordVisible1 by remember { mutableStateOf(false) }
    var isPasswordVisible2 by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Đổi mật khẩu", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = matkhaucu,
                label = { Text("Mật khẩu cũ") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5F9EFF),
                    unfocusedBorderColor = Color(0xFF5F9EFF),
                    focusedLabelColor = Color(0xFF5F9EFF)
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(17.dp),
                onValueChange = { matkhaucu = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = matkhaumoi,
                label = { Text("Mật khẩu mới") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5F9EFF),
                    unfocusedBorderColor = Color(0xFF5F9EFF),
                    focusedLabelColor = Color(0xFF5F9EFF)
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible1 = !isPasswordVisible1 }) {
                        Icon(
                            imageVector = if (isPasswordVisible1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible1) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(17.dp),
                onValueChange = {
                    matkhaumoi = it
                    //Log.d("Thành công", "Cập nhật mật khẩu mới: $it")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = kiemtramkmoi,
                label = { Text("Nhập lại mật khẩu") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5F9EFF),
                    unfocusedBorderColor = Color(0xFF5F9EFF),
                    focusedLabelColor = Color(0xFF5F9EFF)
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible2 = !isPasswordVisible2 }) {
                        Icon(
                            imageVector = if (isPasswordVisible2) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible2) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(17.dp),
                onValueChange = {
                    kiemtramkmoi = it
                    //Log.d("Thành công", "Cập nhật mật khẩu mới: $it")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            //Log.d("Thành công", "trướt BUTTON${password} va ${username} va ${kiemtramkmoi}")
            Button(
                onClick = {
                        if(matkhaucu == "password"){
                            if(matkhaumoi.isEmpty() || kiemtramkmoi.isEmpty()){
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Vui lòng nhập đày đủ thông tin!"
                                    )
                                }
                            }
                            else if (matkhaumoi.contains(" ")){
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Mật khẩu mới không được chứa khoảng trắng!"
                                    )
                                }
                            }
                            else if(matkhaumoi == matkhaucu){
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Mật khẩu mới không được trùng với mật khẩu cũ!"
                                    )
                                }
                            }
                            else if(matkhaumoi != kiemtramkmoi){
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Xác nhận mật khẩu không khớp!"
                                    )
                                }
                            }
                            else{
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Đổi mật khẩu thành công!"
                                    )
                                }
                                val request = ChangePasswordRequest(
                                    username = username,
                                    password = matkhaucu,
                                    newPassword = matkhaumoi,
                                    confirmPassword = kiemtramkmoi
                                )
                                accountViewModel.changePassword(token, request)
                                onPasswordChanged()
                            }
                        }
                        else{
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Mật khẩu cũ không chính xác!"
                                )
                            }
                        }
                },

                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF)),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ĐỔI MẬT KHẨU", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}