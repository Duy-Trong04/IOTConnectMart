package com.example.ungdungbanthietbi_iot.views.check_out

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.MainActivity
import com.example.ungdungbanthietbi_iot.api.PaymentRequest
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.models.CheckoutRequest
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CartViewModel
import com.example.ungdungbanthietbi_iot.models.Device
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.OrderViewModel
import com.example.ungdungbanthietbi_iot.models.OrderRequest
import com.example.ungdungbanthietbi_iot.models.Payment
import com.example.ungdungbanthietbi_iot.models.Product
import com.example.ungdungbanthietbi_iot.models.Shipping
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.utils.formatGiaTien
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestampEX
import com.example.ungdungbanthietbi_iot.utils.isNetworkAvailable
import com.example.ungdungbanthietbi_iot.viewModels.CheckoutState
import com.example.ungdungbanthietbi_iot.viewModels.CustomerState
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.viewModels.VNPayViewModel
import kotlinx.coroutines.flow.first
import java.net.URLEncoder


/** Giao diện màn hình thanh toán (CheckoutScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 06/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Hiển thị màn hình thanh toán, có xu lý chọn phương thức thanh toán hoặc hiện thị 1 hoặc nhiều sản phẩm, tổng thanh toán
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    selectedProducts: List<Triple<String, Int, Int>>,
    tongtien: Double,
    username: String,
    idCustomer: String,
    token: String
) {

    val context = LocalContext.current
    val deviceViewModel: DeviceViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val addressViewModel: AddressViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()

    val listDevice by deviceViewModel.listDevice.collectAsState(initial = emptyList())
    var selectedPaymentMethod by remember { mutableStateOf("Thanh toán khi nhận hàng (COD)") }
    val address = addressViewModel.address
    val isLoadingAddress by addressViewModel.isLoading.collectAsState()
    val errorMessage = addressViewModel.errorMessage
    val amount by remember { mutableDoubleStateOf(tongtien + 30000) }
    // State để theo dõi việc loading sản phẩm
    var isLoadingProducts by remember { mutableStateOf(true) }
    // State để kích hoạt cuộc gọi API
    var checkoutRequest by remember { mutableStateOf<CheckoutRequest?>(null) }
    val customerState by customerViewModel.customerState.collectAsState()
    // Lấy selectedAddressId từ savedStateHandle
    val selectedAddressId by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>("selectedAddressId", null)
        ?.collectAsState() ?: remember { mutableStateOf(null) }

    // Gọi API để lấy thông tin khách hàng
    LaunchedEffect(idCustomer) {
        if (idCustomer.isNotBlank()) {
            customerViewModel.getCustomerById(idCustomer)
        }
    }
    // Lấy địa chỉ: ưu tiên địa chỉ được chọn, nếu không thì lấy mặc định
    LaunchedEffect(username, selectedAddressId) {
        Log.d("CheckoutScreen", "idCustomer: $idCustomer, selectedAddressId: $selectedAddressId")
        if (idCustomer.isEmpty()) {
            addressViewModel.updateErrorMessage("ID khách hàng không hợp lệ")
            //addressViewModel.isLoading = false
            Log.d("CheckoutScreen", "Đã đặt errorMessage và isLoading = false do idCustomer rỗng")
            return@LaunchedEffect
        }
        if (selectedAddressId != null) {
            addressViewModel.getAddressById(selectedAddressId!!)
            Log.d("CheckoutScreen", "Gọi getAddressById với id: $selectedAddressId")
        } else {
            addressViewModel.getAddressDefault(idCustomer)
            Log.d("CheckoutScreen", "Gọi getAddressDefault với customerId: $idCustomer")
        }
    }
    // Lấy thông tin sản phẩm
    val loadedDeviceIds = remember { mutableStateListOf<String>() }
    LaunchedEffect(selectedProducts) {
        Log.d("CheckoutScreen", "Danh sách sản phẩm đã chọn: $selectedProducts")
        deviceViewModel.clearDevices()
        selectedProducts.forEach { triple ->
            Log.d("CheckoutScreen", "Sản phẩm: idDevice=${triple.first}, số lượng=${triple.second}, cartId=${triple.third}")
            val deviceId = triple.first
            if (!loadedDeviceIds.contains(deviceId)) {
                deviceViewModel.getDeviceCheckOut(deviceId)
                loadedDeviceIds.add(deviceId)
            }
        }
    }
    // Theo dõi khi nào các sản phẩm đã load xong
    LaunchedEffect(listDevice, selectedProducts) {
        if (selectedProducts.isNotEmpty()) {
            val loadedDeviceCount = listDevice.distinctBy { it.idDevice }.size
            val expectedDeviceCount = selectedProducts.distinctBy { it.first }.size

            if (loadedDeviceCount >= expectedDeviceCount) {
                isLoadingProducts = false
                Log.d("CheckoutScreen", "Đã load xong tất cả sản phẩm: $loadedDeviceCount/$expectedDeviceCount")
            }
        } else {
            isLoadingProducts = false
        }
    }

//    // Theo dõi trạng thái API
//    LaunchedEffect(Unit) {
//        orderViewModel.checkoutState.collect { state ->
//            when (state) {
//                is CheckoutState.Success -> {
//                    Log.d("CheckoutScreen", "Dữ liệu đơn hàng: ${state.orderData}")
//
//                    // Xóa giỏ hàng
//                    selectedProducts.forEach { triple ->
//                        if (triple.third != 0) {
//                            cartViewModel.deleteCart(triple.third, idCustomer)
//                        }else {
//                            Log.e("CheckoutScreen", "ID giỏ hàng không hợp lệ cho sản phẩm ID: ${triple.first}")
//                        }
//                    }
//
//                    // Chuyển hướng đến màn hình thành công
//                    val orderData = state.orderData
//                    val encodedOrderId = orderData.orderId.let { URLEncoder.encode(it, "UTF-8") } ?: ""
//                    val encodedCreatedAt = orderData.createdAt?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
//                    navController.navigate(
//                        "${Screen.CheckOutSuccess.route}?" +
//                                "username=$username&" +
//                                "id=$idCustomer&" +
//                                "orderId=$encodedOrderId&" +
//                                "totalMoney=${orderData.totalMoney}&" +
//                                "createdAt=$encodedCreatedAt&password=$password"
//                    ) {
//                        popUpTo(0) { inclusive = true }
//                    }
//                }
//                is CheckoutState.Error -> {
//                    // Hiển thị thông báo lỗi
//                    //noticeViewModel.setNotice(Notice("Lỗi", state.message, true))
//                    Log.e("CheckoutScreen", "API Error: ${state.message}")
//                }
//                is CheckoutState.Loading -> {
//                    Log.d("CheckoutScreen", "Đang tạo đơn hàng...")
//                }
//                is CheckoutState.Idle -> {
//                    // Không làm gì khi ở trạng thái Idle
//                }
//            }
//        }
//    }
    val vnPayViewModel: VNPayViewModel = viewModel()
    val paymentUrl by vnPayViewModel.paymentUrl.collectAsState()
    val isLoadingPayment by vnPayViewModel.isLoading.collectAsState()
    // Mở Chrome Custom Tabs khi có paymentUrl
    LaunchedEffect(paymentUrl) {
        if (paymentUrl != null && selectedPaymentMethod == "VNPay") {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                .setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
                .setExitAnimations(context, android.R.anim.fade_out, android.R.anim.fade_in)
                .build()
            try {
                customTabsIntent.launchUrl(context, Uri.parse(paymentUrl))
            } catch (e: Exception) {
                Log.e("CheckoutScreen", "Lỗi mở Custom Tabs: ${e.message}")
                addressViewModel.updateErrorMessage("Không thể mở trình duyệt. Vui lòng kiểm tra Chrome.")
            }
        }
    }
    // Lấy paymentStatus từ MainActivity
    val mainActivity = context as? MainActivity
    val paymentStatus by mainActivity?.paymentStatus?.collectAsState() ?: remember { mutableStateOf<String?>(null) }

    // Kiểm tra DataStore khi khởi tạo
    LaunchedEffect(Unit) {
        val preferences = context.dataStore.data.first()
        val status = preferences[stringPreferencesKey("payment_status")]
        if (status != null && checkoutRequest != null) {
            Log.d("CheckoutScreen", "Payment Status from DataStore: $status")
            when (status) {
                "success" -> {
                    Log.d("CheckoutScreen", "Gọi createOrder từ DataStore với request: $checkoutRequest")
                    orderViewModel.createOrder(checkoutRequest!!)
                    vnPayViewModel.clearPaymentUrl()
                }
                "fail" -> {
                    addressViewModel.updateErrorMessage("Thanh toán thất bại.")
                    vnPayViewModel.clearPaymentUrl()
                }
                "invalid" -> {
                    addressViewModel.updateErrorMessage("Chữ ký không hợp lệ.")
                    vnPayViewModel.clearPaymentUrl()
                }
                "cancelled" -> {
                    addressViewModel.updateErrorMessage("Thanh toán bị hủy.")
                    vnPayViewModel.clearPaymentUrl()
                }
                "expired" -> {
                    addressViewModel.updateErrorMessage("Thanh toán hết hạn.")
                    vnPayViewModel.clearPaymentUrl()
                }
            }
            context.dataStore.edit {
                it.remove(stringPreferencesKey("payment_status"))
                it.remove(stringPreferencesKey("checkout_request"))
            }
        }
    }
    // Xử lý paymentStatus
    LaunchedEffect(paymentStatus) {
        paymentStatus?.let { status ->
            Log.d("CheckoutScreen", "Payment Status: $status")
            when (status) {
                "success" -> {
                    checkoutRequest?.let { request ->
                        Log.d("CheckoutScreen", "Gọi createOrder với request: $request")
                        orderViewModel.createOrder(request)
                    } ?: run {
                        Log.e("CheckoutScreen", "checkoutRequest is null")
                        addressViewModel.updateErrorMessage("Không thể tạo đơn hàng: Dữ liệu thanh toán bị thiếu.")
                    }
                    vnPayViewModel.clearPaymentUrl()
                }
                "fail" -> {
                    addressViewModel.updateErrorMessage("Thanh toán thất bại.")
                    vnPayViewModel.clearPaymentUrl()
                }
                "invalid" -> {
                    addressViewModel.updateErrorMessage("Chữ ký không hợp lệ.")
                    vnPayViewModel.clearPaymentUrl()
                }
                "cancelled" -> {
                    addressViewModel.updateErrorMessage("Thanh toán bị hủy.")
                    vnPayViewModel.clearPaymentUrl()
                }
                "expired" -> {
                    addressViewModel.updateErrorMessage("Thanh toán hết hạn.")
                    vnPayViewModel.clearPaymentUrl()
                }
            }
            mainActivity?._paymentStatus?.value = null
        }
    }
    // Xử lý trạng thái API
    LaunchedEffect(paymentStatus) {
        orderViewModel.checkoutState.collect { state ->
            when (state) {
                is CheckoutState.Success -> {
                    Log.d("CheckoutScreen", "Dữ liệu đơn hàng: ${state.orderData}")
                    selectedProducts.forEach { triple ->
                        if (triple.third != 0) {
                            cartViewModel.removeCart(idCustomer, triple.first)
                        } else {
                            Log.e("CheckoutScreen", "ID giỏ hàng không hợp lệ cho sản phẩm ID: ${triple.first}")
                        }
                    }
                    val orderData = state.orderData
                    val encodedOrderId = URLEncoder.encode(orderData.orderId, "UTF-8")
                    val encodedCreatedAt = orderData.createdAt?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
                    // Lưu thông tin đơn hàng vào DataStore
                    context.dataStore.edit {
                        it[stringPreferencesKey("order_id")] = orderData.orderId
                        it[intPreferencesKey("total_money")] = orderData.totalMoney
                        it[stringPreferencesKey("created_at")] = orderData.createdAt ?: ""
                    }
                    // Điều hướng đến CheckOutSuccessScreen
                    if(selectedPaymentMethod == "Thanh toán khi nhận hàng (COD)") {
                        navController.navigate(
                            "${Screen.CheckOutSuccess.route}?" +
                                    "username=$username&" +
                                    "id=$idCustomer&" +
                                    "orderId=$encodedOrderId&" +
                                    "totalMoney=${orderData.totalMoney}&" +
                                    "createdAt=$encodedCreatedAt&token=$token"
                        ) {
                            navController.currentDestination?.let {
                                popUpTo(it.id) {
                                    inclusive = true
                                }
                            }
                            launchSingleTop = true
                        }
                    }
                    // Xóa trạng thái
                    mainActivity?._paymentStatus?.value = null
                    context.dataStore.edit {
                        it.remove(stringPreferencesKey("payment_status"))
                        it.remove(stringPreferencesKey("checkout_request"))
                    }
                }
                is CheckoutState.Error -> {
                    Log.e("CheckoutScreen", "API Error: ${state.message}")
                    addressViewModel.updateErrorMessage("Lỗi khi tạo đơn hàng: ${state.message}")
                }
                is CheckoutState.Loading -> {
                    Log.d("CheckoutScreen", "Đang tạo đơn hàng...")
                }
                is CheckoutState.Idle -> {}
            }
        }
    }
    var email by remember { mutableStateOf("") }
    // Biến để kiểm soát hiển thị popup
    var showEmailVerificationDialog by remember { mutableStateOf(false) }
    if (isLoadingAddress || isLoadingProducts || isLoadingPayment || customerState is CustomerState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF5D9EFF),
            )
        }
    } else if (errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Thanh toán",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF5D9EFF),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 5.dp)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        "Tổng thanh toán: ${formatGiaTien(amount)}",
                        style = TextStyle(color = Color.Red, fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            // Kiểm tra email_verified khi nhấn nút
                            when (val state = customerState) {
                                is CustomerState.Success -> {
                                    if (!state.customer.email_verified) {
                                        email = state.customer.email
                                        showEmailVerificationDialog = true
                                    } else {
                                        if (address != null) {
                                            if (selectedPaymentMethod == "VNPay" && !isNetworkAvailable(
                                                    context
                                                )
                                            ) {
                                                addressViewModel.updateErrorMessage("Không có kết nối internet. Vui lòng kiểm tra và thử lại.")
                                                return@Button
                                            }
                                            Log.d("CheckoutScreen", "ListDevice: $listDevice")
                                            listDevice.forEach { device ->
                                                Log.d(
                                                    "CheckoutScreen",
                                                    "Device ID: ${device.idDevice}, Name: ${device.name}, Price: ${device.sellingPrice}"
                                                )
                                            }
                                            val request = CheckoutRequest(
                                                shipping = Shipping(
                                                    addressType = "saved",
                                                    savedAddressId = address.id.toString(),
                                                    fullName = address.receiver_name,
                                                    phone = address.phone,
                                                    email = state.customer.email,
                                                    address = "${address.detail}, ${address.street}",
                                                    city = address.city,
                                                    district = address.district,
                                                    ward = address.ward,
                                                    shippingMethod = "standard",
                                                    note = ""
                                                ),
                                                payment = Payment(
                                                    paymentMethod = if (selectedPaymentMethod == "Thanh toán khi nhận hàng (COD)") "COD" else "VNPay",
                                                    sameAsShipping = true,
                                                    cardNumber = "",
                                                    cardName = "",
                                                    cardExpiry = "",
                                                    cardCvc = ""
                                                ),
                                                products = selectedProducts.distinctBy { it.first }
                                                    .map { triple ->
                                                        val device =
                                                            listDevice.find { it.idDevice == triple.first }
                                                        Product(
                                                            id = triple.first,
                                                            name = device?.name
                                                                ?: "Unknown Product",
                                                            price = device?.sellingPrice ?: 0.0,
                                                            quantity = triple.second,
                                                            selected = true
                                                        )
                                                    },
                                                order = OrderRequest(
                                                    customer_id = idCustomer,
                                                    export_date = getCurrentTimestampEX(),
                                                    total_money = tongtien.toInt(),
                                                    discount = 0,
                                                    vat = 0,
                                                    amount = amount.toInt(),
                                                    status = 0
                                                )
                                            )
                                            checkoutRequest = request
                                            if (selectedPaymentMethod == "VNPay") {
                                                vnPayViewModel.createPaymentUrl(
                                                    PaymentRequest(
                                                        amount = amount.toString(),
                                                        bankCode = "",
                                                        returnUrl = "myapp://payment"
                                                    )
                                                )
                                                orderViewModel.createOrder(request)
                                            } else {
                                                orderViewModel.createOrder(request)
                                            }
                                        }
                                    }
                                }
                                is CustomerState.Error -> {
                                    addressViewModel.updateErrorMessage(state.message)
                                }
                                is CustomerState.Loading -> {
                                    // Đang tải, không làm gì
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = ButtonDefaults.buttonElevation(1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF))
                    ) {
                        Text("ĐẶT HÀNG", fontSize = 18.sp)
                    }
                }

            }
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                item {
                    if (address != null) {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(1.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(horizontal = 5.dp),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = "",
                                        tint = Color.Red
                                    )
                                }
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = address.receiver_name,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Thay đổi",
                                            color = Color(0xFF5D9EFF),
                                            modifier = Modifier.clickable {
                                                //navController.navigate("${Screen.Address_Selection.route}?idCustomer=${customer?.id}")
                                                navController.navigate("${Screen.Address_Selection.route}?idCustomer=${idCustomer}&selectedAddressId=${address.id}")
                                            }
                                        )
                                    }
                                    Text(text = address.phone)
                                    Text(
                                        text = address.getFormattedAddress()
                                    )
                                }
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(1.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Không có địa chỉ mặc định",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Thêm địa chỉ",
                                    color = Color(0xFF5D9EFF),
                                    modifier = Modifier.clickable {
                                        navController.navigate("${Screen.Address_Selection.route}?idCustomer=${idCustomer}")
                                    }
                                )
                            }
                        }
                    }
                }
                items(listDevice.distinctBy { it.idDevice }) { device ->
                    selectedProducts.forEach { triple ->
                        if (device.idDevice == triple.first) {
                            DeviceItem(device, triple.second)
                        }
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Phương thức thanh toán",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                start = 15.dp,
                                top = 10.dp,
                                bottom = 5.dp
                            )
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 4.dp)
                                .fillMaxWidth()
                        ) {
                            PaymentMethodOption(
                                method = "Thanh toán khi nhận hàng (COD)",
                                selected = selectedPaymentMethod,
                                onSelected = { selectedPaymentMethod = it }
                            )
                            PaymentMethodOption(
                                method = "VNPay",
                                selected = selectedPaymentMethod,
                                onSelected = { selectedPaymentMethod = it }
                            )
                        }
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 4.dp)
                            .fillMaxWidth()
                            .height(140.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Chi tiết thanh toán", fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tổng tiền hàng")
                                Text(text = formatGiaTien(tongtien))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tổng tiền vận chuyển")
                                Text("30000 VNĐ")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tổng thanh toán")
                                Text(text = formatGiaTien(amount))
                            }
                        }
                    }
                }
            }

        }
    }
    // Hiển thị popup nếu email chưa xác thực
    if (showEmailVerificationDialog) {
        AlertDialog(
            onDismissRequest = { /* Không cho phép đóng bằng cách nhấn bên ngoài */ },
            containerColor = Color.White,
            title = { Text("Thông báo") },
            text = { Text("Email chưa xác thực.\nVui lòng xác thực email để mua hàng !") },
            confirmButton = {
                Button(
                    onClick = {
                        // Chuyển hướng đến màn hình xác thực email (nếu có)
                        navController.navigate("${Screen.EmailVerificationScreen.route}?id=$idCustomer&email=$email&username=$username&token=$token")
                        showEmailVerificationDialog = false // Đóng popup tạm thời
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF))
                ) {
                    Text("Xác thực ngay")
                }
            }
        )
    }
}

