package com.example.ungdungbanthietbi_iot.screen.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.data.Product
import com.example.ungdungbanthietbi_iot.naviation.Screen
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment



/*Người thực hiện: Nguyễn Mạnh Cường
 Ngày viết: 12/12/2024
 ------------------------
 Input: không
 Output: Hiện thị Màn hình Hồ sơ của người dùng
*/
@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun PersonalScreen(
    navcontroller:NavHostController
) {
    //Dữ liệu mẫu
    val products = remember {
        listOf(
            Product(1, "Sản phẩm 1", 100000, "https://via.placeholder.com/150"),
            Product(2, "Sản phẩm 2", 150000, "https://via.placeholder.com/150"),
            Product(3, "Sản phẩm 3", 200000, "https://via.placeholder.com/150"),
            Product(4, "Sản phẩm 4", 200000, "https://via.placeholder.com/150"),
            Product(5, "Sản phẩm 5", 200000, "https://via.placeholder.com/150"),
            Product(6, "Sản phẩm 6", 200000, "https://via.placeholder.com/150")
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hồ sơ cá nhân", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Thực hiện chức năng  Chọn menu*/ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {/*Thuc hien chuc nang Setting*/navcontroller.navigate(Screen.SettingScreen.route)}) {
                    Icon(Icons.Filled.Settings, contentDescription = "Setting")
                    }
                    IconButton(
                        onClick = {/*Thuc hien chuc nang Giỏ hàng*/}) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Gio hang")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5F9EFF),
                    titleContentColor = Color.White
                )
            )
        },

        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                   Box(
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(150.dp)
                           .clickable {
                               //Chuyển màn hình chỉnh sửa hồ sơ
                               navcontroller.navigate(Screen.EditProfileScreen.route)
                           },
                       contentAlignment = Alignment.CenterStart
                   ){
                       Row(
                           horizontalArrangement = Arrangement.Center,
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Image(
                               painter = painterResource(id = R.drawable.avatar),
                               contentDescription = null,
                               modifier = Modifier
                                   .size(100.dp)
                                   .clip(CircleShape),
                               contentScale = ContentScale.Crop
                           )
                           Text(
                               text = "Tên khách hàng",
                               modifier = Modifier.padding(start = 16.dp),
                               fontSize = 20.sp,
                               fontWeight = FontWeight.Bold
                           )
                       }
                   }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Đơn Mua", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        TextButton(
                            onClick = {
                                // Chuyển màn hình Lịch sử mua hàng
                                //Chuyển đến màn hình OrderListScreen(Lịch sử mua hàng)
                                //ở tab có vị tris thứ 3
                                navcontroller.navigate("OrderListScreen/3")
                            }
                        ) {
                            Text(text = "Lịch sử mua hàng")
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OrderStatusItem(
                                icon = Icons.Filled.VerifiedUser,
                                description = "Chờ xác nhận",
                                //Chuyen den tab chờ xác nhận
                                onclick = {
                                    navcontroller.navigate("OrderListScreen/0")
                                    //Chuyển đến màn hình OrderListScreen(Lịch sử mua hàng)
                                    //ở tab có vị trí thứ 0
                                }
                            )
                            OrderStatusItem(
                                icon = Icons.Filled.LocalShipping,
                                description = "Chờ giao hàng",
                                //Chuyển màn hình tab Chờ giao hàng
                                onclick = {
                                    //Chuyển đến màn hình OrderListScreen(Lịch sử mua hàng)
                                    //ở tab có vị trí thứ 1
                                    navcontroller.navigate("OrderListScreen/1")
                                }
                            )
                            OrderStatusItem(
                                icon = Icons.Filled.LocalShipping,
                                description = "Chờ lấy hàng",
                                //Chuyển màn hình tab Chờ lấy hàng
                                onclick = {
                                    //Chuyển đến màn hình OrderListScreen(Lịch sử mua hàng)
                                    //ở tab có vị trí thứ 2
                                    navcontroller.navigate("OrderListScreen/2")
                                }
                            )
                            OrderStatusItem(
                                icon = Icons.Filled.Star,
                                description = "Đánh Giá",
                                //Chuyển màn hình đánh giá sản phẩm
                                onclick = {
                                    /*Thực hiện chức năng chuyển màn hình đánh giá*/
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Sản phẩm đề cử", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        FlowRow(
                            mainAxisSpacing = 16.dp,
                            crossAxisSpacing = 16.dp,
                            modifier = Modifier.fillMaxWidth(),
                            mainAxisAlignment = MainAxisAlignment.Center
                        ) {
                            products.forEach { product ->
                                ProductCard(product)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.ImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = product.NameProduct,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Giá: ${product.Price}",
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}

@Composable
fun OrderStatusItem(icon: ImageVector, description: String,onclick:()->Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onclick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                icon,
                contentDescription = description,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            modifier = Modifier.size(60.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
