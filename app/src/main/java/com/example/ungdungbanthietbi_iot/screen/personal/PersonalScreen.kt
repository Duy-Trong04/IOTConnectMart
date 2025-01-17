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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import com.example.ungdungbanthietbi_iot.screen.home.CardAllDevice
import com.example.ungdungbanthietbi_iot.screen.home.CardFavorites

/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Hồ sơ của người dùng
*/
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
//@Preview(showBackground = true)
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
    val customerViewModel:CustomerViewModel = viewModel()

    // Lấy thông tin tài khoản từ ViewModel
    val account = accountViewModel.account
    val customer = customerViewModel.customer

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
    if (account != null) {
        customerViewModel.getCustomerById(account.idPerson.toString())
    }

    // Biến trạng thái để sản phẩm yêu thích không
    var isFavorite by remember { mutableStateOf(false) }
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
                                navController.navigate(Screen.SettingScreen.route + "/${account.idPerson}/${account.password}")
                            }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Setting")
                        }
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
            },

            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (customer != null) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clickable {
                                        //Chuyển màn hình chỉnh sửa hồ sơ
                                        navController.navigate(Screen.EditProfileScreen.route + "?username=${accountViewModel.username}")
                                    },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.avatar),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${customer.surname} ${customer.lastName}",
                                            modifier = Modifier.padding(start = 16.dp),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = customer.email,
                                            modifier = Modifier.padding(start = 16.dp),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W300
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = customer.phone,
                                            modifier = Modifier.padding(start = 16.dp),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W300
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Đơn mua", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            TextButton(
                                onClick = {
                                    // Chuyển màn hình Lịch sử mua hàng
                                    navController.navigate(Screen.OrderListScreen.route + "?idCustomer=${account.idPerson}")
                                }
                            ) {
                                Text(text = "Lịch sử mua hàng",
                                    fontSize = 20.sp,
                                    color = Color(0xFF5D9EFF)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OrderStatusItem(
                                    icon = Icons.Filled.VerifiedUser,
                                    description = "Chờ xác nhận",
                                    //Chuyen den tab chờ xác nhận
                                    onclick = {
                                        navController.navigate(Screen.OrderListScreen.route + "?idCustomer=${account.idPerson}")
                                    }
                                )
                                OrderStatusItem(
                                    icon = Icons.Filled.LocalShipping,
                                    description = "Chờ lấy hàng",
                                    //Chuyển màn hình tab Chờ giao hàng
                                    onclick = {
                                        navController.navigate(Screen.OrderListScreen.route + "?idCustomer=${account.idPerson}")
                                    }
                                )
                                OrderStatusItem(
                                    icon = Icons.Filled.LocalShipping,
                                    description = "Chờ giao hàng",
                                    //Chuyển màn hình tab Chờ lấy hàng
                                    onclick = {
                                        navController.navigate(Screen.OrderListScreen.route + "?idCustomer=${account.idPerson}")
                                    }
                                )
                                OrderStatusItem(
                                    icon = Icons.Filled.Star,
                                    description = "Đánh Giá",
                                    //Chuyển màn hình đánh giá sản phẩm
                                    onclick = {
                                        /*Thực hiện chức năng chuyển màn hình đánh giá*/
                                        //navController.navigate(Screen.Rating_History.route)
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sản phẩm yêu thích",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        if(listDeviceLiked.isEmpty()){
                            Text(
                                text = "Không có sản phẩm nào trong danh sách yêu thích",
                                color = Color.Gray,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                        else{
                            LazyRow(modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                items(listDeviceLiked){
                                    if (account != null){
                                        CardFavorites(
                                            device = it,
                                            isFavorite,
                                            account.idPerson,
                                            account.username,
                                            navController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun OrderStatusItem(icon: ImageVector, description: String,onclick:()->Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onclick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                icon,
                contentDescription = description,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            modifier = Modifier.size(60.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

