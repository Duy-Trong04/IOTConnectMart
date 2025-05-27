
package com.example.ungdungbanthietbi_iot.views.product_detail

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel
import com.example.ungdungbanthietbi_iot.models.CartEntity
import com.example.ungdungbanthietbi_iot.viewModels.CartViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ImageViewModel
import com.example.ungdungbanthietbi_iot.models.Liked
import com.example.ungdungbanthietbi_iot.viewModels.LikedViewModel
import com.example.ungdungbanthietbi_iot.models.Review
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.views.home.CardDevice
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/** Giao diện màn hình Chi tiết sản phẩm(ProductDetailsScreen)
 * -----------------------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 9/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình Chi tiết sản phẩm
 *  như có đánh giá sản phẩm, thông tin sản phẩm, hình ảnh sản phẩm, gợi ý sản phẩm
 * ---------------------------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
enum class DialogType {
    AddToCart,
    AddressRequired
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    id:String,
    idCustomer:String?,
    username:String?,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    reviewViewModel: ReviewViewModel
) {
    id.toIntOrNull()?.let { deviceViewModel.getDeviceBySlug(it) }
    deviceViewModel.getAllDevice()
    val listAllDevice : List<Device> = deviceViewModel.listAllDevice
    val device = deviceViewModel.device.collectAsState().value

//    val customerViewModel: CustomerViewModel = viewModel()
//    val customer = customerViewModel.customer
//    if(idCustomer != null){
//        LaunchedEffect (idCustomer){
//            customerViewModel.getCustomerById(idCustomer)
//        }
//    }

//    val likedViewModel: LikedViewModel = viewModel()
//    val listLiked = likedViewModel.listLiked

//    val listImage = imageViewModel.listImage
//    LaunchedEffect(id) {
//        imageViewModel.getImageByIdDevice(id)
//    }

//    val listReview = reviewViewModel.listReview
//    LaunchedEffect(id) {
//        reviewViewModel.getReviewByIdDevice(id)
//    }

    val accountViewModel: AccountViewModel = viewModel()
    val account = accountViewModel.account

    if(username != null){
        accountViewModel.getUserByUsername(username)
    }

    var currentIndex by remember { mutableStateOf(0) }
    // Tự động chuyển hình sau mỗi 3 giây
    LaunchedEffect(key1 = currentIndex, key2 = device?.images?.size) {
        if (device?.images?.isNotEmpty() == true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % device.images.size
        }
    }

    val cartViewModel: CartViewModel = viewModel()
    val listCart = cartViewModel.listCart

    if(idCustomer != null){
        LaunchedEffect (listCart.size) {
            cartViewModel.getCartByIdCustomer(idCustomer)
        }
    }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var images by remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    // Tải hình ảnh
    LaunchedEffect(device) {
        if (device != null) {
            bitmap = deviceViewModel.getDeviceImageBitmap(device)
            images = deviceViewModel.getDeviceImagesBitmap(device)
        } else {
            bitmap = null
            images = emptyList()
        }
    }

//    LaunchedEffect(idCustomer) {
//        if(idCustomer!=null){
//
//            likedViewModel.getLikedByIdCustomer(idCustomer)
//        }
//    }


//    // Biến lưu trữ giá trị đánh giá
//    val averageRating = if (listReview.isNotEmpty()) {
//        val avg = listReview.map { it.rating }.average() // Tính trung bình cộng
//        // Làm tròn tới 1 chữ số thập phân
//        (avg * 10.0).roundToInt() / 10.0
//    } else {
//        0.0 // Giá trị mặc định nếu danh sách rỗng
//    }

    //Lưu thông tin sản phẩm để truyền qua màn hình thanh toán
    val selectedProducts = remember { mutableListOf<Triple<Int, Int, Int>>() }

    // Biến lưu trữ giá trị checked
    // Biến trạng thái để sản phẩm yêu thích không
    var isFavorite by remember { mutableStateOf(false) }
//    LaunchedEffect(listLiked) {
//        isFavorite = listLiked.any { it.idDevice == device?.idDevice }
//    }
    // Biến lưu trữ trạng thái hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf<DialogType?>(null) }

    // Biến lưu trữ số lượng sản phẩm
    var quantity by remember { mutableStateOf(1) }

    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    // Hiển thị Snackbar cho "Thêm vào giỏ hàng"
    LaunchedEffect(showSnackbar.value) {
        if (showSnackbar.value) {
            snackbarHostState.showSnackbar(snackbarMessage.value)
            delay(3000)
            showSnackbar.value = false
        }
    }

    val addressViewModel: AddressViewModel = viewModel()
    LaunchedEffect(idCustomer) {
        addressViewModel.getAddressByIdCustomer(idCustomer)
    }

    var isLoading by remember { mutableStateOf(false) } // Thêm trạng thái tải cục bộ

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chi tiết sản phẩm",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Quay về",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White
                ),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        // Icon Tìm kiếm
                        IconButton(onClick = {
                            if(username != null){
                                navController.navigate(Screen.Search_Screen.route + "?username=${username}")
                            }
                            else{
                                navController.navigate(Screen.Search_Screen.route)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tìm kiếm",
                                tint = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp) // Kích thước của Box để chứa icon
                        ) {
                            // Icon giỏ hàng
                            IconButton(onClick = {
                                // vào màn hình giỏ hàng nếu chưa đăng nhập thì vào màn hình đăng nhập(LoginScreen)
                                if(username == null){
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                                else{
                                    navController.navigate(Screen.Cart_Screen.route +"?idCustomer=${idCustomer}&username=${username}")
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Giỏ hàng",
                                    tint = Color.White
                                )
                            }

                            if(listCart.isNotEmpty()) {
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
                    }
                }
            )
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
        },
        content = { padding ->
            if (showDialog && dialogType != null) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        when (dialogType) {
                            DialogType.AddToCart -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row {
                                        AsyncImage(
                                            model = device?.image,
                                            contentDescription = null,
                                            modifier = Modifier.size(100.dp)
                                        )
                                        Column {
                                            if (device != null) {
                                                Text(
                                                    text = "Giá: ${formatGiaTien(device.sellingPrice)}",
                                                    modifier = Modifier.padding(start = 16.dp),
                                                    color = Color.Red
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            if (device != null) {
                                                Text(
                                                    text = "Tồn kho: ${device.stock - quantity}",
                                                    modifier = Modifier.padding(start = 16.dp)
                                                )
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Số lượng",
                                            modifier = Modifier.padding(top = 10.dp),
                                            fontSize = 20.sp
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                if (quantity > 1) quantity--
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Remove,
                                                    contentDescription = "Trừ"
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "$quantity")
                                            Spacer(modifier = Modifier.width(8.dp))
                                            IconButton(onClick = {
                                                quantity++
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Add,
                                                    contentDescription = "Thêm"
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            if (idCustomer == null) {
                                                navController.navigate(Screen.LoginScreen.route)
                                            } else {
                                                var cartNew: CartEntity? = null
                                                var isProductFound = false

                                                for (cart in listCart) {
                                                    if (device != null) {
                                                        if (device.idDevice == cart.idDevice) {
                                                            cart.stock += quantity
                                                            cartViewModel.updateCart(cart)
                                                            isProductFound = true
                                                            quantity = 1
                                                            break
                                                        }
                                                    }
                                                }

                                                if (!isProductFound) {
                                                    cartNew = CartEntity(
                                                        idCustomer = idCustomer,
                                                        idDevice = device!!.idDevice,
                                                        stock = quantity
                                                    )
                                                    cartViewModel.addToCart(cartNew)
                                                    quantity = 1
                                                }
                                            }
                                            showDialog = false
                                            snackbarMessage.value = "Thêm thành công!"
                                            showSnackbar.value = true
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF5D9EFF),
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(5.dp),
                                        elevation = ButtonDefaults.buttonElevation(5.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Thêm vào giỏ hàng",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                            DialogType.AddressRequired -> {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false }, // Đóng khi nhấn ngoài dialog
                                    title = {
                                        Text(
                                            text = "Thông báo"
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "Bạn chưa có địa chỉ giao hàng." +
                                                    "Vui lòng thêm địa chỉ giao hàng!",
                                            fontSize = 16.sp
                                        )
                                    },
                                    confirmButton = {
                                        Row {
                                            Button(
                                                onClick = {
                                                    showDialog = false
                                                    navController.navigate("${Screen.Address_Selection.route}?idCustomer=${idCustomer}")
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFF5D9EFF)
                                                ),
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Text(text = "Thêm địa chỉ",
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                    },
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding),
            )
            {
                item {
                    if (device == null) {
                        // Show a loading indicator or placeholder while device is null
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = Color(0xFF5D9EFF)
                            )
                        }
                    } else {
                        if (images.isNotEmpty()) {
                            val pagerState = rememberPagerState(
                                initialPage = Int.MAX_VALUE / 2,
                                pageCount = { Int.MAX_VALUE }
                            )
                            LaunchedEffect(pagerState.currentPage) {
                                currentIndex = pagerState.currentPage % images.size
                                if (currentIndex < 0) {
                                    currentIndex += images.size
                                }
                            }
                            LaunchedEffect(currentIndex) {
                                val targetPage =
                                    pagerState.currentPage - (pagerState.currentPage % images.size) + currentIndex
                                if (targetPage != pagerState.currentPage) {
                                    pagerState.animateScrollToPage(targetPage)
                                }
                            }
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            ) { page ->
                                val realIndex = page % images.size
                                val adjustedIndex =
                                    if (realIndex < 0) realIndex + images.size else realIndex
                                Image(
                                    bitmap = images[adjustedIndex].asImageBitmap(),
                                    contentDescription = device.name ?: "Hình ảnh sản phẩm",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                images.forEachIndexed { index, _ ->
                                    val isActive = index == currentIndex
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
                                            .clickable { currentIndex = index }
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                bitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                                        modifier = Modifier
                                            .size(250.dp)
                                        //.align(Alignment.CenterHorizontally)
                                    )
                                } ?: run {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color(0xFF5D9EFF)
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    if (device != null) {
                        // Chi tiết sản phẩm
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(
                                    text = device.name,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Giá: ${formatGiaTien(device.sellingPrice)}",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )

                                IconButton(
                                    onClick = {
//                                    if (idCustomer == null) {
//                                        navController.navigate(Screen.LoginScreen.route)
//                                    } else if (!isLoading) {
//                                        isLoading = true // Bắt đầu tải
//                                        if (!isFavorite) {
//                                            val likedNew = Liked(0, idCustomer, device.idDevice)
//                                            likedViewModel.addLiked(likedNew)
//                                            snackbarMessage.value = "Thêm vào yêu thích thành công!"
//                                            isFavorite = true // Cập nhật cục bộ
//                                        } else {
//                                            likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
//                                            snackbarMessage.value = "Xóa khỏi yêu thích!"
//                                            isFavorite = false // Cập nhật cục bộ
//                                        }
//                                        // Làm mới danh sách yêu thích
//                                        likedViewModel.getLikedByIdCustomer(idCustomer)
//                                        deviceViewModel.getDeviceByLiked(idCustomer)
//                                        showSnackbar.value = true
//                                        isLoading = false // Kết thúc tải
//                                    }
                                    },
                                    enabled = !isLoading,
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = Color.Red
                                        )
                                    } else {
                                        Icon(
                                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                            contentDescription = "Favorite",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                            Button(
                                onClick = {
                                    if (idCustomer != null) {
                                        showDialog = true
                                        dialogType = DialogType.AddToCart
                                    } else {
                                        navController.navigate(Screen.LoginScreen.route)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Color(0xFF5D9EFF)
                                )
                            ) {
                                Text(
                                    text = "THÊM VÀO GIỎ HÀNG",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Button(
                                onClick = {
                                    if (idCustomer == null) {
                                        navController.navigate(Screen.LoginScreen.route)
                                    } else {

                                        if (addressViewModel.listAddress.isEmpty()) {
                                            showDialog = true
                                            dialogType = DialogType.AddressRequired
                                        } else {
                                            // Không thêm hoặc cập nhật giỏ hàng
                                            selectedProducts.clear()
                                            selectedProducts.add(
                                                Triple(
                                                    device!!.idDevice,
                                                    quantity,
                                                    0
                                                )
                                            ) // cartId = 0 vì không dùng giỏ hàng

                                            // Tính tổng giá
                                            val totalPrice = device.sellingPrice * quantity

                                            // Tạo chuỗi selectedProducts
                                            val selectedProductsString =
                                                selectedProducts.joinToString(",") { "${it.first}:${it.second}:${it.third}" }

                                            // Điều hướng đến màn hình thanh toán
                                            navController.navigate(
                                                Screen.Check_Out.route +
                                                        "?selectedProducts=$selectedProductsString" +
                                                        "&tongtien=$totalPrice" +
                                                        "&username=$username"
                                            )

                                            // Hiển thị Snackbar
                                            snackbarMessage.value = "Đã chọn sản phẩm để mua ngay!"
                                            showSnackbar.value = true

                                            // Đặt lại số lượng
                                            quantity = 1
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Red
                                )
                            ) {
                                Text(
                                    text = "MUA NGAY",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Mô tả sản phẩm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = device!!.descriptionNormal,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Thông số kỹ thuật",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (device.specifications.isNotEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    device.specifications.forEach { specification ->
                                        Text(
                                            text = specification.name,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                        specification.attributes.forEach { attribute ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = attribute.name,
                                                    fontSize = 14.sp,
                                                    color = Color.Gray
                                                )
                                                Text(
                                                    text = attribute.value,
                                                    fontSize = 14.sp,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "Không có thông số kỹ thuật",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
                item{
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Đánh giá sản phẩm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                "Xem tất cả",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.Product_Reviews.route + "?idDevice=${idCustomer}")
                                }
                            )
                        }
//                        Row(
//                            modifier = Modifier.padding(5.dp).fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ){
//                            for (i in 1..5) {
//                                Text(
//                                    text = if (i <= averageRating) "★" else "☆",
//                                    fontSize = 20.sp,
//                                    color = if (i <= averageRating) Color(0xFFFBC02D) else Color(0xFFFBC02D)
//                                )
//                            }
//                            Text(
//                                text = "${averageRating}/5.0 (${listReview.size} đánh giá)",
//                                modifier = Modifier.padding(start = 8.dp),
//                                color = Color.Gray,
//                                fontSize = 15.sp,
//                                textAlign = TextAlign.Center
//                            )
//                        }
                    }
                }
//                items(listReview.take(2)){
//                    CardReview(review = it, onlick = {
//                        navController.navigate(Screen.Product_Reviews.route + "?idDevice=${it.idDevice}")
//                    },
//                        id.toInt()
//                    )
//                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    // Gợi ý sản phẩm
                    Text(
                        "Gợi ý sản phẩm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        items(listAllDevice){
                            if(username != null){
                                CardDevice(device = it,
                                    isFavorite = isFavorite,
                                    idCustomer,
                                    username,
                                    deviceViewModel = deviceViewModel,
                                    navController
                                )
                            }
                            else{
                                CardDevice(device = it,
                                    isFavorite = isFavorite,
                                    null,
                                    username,
                                    deviceViewModel = deviceViewModel,
                                    navController
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CardReview(review: Review, onlick:() -> Unit, id:Int){

    val customerViewModel: CustomerViewModel = viewModel()
    val listCustomer = customerViewModel.listCustomerReviewDevice

    LaunchedEffect(id) {
        customerViewModel.getCustomerReviewDeviceByIdDevice(id)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onlick
    )
    {
        HorizontalDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, top = 5.dp)) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "avt",
                modifier = Modifier.size(30.dp)
            )
            Column {
                for (customer in listCustomer){
                    if(customer.id == review.idCustomer){
                        Text(text = "${customer.surname} ${customer.lastname}")
                    }
                }
                Row(modifier = Modifier.padding(start = 5.dp))
                {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Sao",
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFFFBC02D) // Màu vàng
                        )
                    }
                }
            }
        }
        Text(
            text = review.comment,
            modifier = Modifier.padding(bottom = 10.dp, start = 15.dp, end = 5.dp)
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
            modifier = Modifier
                .size(360.dp),
            contentScale = ContentScale.Crop
        )
    }
}