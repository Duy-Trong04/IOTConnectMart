package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.data.account.Account
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.customer.Customer
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import com.example.ungdungbanthietbi_iot.screen.home.CardAllDevice
import com.example.ungdungbanthietbi_iot.screen.home.CardFavorites


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PersonalScreen(
    navController: NavController,
    username:String,
    deviceViewModel: DeviceViewModel,
) {

    deviceViewModel.getAllDevice()
    val listAllDevice : List<Device> = deviceViewModel.listAllDevice
    val listDeviceLiked: List<Device> = deviceViewModel.listDeviceOfCustomer

    val navdrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() //xử lý suspending fun (mở và đóng drawer)
    val countries = listOf(
        "Thiết bị chiếu sáng",
        "Thiết bị cảm biến",
        "Thiết bị điện tử thông minh",
        "Đồng hồ thông minh",
    )

    // Lấy ViewModel
    val accountViewModel:AccountViewModel = viewModel()

    // Lấy thông tin tài khoản từ ViewModel
    val account = accountViewModel.account

    // Gọi API nếu taikhoan chưa được lấy
    LaunchedEffect(username) {
        if (username.isNotEmpty()) {
            accountViewModel.getUserByUsername(username)
        }
    }
    if(account != null){
        deviceViewModel.getDeviceByLiked(account.idPerson.toString())
    }
    if(account == null){
        Text(text = "Đang tải thông tin tài khoản...")
        return
    }

    // Biến trạng thái để sản phẩm yêu thích không
    var isFavorite by remember { mutableStateOf(false) }

    // Kiểm tra trạng thái Tab
    var currentTab by remember { mutableStateOf("accountInfo") }
    // Danh mục
    ModalNavigationDrawer(
        drawerState = navdrawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5D9EFF))
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "IOT Connect Mart",
                        modifier = Modifier.padding(5.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng danh mục",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(24.dp)
                            .clickable {
                                // thoát danh mục
                                scope.launch {
                                    navdrawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                        tint = Color.White
                    )
                }
                HorizontalDivider()
                Text(
                    text = "T R A N G  C H Ủ",
                    modifier = Modifier.padding(12.dp).clickable {
                        navController.navigate(Screen.HomeScreen.route + "?username=${username}")
                    },
                    color = Color(0xFF5D9EFF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                countries.forEach { country ->
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 5.dp,
                                        horizontal = 5.dp
                                    )
                                    .drawBehind {
                                        drawLine(
                                            color = Color.Black,
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                            ) {
                                Text(text = country)
                            }
                        }, selected = false, onClick = { /* Chọn danh mục */ })
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Hồ sơ cá nhân", fontWeight = FontWeight.Bold)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                // mở và đóng drawer
                                navdrawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu,
                                contentDescription = "List"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.Cart_Screen.route +"?idCustomer=${account.idPerson}&username=${account.username}")
                            }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Gio hang")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF5F9EFF),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                    //.border(1.dp, Color.Black)
                ){
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box (
                                modifier = Modifier.size(50.dp)
                                    .clip(CircleShape)
                                    .clickable { navController.navigate(Screen.HomeScreen.route + "?username=${username}") },
                                contentAlignment = Alignment.Center,

                                ){
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Trang chủ",
                                    //modifier = Modifier.weight(1f),
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Black
                                )
                            }
                            Text(
                                text = "Trang chủ",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box (
                                modifier = Modifier.size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        scope.launch {
                                            // mở và đóng drawer
                                            navdrawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ){
                                Icon(
                                    imageVector = Icons.Default.Category,
                                    contentDescription = "danh mục",
                                    //modifier = Modifier.weight(1f),
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Black
                                )
                            }
                            Text(
                                text = "Danh mục",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box (
                                modifier = Modifier.size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        navController.navigate(Screen.Search_Screen.route)
                                    },
                                contentAlignment = Alignment.Center,
                            ){
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Tìm kiếm",
                                    //modifier = Modifier.weight(1f),
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Black
                                )
                            }
                            Text(
                                text = "Tìm kiếm",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box (
                                modifier = Modifier.size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        //navController.popBackStack()
                                        navController.navigate(Screen.PersonalScreen.route+ "?username=${accountViewModel.username}")
                                    }
                                    .background(Color(0xFF5D9EFF), RoundedCornerShape(5.dp)),
                                contentAlignment = Alignment.Center,
                            ){
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Tài khoản",
                                    //modifier = Modifier.weight(1f),
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.White
                                )
                            }
                            Text(
                                text = "Tôi",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

        )
        {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {

                        when (currentTab) {

                            "accountInfo" -> AccountInfoSection(username)
                            "cartManagement" -> LaunchedEffect(currentTab) {
                                navController.navigate(Screen.OrderListScreen.route + "?idCustomer=${account.idPerson}")
                            }
                            "changePassword" -> ChangePasswordSection(username)
                            "addresses" -> LaunchedEffect(currentTab) {
                                navController.navigate("${Screen.Address_Selection.route}?idCustomer=${account.idPerson}")
                            }
                            "rating" -> LaunchedEffect(currentTab) {
                                navController.navigate("${Screen.Rating_History.route}?idCustomer=${account.idPerson}")
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Phần menu danh sách bên dưới
                item {
                    AccountOptionsSection(
                        onOptionSelected = { selectedTab ->
                            currentTab = selectedTab
                        },
                        currentTab = currentTab,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun AccountInfoSection(
    username: String
){
    val maxLength = 10

    var accountViewModel: AccountViewModel = viewModel()
    var customerViewModel: CustomerViewModel = viewModel()

    val account = accountViewModel.account
    val customer = customerViewModel.customer

    var isFocused by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    var snackbarHostState = remember {
        SnackbarHostState()
    }

    var scope = rememberCoroutineScope()
    LaunchedEffect(username) {
        if (username.isNotEmpty()) {
            accountViewModel.getUserByUsername(username)
        }
    }

    if (account != null) {
        customerViewModel.getCustomerById(account.idPerson.toString())
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Thông tin tài khoản", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(8.dp))
            if (customer != null) {
                val surname = remember { mutableStateOf(customer.surname) }
                val lastname = remember { mutableStateOf(customer.lastName) }
                val phone = remember { mutableStateOf(customer.phone) }
                val email = remember { mutableStateOf(customer.email) }
                val gender = remember { mutableStateOf(customer.gender) }
                val selectedDay = remember { mutableStateOf(customer.birthdate.split("-")[2]) }
                val selectedMonth = remember { mutableStateOf(customer.birthdate.split("-")[1]) }
                val selectedYear = remember { mutableStateOf(customer.birthdate.split("-")[0]) }

                val initialSurname = remember { mutableStateOf(customer.surname) }
                val initialLastName = remember { mutableStateOf(customer.lastName) }
                val initialPhone = remember { mutableStateOf(customer.phone) }
                val initialEmail = remember { mutableStateOf(customer.email) }
                val initialGender = remember { mutableStateOf(customer.gender) }
                val initialBirthdate = remember { mutableStateOf(customer.birthdate) }

                fun checkIfChanged(): Boolean {
                    return lastname.value != initialLastName.value ||
                            phone.value != initialPhone.value ||
                            email.value != initialEmail.value ||
                            gender.value != initialGender.value ||
                            selectedDay.value != initialBirthdate.value.split("-")[2] ||
                            selectedMonth.value != initialBirthdate.value.split("-")[1] ||
                            selectedYear.value != initialBirthdate.value.split("-")[0]
                }

                LaunchedEffect(lastname.value, phone.value, email.value, gender.value, selectedDay.value, selectedMonth.value, selectedYear.value) {
                    isButtonEnabled = checkIfChanged()
                }

                val fullName = remember { mutableStateOf("${customer.surname} ${customer.lastName}".trim()) }

                // Họ tên
                Text("Họ Tên: ", fontWeight = FontWeight.Bold)
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
                val genderList = listOf("Nam", "Nữ")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Giới tính: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        genderList.forEachIndexed { index, label ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = gender.value == index,
                                    onClick = { gender.value = index },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF5F9EFF)
                                    )
                                )
                                Text(text = label)
                                Spacer(modifier = Modifier.width(8.dp)) // Thêm khoảng cách giữa 2 cái
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

                // Email
                Text("Email: ", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        isButtonEnabled = checkIfChanged()},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
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

                // Ngày sinh
                Text("Ngày sinh: ", fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DropdownMenuField(
                        label = "Ngày",
                        items = (1..31).map { it.toString() },
                        selectedValue = selectedDay.value,
                        onValueChange = { selectedDay.value = it },
                        modifier = Modifier
                            .weight(1.15f)
                            .padding(end = 0.5.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    DropdownMenuField(
                        label = "Tháng",
                        items = (1..12).map { it.toString() },
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
                SnackbarHost(
                    modifier = Modifier.padding(4.dp),
                    hostState = snackbarHostState,
                )

                Button(
                    onClick = {
                        // Xử lý lưu dữ liệu
                        val regexName = "^[a-zA-Z\\p{L} ]+$"
                        val regexPhone = "^\\d{10}$".toRegex()

                        if (lastname.value.isBlank() || !lastname.value.matches(Regex(regexName))) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Họ và tên không hợp lệ",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else if (!regexPhone.matches(phone.value)) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Số điện thoại phải có 10 số."
                                )
                            }
                        } else if (email.value.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Email không được để trống.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else if (!email.value.contains("@")) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Email phải chứa ký tự '@'.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            // Tách họ và tên từ fullName
                            val nameParts = fullName.value.trim().split("\\s+".toRegex())
                            val surnameValue: String
                            val lastNameValue: String

                            if (nameParts.size >= 2) {
                                surnameValue = nameParts[0]                             // Lấy chữ đầu làm Họ
                                lastNameValue = nameParts.drop(1).joinToString(" ")     // Phần còn lại làm Tên
                            } else {
                                surnameValue = ""
                                lastNameValue = nameParts[0]
                            }

                            val khachHang = Customer(
                                id = customer.id,
                                surname = surnameValue,
                                lastName = lastNameValue,
                                email = email.value,
                                phone = phone.value,
                                birthdate = "${selectedYear.value}-${selectedMonth.value}-${selectedDay.value}",
                                gender = gender.value,
                                created_at = "",
                                update_at = "",
                                status = "1"
                            )
                            customerViewModel.updateCustomer(khachHang)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Cập nhật thành công",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),// Bo góc nút
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF))
                ) {
                    Text("LƯU THAY ĐỔI", color = Color.White, fontSize = 16.sp)
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

@Composable
fun AccountOptionsSection(
    onOptionSelected: (String) -> Unit,
    currentTab: String,
    navController: NavController,
) {
    val openDialog = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
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
                label = "Số địa chỉ",
                isSelected = currentTab == "addresses",
                onClick = { onOptionSelected("addresses") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.ShoppingCart,
                label = "Quản lý đơn hàng",
                isSelected = currentTab == "cartManagement",
                onClick = { onOptionSelected("cartManagement") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.Star,
                label = "Đánh giá",
                isSelected = currentTab == "rating",
                onClick = { onOptionSelected("rating") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.Lock,
                label = "Đổi mật khẩu",
                isSelected = currentTab == "changePassword",
                onClick = { onOptionSelected("changePassword") }
            )
            AccountOptionItem(
                iconRes = Icons.Filled.ExitToApp,
                label = "Đăng xuất",
                isSelected = false, // Không cần trạng thái cho mục đăng xuất
                onClick = {
                    openDialog.value = true
                }
            )
        }
    }

    if (openDialog.value == true) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { openDialog.value = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn chắc chắn muốn đăng xuất?", fontSize = 17.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        navController.navigate(Screen.HomeScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5F9EFF),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK", fontSize = 14.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5F9EFF),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", fontSize = 14.sp)
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
fun ChangePasswordSection(
    username: String
) {
    var snackbarHostState = remember {
        SnackbarHostState()
    }
    var scope = rememberCoroutineScope()

    var matkhaucu by remember { mutableStateOf("") }
    var matkhaumoi by remember { mutableStateOf("") }
    var kiemtramkmoi by remember { mutableStateOf("") }

    var accountViewModel: AccountViewModel = viewModel()

    var account = accountViewModel.account

    accountViewModel.getUserByUsername(username)
    var password by remember { mutableStateOf(account?.password) }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordVisible1 by remember { mutableStateOf(false) }
    var isPasswordVisible2 by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
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
                onValueChange = { matkhaumoi = it }
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
                onValueChange = { kiemtramkmoi = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SnackbarHost(
                modifier = Modifier.padding(4.dp),
                hostState = snackbarHostState
            )
            Button(
                onClick = {
                    if (account != null) {
                        if(matkhaucu == password){
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
                                val taiKhoan = Account(
                                    idPerson = account.idPerson,
                                    idRole = "CUS",
                                    username = username,
                                    password = matkhaumoi,
                                    report = 0,
                                    isNew = 1,
                                    status = 1
                                )
                                accountViewModel.updateAccount(taiKhoan)
                            }
                        }
                        else{
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Mật khẩu cũ không chính xác!"
                                )
                            }
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