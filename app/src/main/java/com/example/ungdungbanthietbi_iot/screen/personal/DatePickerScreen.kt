package com.example.ungdungbanthietbi_iot.screen.personal

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*



/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: In ra lịch để cập nhật ngay sinh
*/

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CalendarScreen(onBack: () -> Unit = {}) {
    var selectedDate by remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var selectedMonth by remember { mutableStateOf(LocalDate.now().monthValue) }
    var selectedYear by remember { mutableStateOf(LocalDate.now().year) }

    val currentMonthYear = YearMonth.of(selectedYear, selectedMonth)
    val daysInMonth = (1..currentMonthYear.lengthOfMonth()).toList()
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Lịch", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    Text(
                        text = "${currentMonthYear.format(monthYearFormatter)}",
                        fontSize = if (isTablet()) 24.sp else 18.sp,
                        fontWeight = FontWeight.Bold,
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
                val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(daysOfWeek) { day ->
                        Text(
                            text = day,
                            fontSize = if (isTablet()) 20.sp else 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    // Dates of Selected Month
                    items(daysInMonth) { date ->
                        Text(
                            text = date.toString(),
                            fontSize = if (isTablet()) 20.sp else 16.sp,
                            fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal, // Highlight the selected date
                            color = if (date == selectedDate) Color.Red else Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { selectedDate = date } // Click để chọn ngày
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* Xử lý nút CLEAR */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF))
                    ) {
                        Text("CLEAR", color = Color.White, fontSize = if (isTablet()) 20.sp else 16.sp)
                    }
                    Button(
                        onClick = { /* Xử lý nút ACTION */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF))
                    ) {
                        Text("ACTION", color = Color.White, fontSize = if (isTablet()) 20.sp else 16.sp)
                    }
                    Button(
                        onClick = { /* Xử lý nút SET */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F9EFF))
                    ) {
                        Text("SET", color = Color.White, fontSize = if (isTablet()) 20.sp else 16.sp)
                    }
                }
            }
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
