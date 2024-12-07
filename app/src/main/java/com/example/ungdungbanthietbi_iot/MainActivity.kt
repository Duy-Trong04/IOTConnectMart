package com.example.ungdungbanthietbi_iot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.naviation.NavGraph
import com.example.ungdungbanthietbi_iot.screen.Setting.ChangePassword
import com.example.ungdungbanthietbi_iot.screen.Setting.ContactScreen
import com.example.ungdungbanthietbi_iot.screen.personal.AccountSettingsScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditProfileScreen
import com.example.ungdungbanthietbi_iot.screen.personal.OrderListScreen
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UngDungBanThietBi_IOTTheme {
                val controller = rememberNavController()
                NavGraph(controller)
                //PersonalScreen()
                //AccountSettingsScreen()
                //ChangePassword()
                //ContactScreen()
                //EditProfileScreen()
                //OrderListScreen()
            }
        }
    }
}
