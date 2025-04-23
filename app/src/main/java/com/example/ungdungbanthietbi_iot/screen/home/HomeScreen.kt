package com.example.ungdungbanthietbi_iot.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.ungdungbanthietbi_iot.R
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
import java.text.DecimalFormat
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import com.example.ungdungbanthietbi_iot.data.cart.CartViewModel
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien

/** Giao diện màn hình Trang chủ (HomeScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 8/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình Trang chủ
 *  như có danh sách sản phẩm nổi bật, sản phẩm yêu thích, sản phẩm gợi ý
 * ------------------------------------------------------------
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 19/12/2024
 * ------------------------------------------------------------
 * Nội dung cập nhật: thêm nút nổi để khi ấn thì sẽ trượt lên trên
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    deviceViewModel: DeviceViewModel,
    slideShowViewModel: SlideShowViewModel,
    username:String?,
) {

    deviceViewModel.getAllDevice()
    deviceViewModel.getDeviceFeatured()
    var listAllDevice : List<Device> = deviceViewModel.listAllDevice
    var listDeviceFeatured :List<Device> = deviceViewModel.listDeviceFeatured
    val listDeviceLiked: List<Device> = deviceViewModel.listDeviceOfCustomer
    var listSlideShow = slideShowViewModel.listSlideShow

    var currentIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        slideShowViewModel.getAllSlideShow()
    }

    // Tự động chuyển hình sau mỗi 3 giây
    LaunchedEffect(key1 = currentIndex, key2 = listSlideShow.size) {
        if (listSlideShow.isNotEmpty()) {
            delay(3000)
            currentIndex = (currentIndex + 1) % listSlideShow.size
        }
    }

    val cartViewModel: CartViewModel = viewModel()
    val listCart = cartViewModel.listCart

    val accountViewModel:AccountViewModel = viewModel()
    val account = accountViewModel.account

    if(username != null){
        accountViewModel.getUserByUsername(username)
    }

    if(account != null){
        deviceViewModel.getDeviceByLiked(account.idPerson.toString())
        cartViewModel.getCartByIdCustomer(account.idPerson.toString())
    }

    val navdrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() //xử lý suspending fun (mở và đóng drawer)
    val countries = listOf(
        "Thiết bị chiếu sáng",
        "Thiết bị cảm biến",
        "Thiết bị điện tử thông minh",
        "Đồng hồ thông minh",
    )

    // Tạo LazyListState để quản lý trạng thái cuộn
    val listState = rememberLazyListState()
    // Coroutine để thực hiện cuộn
    val coroutineScope = rememberCoroutineScope()
    // Biến trạng thái để theo dõi có đang cuộn hay không
    var isScrolling by remember { mutableStateOf(false) }
    // Biến trạng thái để sản phẩm yêu thích không
    var isFavorite by remember { mutableStateOf(false) }

    // Theo dõi trạng thái cuộn
    LaunchedEffect(listState.isScrollInProgress) {
        isScrolling = listState.isScrollInProgress
    }

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
                        fontSize = 22.sp,
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
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            if(username == null){
                                navController.navigate(Screen.HomeScreen.route)
                            }
                            else{
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
                        }, selected = false, onClick = { /* Chọn danh mục */ }
                    )
                }
            }
        }
    ) {
        // Màn hình chính
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "IOT Connect Mart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF5D9EFF),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {

                        IconButton(onClick = {
                            scope.launch {
                                // mở và đóng drawer
                                navdrawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .size(48.dp) // Kích thước của Box để chứa icon
                        ) {
                            // Icon giỏ hàng
                            IconButton(onClick = {
                                // vào màn hình giỏ hàng nếu chưa đăng nhập thì vào màn hình đăng nhập(LoginScreen)
                                if(account == null){
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                                else{
                                    navController.navigate(Screen.Cart_Screen.route +"?idCustomer=${account.idPerson}&username=${account.username}")
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Giỏ hàng",
                                    tint = Color.White
                                )
                            }

                            // Số lượng giỏ hàng nằm đè lên góc phải của icon
                            Text(
                                text = "${listCart.size}", // Thay bằng biến nếu cần động
                                color = Color(0xFF5D9EFF),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center, // Căn giữa text
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp) // Đảm bảo kích thước đủ lớn cho hình tròn
                                    .offset(x = (-2).dp, y = (-2).dp)
                                    .background(color = Color.White, CircleShape)
                                    .clip(CircleShape) // Cắt theo hình tròn nếu cần
                                    //.padding(4.dp)
                                    .wrapContentSize(align = Alignment.Center) // Đảm bảo nội dung nằm giữa hình tròn
                            )
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
                        //.border(1.dp, Color.Black)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box (
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if(username == null){
                                            navController.navigate(Screen.HomeScreen.route)
                                        }
                                        else{
                                            navController.navigate(Screen.HomeScreen.route + "?username=${username}")
                                        }
                                    }
                                    .background(Color(0xFF5D9EFF), RoundedCornerShape(5.dp)),
                                contentAlignment = Alignment.Center,

                                ){
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Trang chủ",
                                    //modifier = Modifier.weight(1f),
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.White
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
                                modifier = Modifier
                                    .size(50.dp)
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
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if(username != null){
                                            navController.navigate(Screen.Search_Screen.route + "?username=${accountViewModel.username}")
                                        }
                                        else{
                                            navController.navigate(Screen.Search_Screen.route)
                                        }
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
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if (username != null) {
                                            navController.navigate(Screen.PersonalScreen.route + "?username=${accountViewModel.username}")
                                        } else {
                                            navController.navigate(Screen.LoginScreen.route)
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ){
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Tài khoản",
                                    //modifier = Modifier.weight(1f),
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Black
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
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0) // Cuộn đến mục đầu tiên
                    }
                },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = if (isScrolling) 0.dp else 6.dp // Không đổ bóng khi trong suốt
                    ),
                    containerColor = Color(0xFF5D9EFF).copy(alpha = if (isScrolling) 0.2f else 0.8f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Nút lên",
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                state = listState
            )
            {
                item {
                    if (listSlideShow.isNotEmpty()) {
                        // Tạo PagerState với số lượng trang "vô hạn"
                        val pagerState = rememberPagerState(
                            initialPage = Int.MAX_VALUE / 2, // Bắt đầu ở giữa để lướt cả hai hướng
                            pageCount = { Int.MAX_VALUE } // Số lượng trang vô hạn
                        )

                        // Ánh xạ currentIndex với vị trí thật trong listSlideShow
                        LaunchedEffect(pagerState.currentPage) {
                            currentIndex = pagerState.currentPage % listSlideShow.size
                            if (currentIndex < 0) {
                                currentIndex += listSlideShow.size // Đảm bảo currentIndex luôn dương
                            }
                        }

                        // Tự động chuyển đổi vẫn hoạt động dựa trên currentIndex
                        LaunchedEffect(currentIndex) {
                            val targetPage = pagerState.currentPage - (pagerState.currentPage % listSlideShow.size) + currentIndex
                            if (targetPage != pagerState.currentPage) {
                                pagerState.animateScrollToPage(targetPage)
                            }
                        }

                        // HorizontalPager với lướt vô hạn
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        ) { page ->
                            // Ánh xạ page thành chỉ số thật trong listSlideShow
                            val realIndex = page % listSlideShow.size
                            val adjustedIndex = if (realIndex < 0) realIndex + listSlideShow.size else realIndex
                            SlideImage(
                                painter = rememberImagePainter(data = listSlideShow[adjustedIndex].image),
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Indicator (các chấm tròn dưới SlideShow)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            listSlideShow.forEachIndexed { index, _ ->
                                val animatedColor by animateColorAsState(
                                    targetValue = if (index == currentIndex) Color(0xFF5D9EFF) else Color.LightGray
                                )
                                Box(
                                    modifier = Modifier
                                        .size(17.dp)
                                        .padding(4.dp)
                                        .background(
                                            color = animatedColor,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            // Chuyển đến trang tương ứng
                                            currentIndex = index
                                        }
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No slides available",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    // Sản phẩm nổi bật
                    Text("Sản phẩm nổi bật", modifier = Modifier.padding(12.dp),
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        items(listDeviceFeatured){
                            if(account != null){
                                CardDeviceFeatured(device = it,
                                    isFavorite = isFavorite,
                                    account.idPerson,
                                    account.username,
                                    navController
                                )
                            }
                            else{
                                CardDeviceFeatured(
                                    device = it,
                                    isFavorite = isFavorite,
                                    null,
                                    username,
                                    navController
                                )
                            }

                        }
                    }
                }
                item {
                    // Sản phầm yêu thích
                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Sản phẩm yêu thích",
                            modifier = Modifier.padding(20.dp),
                            color = Color(0xFF085979),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Xem tất cả",
                            modifier = Modifier
                                .padding(20.dp)
                                .clickable {
                                    if(account == null){
                                        navController.navigate(Screen.LoginScreen.route)
                                    }
                                    else{
                                        navController.navigate(Screen.Favorites_Screen.route +"?idCustomer=${account.idPerson}&username=${account.username}")
                                    }
                                },
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                item {
                    // Tất cả sản phẩm
                    Text(
                        "Tất cả sản phẩm", modifier = Modifier.padding(20.dp),
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )
                }
                val pairedDevices = listAllDevice.chunked(2) // Chia danh sách thành các nhóm 2 phần tử
                items(pairedDevices) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { device ->
                            Box(
                                modifier = Modifier
                                    .weight(1f) // Mỗi sản phẩm chiếm 50% chiều rộng
                                    .padding(vertical = 5.dp)
                            ) {
                                if (account != null) {
                                    CardAllDevice(
                                        device = device,
                                        isFavorite = isFavorite,
                                        idCustomer = account.idPerson,
                                        username = account.username,
                                        navController = navController
                                    )
                                } else {
                                    CardAllDevice(
                                        device = device,
                                        isFavorite = isFavorite,
                                        idCustomer = null,
                                        username = username,
                                        navController = navController
                                    )
                                }
                            }
                        }
                        // Nếu chỉ có 1 sản phẩm trong cặp, thêm một Box trống để giữ layout
                        if (pair.size == 1) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardDeviceFeatured(device: Device, isFavorite:Boolean, idCustomer:String?, username: String?, navController: NavController){
    var check  by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked = likedViewModel.listLiked
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
            .width(200.dp)// Đặt chiều rộng cố định cho Card
            .height(250.dp)
            .padding(8.dp),
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
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //load hình ảnh từ API
                AsyncImage(
                    model = device.image,
                    contentDescription = null,
                    modifier = Modifier
                        .width(190.dp)
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = device.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                if(idCustomer == null){
                    navController.navigate(Screen.LoginScreen.route)
                }
                else{
                    var likedNew: Liked? = null
                    var isProductFound = false
                    for (liked in listLiked) {
                        if (device.idDevice == liked.idDevice) {
                            //likedViewModel.updateLiked(liked)
                            isProductFound = true
                            break
                        }
                    }

                    // Nếu sản phẩm không tìm thấy trong giỏ hàng thì thêm mới
                    if(!isProductFound){
                        likedNew = Liked(0, idCustomer, device.idDevice)
                        likedViewModel.addLiked(likedNew)
                        check = true
                    }
                    else{
                        likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
                        check = false
                    }
                    // Làm mới danh sách
                    likedViewModel.getLikedByIdCustomer(idCustomer)
                }
            },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if(check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }
    }
}


@Composable
fun CardAllDevice(device: Device, isFavorite:Boolean, idCustomer:String?, username: String?, navController: NavController){
    var check  by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked = likedViewModel.listLiked
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
            .width(200.dp)// Đặt chiều rộng cố định cho Card
            .height(250.dp)
            .padding(8.dp),
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
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //load hình ảnh từ API
                AsyncImage(
                    model = device.image,
                    contentDescription = null,
                    modifier = Modifier
                        .width(190.dp)
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = device.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                if(idCustomer == null){
                    navController.navigate(Screen.LoginScreen.route)
                }
                else{
                    var likedNew: Liked? = null
                    var isProductFound = false
                    for (liked in listLiked) {
                        if (device.idDevice == liked.idDevice) {
                            likedViewModel.updateLiked(liked)
                            isProductFound = true
                            break
                        }
                    }

                    // Nếu sản phẩm không tìm thấy trong giỏ hàng thì thêm mới
                    if(!isProductFound){
                        likedNew = Liked(0, idCustomer, device.idDevice)
                        likedViewModel.addLiked(likedNew)
                        check = true
                    }
                    else{
                        likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
                        check = false
                    }
                    // Làm mới danh sách
                    likedViewModel.getLikedByIdCustomer(idCustomer)
                }
            },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if(check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }

    }
}

@Composable
fun CardFavorites(device: Device, isFavorite:Boolean, idCustomer:String?, username: String?, navController: NavController){
    var check  by remember { mutableStateOf(isFavorite) }
    val likedViewModel: LikedViewModel = viewModel()
    val listLiked = likedViewModel.listLiked
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
            .width(200.dp)// Đặt chiều rộng cố định cho Card
            .height(250.dp)
            .padding(8.dp),
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
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //load hình ảnh từ API
                AsyncImage(
                    model = device.image,
                    contentDescription = null,
                    modifier = Modifier
                        .width(190.dp)
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = device.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                if(idCustomer == null){
                    navController.navigate(Screen.LoginScreen.route)
                }
                else{
                    var likedNew: Liked? = null
                    var isProductFound = false
                    for (liked in listLiked) {
                        if (device.idDevice == liked.idDevice) {
                            likedViewModel.updateLiked(liked)
                            isProductFound = true
                            break
                        }
                    }

                    // Nếu sản phẩm không tìm thấy thì thêm mới
                    if(!isProductFound){
                        likedNew = Liked(0, idCustomer, device.idDevice)
                        likedViewModel.addLiked(likedNew)
                        check = true
                    }
                    else{
                        likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
                        check = false
                    }
                    // Làm mới danh sách
                    likedViewModel.getLikedByIdCustomer(idCustomer)
                }
            },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if(check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideImage(painter: Painter) {
    AnimatedContent(
        targetState = painter,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = { fadeIn() with fadeOut() }
    ) { targetPainter ->
        Image(
            painter = targetPainter,
            contentDescription = null,
            modifier = Modifier
                .size(360.dp),
            contentScale = ContentScale.Crop
        )
    }
}