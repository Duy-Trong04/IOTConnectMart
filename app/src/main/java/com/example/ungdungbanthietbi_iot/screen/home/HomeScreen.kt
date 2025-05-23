package com.example.ungdungbanthietbi_iot.screen.home

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.ungdungbanthietbi_iot.data.account.Account
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.liked.Liked
import com.example.ungdungbanthietbi_iot.data.liked.LikedViewModel
import com.example.ungdungbanthietbi_iot.data.slideshow.SlideShow
import com.example.ungdungbanthietbi_iot.data.slideshow.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ungdungbanthietbi_iot.data.cart.CartViewModel
import com.example.ungdungbanthietbi_iot.screen.notification.NotificationScreen
import com.example.ungdungbanthietbi_iot.screen.personal.PersonalScreen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien

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

    val accountViewModel: AccountViewModel = viewModel()
    val account = accountViewModel.account

    if (username != null) {
        accountViewModel.getUserByUsername(username)
    }

    LaunchedEffect (deviceViewModel.listDeviceOfCustomer){
        if (account != null) {
            deviceViewModel.getDeviceByLiked(account.idPerson.toString())
            cartViewModel.getCartByIdCustomer(account.idPerson.toString())
        }
    }


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

    ModalNavigationDrawer(
        drawerState = navdrawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5D9EFF))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "IOT Connect Mart",
                        modifier = Modifier.padding(3.dp),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng danh mục",
                        modifier = Modifier
                            .padding(end = 3.dp)
                            .size(24.dp)
                            .clickable {
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
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (username == null) {
                                navController.navigate(Screen.HomeScreen.route)
                            } else {
                                navController.navigate(Screen.HomeScreen.route + "?username=${username}")
                            }
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
                                    .padding(vertical = 3.dp, horizontal = 3.dp)
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
                        }, selected = false, onClick = { /* Chọn danh mục */ }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if(selectedTabIndex == 3){
                            Text(
                                text = "Hồ sơ cá nhân",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                        else{
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
                        if (account != null) {
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
                                        text = account.username.first().toString().uppercase(),
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
                                if (account == null) {
                                    navController.navigate(Screen.LoginScreen.route)
                                } else {
                                    navController.navigate(
                                        Screen.Cart_Screen.route +
                                                "?idCustomer=${account.idPerson}&username=${account.username}"
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
                BottomAppBar(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.offset(y = 16.dp) // Dịch chuyển BottomAppBar xuống 16dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavItem(
                            icon = Icons.Default.Home,
                            label = "Trang chủ",
                            isSelected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 }
                        )
                        NavItem(
                            icon = Icons.Default.Category,
                            label = "Danh mục",
                            isSelected = selectedTabIndex == 1,
                            onClick = {
                                scope.launch {
                                    navdrawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                        NavItem(
                            icon = Icons.Default.Notifications,
                            label = "Thông báo",
                            isSelected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 }
                        )
                        NavItem(
                            icon = Icons.Default.Person,
                            label = "Tôi",
                            isSelected = selectedTabIndex == 3,
                            onClick = {
                                if(username != null) selectedTabIndex = 3
                                else {
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                            }
                        )
                    }
                }
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
                    account = account,
                    navController = navController,
                    username = username,
                    isFavorite = isFavorite,
                    listAllDevice = listAllDevice,
                    listDeviceFeatured = listDeviceFeatured,
                    listDeviceLiked = deviceViewModel.listDeviceOfCustomer,
                    categories = categories
                )
                2 -> if(account != null) NotificationScreen(navController = navController, idUser = account.idPerson)
                    else NotificationScreen(navController = navController, idUser = "")
                3 -> username?.let { PersonalScreen(navController = navController, username = it, deviceViewModel = deviceViewModel) }

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
    account: Account?,
    navController: NavController,
    username: String?,
    isFavorite: Boolean,
    listAllDevice: List<Device>,
    listDeviceFeatured: List<Device>,
    listDeviceLiked: List<Device>,
    categories: List<Category>
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    // Hàm xử lý refresh
    fun onRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            // Load lại dữ liệu
            deviceViewModel.getAllDevice()
            deviceViewModel.getDeviceFeatured()
            if (account != null) {
                deviceViewModel.getDeviceByLiked(account.idPerson.toString())
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
                .padding(padding),
            state = listState
        ) {
            item {
                if (listSlideShow.isNotEmpty()) {
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { listSlideShow.size }
                    )
                    val coroutineScope = rememberCoroutineScope()
                    // Tự động chuyển slide
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(3000)
                            val nextPage = (pagerState.currentPage + 1) % listSlideShow.size
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(bottom = 4.dp)
                    ) { page ->
                        SlideImage(
                            painter = rememberImagePainter(data = listSlideShow[page].image),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        listSlideShow.forEachIndexed { index, _ ->
                            val isActive = index == pagerState.currentPage
                            val animatedWidth by animateFloatAsState(
                                targetValue = if (isActive) 24f else 8f,
                                animationSpec = tween(300), label = ""
                            )
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(width = animatedWidth.dp, height = 4.dp)
                                    .background(
                                        color = if (isActive) Color(0xFF1E88E5) else Color(
                                            0xFFB0BEC5
                                        ),
                                        shape = RoundedCornerShape(2.dp)
                                    )
                                    .clickable {
                                        // Chuyển đến slide khi nhấp vào chấm
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                            )
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
                            idCustomer = account?.idPerson,
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
                                if (account == null) {
                                    navController.navigate(Screen.LoginScreen.route)
                                } else {
                                    navController.navigate(
                                        Screen.Favorites_Screen.route +
                                                "?idCustomer=${account.idPerson}&username=${account.username}"
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
                            if (account != null) {
                                CardFavorites(
                                    device = device,
                                    isFavorite = isFavorite,
                                    idCustomer = account.idPerson,
                                    username = account.username,
                                    deviceViewModel = deviceViewModel,
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
                                idCustomer = account?.idPerson,
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
fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(200), label = ""
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(70.dp)
            .clickable(onClick = onClick)
            .scale(scale)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(28.dp),
            tint = if (isSelected) Color(0xFF1E88E5) else Color(0xFF616161)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF1E88E5) else Color(0xFF616161),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun CardFavorites(device: Device, isFavorite:Boolean, idCustomer:String?, username: String?, deviceViewModel: DeviceViewModel, navController: NavController){
    var check  by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked = likedViewModel.listLiked
    var isLoading by remember { mutableStateOf(false) } // Thêm trạng thái tải cục bộ
    LaunchedEffect(idCustomer) {
        if(idCustomer!=null){
            likedViewModel.getLikedByIdCustomer(idCustomer)
        }
    }
    LaunchedEffect(listLiked) {
        check = listLiked.any { it.idDevice == device.idDevice }
    }
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp),
        onClick = {
            if (username != null){
                navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.idDevice}&idCustomer=${idCustomer}&username=${username}")
            }
            else{
                navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.idDevice}&idCustomer=${idCustomer}")
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //load hình ảnh từ API
                AsyncImage(
                    model = device.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(130.dp)
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
                    text = formatGiaTien(device.sellingPrice),
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            IconButton(onClick = {
                    if (idCustomer == null) {
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (!isLoading) {
                        isLoading = true // Bắt đầu tải
                        if (!check) {
                            val likedNew = Liked(0, idCustomer, device.idDevice)
                            likedViewModel.addLiked(likedNew)
                            check = true // Cập nhật cục bộ
                        } else {
                            likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
                            check = false // Cập nhật cục bộ
                        }
                        // Làm mới danh sách yêu thích
                        likedViewModel.getLikedByIdCustomer(idCustomer)
                        deviceViewModel.getDeviceByLiked(idCustomer)
                        isLoading = false // Kết thúc tải
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
    var check by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked = likedViewModel.listLiked
    var isLoading by remember { mutableStateOf(false) } // Thêm trạng thái tải cục bộ

    LaunchedEffect(idCustomer) {
        if (idCustomer != null) {
            likedViewModel.getLikedByIdCustomer(idCustomer)
        }
    }
    LaunchedEffect(listLiked) {
        check = listLiked.any { it.idDevice == device.idDevice }
    }
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .padding(4.dp),
        onClick = {
            if (username != null) {
                navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.idDevice}&idCustomer=${idCustomer}&username=${username}")
            } else {
                navController.navigate(Screen.ProductDetailsScreen.route + "?id=${device.idDevice}")
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
                    model = device.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(130.dp)
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
                    text = formatGiaTien(device.sellingPrice),
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
                        isLoading = true // Bắt đầu tải
                        if (!check) {
                            val likedNew = Liked(0, idCustomer, device.idDevice)
                            likedViewModel.addLiked(likedNew)
                            check = true // Cập nhật cục bộ
                        } else {
                            likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
                            check = false // Cập nhật cục bộ
                        }
                        // Làm mới danh sách yêu thích
                        likedViewModel.getLikedByIdCustomer(idCustomer)
                        deviceViewModel.getDeviceByLiked(idCustomer)
                        isLoading = false // Kết thúc tải
                    }
                },
                enabled = !isLoading, // Vô hiệu hóa khi đang tải
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