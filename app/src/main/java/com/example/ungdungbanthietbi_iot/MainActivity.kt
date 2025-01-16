package com.example.ungdungbanthietbi_iot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.image_device.ImageViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.data.slideshow.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.NavGraph
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme

class MainActivity : ComponentActivity() {
    private val deviceViewModel by viewModels<DeviceViewModel>()
    private val slideShowViewModel by viewModels<SlideShowViewModel>()
    private val imageViewModel by viewModels<ImageViewModel>()
    private val reviewViewModel by viewModels<ReviewViewModel>()
    private val accountViewModel by viewModels<AccountViewModel>()
    private val customerViewModel by viewModels<CustomerViewModel>()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UngDungBanThietBi_IOTTheme {
                navController = rememberNavController()
                NavGraph(navController = navController,
                    deviceViewModel = deviceViewModel,
                    slideShowViewModel = slideShowViewModel,
                    imageViewModel = imageViewModel,
                    reviewViewModel = reviewViewModel,
                    accountViewModel = accountViewModel,
                    customerViewModel = customerViewModel
                )
            }
        }
    }
}

