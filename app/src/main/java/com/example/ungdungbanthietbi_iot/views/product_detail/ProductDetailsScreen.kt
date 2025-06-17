
package com.example.ungdungbanthietbi_iot.views.product_detail

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.AddShoppingCart
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.api.AddCartRequest
import com.example.ungdungbanthietbi_iot.api.AddLikedRequest
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel
import com.example.ungdungbanthietbi_iot.models.CartEntity
import com.example.ungdungbanthietbi_iot.viewModels.CartViewModel
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ImageViewModel
import com.example.ungdungbanthietbi_iot.viewModels.LikedViewModel
import com.example.ungdungbanthietbi_iot.models.Reviews
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatDateTimeZone
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
    AddressRequired,
    BuyNow // Thêm loại dialog mới cho Mua ngay
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    id:String,
    idCustomer:String?,
    username:String?,
    password: String?,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    reviewViewModel: ReviewViewModel
) {

    val listAllDevice : List<Device> = deviceViewModel.listAllDevice
    val device = deviceViewModel.device.collectAsState().value

    val likedViewModel: LikedViewModel = viewModel()
    val listLiked by likedViewModel.listLiked.collectAsState()

    val listReview by reviewViewModel.listReviews.collectAsState()
    LaunchedEffect(id) {
        reviewViewModel.getReviewByIdDevice(id)
        deviceViewModel.getDeviceBySlug(id)
        deviceViewModel.getAllDevice()
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    // Tự động chuyển hình sau mỗi 3 giây
    LaunchedEffect(key1 = currentIndex, key2 = device?.images?.size) {
        if (device?.images?.isNotEmpty() == true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % device.images.size
        }
    }

    val cartViewModel: CartViewModel = viewModel()
    val listCart by cartViewModel.listProductCart.collectAsState()

    if(idCustomer != null){
        LaunchedEffect (listCart.size) {
            cartViewModel.getCartProducts(idCustomer)
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

    LaunchedEffect(idCustomer) {
        if(idCustomer!=null){

            likedViewModel.getLikedByIdCustomer(idCustomer)
        }
    }


    // Biến lưu trữ giá trị đánh giá
    val averageRating = if (listReview.isNotEmpty()) {
        val avg = listReview.map { it.rating }.average() // Tính trung bình cộng
        // Làm tròn tới 1 chữ số thập phân
        (avg * 10.0).roundToInt() / 10.0
    } else {
        0.0 // Giá trị mặc định nếu danh sách rỗng
    }

    //Lưu thông tin sản phẩm để truyền qua màn hình thanh toán
    val selectedProducts = remember { mutableListOf<Triple<String, Int, Int>>() }

    // Biến trạng thái để sản phẩm yêu thích không
    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(listLiked) {
        isFavorite = listLiked.any { it.product_id == device?.idDevice }
    }
    // Biến lưu trữ trạng thái hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf<DialogType?>(null) }

    // Biến lưu trữ số lượng sản phẩm
    var quantity by remember { mutableIntStateOf(1) }
    var buyNowQuantity by remember { mutableIntStateOf(1) } // Biến mới cho số lượng khi mua ngay
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
    val isLoadingAddress by addressViewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        if (idCustomer != null) {
            addressViewModel.getCustomerAddressBook(idCustomer)
        }
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
                                navController.navigate( Screen.Search_Screen.route + "?username=${username}&idCustomer=$id&password=$password")
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
                                    navController.navigate(Screen.Cart_Screen.route +"?idCustomer=${idCustomer}&username=${username}&password=$password")
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
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (device != null) {
                    IconButton(
                        onClick = {
                            if (idCustomer == null) {
                                navController.navigate(Screen.LoginScreen.route)
                            } else if (!isLoading) {
                                isLoading = true // Bắt đầu tải
                                if (!isFavorite) {
                                    val likedNew = AddLikedRequest(
                                        customer_id = idCustomer,
                                        product_id = device.idDevice
                                    )
                                    likedViewModel.addLiked(likedNew)
                                    snackbarMessage.value = "Thêm vào yêu thích thành công!"
                                    isFavorite = true // Cập nhật cục bộ
                                } else {
                                    val likedItem =
                                        listLiked.find { it.product_id == device.idDevice }
                                    likedItem?.id?.let { likedId ->
                                        likedViewModel.deleteLiked(
                                            idCustomer,
                                            likedId
                                        ) // Pass liked_id to delete
                                    }
                                    snackbarMessage.value = "Xóa khỏi yêu thích!"
                                    isFavorite = false // Cập nhật cục bộ
                                }
                                // Làm mới danh sách yêu thích
                                likedViewModel.getLikedByIdCustomer(idCustomer)
                                showSnackbar.value = true
                                isLoading = false // Kết thúc tải
                            }
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
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {
                        if (idCustomer != null) {
                            showDialog = true
                            dialogType = DialogType.AddToCart
                        } else {
                            navController.navigate(Screen.LoginScreen.route)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddShoppingCart,
                        contentDescription = "Thêm vào giỏ hàng",
                        tint = Color(0xFF5D9EFF)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (idCustomer != null) {
                            showDialog = true
                            dialogType = DialogType.BuyNow
                        } else {
                            navController.navigate(Screen.LoginScreen.route)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF5D9EFF)
                    ),
                    enabled = !isLoadingAddress
                ) {
                    Text(
                        text = "MUA NGAY",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
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
                    containerColor = if (data.visuals.message.contains("Lỗi")) Color(0xFFFFCDD2) else Color.White,
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
                                        bitmap?.let {
                                            if (device != null) {
                                                Image(
                                                    bitmap = it.asImageBitmap(),
                                                    contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                                                    modifier = Modifier
                                                        .size(100.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        } ?: run {
                                            Image(
                                                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                                contentDescription = "Product Image",
                                                modifier = Modifier
                                                    .size(100.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
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
                                                    text = "Tồn kho: ${device.stock}",
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
//                                                val cartNew: CartEntity?
//                                                var isProductFound = false
//
//                                                for (cart in listCart) {
//                                                    if (device != null) {
//                                                        if (device.idDevice == cart.idDevice) {
//                                                            cart.stock += quantity
//                                                            cartViewModel.updateCart(cart)
//                                                            isProductFound = true
//                                                            quantity = 1
//                                                            break
//                                                        }
//                                                    }
//                                                }
//
//                                                if (!isProductFound) {
//                                                    cartNew = CartEntity(
//                                                        idCustomer = idCustomer,
//                                                        idDevice = device!!.idDevice,
//                                                        stock = quantity
//                                                    )
                                                    val addToCart = AddCartRequest(
                                                        customer_id = idCustomer,
                                                        product_id = device!!.idDevice,
                                                        quantity = quantity
                                                    )
//                                                    cartViewModel.addToCart(cartNew)
                                                    cartViewModel.addCart(idCustomer, addToCart)
                                                    quantity = 1
//                                                }
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
                                        elevation = ButtonDefaults.buttonElevation(1.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Thêm vào giỏ hàng",
                                            color = Color.White,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                            DialogType.AddressRequired -> {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = { Text(text = "Thông báo", fontWeight = FontWeight.Bold) },
                                    text = {
                                        Text(
                                            text = "Bạn chưa có địa chỉ giao hàng. Vui lòng thêm địa chỉ để tiếp tục mua sắm.",
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                showDialog = false
                                                navController.navigate("${Screen.Add_Address.route}?idCustomer=${idCustomer}")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF5D9EFF),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(text = "Thêm địa chỉ", fontSize = 16.sp)
                                        }
                                    },
                                    dismissButton = {
                                        Button(
                                            onClick = {
                                                showDialog = false
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.LightGray,
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text(text = "Hủy", fontSize = 16.sp)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            DialogType.BuyNow -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row {
                                        bitmap?.let {
                                            if (device != null) {
                                                Image(
                                                    bitmap = it.asImageBitmap(),
                                                    contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                                                    modifier = Modifier
                                                        .size(100.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        } ?: run {
                                            Image(
                                                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                                contentDescription = "Product Image",
                                                modifier = Modifier
                                                    .size(100.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
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
                                                    text = "Tồn kho: ${device.stock}",
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
                                                if (buyNowQuantity > 1) buyNowQuantity--
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Remove,
                                                    contentDescription = "Trừ"
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "$buyNowQuantity")
                                            Spacer(modifier = Modifier.width(8.dp))
                                            IconButton(onClick = {
                                                buyNowQuantity++
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
                                            } else if (isLoadingAddress) {
                                                snackbarMessage.value = "Đang tải thông tin địa chỉ..."
                                                showSnackbar.value = true
                                            } else if (addressViewModel.errorMessage != null) {
                                                snackbarMessage.value = addressViewModel.errorMessage ?: "Lỗi không xác định"
                                                showSnackbar.value = true
                                            } else if (addressViewModel.addressDatas?.address_books?.isEmpty() == true) {
                                                showDialog = true
                                                dialogType = DialogType.AddressRequired
                                            } else {
                                                selectedProducts.clear()
                                                selectedProducts.add(Triple(device!!.idDevice, buyNowQuantity, 0))
                                                val totalPrice = device.sellingPrice * buyNowQuantity
                                                val selectedProductsString = selectedProducts.joinToString(",") { "${it.first}:${it.second}:${it.third}" }
                                                navController.navigate(
                                                    Screen.Check_Out.route +
                                                            "?selectedProducts=$selectedProductsString" +
                                                            "&tongtien=$totalPrice" +
                                                            "&username=$username" + "&id=$idCustomer&password=$password"
                                                )
                                                snackbarMessage.value = "Đã chọn sản phẩm để mua ngay!"
                                                showSnackbar.value = true
                                                buyNowQuantity = 1
                                                showDialog = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Red,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(5.dp),
                                        elevation = ButtonDefaults.buttonElevation(1.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Mua ngay",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
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
                    .padding(padding).padding(horizontal = 12.dp),
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
                                    contentDescription = device.name,
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
                                    .height(250.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                bitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                                        modifier = Modifier
                                            .size(250.dp),
                                        contentScale = ContentScale.Crop
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
                                    if (idCustomer == null) {
                                        navController.navigate(Screen.LoginScreen.route)
                                    } else if (!isLoading) {
                                        isLoading = true // Bắt đầu tải
                                        if (!isFavorite) {
                                            val likedNew = AddLikedRequest(
                                                customer_id = idCustomer,
                                                product_id = device.idDevice
                                            )
                                            likedViewModel.addLiked(likedNew)
                                            snackbarMessage.value = "Thêm vào yêu thích thành công!"
                                            isFavorite = true // Cập nhật cục bộ
                                        } else {
                                            val likedItem =
                                                listLiked.find { it.product_id == device.idDevice }
                                            likedItem?.id?.let { likedId ->
                                                likedViewModel.deleteLiked(
                                                    idCustomer,
                                                    likedId
                                                ) // Pass liked_id to delete
                                            }
                                            snackbarMessage.value = "Xóa khỏi yêu thích!"
                                            isFavorite = false // Cập nhật cục bộ
                                        }
                                        // Làm mới danh sách yêu thích
                                        likedViewModel.getLikedByIdCustomer(idCustomer)
                                        showSnackbar.value = true
                                        isLoading = false // Kết thúc tải
                                    }
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
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Mô tả sản phẩm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = device.descriptionNormal,
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
                            .padding(horizontal = 20.dp, vertical = 8.dp)
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
                                fontSize = 16.sp, // Giảm nhẹ để cân bằng giao diện
                                color = Color(0xFF5D9EFF),
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.Product_Reviews.route + "?idDevice=${id}")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            for (i in 1..5) {
                                Text(
                                    text = if (i <= averageRating) "★" else "☆",
                                    fontSize = 20.sp,
                                    color = if (i <= averageRating) Color(0xFFFBC02D) else Color(0xFFFBC02D)
                                )
                            }
                            Text(
                                text = "${averageRating}/5.0 (${listReview.size} đánh giá)",
                                modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically),
                                color = Color.Gray,
                                fontSize = 14.sp,
                            )
                        }
                        HorizontalDivider()
                    }

                }
                items(listReview.take(2)){
                    CardReview(
                        review = it,
                        onClick = {
                            navController.navigate(Screen.Product_Reviews.route + "?idDevice=${it.idDevice}")
                        }
                    )
                }
                item {
                    HorizontalDivider()
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
                                    password,
                                    deviceViewModel = deviceViewModel,
                                    navController
                                )
                            }
                            else{
                                CardDevice(device = it,
                                    isFavorite = isFavorite,
                                    null,
                                    null,
                                    password,
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
fun CardReview(review: Reviews, onClick:() -> Unit){
    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    // Tải hình ảnh
    LaunchedEffect(review) {
        bitmap = review.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }
    // Lấy thông tin customer từ review
    val customerName = "${review.surname} ${review.lastname}".trim()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    )
    {
        Spacer(modifier = Modifier.height(4.dp))
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5D9EFF)) // Màu nền xanh
                ) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = customerName.ifEmpty { "Hình ảnh sản phẩm" },
                            modifier = Modifier
                                .size(28.dp),
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .size(28.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = customerName.ifEmpty { "Khách hàng ẩn danh" },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        modifier = Modifier.size(16.dp),
                        tint = if (index < review.rating) Color(0xFFFBC02D)
                        else Color(0xFFE0E0E0)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            review.comment?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                )
            }
            Text(
                text = formatDateTimeZone(review.created_at),
                fontSize = 12.sp,
                color = Color.LightGray
            )
        }
    }
}