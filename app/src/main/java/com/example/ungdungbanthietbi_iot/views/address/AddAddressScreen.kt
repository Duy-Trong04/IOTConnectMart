package com.example.ungdungbanthietbi_iot.views.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.api.CreateAddressRequest
import com.example.ungdungbanthietbi_iot.models.District
import com.example.ungdungbanthietbi_iot.models.Province
import com.example.ungdungbanthietbi_iot.models.Ward
import com.example.ungdungbanthietbi_iot.viewModels.AddressViewModel

/** Giao diện màn hình thêm địa chỉ (AddAddressScreen)
 * -------------------------------------------
 * Người code: Duy Trọng
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Hiển thị giao diện thêm địa chỉ mới, bao gồm các TextField để nhập
 * thông tin và một Switch để đặt làm địa chỉ mặc định. Có nút "THÊM ĐỊA CHỈ" để thêm địa chỉ vào danh sách.
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    navController: NavController,
    idCustomer:String
){

    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showProvinceRequiredDialog by remember { mutableStateOf(false) }
    var showDistrictRequiredDialog by remember { mutableStateOf(false) }
    val addressViewModel: AddressViewModel = viewModel()
    val uiState by addressViewModel.uiState.collectAsState()

    // Biến trạng thái lưu thông tin nhập vào
    var hoten by remember { mutableStateOf("") }// Tên đầy đủ
    var phone by remember { mutableStateOf("") }// Số điện thoại
    var street by remember { mutableStateOf("") }// Địa chỉ chi tiết
    var detail by remember { mutableStateOf("") }// Địa chỉ chi tiết
    var isDefault by remember { mutableStateOf(false) } // Trạng thái của Switch đặt làm địa chỉ mặc định
    val validatePhone by remember { mutableStateOf(false) }

    // Biến trạng thái cho dropdown
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }

    // Hiển thị lỗi từ ViewModel
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            errorMessage = it
            showDialog = true
        }
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Thêm địa chỉ", textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
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
        // Thanh điều hướng hoặc nút hành động ở dưới cùng (bottomBar)
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 10.dp).padding(bottom = 10.dp)
            ) {
                // Nút Switch: Đặt làm địa chỉ mặc định
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Đặt làm địa chỉ mặc định",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,// Màu khi bật
                            uncheckedThumbColor = Color.Gray, // Màu khi tắt
                            checkedTrackColor = Color(0xFF5D9EFF)// Màu đường chạy khi bật
                        ),
                        modifier = Modifier.scale(0.6f)// Thu nhỏ kích thước Switch
                    )
                }
                // Nút thêm địa chỉ
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (hoten.trim().isEmpty() || phone.trim().isEmpty() ||
                            selectedProvince == null || selectedDistrict == null || selectedWard == null ||
                            street.trim().isEmpty()
                        ) {
                            errorMessage = "Vui lòng nhập đầy đủ thông tin địa chỉ"
                            showDialog = true
                            return@Button
                        }
                        if (phone.length != 10) {
                            errorMessage = "Số điện thoại phải đủ 10 số"
                            return@Button
                        }
                        val createAddress = CreateAddressRequest(
                            customer_id = idCustomer,
                            receiver_name = hoten,
                            phone = phone,
                            district = selectedDistrict!!.DistrictName,
                            city = selectedProvince!!.ProvinceName,
                            ward = selectedWard!!.WardName,
                            street = street,
                            detail = detail,
                            is_default = isDefault
                        )
                        addressViewModel.createAddress(createAddress)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    ),
                    shape = RoundedCornerShape(10.dp),// Bo góc nút
                    elevation = ButtonDefaults.buttonElevation(1.dp)// Tạo độ nổi
                ) {
                    Text("Thêm địa chỉ", fontSize = 16.sp)
                }
            }
        }
    ) {
        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Thông báo") },
                containerColor = Color.White,
                text = { Text(text = errorMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D9EFF)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Xác nhận")
                    }
                }
            )
        }
        if (showProvinceRequiredDialog) {
            AlertDialog(
                onDismissRequest = { showProvinceRequiredDialog = false },
                title = { Text("Thông báo") },
                containerColor = Color.White,
                text = { Text("Vui lòng chọn tỉnh/thành phố trước") },
                confirmButton = {
                    Button(
                        onClick = { showProvinceRequiredDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Xác nhận")
                    }
                }
            )
        }
        if (showDistrictRequiredDialog) {
            AlertDialog(
                onDismissRequest = { showDistrictRequiredDialog = false },
                title = { Text("Thông báo") },
                containerColor = Color.White,
                text = { Text("Vui lòng chọn quận/huyện trước") },
                confirmButton = {
                    Button(
                        onClick = { showDistrictRequiredDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9EFF)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Xác nhận")
                    }
                }
            )
        }
        // Nội dung chính (LazyColumn) hiển thị danh sách các trường nhập liệu
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(it).background(Color.White)
                .padding(10.dp)
        ){
            item{
                Text(
                    text = "Thông tin người nhận",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                TextField(
                    value = hoten,
                    onValueChange = { hoten = it },
                    label = { Text("Họ và tên") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                if(validatePhone){
                    Text(
                        text = "Số điện thoại phải đủ 10 số",
                        color = Color.Red
                    )
                }
            }
            // Nhóm trường "Địa chỉ"
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Địa chỉ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                ProvinceDropdown(
                    provinces = uiState.provinces,
                    selectedProvince = selectedProvince,
                    onProvinceSelected = { province ->
                        if (selectedProvince?.ProvinceID != province.ProvinceID) {
                            selectedProvince = province
                            selectedDistrict = null
                            selectedWard = null
                            addressViewModel.fetchDistricts(province.ProvinceID)
                        }
                    },
                    isLoading = uiState.isLoading
                )
                DistrictDropdown(
                    districts = uiState.districts,
                    selectedDistrict = selectedDistrict,
                    onDistrictSelected = { district ->
                        if (selectedDistrict?.DistrictID != district.DistrictID) {
                            selectedDistrict = district
                            selectedWard = null
                            addressViewModel.fetchWards(district.DistrictID)
                        }
                    },
                    isLoading = uiState.isLoading,
                    enabled = selectedProvince != null,
                    onProvinceRequired = { showProvinceRequiredDialog = true }
                )
                WardDropdown(
                    wards = uiState.wards,
                    selectedWard = selectedWard,
                    selectedProvince = selectedProvince,
                    onWardSelected = { ward ->
                        selectedWard = ward
                    },
                    isLoading = uiState.isLoading,
                    enabled = selectedDistrict != null,
                    onProvinceRequired = { showProvinceRequiredDialog = true },
                    onDistrictRequired = { showDistrictRequiredDialog = true }
                )
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Đường") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5D9EFF),
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                TextField(
                    value = detail,
                    onValueChange = { detail = it },
                    label = { Text("Địa chỉ chi tiết") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF5D9EFF),
                        cursorColor = Color(0xFF5D9EFF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)// Bàn phím văn bản
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceDropdown(
    provinces: List<Province>,
    selectedProvince: Province?,
    onProvinceSelected: (Province) -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (!isLoading && provinces.isNotEmpty()) expanded = !expanded }
    ) {
        TextField(
            value = selectedProvince?.ProvinceName ?: "Chọn tỉnh/thành phố",
            onValueChange = {},
            readOnly = true,
            label = { Text("Tỉnh/Thành phố") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF5D9EFF),
                focusedLabelColor = Color(0xFF5D9EFF),
                cursorColor = Color(0xFF5D9EFF)
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .heightIn(max = 400.dp)
        ) {
            provinces
                .forEach { province ->
                    DropdownMenuItem(
                        text = { Text(province.ProvinceName) },
                        onClick = {
                            onProvinceSelected(province)
                            expanded = false
                        }
                    )
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictDropdown(
    districts: List<District>,
    selectedDistrict: District?,
    onDistrictSelected: (District) -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    onProvinceRequired: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled && !isLoading && districts.isNotEmpty()) {
                expanded = !expanded
            } else if (!enabled) {
                onProvinceRequired()
            }
        },
        //modifier = Modifier.alpha(if (enabled) 1f else 0.5f)
    ) {
        TextField(
            value = selectedDistrict?.DistrictName ?: "Chọn quận/huyện",
            onValueChange = {},
            readOnly = true,
            label = { Text("Quận/Huyện") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF5D9EFF),
                focusedLabelColor = Color(0xFF5D9EFF),
                cursorColor = Color(0xFF5D9EFF)
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            //enabled = enabled && !isLoading
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .heightIn(max = 400.dp),
        ) {
            districts
                .forEach { district ->
                    DropdownMenuItem(
                        text = { Text(district.DistrictName) },
                        onClick = {
                            onDistrictSelected(district)
                            expanded = false
                        }
                    )
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardDropdown(
    wards: List<Ward>,
    selectedWard: Ward?,
    selectedProvince: Province?,
    onWardSelected: (Ward) -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    onProvinceRequired: () -> Unit,
    onDistrictRequired: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled && !isLoading && wards.isNotEmpty()) {
                expanded = !expanded
            } else if (!enabled) {
                if (selectedProvince == null) {
                    onProvinceRequired()
                } else {
                    onDistrictRequired()
                }
            }
        },
        //modifier = Modifier.alpha(if (enabled) 1f else 0.5f)
    ) {
        TextField(
            value = selectedWard?.WardName ?: "Chọn phường/xã",
            onValueChange = {},
            readOnly = true,
            label = { Text("Phường/Xã") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF5D9EFF),
                focusedLabelColor = Color(0xFF5D9EFF),
                cursorColor = Color(0xFF5D9EFF)
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            //enabled = enabled && !isLoading
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .heightIn(max = 400.dp)
        ) {
            wards
                .forEach { ward ->
                    DropdownMenuItem(
                        text = { Text(ward.WardName) },
                        onClick = {
                            onWardSelected(ward)
                            expanded = false
                        }
                    )
                }
        }
    }
}