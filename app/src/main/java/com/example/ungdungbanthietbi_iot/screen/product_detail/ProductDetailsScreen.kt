package com.example.ungdungbanthietbi_iot.screen.product_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.cart.Cart
import com.example.ungdungbanthietbi_iot.data.cart.CartViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.data.device.Device
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.image_device.ImageViewModel
import com.example.ungdungbanthietbi_iot.data.liked.Liked
import com.example.ungdungbanthietbi_iot.data.liked.LikedViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.Review
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.screen.home.CardAllDevice
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
fun ProductDetailsScreen(
    navController: NavController,
    id:String,
    idCustomer:String?,
    username:String?,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    reviewViewModel: ReviewViewModel
) {
    var device:Device by remember {
        mutableStateOf(Device (0, "", "", "","", "", 0.0, 0, "", "", 0,0))
    }
    deviceViewModel.getDeviceBySlug(id)
    deviceViewModel.getAllDevice()
    var listAllDevice : List<Device> = deviceViewModel.listAllDevice
    device = deviceViewModel.device

    val customerViewModel: CustomerViewModel = viewModel()
    val customer = customerViewModel.customer
    if(idCustomer != null){
        LaunchedEffect (idCustomer){
            customerViewModel.getCustomerById(idCustomer)
        }
    }

    val likedViewModel:LikedViewModel = viewModel()
    val listLiked = likedViewModel.listLiked

    val listImage = imageViewModel.listImage
    LaunchedEffect(id) {
        imageViewModel.getImageByIdDevice(id)
    }

    val listReview = reviewViewModel.listReview
    LaunchedEffect(id) {
        reviewViewModel.getReviewByIdDevice(id)
    }

    val accountViewModel: AccountViewModel = viewModel()
    val account = accountViewModel.account

    if(username != null){
        accountViewModel.getUserByUsername(username)
    }
    //format giá sản phẩm
    val formatter = DecimalFormat("#,###,###")
    val formattedPrice = formatter.format(device.sellingPrice)

    var currentIndex by remember { mutableStateOf(0) }
    // Tự động chuyển hình sau mỗi 3 giây
    LaunchedEffect(key1 = currentIndex, key2 = listImage.size) {
        if (listImage.isNotEmpty()) {
            delay(3000)
            currentIndex = (currentIndex + 1) % listImage.size
        }
    }

    val cartViewModel:CartViewModel = viewModel()
    val listCart = cartViewModel.listCart
    LaunchedEffect(idCustomer) {
        if(idCustomer!=null){
            cartViewModel.getCartByIdCustomer(idCustomer)
            likedViewModel.getLikedByIdCustomer(idCustomer)
        }
    }

    // Biến trạng thái để sản phẩm yêu thích không
    var isFavorite by remember { mutableStateOf(false) }
    // Biến lưu trữ giá trị đánh giá
    var averageRating = if (listReview.isNotEmpty()) {
        val avg = listReview.map { it.rating }.average() // Tính trung bình cộng
        // Làm tròn tới 1 chữ số thập phân
        (avg * 10.0).roundToInt() / 10.0
    } else {
        0.0 // Giá trị mặc định nếu danh sách rỗng
    }

    //Lưu thông tin sản phẩm để truyền qua màn hình thanh toán
    val selectedProducts = remember { mutableListOf<Triple<Int, Int, Int>>() }

    // Biến lưu trữ giá trị checked
    var isChecked by remember { mutableStateOf(false) }
    // Biến lưu trữ trạng thái hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }
    // Biến lưu trữ số lượng sản phẩm
    var quantity by remember { mutableStateOf(1) }

    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    val showSnackbarF = remember { mutableStateOf(false) }
    val snackbarMessageF = remember { mutableStateOf("") }
    // Màn hình chính
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
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
                                navController.navigate(Screen.Search_Screen.route + "?username=${accountViewModel.username}")
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

                        // Icon Giỏ hàng
                        IconButton(onClick = {
                            if(account == null){
                                navController.navigate(Screen.LoginScreen.route)
                            }
                            else{
                                navController.navigate(Screen.Cart_Screen.route +"?idCustomer=${account.idPerson}&username=${account.username}")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Giỏ hàng",
                                tint = Color.White
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
                    //.border(1.dp, Color.Black)
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        //navController.navigate(Screen.ContactScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = "Liên hệ",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    IconButton(onClick = {
                        if(idCustomer != null){
                            showDialog = true
                        }
                        else{
                            navController.navigate(Screen.LoginScreen.route)
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Thêm vào giỏ hàng",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Dialog hiển thị khi người dùng nhấn nút
                    if (showDialog) {
                        Dialog(onDismissRequest = { showDialog = false }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row() {
                                        AsyncImage(
                                            model = device.image,
                                            contentDescription = null,
                                            modifier = Modifier.size(100.dp)
                                        )
                                        Column(){
                                            Text(
                                                text = "${formattedPrice} VNĐ",
                                                modifier = Modifier.padding(start = 16.dp),
                                                color = Color.Red
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "${100 - quantity}",
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
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
                                        // Chọn số lượng sản phẩm
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                if (quantity > 1) quantity--
                                            }) {
                                                Icon(imageVector = Icons.Filled.Remove,
                                                    contentDescription = "Trừ"
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "$quantity")
                                            Spacer(modifier = Modifier.width(8.dp))
                                            IconButton(onClick = {
                                                quantity++
                                            }) {
                                                Icon(imageVector = Icons.Filled.Add,
                                                    contentDescription = "Trừ"
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Nút xác nhận
                                    Button(
                                        onClick = {
                                            if(idCustomer == null){
                                                navController.navigate(Screen.LoginScreen.route)
                                            }
                                            else{
                                                var cartNew: Cart? = null
                                                var isProductFound = false

                                                for(cart in listCart){
                                                    if(device.idDevice == cart.idDevice){
                                                        cart.stock += quantity
                                                        cartViewModel.updateCart(cart)
                                                        isProductFound = true
                                                        break
                                                    }
                                                }

                                                // Nếu sản phẩm không tìm thấy trong giỏ hàng thì thêm mới
                                                if(!isProductFound){
                                                    cartNew = Cart(0, idCustomer, device.idDevice,  quantity)
                                                    cartViewModel.addToCart(cartNew)
                                                }

                                                // Làm mới danh sách giỏ hàng
                                                cartViewModel.getCartByIdCustomer(idCustomer)
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
                                        Text("Thêm vào giỏ hàng",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (showSnackbar.value) {
                        LaunchedEffect(Unit) {
                            delay(3000) // Chờ 3000ms (3 giây)
                            showSnackbar.value = false // Đặt giá trị để tắt Snackbar
                        }
                        Snackbar(
                            modifier = Modifier.padding(16.dp),
                            //containerColor = Color.LightGray,
                            action = {
                                TextButton(onClick = { showSnackbar.value = false }) {
                                    Text(text = "Đóng", color = Color.White)
                                }
                            }
                        ) {
                            Text(snackbarMessage.value)
                        }
                    }
                    Button(
                        onClick = {
                            //navController.navigate(Screen.Check_Out.route)
                        },
                        modifier = Modifier.width(240.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF),
                        ),
                    ){
                        Text(
                            text = "Mua Ngay",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        )
        {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                change.consume() // Tiêu thụ sự kiện kéo
                                if (dragAmount > 0 && listImage.isNotEmpty()) { // Trượt sang phải
                                    currentIndex =
                                        if (currentIndex == 0) listImage.size - 1 else currentIndex - 1
                                } else if (listImage.isNotEmpty()) { // Trượt sang trái
                                    currentIndex = (currentIndex + 1) % listImage.size
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (listImage.isNotEmpty()) {
                        SlideImage(painter = rememberImagePainter(data = listImage[currentIndex].image))
                    } else {
                        Text(text = "No slides available", fontSize = 16.sp, modifier = Modifier.padding(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Indicator (dấu chấm dưới)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listImage.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(17.dp) // Tăng kích thước của các chấm tròn
                                .padding(4.dp)
                                .background(
                                    color = if (index == currentIndex) Color(0xFF5D9EFF) else Color.LightGray,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            item {
                // Chi tiết sản phẩm
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Giá ${formattedPrice}đ",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        var check  by remember { mutableStateOf(false) }
                        LaunchedEffect(listLiked) {
                            check = listLiked.any { it.idDevice == device.idDevice }
                        }
                        IconButton(onClick = {
                            if(idCustomer == null){
                                navController.navigate(Screen.LoginScreen.route)
                            }
                            else{
                                var likedNew:Liked? = null
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
                                    showSnackbarF.value = true
                                    snackbarMessageF.value = "Thêm vào yêu thích thành công!"
                                }
                                else{
                                    likedViewModel.deleteLikedByCustomer(idCustomer, device.idDevice)
                                    check = false
                                    showSnackbarF.value = true
                                    snackbarMessageF.value = "Xóa khỏi yêu thích!"
                                }
                                // Làm mới danh sách
                                likedViewModel.getLikedByIdCustomer(idCustomer)
                            }
                        }) {
                            Icon(imageVector = if(check) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "",
                                tint = Color.Red,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    if (showSnackbarF.value) {
                        LaunchedEffect(Unit) {
                            delay(3000) // Chờ 3000ms (3 giây)
                            showSnackbarF.value = false // Đặt giá trị để tắt Snackbar
                        }
                        Snackbar(
                            modifier = Modifier.padding(16.dp),
                            action = {
                                TextButton(onClick = { showSnackbarF.value = false }) {
                                    Text(text = "Đóng", color = Color.White)
                                }
                            }
                        ) {
                            Text(snackbarMessageF.value)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
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
                            fontSize = 20.sp
                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Mô tả sản phẩm",
                        color = Color(0xFF5D9EFF),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = device.descriptionNormal,
                        color = Color.Black,
                    )
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
                            color = Color(0xFF085979),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "Xem tất cả",
                            color = Color(0xFF085979),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Product_Reviews.route + "?idDevice=${id}")
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.padding(5.dp)
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
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Gray,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            items(listReview.take(2)){
                CardReview(review = it, isChecked = isChecked, onlick = {
                    navController.navigate(Screen.Product_Reviews.route + "?idDevice=${it.idDevice}")
                },
                    id.toInt()
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Gợi ý sản phẩm
                Text(
                    "Gợi ý sản phẩm",
                    color = Color(0xFF085979),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items(listAllDevice){
                        if(account != null){
                            CardAllDevice(device = it,
                                isFavorite = isFavorite,
                                account.idPerson,
                                account.username,
                                navController
                            )
                        }
                        else{
                            CardAllDevice(device = it,
                                isFavorite = isFavorite,
                                null,
                                username,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardReview(review: Review, isChecked:Boolean, onlick:() -> Unit, id:Int){
    var currentChecked by remember { mutableStateOf(isChecked) }

    val customerViewModel:CustomerViewModel = viewModel()
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
        Divider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, top = 5.dp)) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "avt",
                modifier = Modifier.size(30.dp)
            )
            Column() {
                for (customer in listCustomer){
                    if(customer.id == review.idCustomer){
                        Text(text = "${customer.surname} ${customer.lastName}")
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentChecked = !currentChecked
                },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = if (currentChecked) "check" else "not check",
                        tint = if(currentChecked) Color(0xFFFBC02D) else Color.Gray
                    )
                }
                Text("Hữu ích", modifier = Modifier.padding(end = 5.dp, start = 5.dp))
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