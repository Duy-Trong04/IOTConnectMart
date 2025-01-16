package com.example.ungdungbanthietbi_iot.screen.personal

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ungdungbanthietbi_iot.data.customer.Birthdate
import com.example.ungdungbanthietbi_iot.data.customer.CheckCustomer
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    customerID: String?,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var selectedMonth by remember { mutableStateOf(LocalDate.now().monthValue) }
    var selectedYear by remember { mutableStateOf(LocalDate.now().year) }
    var birthdateModel: CustomerViewModel = viewModel()

    val currentMonthYear = YearMonth.of(selectedYear, selectedMonth)
    val daysInMonth = (1..currentMonthYear.lengthOfMonth()).toList()
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    // Kiểm tra xem thiết bị có phải là máy tính bảng hay không
    val isTablet = isTablet()

    // Đặt kích thước font chữ dựa trên loại thiết bị
    val fontSize = if (isTablet) 24.sp else 16.sp
    val dayFontSize = if (isTablet) 20.sp else 14.sp

    // State để kiểm soát BottomSheet (hoặc Dialog)
    var showMonthPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }

    // Show Dialog để chọn năm trước khi chuyển sang tháng
    if (showYearPicker) {
        YearPickerDialog(
            selectedYear = selectedYear,
            onYearSelected = { year ->
                selectedYear = year
                showYearPicker = false // Ẩn dialog sau khi chọn xong
            },
            onDismiss = { showYearPicker = false }
        )
    }

    // Show Dialog hoặc BottomSheet để chọn tháng/năm
    if (showMonthPicker) {
        MonthPickerDialog(
            currentMonth = selectedMonth,
            currentYear = selectedYear,
            onMonthYearSelected = { month, year ->
                selectedMonth = month
                selectedYear = year
                showMonthPicker = false // Ẩn dialog sau khi chọn xong
            },
            onDismiss = { showMonthPicker = false }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onDateSelected(LocalDate.of(selectedYear, selectedMonth, selectedDate))
                val birthdate = LocalDate.of(selectedYear, selectedMonth, selectedDate)
                val b = Birthdate(customerID.toString(), birthdate.toString())
                birthdateModel.updateBirthdate(b)
                onDismiss()
            }) {
                Text("Xác nhận", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Đóng", color = Color.White)
            }
        },
        title = {
            Text("Chọn ngày sinh", fontSize = fontSize, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                // Năm chọn (Year Picker)
//                Text(
//                    text = "Chọn năm: $selectedYear",
//                    fontSize = fontSize,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .clickable { showYearPicker = true } // Click để chọn năm
//                        .padding(8.dp),
//                    color = Color(0xFF5F9EFF)
//                )

                // Month and Year Controls
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        if (selectedMonth == 1) {
                            selectedMonth = 12
                            selectedYear -= 1
                        } else {
                            selectedMonth -= 1
                        }
                    }) {
                        Text("<")
                    }

                    // Allow the user to click on the month/year to show a picker
                    Text(
                        text = "${currentMonthYear.format(monthYearFormatter)}",
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showMonthPicker = true },
                        color = Color(0xFF5F9EFF)
                    )

                    Button(onClick = {
                        if (selectedMonth == 12) {
                            selectedMonth = 1
                            selectedYear += 1
                        } else {
                            selectedMonth += 1
                        }
                    }) {
                        Text(">")
                    }
                }

                // Header: Sun - Sat
                val daysOfWeek = listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(daysOfWeek) { day ->
                        Text(
                            text = day,
                            fontSize = dayFontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    // Dates of Selected Month
                    items(daysInMonth) { date ->
                        Text(
                            text = date.toString(),
                            fontSize = dayFontSize,
                            fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal, // Highlight the selected date
                            color = if (date == selectedDate) Color.Red else Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { selectedDate = date } // Click để chọn ngày
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPickerDialog(
    currentMonth: Int,
    currentYear: Int,
    onMonthYearSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    var showMonthPicker by remember { mutableStateOf(false) }  // State kiểm soát việc hiển thị dropdown cho tháng
    var showYearPicker by remember { mutableStateOf(false) }  // State kiểm soát việc hiển thị dialog cho năm

    // Khi người dùng chọn năm, hiển thị dialog YearPicker
    if (showYearPicker) {
        YearPickerDialog(
            selectedYear = selectedYear,
            onYearSelected = { newYear ->
                selectedYear = newYear
                showYearPicker = false  // Đóng dialog năm sau khi chọn
            },
            onDismiss = { showYearPicker = false }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onMonthYearSelected(selectedMonth, selectedYear)
                onDismiss()  // Đóng dialog khi xác nhận
            }) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Đóng")
            }
        },
        title = { Text("Chọn tháng với năm") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Hiển thị tháng hiện tại và cho phép người dùng nhấn vào để chọn tháng
                Column {
                    Text(
                        text = "Tháng ${selectedMonth}",
                        modifier = Modifier.clickable {
                            showMonthPicker = true
                        },  // Nhấn vào để chọn tháng
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5F9EFF)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Năm ${selectedYear}",
                        modifier = Modifier.clickable {
                            showYearPicker = true
                        },  // Nhấn vào để chọn năm
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5F9EFF)
                    )
                }

                // Nếu showMonthPicker là true, hiển thị DropdownMenu để chọn tháng
                if (showMonthPicker) {
                    val months = listOf(
                        "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
                    )

                    // Hiển thị DropdownMenu để chọn tháng
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { showMonthPicker = false }
                    ) {
                        months.forEachIndexed { index, month ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedMonth = index + 1
                                    showMonthPicker = false  // Ẩn dropdown sau khi chọn
                                },
                                text = { Text(month) }
                            )
                        }
                    }
                }
            }
        }
    )
}

// Dialog chọn năm
@Composable
fun YearPickerDialog(
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var yearInput by remember { mutableStateOf(selectedYear.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onYearSelected(yearInput.toIntOrNull() ?: selectedYear)
            }) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Đóng")
            }
        },
        title = { Text("Chọn năm") },
        text = {
            TextField(
                value = yearInput,
                onValueChange = { yearInput = it },
                label = { Text("Nhập năm") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
    )
}



//Input: Không có tham số đầu vào.
//Hàm sử dụng LocalConfiguration.current để lấy cấu hình hiện tại của thiết bị.
@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}