@Composable
fun PaymentMethodOption(
    method: String,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = method,
            modifier = Modifier.padding(start = 25.dp)
        )
        RadioButton(
            selected = selected == method,
            onClick = { onSelected(method) },
            colors = RadioButtonDefaults.colors(
                unselectedColor = Color(0xFF5D9EFF),
                selectedColor = Color(0xFF5D9EFF)
            )
        )
    }
}

/** Card chứa thông tin sản phẩm của màn hình thanh toán (CheckoutItem)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 06/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: device: Device, stock:Int
 *
 * Output: Hiển thị thông tin của 1 sản phẩm gồm tên, giá, số lượng và tổng tiền của phẩm đó
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@Composable
fun DeviceItem(
    device: Device,
    stock:Int
) {
    val deviceViewModel: DeviceViewModel = viewModel()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    // Tải hình ảnh
    LaunchedEffect(device) {
        bitmap = deviceViewModel.getDeviceImageBitmap(device)
    }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = device.name.ifEmpty { "Hình ảnh sản phẩm" },
                        modifier = Modifier
                            .size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        device.name,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatGiaTien(device.sellingPrice),
                            color = Color.Red
                        )
                        Text(
                            "x$stock"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp, start = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng số tiền (${stock} sản phẩm)",
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = formatGiaTien(device.sellingPrice *stock),
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

