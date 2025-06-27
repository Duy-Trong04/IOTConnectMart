package com.example.ungdungbanthietbi_iot.views.personal

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.api.VerifyOtpChangeEmailRequest
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerState
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.viewModels.VerifyEmailUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Giao diện màn hình xác thực OTP (VerifyOTPScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối cùng: 15/05/2025
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình xác thực OTP với các ô nhập riêng biệt
 * ------------------------------------------------------------
 * Người cập nhật: [Tên người cập nhật]
 * Ngày cập nhật: 15/05/2025
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 * - Thiết kế lại giao diện OTP với từng ô nhập riêng, bo góc 5.dp, viền xanh
 * - Tự động chuyển focus sang ô tiếp theo sau khi nhập
 * - Hỗ trợ xóa và chuyển về ô trước đó
 *
 */
private data class OtpField(
    val text: String,
    val index: Int,
    val focusRequester: FocusRequester? = null
)

@Composable
fun OtpInputField(
    otp: MutableState<String>, // The current OTP value.
    count: Int = 6, // Number of OTP boxes.
    otpBoxModifier: Modifier = Modifier
        .border(1.pxToDp(), Color.Gray)
        .background(Color.White),
    otpTextType: KeyboardType = KeyboardType.Number,
    textColor: Color = Color.Black,
) {

    val scope = rememberCoroutineScope()

    // Initialize state for each OTP box with its character and optional focus requester.
    val otpFieldsValues = remember {
        (0 until count).mapIndexed { index, i ->
            mutableStateOf(
                OtpField(
                    text = otp.value.getOrNull(i)?.toString() ?: "",
                    index = index,
                    focusRequester = FocusRequester()
                )
            )
        }
    }

    // Update each OTP box's value when the overall OTP value changes, and manage focus.
    LaunchedEffect(key1 = otp.value) {
        for (i in otpFieldsValues.indices) {
            otpFieldsValues[i].value =
                otpFieldsValues[i].value.copy(text = otp.value.getOrNull(i)?.toString() ?: "")
        }
        // Request focus on the first box if the OTP is blank (e.g., reset).
        if (otp.value.isBlank()) {
            try {
                otpFieldsValues[0].value.focusRequester?.requestFocus()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Create a row of OTP boxes.
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(count) { index ->
            // For each OTP box, manage its value, focus, and what happens on value change.
            OtpBox(
                modifier = otpBoxModifier,
                otpValue = otpFieldsValues[index].value,
                textType = otpTextType,
                textColor = textColor,
                isLastItem = index == count - 1, // Check if this box is the last in the sequence.
                totalBoxCount = count,
                onValueChange = { newValue ->
                    // Handling logic for input changes, including moving focus and updating OTP state.
                    scope.launch {
                        handleOtpInputChange(index, count, newValue, otpFieldsValues, otp)
                    }
                },
                onFocusSet = { focusRequester ->
                    // Save the focus requester for each box to manage focus programmatically.
                    otpFieldsValues[index].value =
                        otpFieldsValues[index].value.copy(focusRequester = focusRequester)
                },
                onNext = {
                    // Attempt to move focus to the next box when the "next" action is triggered.
                    focusNextBox(index, count, otpFieldsValues)
                },
            )
        }
    }
}

private fun handleOtpInputChange(
    index: Int,
    count: Int,
    newValue: String,
    otpFieldsValues: List<MutableState<OtpField>>,
    otp: MutableState<String>
) {
    // Handle input for the current box.
    if (newValue.length <= 1) {
        // Directly set the new value if it's a single character.
        otpFieldsValues[index].value = otpFieldsValues[index].value.copy(text = newValue)
    } else if (newValue.length == 2) {
        // If length of new value is 2, we can guess the user is typing focusing on current box
        // In this case set the unmatched character only
        otpFieldsValues[index].value =
            otpFieldsValues[index].value.copy(text = newValue.lastOrNull()?.toString() ?: "")
    } else if (newValue.isNotEmpty()) {
        // If pasting multiple characters, distribute them across the boxes starting from the current index.
        newValue.forEachIndexed { i, char ->
            if (index + i < count) {
                otpFieldsValues[index + i].value =
                    otpFieldsValues[index + i].value.copy(text = char.toString())
            }
        }
    }

    // Update the overall OTP state.
    var currentOtp = ""
    otpFieldsValues.forEach {
        currentOtp += it.value.text
    }

    try {
        // Logic to manage focus.
        if (newValue.isEmpty() && index > 0) {
            // If clearing a box and it's not the first box, move focus to the previous box.
            otpFieldsValues.getOrNull(index - 1)?.value?.focusRequester?.requestFocus()
        } else if (index < count - 1 && newValue.isNotEmpty()) {
            // If adding a character and not on the last box, move focus to the next box.
            otpFieldsValues.getOrNull(index + 1)?.value?.focusRequester?.requestFocus()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    otp.value = currentOtp
}

private fun focusNextBox(
    index: Int,
    count: Int,
    otpFieldsValues: List<MutableState<OtpField>>
) {
    if (index + 1 < count) {
        // Move focus to the next box if the current one is filled and it's not the last box.
        try {
            otpFieldsValues[index + 1].value.focusRequester?.requestFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
private fun OtpBox(
    modifier: Modifier,
    otpValue: OtpField, // Current value of this OTP box.
    textType: KeyboardType = KeyboardType.Number,
    textColor: Color = Color.Black,
    isLastItem: Boolean, // Whether this box is the last in the sequence.
    totalBoxCount: Int, // Total number of OTP boxes for layout calculations.
    onValueChange: (String) -> Unit, // Callback for when the value changes.
    onFocusSet: (FocusRequester) -> Unit, // Callback to set focus requester.
    onNext: () -> Unit, // Callback for handling "next" action, typically moving focus forward.
) {
    val focusManager = LocalFocusManager.current
    val focusRequest = otpValue.focusRequester ?: FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Calculate the size of the box based on screen width and total count.
    // If you're using this in Kotlin multiplatform mobile
    // val screenWidth = LocalWindowInfo.current.containerSize.width
    // If you're using this in Android
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.dpToPx().toInt()
    val paddingValue = 6
    val totalBoxSize = (screenWidth / totalBoxCount) - paddingValue * totalBoxCount

    Box(
        modifier = modifier
            .size(totalBoxSize.pxToDp()),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = TextFieldValue(otpValue.text, TextRange(maxOf(0, otpValue.text.length))),
            onValueChange = {
                // Logic to prevent re-triggering onValueChange when focusing.
                if (!it.text.equals(otpValue)) {
                    onValueChange(it.text)
                }
            },
            // Setup for focus and keyboard behavior.
            modifier = Modifier
                .testTag("otpBox${otpValue.index}")
                .focusRequester(focusRequest)
                .onGloballyPositioned {
                    onFocusSet(focusRequest)
                },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                color = textColor
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = textType,
                imeAction = if (isLastItem) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                },
                onDone = {
                    // Hide keyboard and clear focus when done.
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
            visualTransformation = getVisualTransformation(textType),
        )
    }
}

@Composable
private fun getVisualTransformation(textType: KeyboardType) =
    if (textType == KeyboardType.NumberPassword || textType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VerifiedEmailScreen(
    navController: NavController,
    id: String,
    email: String,
    username: String,
    token: String
) {
    val customerViewModel: CustomerViewModel = viewModel()

    val customerState by customerViewModel.customerState.collectAsState()
    val accountViewModel: AccountViewModel = viewModel()
    var isResendEnabled by remember { mutableStateOf(false) }
    var timer by remember { mutableIntStateOf(120) }
    val otpValue = remember {
        mutableStateOf("")
    }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val isLoading by accountViewModel.isLoading.collectAsState()

    LaunchedEffect(id) {
        if (id.isBlank()) {
            customerViewModel.setErrorState("Lỗi: ID khách hàng không hợp lệ")
        }
        else{
            customerViewModel.getCustomerById(id)
        }
    }

    // Bắt đầu đếm ngược thời gian
    LaunchedEffect(key1 = timer) {
        if (timer > 0) {
            delay(1000L)
            timer -= 1
        } else {
            isResendEnabled = true
        }
    }

    val verifyEmailResult by accountViewModel.verifyEmailResult.collectAsState()
    // Xử lý phản hồi từ API
    LaunchedEffect(verifyEmailResult) {
        when (verifyEmailResult) {
            is VerifyEmailUiState.Success -> {
                showSuccessDialog = true
                errorMessage = (verifyEmailResult as VerifyEmailUiState.Success).message
            }
            is VerifyEmailUiState.Error -> {
                showErrorDialog = true
                errorMessage = (verifyEmailResult as VerifyEmailUiState.Error).message
            }
            else -> {
                // Không làm gì khi Idle hoặc Loading
            }
        }
    }

    Scaffold{
        when (val state = customerState) {
            is CustomerState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(600.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF5F9EFF)
                    )
                }
            }
            is CustomerState.Success -> {
                val customer = state.customer
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Box(modifier = Modifier.height(24.dp))
                    Image(
                        painter = painterResource(id = R.drawable.email_verified),
                        contentDescription = "Forgot Password Illustration",
                        modifier = Modifier
                            .weight(3.0f)
                            .padding(horizontal = 32.dp),
                        contentScale = ContentScale.Fit,
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(7.0f)
                    ) {
                        Text(
                            text = "Nhập mã xác thực OTP", style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D9EFF)
                            )
                        )
                        Text(
                            text = "Chúng tôi đã gửi cho bạn mã xác nhận gồm 6 chữ số.\nHãy nhập mã gồm 6 chữ số mà chúng tôi đã gửi đến email của bạn.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OtpInputField(
                            otp = otpValue,
                            count = 6,
                            textColor = Color(0xFF5D9EFF),
                            otpBoxModifier = Modifier
                                .border(4.pxToDp(), Color(0xFF5D9EFF), shape = RoundedCornerShape(12.pxToDp()))
                        )
                        TextButton(
                            onClick = {
                                accountViewModel.sendOtp(email)
                                isResendEnabled = false
                                timer = 120
                            },
                            enabled = isResendEnabled,
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF5D9EFF)
                            )
                        ) {
                            Text(
                                text = if (timer > 0) "Gửi lại mã sau ${timer}s" else "Gửi lại",
                                color = if (timer <= 0) Color(0xFF5D9EFF) else Color.Gray,
                            )
                        }

                        Button(
                            onClick = {
                                if (otpValue.value.isNotEmpty()) {
                                    val request = VerifyOtpChangeEmailRequest(
                                        email = email,
                                        otp = otpValue.value
                                    )
                                    accountViewModel.verifyOtpChangeEmail(request)
                                } else {
                                    errorMessage = "Vui lòng nhập mã OTP"
                                    showErrorDialog = true
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5D9EFF)
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 4.dp,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Text(
                                    text = "Xác nhận",
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Box(modifier = Modifier.height(16.dp))
                        // Error Dialog
                        if (showErrorDialog) {
                            AlertDialog(
                                onDismissRequest = { showErrorDialog = false },
                                title = { Text("Thông báo") },
                                containerColor = Color.White,
                                text = { Text(errorMessage) },
                                confirmButton = {
                                    Button(
                                        onClick = { showErrorDialog = false },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF5D9EFF)
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Xác nhận")
                                    }
                                }
                            )
                        }
                        if (showSuccessDialog) {
                            AlertDialog(
                                onDismissRequest = {},
                                title = { Text("Thông báo") },
                                containerColor = Color.White,
                                text = { Text(errorMessage) },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            showSuccessDialog = false
                                            if (verifyEmailResult is VerifyEmailUiState.Success) {
                                                navController.popBackStack()
                                            }
                                        },
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
                    }
                }
            }
            is CustomerState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            customerViewModel.getCustomerById(id)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF))
                    ) {
                        Text("Thử lại", color = Color.White)
                    }
                }
            }
        }
    }
}