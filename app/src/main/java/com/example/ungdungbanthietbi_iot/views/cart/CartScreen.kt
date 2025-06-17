package com.example.ungdungbanthietbi_iot.views.cart

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.api.AddCartRequest
import com.example.ungdungbanthietbi_iot.api.ProductInCart
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CartViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTienInt
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel

/** Giao diện màn hình giỏ hàng (CartScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 05/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input:
 *
 * Output: Hiển thị màn hình giỏ hàng, có xử lý check chọn nhiều sản phẩm để mua hoặc xóa khỏi giỏ hàng và hiển thị tổng tiền.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
// Enum để xác định loại hành động xóa
enum class DeleteAction {
    SINGLE, // Xóa một sản phẩm
    ALL     // Xóa tất cả sản phẩm được chọn
}

@Composable
fun CartItem(
    product: ProductInCart,
    idCustomer: String,
    username: String,
    selectedItems: MutableMap<String, Boolean>,
    selectedProducts: MutableList<Triple<String, Int, Int>>,
    cartViewModel: CartViewModel,
    navController: NavController,
    onCalculateTotalPrice: () -> Unit,
    onShowDeleteDialog: (String, DeleteAction) -> Unit
) {

    // Lấy thông tin cấu hình màn hình
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isSmallScreen = screenWidth < 360.dp
    val isLargeScreen = screenWidth > 600.dp

    // Điều chỉnh kích thước dựa trên màn hình
    val imageSize = when {
        isLargeScreen -> 180.dp
        isSmallScreen -> 100.dp
        else -> 150.dp
    }
    val fontSizeTitle = when {
        isLargeScreen -> 18.sp
        isSmallScreen -> 14.sp
        else -> 16.sp
    }
    val fontSizePrice = when {
        isLargeScreen -> 16.sp
        isSmallScreen -> 12.sp
        else -> 14.sp
    }
    val buttonSize = when {
        isLargeScreen -> 40.dp
        isSmallScreen -> 24.dp
        else -> 32.dp
    }
    val quantityButtonSize = when {
        isLargeScreen -> 36.dp
        isSmallScreen -> 24.dp
        else -> 28.dp
    }
    val paddingValue = when {
        isLargeScreen -> 8.dp
        isSmallScreen -> 4.dp
        else -> 6.dp
    }

    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var soLuong by remember { mutableIntStateOf(product.quantity) }

    // Tải hình ảnh
    LaunchedEffect(product) {
        bitmap = product.image?.let { deviceViewModel.getDeviceImageBitmapImage(it) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValue)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController.navigate(
                Screen.ProductDetailsScreen.route +
                        "?id=${product.id}&idCustomer=${idCustomer}&username=${username}"
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValue),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Checkbox chọn sản phẩm
            Checkbox(
                checked = selectedItems[product.id] == true,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF5D9EFF),
                    uncheckedColor = Color.Gray,
                ),
                onCheckedChange = { isChecked ->
                    selectedItems[product.id] = isChecked
                    if (isChecked) {
                        selectedProducts.removeAll { it.first == product.id }
                        selectedProducts.add(Triple(product.id, soLuong, product.id.toInt()))
                    } else {
                        selectedProducts.removeAll { it.first == product.id }
                    }
                    onCalculateTotalPrice()
                },
                modifier = Modifier.size(buttonSize)
            )
            if(product.image?.isEmpty() == true){
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(imageSize)
                        .padding(start = paddingValue),
                    contentScale = ContentScale.Crop
                )
            }
            else {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = product.name.ifEmpty { "Hình ảnh sản phẩm" },
                        modifier = Modifier
                            .size(imageSize)
                            .padding(start = paddingValue),
                        contentScale = ContentScale.Fit
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Cột chứa thông tin sản phẩm
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = paddingValue)
            ) {
                // Tên sản phẩm
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSizeTitle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(paddingValue))

                // Giá sản phẩm
                Text(
                    text = formatGiaTienInt(product.selling_price),
                    color = Color.Red,
                    fontSize = fontSizePrice,
                    fontWeight = FontWeight.SemiBold
                )

                // Bộ điều khiển số lượng (nằm dưới giá)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(end = paddingValue)
                ) {
                    // Nút giảm số lượng
                    IconButton(
                        onClick = {
                            if (soLuong > 1) {
                                soLuong--
                                //product.quantity = soLuong
                                val updateQuantity = AddCartRequest(
                                    customer_id = idCustomer,
                                    product_id = product.id,
                                    quantity = soLuong
                                )
                                cartViewModel.updateQuantity(updateQuantity)
                                val index = selectedProducts.indexOfFirst { it.first == product.id }
                                if (index != -1) {
                                    selectedProducts[index] = Triple(product.id, soLuong, product.id.toInt())
                                } else if (selectedItems[product.id] == true) {
                                    selectedProducts.add(Triple(product.id, soLuong, product.id.toInt()))
                                }
                                onCalculateTotalPrice()
                            }
                        },
                        modifier = Modifier
                            .size(quantityButtonSize)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease Quantity",
                            modifier = Modifier.size(quantityButtonSize * 0.7f),
                        )
                    }

                    // Hiển thị số lượng
                    AnimatedContent(
                        targetState = soLuong,
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                    (slideOutVertically { height -> -height } + fadeOut())
                        }, label = ""
                    ) { targetCount ->
                        Text(
                            text = targetCount.toString(),
                            modifier = Modifier
                                .padding(horizontal = paddingValue)
                                .width(quantityButtonSize)
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .border(1.dp, Color.Transparent, RoundedCornerShape(4.dp)),
                            fontSize = fontSizePrice,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }

                    // Nút tăng số lượng
                    IconButton(
                        onClick = {
                            if (soLuong < 500) {
                                soLuong++
                                //product.quantity = soLuong
                                val updateQuantity = AddCartRequest(
                                    customer_id = idCustomer,
                                    product_id = product.id,
                                    quantity = soLuong
                                )
                                cartViewModel.updateQuantity(updateQuantity)
                                val index = selectedProducts.indexOfFirst { it.first == product.id }
                                if (index != -1) {
                                    selectedProducts[index] = Triple(product.id, soLuong, product.id.toInt())
                                }else if (selectedItems[product.id] == true) {
                                    selectedProducts.add(Triple(product.id, soLuong, product.id.toInt()))
                                }
                                onCalculateTotalPrice()
                            }
                        },
                        modifier = Modifier
                            .size(quantityButtonSize)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase Quantity",
                            modifier = Modifier.size(quantityButtonSize * 0.7f),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    idCustomer: String,
    username: String,
    password: String
) {
    val cartViewModel: CartViewModel = viewModel()
    val addressViewModel: AddressViewModel = viewModel()

    // Lấy danh sách giỏ hàng và sản phẩm
    val uiState by cartViewModel.uiState.collectAsState()
    val listProductCart by cartViewModel.listProductCart.collectAsState()
    val listAddress = addressViewModel.listAddress

    // Biến lưu tổng tiền
    var totalPrice by remember { mutableIntStateOf(0) }
    // Biến lưu trạng thái checkbox của từng sản phẩm
    val selectedItems = remember { mutableStateMapOf<String, Boolean>() }
    // Lưu thông tin sản phẩm để truyền qua màn hình thanh toán
    val selectedProducts = remember { mutableListOf<Triple<String, Int, Int>>() }

    var showDialog by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }

    // Hàm tính tổng tiền
    fun calculateTotalPrice(products: List<ProductInCart>) {
        totalPrice = if (products.isEmpty()) {
            0
        } else {
            products.filter { selectedItems[it.id] == true }.sumOf { product ->
                product.selling_price * product.quantity
            }
        }
    }

    // Lấy dữ liệu và tính tổng tiền ban đầu
    LaunchedEffect(idCustomer) {
        cartViewModel.getCartProducts(idCustomer)
        addressViewModel.getAddressDefault(idCustomer)
    }

    // Khởi tạo selectedItems khi listCart thay đổi
    LaunchedEffect(listProductCart) {
        selectedItems.clear()
        listProductCart.forEach { selectedItems[it.id] = false }
        selectedProducts.clear()
        calculateTotalPrice(listProductCart)
    }

    // Tính tổng tiền khi dữ liệu giỏ hàng hoặc sản phẩm thay đổi
    LaunchedEffect(listProductCart) {
        calculateTotalPrice(listProductCart) // Tính tổng tiền khi dữ liệu thay đổi
    }

    // Biến trạng thái cho dialog xác nhận
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteAction by remember { mutableStateOf<DeleteAction?>(null) }
    var cartIdToDelete by remember { mutableStateOf<String?>(null) } // Lưu id của sản phẩm cần xóa (cho xóa một sản phẩm)

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Giỏ hàng (${listProductCart.size})",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    // Xóa hết
                    TextButton(
                        onClick = {
                            if (selectedItems.values.any { it }) {
                                deleteAction = DeleteAction.ALL // Đặt hành động là xóa tất cả
                                showDeleteDialog = true // Hiển thị dialog xác nhận
                            } else {
                                showDialogDelete = true
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFF5D9EFF)
                        )
                    ) {
                        Text(
                            text = "Xóa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    // Nút quay lại
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedItems.isNotEmpty() && selectedItems.values.all { it } && listProductCart.isNotEmpty(),
                            onCheckedChange = { isChecked ->
                                // Cập nhật trạng thái chọn tất cả sản phẩm
                                listProductCart.forEach { cart ->
                                    selectedItems[cart.id] = isChecked
                                    if (isChecked) {
                                        // Thêm sản phẩm vào danh sách selectedProducts
                                        selectedProducts.add(Triple(cart.id, cart.quantity, cart.id.toInt()))
                                    } else {
                                        // Xóa sản phẩm khỏi danh sách selectedProducts
                                        selectedProducts.removeAll { it.first == cart.id }
                                    }
                                }
                                calculateTotalPrice(listProductCart) // Tính lại tổng tiền
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF5D9EFF),
                                uncheckedColor = Color.Gray
                            )
                        )
                        Text(
                            text = "Tất cả",
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                    // Hiển thị tổng giá thanh toán
                    Text(
                        "Tổng: ${formatGiaTienInt(totalPrice)}",
                        style = TextStyle(color = Color.Red, fontSize = 18.sp)
                    )
                }
                // Nút mua hàng
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (selectedProducts.isEmpty()) {
                            showDialog = true // Hiển thị dialog nếu không có sản phẩm nào được chọn
                        } else if (listAddress.isEmpty()) { // Kiểm tra danh sách địa chỉ rỗng
                            openDialog = true // Hiển thị dialog thông báo thêm địa chỉ
                        } else {
                            val selectedProductsString = selectedProducts.joinToString(",") { "${it.first}:${it.second}:${it.third}" }
                            navController.navigate(Screen.Check_Out.route + "?selectedProducts=${selectedProductsString}&tongtien=${totalPrice}&username=${username}&id=$idCustomer&password=$password")
                        }
                    },
                    shape = RoundedCornerShape(5.dp),
                    elevation = ButtonDefaults.buttonElevation(1.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    )
                ) {
                    Text(
                        "MUA HÀNG",
                        fontSize = 20.sp
                    )
                }
                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = { openDialog = false }, // Đóng khi nhấn ngoài dialog
                        containerColor = Color.White,
                        title = {
                            Text(
                                text = "Thông báo"
                            )
                        },
                        text = {
                            Text(
                                text = "Bạn chưa có địa chỉ giao hàng.\n" +
                                        "Vui lòng thêm địa chỉ giao hàng!"
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog = false
                                    navController.navigate("${Screen.Add_Address.route}?idCustomer=${idCustomer}")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5D9EFF)
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Thêm địa chỉ",
                                    fontSize = 18.sp
                                )
                            }
                        },
                    )
                }
            }
        }
    ) { padding ->
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Thông báo") },
                text = { Text(text = "Vui lòng chọn sản phẩm để mua.") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("OK")
                    }
                },
                containerColor = Color.White
            )
        }
        if (showDialogDelete) {
            AlertDialog(
                onDismissRequest = { showDialogDelete = false },
                containerColor = Color.White,
                title = { Text(text = "Thông báo") },
                text = { Text(text = "Vui lòng chọn sản phẩm để xóa.") },
                confirmButton = {
                    Button(
                        onClick = { showDialogDelete = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("OK")
                    }
                }
            )
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    deleteAction = null
                    cartIdToDelete = null
                },
                containerColor = Color.White,
                title = { Text(text = "Xác nhận xóa") },
                text = {
                    Text(
                        text = when (deleteAction) {
                            DeleteAction.SINGLE -> "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?"
                            DeleteAction.ALL -> "Bạn có chắc chắn muốn xóa sản phẩm được chọn khỏi giỏ hàng?"
                            null -> ""
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            when (deleteAction) {
                                DeleteAction.SINGLE -> {
                                    cartIdToDelete?.let { cartId ->
                                        cartViewModel.removeCart(idCustomer, cartId)
                                        calculateTotalPrice(listProductCart)
                                    }
                                }
                                DeleteAction.ALL -> {
                                    val selectedIds = selectedItems.filterValues { it }.keys.toList()
                                    cartViewModel.removeAllSelectedCarts(selectedIds, idCustomer)
                                    selectedItems.clear()
                                    selectedProducts.clear()
                                    calculateTotalPrice(listProductCart)
                                }
                                null -> {}
                            }
                            showDeleteDialog = false
                            deleteAction = null
                            cartIdToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Xóa")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            deleteAction = null
                            cartIdToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Hủy")
                    }
                }
            )
        }
        // Danh sách sản phẩm
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            when (uiState) {
                is CartViewModel.UiState.Initial -> {
                    item {
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
                is CartViewModel.UiState.Loading -> {
                    item {
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
                is CartViewModel.UiState.SuccessGet, is CartViewModel.UiState.SuccessAdd -> {
                    val response = (uiState as? CartViewModel.UiState.SuccessGet)?.response
                        ?: (uiState as? CartViewModel.UiState.SuccessGet)?.response
                    if (response?.data.isNullOrEmpty()) {
                        item {
                            Text(
                                text = "Giỏ hàng trống!",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    } else {
                        items(response?.data ?: emptyList()) { product ->
                            CartItem(
                                product = product,
                                idCustomer = idCustomer,
                                username = username,
                                selectedItems = selectedItems,
                                selectedProducts = selectedProducts,
                                cartViewModel = cartViewModel,
                                navController = navController,
                                onCalculateTotalPrice = { calculateTotalPrice(listProductCart) },
                                onShowDeleteDialog = { cartId, action ->
                                    cartIdToDelete = cartId
                                    deleteAction = action
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
                is CartViewModel.UiState.Error -> {}
            }
        }
    }
}