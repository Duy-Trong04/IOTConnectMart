package com.example.ungdungbanthietbi_iot.views.home

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
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
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.ViewComfyAlt
import androidx.compose.material.icons.outlined.ViewCozy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.ungdungbanthietbi_iot.api.LikedProduct
import com.example.ungdungbanthietbi_iot.models.Account
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.models.Liked
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
import com.example.ungdungbanthietbi_iot.views.components.AnimatedNavigationBar
import com.example.ungdungbanthietbi_iot.views.components.ButtonData

data class Category(
    val id: Int,
    val name: String,
    val imageUrl: String
)

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
    deviceViewModel.getAllDevice()
    deviceViewModel.getDeviceFeatured()
    val listAllDevice: List<Device> = deviceViewModel.listAllDevice
    val listDeviceFeatured: List<Device> = deviceViewModel.listDeviceFeatured
    val listSlideShow = slideShowViewModel.listSlideShow

    LaunchedEffect(Unit) {
        slideShowViewModel.getAllSlideShow()
    }

    val cartViewModel: CartViewModel = viewModel()
    val listCart = cartViewModel.listCart

//    val accountViewModel: AccountViewModel = viewModel()
//    val account = accountViewModel.account
//
//    if (username != null) {
//        accountViewModel.getUserByUsername(username)
//    }
//
//    LaunchedEffect(deviceViewModel.listDeviceOfCustomer) {
//        if (account != null) {
//            deviceViewModel.getDeviceByLiked(account.idPerson.toString())
//            cartViewModel.getCartByIdCustomer(account.idPerson.toString())
//        }
//    }

    val navdrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val countries = listOf(
        "Thiết bị chiếu sáng",
        "Thiết bị cảm biến",
        "Thiết bị điện tử thông minh",
        "Đồng hồ thông minh",
    )

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isScrolling by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(listState.isScrollInProgress) {
        isScrolling = listState.isScrollInProgress
    }
    val categories = listOf(
        Category(1, "Thiết bị chiếu sáng", "https://m.media-amazon.com/images/I/71cH4xU1L4L._AC_UF1000,1000_QL80_.jpg"),
        Category(2, "Thiết bị cảm biến", "https://m.media-amazon.com/images/I/61+WBqaGHEL._AC_UF1000,1000_QL80_.jpg"),
        Category(3, "Thiết bị điện tử thông minh", "https://static-ecapac.acer.com/media/catalog/product/cache/a17a77e026ef2eddd3ecae104c32cc71/h/e/hero_chromebook_plus_514_cha_backlit_1.png"),
        Category(4, "Đồng hồ thông minh", "https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/a/p/apple-watch-se-2023-lte-40mm.png")
    )

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    //Log.d("Thành công", "Đổi mật khẩu thành công ${username}va ${password}")
    ModalNavigationDrawer(
        drawerState = navdrawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5D9EFF))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "IOT Connect Mart",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng danh mục",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                scope.launch {
                                    navdrawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()

                // Home Item
                NavigationDrawerItem(
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = Color(0xFF5D9EFF), // màu xanh đồng nhất với header
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Trang chủ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D9EFF),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    },
                    selected = false,
                    onClick = {
                        if (username == null) {
                            navController.navigate(Screen.HomeScreen.route)
                        } else {
                            navController.navigate(Screen.HomeScreen.route + "?username=${username}")
                        }
                    }
                )


                Spacer(modifier = Modifier.height(8.dp))

                // Danh mục
                countries.forEach { country ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .clickable { /* Xử lý chọn danh mục */ },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = Color(0xFF5D9EFF),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = country,
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    )
    {
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
                                text = "IOT Connect Mart",
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
                            val route = if (username != null)
                                Screen.Search_Screen.route + "?username=${username}"
                            else Screen.Search_Screen.route
                            navController.navigate(route)
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
                                                "?idCustomer=${id}&username=${username}"
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
//                NavigationBar(
//                    containerColor = Color.White,
//                    contentColor = Color.Black,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .border(
//                            width = 1.dp,
//                            color = Color(0xFF5D9EFF),
//                            shape = RoundedCornerShape(0.dp) // Hình chữ nhật, không bo góc
//                        )
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(start = 25.dp, end = 25.dp),
//                        horizontalArrangement = Arrangement.SpaceAround,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        NavigationBarItem(
//                            icon = {
//                                Icon(
//                                    imageVector = if (selectedTabIndex == 0) Icons.Default.Home else Icons.Outlined.Home,
//                                    contentDescription = "Trang chủ",
//                                    modifier = Modifier.size(28.dp),
//                                    tint = if (selectedTabIndex == 0) Color(0xFF1E88E5) else Color(0xFF616161)
//                                )
//                            },
//                            label = {
//                                Text(
//                                    text = "Trang chủ",
//                                    fontSize = 12.sp,
//                                    color = if (selectedTabIndex == 0) Color(0xFF1E88E5) else Color(0xFF616161),
//                                    fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
//                                )
//                            },
//                            selected = selectedTabIndex == 0,
//                            onClick = { selectedTabIndex = 0 }
//                        )
//                        NavigationBarItem(
//                            icon = {
//                                Icon(
//                                    imageVector = Icons.Outlined.GridView,
//                                    contentDescription = "Danh mục",
//                                    modifier = Modifier.size(28.dp),
//                                    tint = if (selectedTabIndex == 1) Color(0xFF1E88E5) else Color(0xFF616161)
//                                )
//                            },
//                            label = {
//                                Text(
//                                    text = "Danh mục",
//                                    fontSize = 12.sp,
//                                    color = if (selectedTabIndex == 1) Color(0xFF1E88E5) else Color(0xFF616161),
//                                    fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
//                                )
//                            },
//                            selected = selectedTabIndex == 1,
//                            onClick = {
//                                scope.launch {
//                                    navdrawerState.apply {
//                                        if (isClosed) open() else close()
//                                    }
//                                }
//                            }
//                        )
//                        NavigationBarItem(
//                            icon = {
//                                Icon(
//                                    imageVector = if (selectedTabIndex == 2) Icons.Default.Notifications else Icons.Outlined.Notifications,
//                                    contentDescription = "Thông báo",
//                                    modifier = Modifier.size(28.dp),
//                                    tint = if (selectedTabIndex == 2) Color(0xFF1E88E5) else Color(0xFF616161)
//                                )
//                            },
//                            label = {
//                                Text(
//                                    text = "Thông báo",
//                                    fontSize = 12.sp,
//                                    color = if (selectedTabIndex == 2) Color(0xFF1E88E5) else Color(0xFF616161),
//                                    fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal
//                                )
//                            },
//                            selected = selectedTabIndex == 2,
//                            onClick = { selectedTabIndex = 2 }
//                        )
//                        NavigationBarItem(
//                            icon = {
//                                Icon(
//                                    imageVector = if (selectedTabIndex == 3) Icons.Default.Person else Icons.Outlined.Person,
//                                    contentDescription = "Tôi",
//                                    modifier = Modifier.size(28.dp),
//                                    tint = if (selectedTabIndex == 3) Color(0xFF1E88E5) else Color(0xFF616161)
//                                )
//                            },
//                            label = {
//                                Text(
//                                    text = "Tôi",
//                                    fontSize = 12.sp,
//                                    color = if (selectedTabIndex == 3) Color(0xFF1E88E5) else Color(0xFF616161),
//                                    fontWeight = if (selectedTabIndex == 3) FontWeight.Bold else FontWeight.Normal
//                                )
//                            },
//                            selected = selectedTabIndex == 3,
//                            onClick = {
//                                if (id != null) selectedTabIndex = 3
//                                else {
//                                    navController.navigate(Screen.LoginScreen.route)
//                                }
//                            }
//                        )
//                    }
//                }
                val buttons = listOf(
                    ButtonData("Trang chủ", if (selectedTabIndex == 0) Icons.Default.Home else Icons.Outlined.Home) {
                        selectedTabIndex = 0
                    },
                    ButtonData("Danh mục", Icons.Outlined.GridView) {
                        scope.launch {
                            navdrawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    ButtonData("Thông báo", if (selectedTabIndex == 2) Icons.Default.Notifications else Icons.Outlined.Notifications) {
                        selectedTabIndex = 2
                    },
                    ButtonData("Tôi", if (selectedTabIndex == 3) Icons.Default.Person else Icons.Outlined.Person) {
                        if (id != null) selectedTabIndex = 3
                        else navController.navigate(Screen.LoginScreen.route)
                    }
                )
                AnimatedNavigationBar(
                    buttons = buttons,
                    barColor = Color.White,
                    circleColor = Color(0xFF5D9EFF),
                    selectedColor = Color(0xFF1E88E5),
                    unselectedColor = Color(0xFF616161)
                )
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
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Nút lên",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { padding ->
            when (selectedTabIndex) {
                0 -> HomeContent(
                    padding = padding,
                    listState = listState,
                    listSlideShow = listSlideShow,
                    deviceViewModel = deviceViewModel,
                    navController = navController,
                    username = username,
                    id = id,
                    isFavorite = isFavorite,
                    listAllDevice = listAllDevice,
                    listDeviceFeatured = listDeviceFeatured,
                    listDeviceLiked = deviceViewModel.listDeviceOfCustomer,
                    categories = categories,
                )
                2 -> if (username != null) NotificationScreen(navController = navController, idUser = id)
                else NotificationScreen(navController = navController, idUser = "")
                3 -> if(username != null && id != null)
                    PersonalScreen(
                        navController = navController,
                        username = username,
                        id = id,
                        deviceViewModel = deviceViewModel,
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
    isFavorite: Boolean,
    listAllDevice: List<Device>,
    listDeviceFeatured: List<Device>,
    listDeviceLiked: List<Device>,
    categories: List<Category>,
    likedViewModel: LikedViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    // Trạng thái lưu danh sách sản phẩm yêu thích
    var listFavoriteProducts by remember { mutableStateOf<List<LikedProduct>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Làm mới danh sách yêu thích
    fun refreshFavorites() {
        if (id != null) {
            coroutineScope.launch {
                isLoading = true
                likedViewModel.getLikedProducts(id).onSuccess { response ->
                    if (response.status_code == 200) {
                        listFavoriteProducts = response.data.data
                    } else {
                        errorMessage = "Lỗi khi tải danh sách yêu thích: Mã trạng thái ${response.status_code}"
                    }
                    isLoading = false
                }.onFailure { exception ->
                    errorMessage = exception.message
                    isLoading = false
                }
            }
        }
    }

    // Gọi API khi composable được tạo hoặc id thay đổi
    LaunchedEffect(id) {
        refreshFavorites()
    }

    // Hàm xử lý refresh
    fun onRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            deviceViewModel.getAllDevice()
            refreshFavorites()
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
                if (listSlideShow.isNotEmpty()) {
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { listSlideShow.size }
                    )
                    val coroutineScope = rememberCoroutineScope()
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(3000)
                            val nextPage = (pagerState.currentPage + 1) % listSlideShow.size
                            pagerState.animateScrollToPage(nextPage)
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
                            SlideImage(
                                painter = rememberAsyncImagePainter(model = listSlideShow[page].image),
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(vertical = 15.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            listSlideShow.forEachIndexed { index, _ ->
                                val isActive = index == pagerState.currentPage
                                val animatedWidth by animateFloatAsState(
                                    targetValue = if (isActive) 32f else 12f,
                                    animationSpec = tween(300), label = ""
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp)
                                        .size(width = animatedWidth.dp, height = 6.dp)
                                        .background(
                                            color = if (isActive) Color(0xFF1E88E5) else Color(0xFFB0BEC5),
                                            shape = RoundedCornerShape(3.dp)
                                        )
                                        .clickable {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
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
                            username = username
                        )
                    }
                }
            }
            item {
                SectionTitle("Sản phẩm nổi bật")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listDeviceFeatured) { device ->
                        CardDevice(
                            device = device,
                            isFavorite = isFavorite,
                            idCustomer = id,
                            username = username,
                            deviceViewModel = deviceViewModel,
                            navController = navController
                        )
                    }
                }
            }
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
                                                "?idCustomer=${id}&username=${username}"
                                    )
                                }
                            }
                            .padding(end = 20.dp)
                    )
                }
                if (isLoading) {
                    Text(
                        text = "Đang tải...",
                        color = Color(0xFF616161),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "Đã xảy ra lỗi",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else if (listFavoriteProducts.isEmpty()) {
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
                        items(listFavoriteProducts) { product ->
                            if (username != null) {
                                CardFavorites(
                                    device = product,
                                    isFavorite = true,
                                    idCustomer = id,
                                    username = username,
                                    likedViewModel = likedViewModel,
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
        color = Color(0xFF0D47A1),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CardFavorites(
    device: LikedProduct,
    isFavorite: Boolean,
    idCustomer: String?,
    username: String?,
    likedViewModel: LikedViewModel,
    navController: NavController
) {
    var check by remember { mutableStateOf(isFavorite) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Kiểm tra trạng thái yêu thích
    LaunchedEffect(idCustomer, likedViewModel.listLiked) {
        if (idCustomer != null) {
            check = likedViewModel.listLiked.any { it.id == device.id }
        }
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp),
        onClick = {
            if (username != null) {
                navController.navigate(
                    Screen.ProductDetailsScreen.route +
                            "?id=${device.id}&idCustomer=${idCustomer}&username=${username}"
                )
            } else {
                navController.navigate(
                    Screen.ProductDetailsScreen.route +
                            "?id=${device.id}&idCustomer=${idCustomer}"
                )
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = device.image ?: "",
                    contentDescription = device.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
//                    placeholder = painterResource(R.drawable.placeholder_image),
//                    error = painterResource(R.drawable.placeholder_image),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = device.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatGiaTien(device.selling_price.toDouble()),
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            IconButton(
                onClick = {
                    if (idCustomer == null) {
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (!isLoading) {
                        coroutineScope.launch {
                            isLoading = true
                            if (!check) {
                                likedViewModel.addLikedProduct(idCustomer, device.id.toString())
                                    .onSuccess { check = true }
                                    .onFailure { /* Xử lý lỗi nếu cần */ }
                            } else {
                                likedViewModel.deleteLikedProduct(idCustomer, device.id.toString())
                                    .onSuccess { check = false }
                                    .onFailure { /* Xử lý lỗi nếu cần */ }
                            }
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Red
                    )
                } else {
                    Icon(
                        imageVector = if (check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun CardDevice(
    device: Device,
    isFavorite: Boolean,
    idCustomer: String?,
    username: String?,
    deviceViewModel: DeviceViewModel,
    navController: NavController
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var check by remember { mutableStateOf(isFavorite) }
    var isLoading by remember { mutableStateOf(false) }
    val likedViewModel: LikedViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    // Tải hình ảnh bất đồng bộ
    LaunchedEffect(device) {
        bitmap = deviceViewModel.getDeviceImageBitmap(device)
    }

    // Kiểm tra trạng thái yêu thích
    LaunchedEffect(idCustomer, likedViewModel.listLiked) {
        if (idCustomer != null) {
            check = likedViewModel.listLiked.any { it.id == device.idDevice }
        }
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp),
        onClick = {
            if (username != null) {
                navController.navigate(
                    Screen.ProductDetailsScreen.route +
                            "?id=${device.idDevice}&idCustomer=${idCustomer}&username=${username}"
                )
            } else {
                navController.navigate(
                    Screen.ProductDetailsScreen.route + "?id=${device.idDevice}"
                )
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)),
                        contentScale = ContentScale.Fit
                    )
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF5D9EFF)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = device.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatGiaTien(device.sellingPrice.toDouble()),
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            IconButton(
                onClick = {
                    if (idCustomer == null) {
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (!isLoading) {
                        coroutineScope.launch {
                            isLoading = true
                            if (!check) {
                                likedViewModel.addLikedProduct(idCustomer, device.idDevice.toString())
                                    .onSuccess { check = true }
                                    .onFailure { /* Xử lý lỗi nếu cần */ }
                            } else {
                                likedViewModel.deleteLikedProduct(idCustomer, device.idDevice.toString())
                                    .onSuccess { check = false }
                                    .onFailure { /* Xử lý lỗi nếu cần */ }
                            }
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Red
                    )
                } else {
                    Icon(
                        imageVector = if (check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, navController: NavController, username: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable {
                val route = if (username != null)
                    Screen.Search_Screen.route + "?category=${category.name}&username=${username}"
                else
                    Screen.Search_Screen.route + "?category=${category.name}"
                navController.navigate(route)
            }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5))
                .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(category.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = category.name,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Fit
            )
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideImage(painter: Painter) {
    AnimatedContent(
        targetState = painter,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = { fadeIn() with fadeOut() }, label = ""
    ) { targetPainter ->
        Image(
            painter = targetPainter,
            contentDescription = null,
            modifier = Modifier.size(360.dp),
            contentScale = ContentScale.Crop
        )
    }
}