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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

/** Giao diện màn hình Trang chủ (HomeScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 8/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình Trang chủ
 *  như có danh sách sản phẩm nổi bật, sản phẩm yêu thích, sản phẩm gợi ý
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val navdrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() //xử lý suspending fun (mở và đóng drawer)
    val countries = listOf(
        "Điện gia dụng",
        "Điện chiếu sáng",
        "Thiết bị cảm biến",
        "Xả kho",
    )

    // Danh mục
    ModalNavigationDrawer(
        drawerState = navdrawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5D9EFF))
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "IOT Connect Smart",
                        modifier = Modifier.padding(5.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng danh mục",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(24.dp)
                            .clickable {
                                // thoát danh mục
                                scope.launch {
                                    navdrawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                        tint = Color.White
                    )
                }
                HorizontalDivider()
                Text(
                    text = "T R A N G  C H Ủ",
                    modifier = Modifier.padding(15.dp),
                    color = Color(0xFF5D9EFF),
                    fontWeight = FontWeight.Bold
                )
                countries.forEach { country ->
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 5.dp,
                                        horizontal = 5.dp
                                    )
                                    .drawBehind {
                                        drawLine(
                                            color = Color.Black,
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                            ) {
                                Text(text = country)
                            }
                        }, selected = false, onClick = { /* Chọn danh mục */ })
                }
            }
        }
    ) {
        // Màn hình chính
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "IOT Connect Smart",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth().padding(start = 50.dp),
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xFF5D9EFF),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {

                        IconButton(onClick = {
                            scope.launch {
                                // mở và đóng drawer
                                navdrawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        )
                        {
                            // Icon Tìm kiếm
                            IconButton(onClick = {
                                navController.navigate(Screen.Search_Screen.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Tìm kiếm",
                                    tint = Color.White
                                )
                            }

                            // Icon Giỏ hàng
                            IconButton(onClick = {
                                // vào màn hình giỏ hàng nếu chưa đăng nhập thì vào màn hình đăng nhập(LoginScreen)
                                navController.navigate(Screen.LoginScreen.route)
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
                ){
                    Row(modifier = Modifier.fillMaxWidth()){
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Trang chủ",
                            modifier = Modifier.weight(1f),
                            tint = Color(0xFF5D9EFF)
                        )
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "",
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Thông báo",
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Tài khoản",
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        ) {
            padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
            {
                item {
                    Image(
                        painter = painterResource(R.drawable.den2),
                        contentDescription = "sk nổi bật",
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

                    // Sản phẩm nổi bật
                    Text("Sản phẩm nổi bật", modifier = Modifier.padding(20.dp),
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(modifier = Modifier.fillMaxWidth().padding(10.dp)
                        .clickable{
                            // Chuyển sang màn hình chi tiết sản phẩm(ProductDetailsScreen)
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
                                        painter = painterResource(R.drawable.den1),
                                        contentDescription = "sp nổi bật",
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
                                    painter = painterResource(R.drawable.tim),
                                    contentDescription = "sp nổi bật",
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
                                        painter = painterResource(R.drawable.den2),
                                        contentDescription = "sp nổi bật",
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
                                    contentDescription = "sp nổi bật",
                                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                )
                            }
                        }
                        item{
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.den3),
                                        contentDescription = "sp nổi bật",
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
                                    contentDescription = "sp nổi bật",
                                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                )
                            }
                        }
                    }

                    // Sản phầm yêu thích
                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text("Sản phẩm yêu thích", modifier = Modifier.padding(20.dp),
                            color = Color(0xFF085979),
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                            navController.navigate(Screen.Favorites_Screen.route)
                        }) {
                            Text("Xem tất cả", modifier = Modifier.padding(20.dp),
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    LazyRow(modifier = Modifier.fillMaxWidth().padding(10.dp)
                        .clickable{
                            // Chuyển sang màn hình chi tiết sản phẩm(ProductDetailsScreen)
                            navController.navigate(Screen.ProductDetailsScreen.route)
                        },
                        horizontalArrangement = Arrangement.Start) {
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
                                        contentDescription = "sp yêu thích",
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
                                    painter = painterResource(R.drawable.tim),
                                    contentDescription = "sp yêu thích",
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
                                         painter = painterResource(R.drawable.den2),
                                         contentDescription = "sp yêu thích",
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
                                     painter = painterResource(R.drawable.tim),
                                     contentDescription = "sp yêu thích",
                                     modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                 )
                             }
                         }
                        item{
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.den1),
                                        contentDescription = "sp yêu thích",
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
                                    painter = painterResource(R.drawable.tim),
                                    contentDescription = "sp yêu thích",
                                    modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
                                )
                            }
                        }
                    }

                    // Gợi ý sản phẩm
                    Text("Gợi ý sản phẩm", modifier = Modifier.padding(20.dp),
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                        )
                    LazyRow(modifier = Modifier.fillMaxWidth().padding(10.dp)
                        .clickable{
                            //chuyển sang màn hình chi tiết sản phẩm(ProductDetailsScreen)
                            navController.navigate(Screen.ProductDetailsScreen.route)
                        },
                        horizontalArrangement = Arrangement.Start) {
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
                        item{
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

