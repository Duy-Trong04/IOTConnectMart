package com.example.ungdungbanthietbi_iot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController

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
fun ProductDetailsScreen(navController: NavController) {
    // Biến lưu trữ giá trị đánh giá
    var rating by remember { mutableStateOf(1) }
    // Biến lưu trữ giá trị check
    var isChecked2 by remember { mutableStateOf(false) }
    var isChecked1 by remember { mutableStateOf(true) }
    // Biến lưu trữ trạng thái hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }
    // Biến lưu trữ số lượng sản phẩm
    var quantity by remember { mutableStateOf(1) }
    // Màn hình chính
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(
                            text = "Chi tiết sản phẩm",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth().padding(start = 70.dp),
                            textAlign = TextAlign.Center
                        )
                    IconButton(
                        onClick = {
                            // Quay về màn hình danh sách sản phẩm
                            navController.navigate(Screen.HomeScreen.route) },
                        modifier = Modifier.padding(start = 10.dp)
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
                            // Xử lý sự kiện tìm kiếm ở đây
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tìm kiếm",
                                tint = Color.White
                            )
                        }

                        // Icon Giỏ hàng
                        IconButton(onClick = {
                            // vào màn hình giỏ hàng
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
                    .border(1.dp, Color.Black)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Trang chủ",
                        modifier = Modifier.weight(1f),

                        )
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Giỏ hàng",
                        modifier = Modifier.weight(1f).clickable { showDialog = true },
                    )
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
                                        Image(
                                            painter = painterResource(R.drawable.den2),
                                            contentDescription = "Logo",
                                            modifier = Modifier.size(100.dp)
                                        )
                                        Column(){
                                        Text(
                                            text = "Giá: xxxxx",
                                            modifier = Modifier.padding(start = 16.dp),
                                            color = Color.Red
                                        )
                                            Text(
                                            text = "Kho: xxxxx",
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
                                            Button(onClick = { if (quantity > 1) quantity-- },
                                                colors = ButtonDefaults.buttonColors(
                                                    backgroundColor = Color.White,
                                                ),
                                                ) {
                                                Text("-", color = Color.Red, fontWeight = FontWeight.Bold)
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "$quantity")
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Button(onClick = { quantity++ },
                                                colors = ButtonDefaults.buttonColors(
                                                    backgroundColor = Color.White,
                                                ),
                                                ) {
                                                Text("+", color = Color(0xFF008000), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Nút xác nhận
                                    Button(
                                        onClick = {
                                            // Xử lý thêm vào giỏ hàng
                                            println("Thêm $quantity sản phẩm vào giỏ hàng")
                                            showDialog = false
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(0xFF5D9EFF),
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.End)
                                    ) {
                                        Text("Thêm vào giỏ hàng",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "Mua Ngay",
                        modifier = Modifier.weight(1f)
                            .clickable {/* Chuyển sang màn hình mua hàng */ },
                        textAlign = TextAlign.Center,
                        color = Color(0xFF5D9EFF),
                        fontWeight = FontWeight.Bold
                    )
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
                Image(
                    painter = painterResource(R.drawable.den2),
                    contentDescription = "ds ảnh sản phẩm",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp),
                    alignment = Alignment.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().size(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Tròn đen
                    Box(
                        modifier = Modifier
                            //.size(12.dp)
                            .background(Color.Black, shape = CircleShape)
                            .padding(4.dp) // Padding để tạo không gian
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa các nút

                    // Tròn xám
                    Box(
                        modifier = Modifier
                            //.size(12.dp)
                            .background(Color.DarkGray, shape = CircleShape)
                            .padding(4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa các nút

                    // Tròn xám nhạt
                    Box(
                        modifier = Modifier
                            //.size(12.dp)
                            .background(Color.Gray, shape = CircleShape)
                            .padding(4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa các nút

                    // Tròn xám rất nhạt
                    Box(
                        modifier = Modifier
                            //.size(12.dp)
                            .background(Color.LightGray, shape = CircleShape)
                            .padding(4.dp)
                    )
                }

                // Chi tiết sản phẩm
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Giá: xxxxx",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Đã bán 1k",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Tên sản phẩm ",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Image(
                            painter = painterResource(R.drawable.tim),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        "Mô tả sản phẩm",
                        color = Color(0xFF5D9EFF),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "- Đây là một thiết bị, giống như tivi, " +
                                "camera an ninh hoặc thiết bị tập thể dục đã được trao cho khả năng điện toán. " +
                                "Thiết bị này thu thập dữ liệu từ môi trường xung quanh, " +
                                "thao tác nhập liệu của người dùng hoặc mô thức sử dụng " +
                                "và truyền cũng như nhận dữ liệu qua Internet từ ứng dụng IoT của nó.",
                        color = Color.Black,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Đánh giá sản phẩm",
                            color = Color(0xFF085979),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "Xem tất cả >",
                            color = Color(0xFF085979),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Row(
                        modifier = Modifier.padding(5.dp)
                    )
                    {
                        // 5 sao vàng
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Sao",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFFFBC02D) // Màu vàng
                            )
                        }
                        Text(
                            "5/5 (x đánh giá)",
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Gray,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray),
                    )
                    {
                        Row(modifier = Modifier.fillMaxWidth().padding(start = 5.dp)) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "avt",
                                modifier = Modifier.size(30.dp)
                            )
                            Column() {
                                Text("Văn Nam Cao")
                                Row(modifier = Modifier.padding(start = 5.dp))
                                {
                                    repeat(5) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Sao",
                                            modifier = Modifier.size(10.dp),
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
                                Checkbox(
                                    checked = isChecked1,
                                    onCheckedChange = { isChecked1 = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.Green, // Màu dấu tích khi được chọn
                                        uncheckedColor = Color.Gray, // Màu của ô khi chưa được chọn
                                        checkmarkColor = Color.White // Màu của dấu tích
                                    )
                                )
                                Text("Hữu ích", modifier = Modifier.padding(end = 5.dp))
                            }
                        }
                        Text(
                            "Sản phẩm này tốt lắm tôi rất thích",
                            modifier = Modifier.padding(bottom = 10.dp, start = 15.dp, end = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray),
                    )
                    {
                        Row(modifier = Modifier.fillMaxWidth().padding(start = 5.dp)) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "avt",
                                modifier = Modifier.size(30.dp)
                            )
                            Column() {
                                Text("Nguyễn Văn A")
                                Row(modifier = Modifier.padding(start = 5.dp))
                                {
                                    repeat(5) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Sao",
                                            modifier = Modifier.size(10.dp),
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
                                Checkbox(
                                    checked = isChecked2,
                                    onCheckedChange = { isChecked2 = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.Green, // Màu dấu tích khi được chọn
                                        uncheckedColor = Color.Gray, // Màu của ô khi chưa được chọn
                                        checkmarkColor = Color.White // Màu của dấu tích
                                    )
                                )
                                Text("Hữu ích", modifier = Modifier.padding(end = 5.dp))
                            }
                        }
                        Text(
                            "Sản phẩm này tốt lắm tôi rất thích",
                            modifier = Modifier.padding(bottom = 10.dp, start = 15.dp, end = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))


                    // Gợi ý sản phẩm
                    Text(
                        "Gợi ý sản phẩm",
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                            .clickable{
                                navController.navigate(Screen.ProductDetailsScreen.route)
                            },
                        horizontalArrangement = Arrangement.Start
                    ) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.den2),
                                        contentDescription = "sp gợi ý",
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(100.dp)
                                    )
                                    Text("Tên sản phầm")
                                    Text(
                                        "Giá sản phẩm", color = Color.Red,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Image(
                                    painter = painterResource(R.drawable.timtrang),
                                    contentDescription = "sp gợi ý",
                                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                )
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.den1),
                                        contentDescription = "sp gợi ý",
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(100.dp)
                                    )
                                    Text("Tên sản phầm")
                                    Text(
                                        "Giá sản phẩm", color = Color.Red,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Image(
                                    painter = painterResource(R.drawable.timtrang),
                                    contentDescription = "sp gợi ý",
                                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                )
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.den3),
                                        contentDescription = "sp gợi ý",
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(100.dp)
                                    )
                                    Text("Tên sản phầm")
                                    Text(
                                        "Giá sản phẩm", color = Color.Red,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Image(
                                    painter = painterResource(R.drawable.timtrang),
                                    contentDescription = "sp gợi ý",
                                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

