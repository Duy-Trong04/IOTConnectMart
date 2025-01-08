package com.example.ungdungbanthietbi_iot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.NavGraph
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme

class MainActivity : ComponentActivity() {
    private val deviceViewModel by viewModels<DeviceViewModel>()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UngDungBanThietBi_IOTTheme {
                navController = rememberNavController()
                NavGraph(navController = navController, deviceViewModel = deviceViewModel)
            }
        }
    }
}

