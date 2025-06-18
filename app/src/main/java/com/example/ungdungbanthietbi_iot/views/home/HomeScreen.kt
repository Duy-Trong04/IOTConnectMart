package com.example.ungdungbanthietbi_iot.views.home

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ungdungbanthietbi_iot.api.AddLikedRequest
import com.example.ungdungbanthietbi_iot.api.LikedProduct
import com.example.ungdungbanthietbi_iot.models.Category
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.LikedViewModel
import com.example.ungdungbanthietbi_iot.models.SlideShow
import com.example.ungdungbanthietbi_iot.viewModels.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ungdungbanthietbi_iot.viewModels.CartViewModel
import com.example.ungdungbanthietbi_iot.views.notification.NotificationScreen
import com.example.ungdungbanthietbi_iot.views.personal.PersonalScreen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import com.example.ungdungbanthietbi_iot.utils.formatGiaTienInt
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CategoryViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerState
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.views.components.ParentCategoryItem

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    deviceViewModel: DeviceViewModel,
    slideShowViewModel: SlideShowViewModel,
    username: String?,
    id: String?,
    password: String?
) {
    val context = LocalContext.current
    val categoryViewModel:CategoryViewModel = viewModel()
    val accountViewModel: AccountViewModel = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()
    val listAllDevice: List<Device> = deviceViewModel.listAllDevice
    val listDeviceFeatured: List<Device> = deviceViewModel.listDeviceFeatured
    val listCategories by categoryViewModel.listCategories.collectAsState()
    val isLoadingCategories by categoryViewModel.isLoading.collectAsState()
    val errorMessage by categoryViewModel.errorMessage.collectAsState()
    val listSlideShow by slideShowViewModel.listSlideShows.collectAsState()

    val likedViewModel: LikedViewModel = viewModel()
    val listLiked by likedViewModel.listLiked.collectAsState()
    LaunchedEffect(id) {
        slideShowViewModel.getAllSlideShow()
        deviceViewModel.getAllDevice()
        categoryViewModel.getCategories()
        if(id != null){
            likedViewModel.getLikedByIdCustomer(id)
            customerViewModel.getCustomerById(id)
        }
    }

    val cartViewModel: CartViewModel = viewModel()
    val listCart by cartViewModel.listProductCart.collectAsState()
    if(id != null){
        LaunchedEffect (listCart.size) {
            cartViewModel.getCartProducts(id)
        }
    }
    val navdrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isScrolling by remember { mutableStateOf(false) }
    val isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(listState.isScrollInProgress) {
        isScrolling = listState.isScrollInProgress
    }

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val openDialog = remember { mutableStateOf(false) }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    ModalNavigationDrawer(
        drawerState = navdrawerState,
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White,
                drawerTonalElevation = DrawerDefaults.ModalDrawerElevation
            ){
                // Header hiển thị thông tin khách hàng
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5D9EFF))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val customerState by customerViewModel.customerState.collectAsState()
                    when (val state = customerState) {
                        is CustomerState.Loading -> {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                        is CustomerState.Success -> {
                            val customer = state.customer
                            // Tải hình ảnh bất đồng bộ
                            LaunchedEffect(customer) {
                                bitmap = customer.image?.let {
                                    deviceViewModel.getDeviceImageBitmapImage(
                                        it
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                bitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = customer.fullname.ifEmpty { "Hình ảnh sản phẩm" },
                                        modifier = Modifier.size(80.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } ?: run {
                                    Image(
                                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                        contentDescription = "Product Image",
                                        modifier = Modifier.size(80.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
                                ){
                                    Text(
                                        text = customer.fullname,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "SĐT: ${customer.phone}",
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    customer.email.let {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Email: $it",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                        is CustomerState.Error -> {
                            Text(
                                text = "Lỗi: ${state.message}",
                                color = Color(0xFFFF4444),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        navdrawerState.close()
                                        navController.navigate(Screen.LoginScreen.route)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF5D9EFF)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Đăng nhập lại")
                            }
                        }
                    }
                    if (customerState !is CustomerState.Success && id.isNullOrBlank()) {
                        Text(
                            text = "Vui lòng đăng nhập để xem thông tin",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    navdrawerState.close()
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF5D9EFF)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Đăng nhập ngay")
                        }
                    }
                }

                // Xử lý trạng thái tải và lỗi
                when {
                    isLoadingCategories -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF5D9EFF),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Lỗi không xác định",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    listCategories.isEmpty() -> {
                        Text(
                            text = "Không có danh mục nào",
                            color = Color(0xFF616161),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            item {
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Home,
                                            contentDescription = "Trang chủ",
                                            tint = Color(0xFF5D9EFF)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Trang chủ",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            navdrawerState.close()
                                            selectedTabIndex = 0
                                        }
                                    },
                                    modifier = Modifier.padding( vertical = 4.dp)
                                )
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = "Yêu thích",
                                            tint = Color(0xFF5D9EFF)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Yêu thích",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            navdrawerState.close()
                                            if (username != null) {
                                                navController.navigate(
                                                    Screen.Favorites_Screen.route +
                                                            "?idCustomer=${id}&username=${username}&password=$password"
                                                )
                                            }
                                            else {
                                                navController.navigate(Screen.LoginScreen.route)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Outlined.ShoppingCart,
                                            contentDescription = "Giỏ hàng",
                                            tint = Color(0xFF5D9EFF)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Giỏ hàng",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            navdrawerState.close()
                                            if (username != null) {
                                                navController.navigate(
                                                    Screen.Cart_Screen.route +
                                                            "?idCustomer=${id}&username=${username}&password=$password"
                                                )
                                            }
                                            else {
                                                navController.navigate(Screen.LoginScreen.route)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Notifications,
                                            contentDescription = "Thông báo",
                                            tint = Color(0xFF5D9EFF)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Thông báo",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            navdrawerState.close()
                                            selectedTabIndex = 2
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Outlined.PersonOutline,
                                            contentDescription = "Thông tin cá nhân",
                                            tint = Color(0xFF5D9EFF)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Thông tin cá nhân",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            navdrawerState.close()
                                            selectedTabIndex = 3
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Info,
                                            contentDescription = "Giới thiệu",
                                            tint = Color(0xFF5D9EFF)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Giới thiệu",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            //Chuyển sang màn hình giới thiệu
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                if(id != null && username != null) {
                                    NavigationDrawerItem(
                                        icon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.Logout,
                                                contentDescription = "Đăng xuất",
                                                tint = Color.Red
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = "Đăng xuất",
                                                color = Color.Red,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        },
                                        selected = false,
                                        onClick = {
                                            scope.launch {
                                                openDialog.value = true
                                            }
                                        },
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                            item{
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF5D9EFF)),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Danh mục sản phẩm",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth().padding(start = 10.dp)
                                    )
                                }
                            }
                            items(listCategories.filter { !it.is_hide }) { category ->
                                ParentCategoryItem(
                                    category = category,
                                    navController = navController,
                                    username = username,
                                    idCustomer = id,
                                    password = password,
                                    depth = 0,
                                    navdrawerState = navdrawerState,
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = { selectedCategory = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (selectedTabIndex == 3) {
                            Text(
                                text = "Hồ sơ cá nhân",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "SNS Store",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF5D9EFF),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        if (username != null) {
                            IconButton(onClick = {
                                selectedTabIndex = 3
                            }) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = username.first().toString().uppercase(),
                                        color = Color(0xFF5D9EFF),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screen.Search_Screen.route + "?username=${username}&idCustomer=$id&password=$password")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(end = 4.dp)
                        ) {
                            IconButton(onClick = {
                                if (username == null) {
                                    navController.navigate(Screen.LoginScreen.route)
                                } else {
                                    navController.navigate(
                                        Screen.Cart_Screen.route +
                                                "?idCustomer=${id}&username=${username}&password=$password"
                                    )
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart,
                                    contentDescription = "Cart",
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
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFF5D9EFF),
                            shape = RoundedCornerShape(0.dp) // Hình chữ nhật, không bo góc
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selectedTabIndex == 0) Icons.Default.Home else Icons.Outlined.Home,
                                    contentDescription = "Trang chủ",
                                    modifier = Modifier.size(28.dp),
                                    tint = if (selectedTabIndex == 0) Color(0xFF1E88E5) else Color(0xFF616161)
                                )
                            },
                            label = {
                                Text(
                                    text = "Trang chủ",
                                    fontSize = 12.sp,
                                    color = if (selectedTabIndex == 0) Color(0xFF1E88E5) else Color(0xFF616161),
                                    fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 }
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.GridView,
                                    contentDescription = "Danh mục",
                                    modifier = Modifier.size(28.dp),
                                    tint = if (selectedTabIndex == 1) Color(0xFF1E88E5) else Color(0xFF616161)
                                )
                            },
                            label = {
                                Text(
                                    text = "Danh mục",
                                    fontSize = 12.sp,
                                    color = if (selectedTabIndex == 1) Color(0xFF1E88E5) else Color(0xFF616161),
                                    fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = selectedTabIndex == 1,
                            onClick = {
                                scope.launch {
                                    navdrawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selectedTabIndex == 2) Icons.Default.Notifications else Icons.Outlined.Notifications,
                                    contentDescription = "Thông báo",
                                    modifier = Modifier.size(28.dp),
                                    tint = if (selectedTabIndex == 2) Color(0xFF1E88E5) else Color(0xFF616161)
                                )
                            },
                            label = {
                                Text(
                                    text = "Thông báo",
                                    fontSize = 12.sp,
                                    color = if (selectedTabIndex == 2) Color(0xFF1E88E5) else Color(0xFF616161),
                                    fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 }
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selectedTabIndex == 3) Icons.Default.Person else Icons.Outlined.Person,
                                    contentDescription = "Tôi",
                                    modifier = Modifier.size(28.dp),
                                    tint = if (selectedTabIndex == 3) Color(0xFF1E88E5) else Color(0xFF616161)
                                )
                            },
                            label = {
                                Text(
                                    text = "Tôi",
                                    fontSize = 12.sp,
                                    color = if (selectedTabIndex == 3) Color(0xFF1E88E5) else Color(0xFF616161),
                                    fontWeight = if (selectedTabIndex == 3) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = selectedTabIndex == 3,
                            onClick = {
                                if (id != null) selectedTabIndex = 3
                                else {
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                            }
                        )
                    }
                }
//                val buttons = listOf(
//                    ButtonData("Trang chủ", if (selectedTabIndex == 0) Icons.Default.Home else Icons.Outlined.Home) {
//                        selectedTabIndex = 0
//                    },
//                    ButtonData("Danh mục", Icons.Outlined.GridView) {
//                        scope.launch {
//                            navdrawerState.apply {
//                                if (isClosed) open() else close()
//                            }
//                        }
//                    },
//                    ButtonData("Thông báo", if (selectedTabIndex == 2) Icons.Default.Notifications else Icons.Outlined.Notifications) {
//                        selectedTabIndex = 2
//                    },
//                    ButtonData("Tôi", if (selectedTabIndex == 3) Icons.Default.Person else Icons.Outlined.Person) {
//                        if (id != null) selectedTabIndex = 3
//                        else navController.navigate(Screen.LoginScreen.route)
//                    }
//                )
//                AnimatedNavigationBar(
//                    buttons = buttons,
//                    barColor = Color.White,
//                    circleColor = Color(0xFF5D9EFF),
//                    selectedColor = Color(0xFF1E88E5),
//                    unselectedColor = Color(0xFF616161)
//                )
            },
            floatingActionButton = {
                if (selectedTabIndex == 0) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = if (isScrolling) 0.dp else 6.dp
                        ),
                        containerColor = Color(0xFF5D9EFF).copy(alpha = if (isScrolling) 0.2f else 0.8f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Nút lên",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { padding ->
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
            when (selectedTabIndex) {
                0 -> HomeContent(
                    padding = padding,
                    listState = listState,
                    listSlideShow = listSlideShow,
                    deviceViewModel = deviceViewModel,
                    navController = navController,
                    username = username,
                    id = id,
                    password = password,
                    isFavorite = isFavorite,
                    listAllDevice = listAllDevice,
                    listDeviceFeatured = listDeviceFeatured,
                    listDeviceLiked = listLiked,
                    categories = listCategories
                )
                2 -> if (username != null) NotificationScreen(navController = navController, idUser = id)
                else NotificationScreen(navController = navController, idUser = "")
                3 -> if(username != null && id != null)
                    PersonalScreen(
                        navController = navController,
                        username = username,
                        id = id,
                        password = password
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    padding: PaddingValues,
    listState: LazyListState,
    listSlideShow: List<SlideShow>,
    deviceViewModel: DeviceViewModel,
    navController: NavController,
    username: String?,
    id: String?,
    password: String?,
    isFavorite: Boolean,
    listAllDevice: List<Device>,
    listDeviceFeatured: List<Device>,
    listDeviceLiked: List<LikedProduct>,
    categories: List<Category>
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    val likedViewModel: LikedViewModel = viewModel()
    // Hàm xử lý refresh
    fun onRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            // Load lại dữ liệu
            deviceViewModel.getAllDevice()
//            deviceViewModel.getDeviceFeatured()
            if (username != null) {
                if (id != null) {
                    likedViewModel.getLikedByIdCustomer(id)
                }
            }
            // Giả lập thời gian load (có thể bỏ nếu API nhanh)
            delay(1000)
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            state = listState
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF5D9EFF), // Màu xanh của TopAppBar
                                    Color(0xFF5D9EFF), // Giữ màu xanh đậm lâu hơn
                                    Color(0xFF5D9EFF).copy(alpha = 0.5f), // Nhạt dần
                                    Color(0xFF5D9EFF).copy(alpha = 0.3f),
                                    Color(0xFF5D9EFF).copy(alpha = 0.1f)// Nhạt hơn nữa
                                    //Color.Transparent // Trong suốt ở dưới
                                ),
                                startY = 0f,
                                endY = 350f // Kéo dài qua slideshow
                            ),
                            shape = RoundedCornerShape(
                                bottomStart = 25.dp,
                                bottomEnd = 25.dp
                            )
                        )
                        .padding(bottom = 8.dp) // Đảm bảo không ảnh hưởng đến nội dung bên dưới
                ) {
                    if (listSlideShow.isNotEmpty()) {
                        val realPageCount = listSlideShow.size
                        val fakePageCount = Int.MAX_VALUE
                        val initialPage = fakePageCount / 2 - (fakePageCount / 2 % realPageCount)
                        val pagerState = rememberPagerState(
                            initialPage = initialPage,
                            pageCount = { fakePageCount }
                        )
                        val coroutineScope = rememberCoroutineScope()

                        // Tự động chuyển slide
                        DisposableEffect(Unit) {
                            val job = coroutineScope.launch {
                                while (true) {
                                    delay(3000)
                                    val nextPage = pagerState.currentPage + 1
                                    pagerState.animateScrollToPage(nextPage)
                                }
                            }
                            onDispose {
                                job.cancel() // Hủy job khi composable bị hủy
                            }
                        }

                        Box {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(4.dp, RoundedCornerShape(16.dp))
                            ) { page ->
                                val realIndex = page % realPageCount
                                SlideImage(base64String = listSlideShow[realIndex].image)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(vertical = 15.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                listSlideShow.forEachIndexed { index, _ ->
                                    val isActive = index == (pagerState.currentPage % realPageCount)
                                    val animatedWidth by animateFloatAsState(
                                        targetValue = if (isActive) 32f else 12f,
                                        animationSpec = tween(300), label = ""
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 6.dp)
                                            .size(width = animatedWidth.dp, height = 6.dp)
                                            .background(
                                                color = if (isActive) Color(0xFF1E88E5) else Color(
                                                    0xFFB0BEC5
                                                ),
                                                shape = RoundedCornerShape(3.dp)
                                            )
                                            .clickable {
                                                coroutineScope.launch {
                                                    val targetPage =
                                                        (pagerState.currentPage / realPageCount) * realPageCount + index
                                                    pagerState.animateScrollToPage(targetPage)
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF5D9EFF),
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
            item {
                SectionTitle("Danh mục sản phẩm")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            navController = navController,
                            username = username,
                            idCustomer = id
                        )
                    }
                }
            }
//            item {
//                SectionTitle("Sản phẩm nổi bật")
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 12.dp),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(listDeviceFeatured) { device ->
//                        CardDevice(
//                            device = device,
//                            isFavorite = isFavorite,
//                            idCustomer = id,
//                            username = username,
//                            password = password,
//                            deviceViewModel = deviceViewModel,
//                            navController = navController
//                        )
//                    }
//                }
//            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle("Sản phẩm yêu thích")
                    Text(
                        text = "Xem tất cả",
                        fontSize = 14.sp,
                        color = Color(0xFF1E88E5),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable {
                                if (username == null) {
                                    navController.navigate(Screen.LoginScreen.route)
                                } else {
                                    navController.navigate(
                                        Screen.Favorites_Screen.route +
                                                "?idCustomer=${id}&username=${username}&password=$password"
                                    )
                                }
                            }
                            .padding(end = 20.dp)
                    )
                }
                if (listDeviceLiked.isEmpty()) {
                    Text(
                        text = "Chưa có sản phẩm yêu thích",
                        color = Color(0xFF616161),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listDeviceLiked) { device ->
                            if (username != null) {
                                CardFavorites(
                                    device = device,
                                    isFavorite = isFavorite,
                                    idCustomer = id,
                                    username = username,
                                    password = password,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
            item {
                SectionTitle("Tất cả sản phẩm")
            }
            val pairedDevices = listAllDevice.chunked(2)
            items(pairedDevices) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { device ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            CardDevice(
                                device = device,
                                isFavorite = isFavorite,
                                idCustomer = id,
                                username = username,
                                password = password,
                                deviceViewModel = deviceViewModel,
                                navController = navController
                            )
                        }
                        if (pair.size == 1) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 12.dp),
        color = Color(0xFF5D9EFF),
        fontSize = 18.sp
    )
}

@Composable
fun CardFavorites(device: LikedProduct, isFavorite: Boolean, idCustomer: String?, username: String?, password: String?, navController: NavController) {
    var check by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked by likedViewModel.listLiked.collectAsState()
    var isLoading by remember { mutableStateOf(false) } // Thêm trạng thái tải cục bộ
    LaunchedEffect(idCustomer) {
        if (idCustomer != null) {
            likedViewModel.getLikedByIdCustomer(idCustomer)
        }
    }
    LaunchedEffect(listLiked) {
        check = listLiked.any { it.product_id == device.product_id }
    }
    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    // Tải hình ảnh
    LaunchedEffect(device) {
        bitmap = device.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp), // Tăng padding để tạo khoảng cách giữa các card
        onClick = {
            if (username != null) {
                navController.navigate(
                    Screen.ProductDetailsScreen.route +
                            "?id=${device.product_id}&idCustomer=${idCustomer}&username=${username}&password=$password"
                )
            } else {
                navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.product_id}")
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp // Tăng bóng nhẹ để card nổi bật hơn
        ),
        shape = RoundedCornerShape(12.dp) // Bo góc lớn hơn cho cảm giác mềm mại
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Padding bên trong card đồng đều
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f) // Tỷ lệ 1:1 cho hình ảnh
                    .clip(RoundedCornerShape(12.dp)) // Bo góc hình ảnh đồng bộ với card
                    .background(Color(0xFFF5F5F5)) // Màu nền nhẹ khi chưa có hình
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                IconButton(
                    onClick = {
                        if (idCustomer == null) {
                            navController.navigate(Screen.LoginScreen.route)
                        } else if (!isLoading) {
                            isLoading = true // Bắt đầu tải
                            if (!check) {
                                val likedNew = AddLikedRequest(
                                    customer_id = idCustomer,
                                    product_id = device.product_id
                                )
                                likedViewModel.addLiked(likedNew)
                                check = true // Cập nhật cục bộ
                            } else {
                                likedViewModel.deleteLiked(idCustomer, device.product_id)
                                check = false
                                likedViewModel.getLikedByIdCustomer(idCustomer)
                            }
                            isLoading = false // Kết thúc tải
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .size(32.dp) // Tăng kích thước nút
                        .align(Alignment.TopEnd)
                        .padding(4.dp) // Padding để nút không sát viền
                        .background(
                            color = Color.White.copy(alpha = 0.8f), // Nền trắng mờ
                            shape = CircleShape
                        )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Red,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = device.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold, // Giảm độ đậm để tinh tế hơn
                fontSize = 14.sp, // Giảm kích thước chữ
                maxLines = 2, // Giới hạn 2 dòng để tránh tràn
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatGiaTienInt(device.selling_price),
                color = Color(0xFFE91E63), // Màu hồng đậm hơn để nổi bật
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CardDevice(
    device: Device,
    isFavorite: Boolean,
    idCustomer: String?,
    username: String?,
    password: String?,
    deviceViewModel: DeviceViewModel,
    navController: NavController
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var check by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked by likedViewModel.listLiked.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    // Tải hình ảnh bất đồng bộ
    LaunchedEffect(device) {
        bitmap = deviceViewModel.getDeviceImageBitmap(device)
    }

    LaunchedEffect(idCustomer) {
        if (idCustomer != null) {
            likedViewModel.getLikedByIdCustomer(idCustomer)
        }
    }

    LaunchedEffect(listLiked) {
        check = listLiked.any { it.product_id == device.idDevice }
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp), // Tăng padding để tạo khoảng cách giữa các card
        onClick = {
            if (username != null) {
                navController.navigate(
                    Screen.ProductDetailsScreen.route +
                            "?id=${device.idDevice}&idCustomer=${idCustomer}&username=${username}&password=$password"
                )
            } else {
                navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.idDevice}")
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp // Tăng bóng nhẹ để card nổi bật hơn
        ),
        shape = RoundedCornerShape(12.dp) // Bo góc lớn hơn cho cảm giác mềm mại
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Padding bên trong card đồng đều
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f) // Tỷ lệ 1:1 cho hình ảnh
                    .clip(RoundedCornerShape(12.dp)) // Bo góc hình ảnh đồng bộ với card
                    .background(Color(0xFFF5F5F5)) // Màu nền nhẹ khi chưa có hình
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop // Crop để hình ảnh lấp đầy khung
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                // Nút yêu thích ở góc trên bên phải
                IconButton(
                    onClick = {
                        if (idCustomer == null) {
                            navController.navigate(Screen.LoginScreen.route)
                        } else if (!isLoading) {
                            isLoading = true
                            if (!check) {
                                val likedNew = AddLikedRequest(
                                    customer_id = idCustomer,
                                    product_id = device.idDevice
                                )
                                likedViewModel.addLiked(likedNew)
                                check = true
                            } else {
                                likedViewModel.deleteLiked(idCustomer, device.idDevice)
                                check = false
                                likedViewModel.getLikedByIdCustomer(idCustomer)
                            }
                            isLoading = false
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .size(32.dp) // Tăng kích thước nút
                        .align(Alignment.TopEnd)
                        .padding(4.dp) // Padding để nút không sát viền
                        .background(
                            color = Color.White.copy(alpha = 0.8f), // Nền trắng mờ
                            shape = CircleShape
                        )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Red,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách lớn hơn một chút
            Text(
                text = device.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold, // Giảm độ đậm để tinh tế hơn
                fontSize = 14.sp, // Giảm kích thước chữ
                maxLines = 2, // Giới hạn 2 dòng để tránh tràn
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatGiaTien(device.sellingPrice),
                color = Color(0xFFE91E63), // Màu hồng đậm hơn để nổi bật
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            //Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách dưới cùng
        }
    }
}

@Composable
fun CategoryItem(category: Category, navController: NavController, username: String?, idCustomer: String?) {
    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    // Tải hình ảnh
    LaunchedEffect(category) {
        bitmap = category.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable {
                val route = if (username != null && idCustomer != null)
                    Screen.Category_Screen.route + "?category=${category.name}&username=${username}&idCustomer=$idCustomer"
                else
                    Screen.Category_Screen.route + "?category=${category.name}"
                navController.navigate(route)
            }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5))
                .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = category.name.ifEmpty { "Hình ảnh sản phẩm" },
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            } ?: run {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SlideImage(base64String: String) {
    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect (Unit){
        bitmap = deviceViewModel.getDeviceImageBitmapImage(base64String)
    }
    AnimatedContent(
        targetState = bitmap,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = { fadeIn() togetherWith fadeOut() }, label = ""
    ) { targetBitmap ->
        Image(
            painter = rememberAsyncImagePainter(model = targetBitmap),
            contentDescription = null,
            modifier = Modifier.size(360.dp),
            contentScale = ContentScale.Crop
        )
    }
}