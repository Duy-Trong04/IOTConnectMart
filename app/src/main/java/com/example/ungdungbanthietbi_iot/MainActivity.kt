package com.example.ungdungbanthietbi_iot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.naviation.NavGraph
import com.example.ungdungbanthietbi_iot.screen.Setting.ChangePassword
import com.example.ungdungbanthietbi_iot.screen.Setting.ContactScreen
import com.example.ungdungbanthietbi_iot.screen.personal.AccountSettingsScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditProfileScreen
import com.example.ungdungbanthietbi_iot.screen.personal.OrderListScreen
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UngDungBanThietBi_IOTTheme {
                navController = rememberNavController()
                NavGraph(navController = navController)
                //CartScreen()
                //FavoritesScreen()
                //CheckoutScreen()
                //SearchScreen()
                //SearchResultsScreen()
                //AddressSelectionScreen()
                //AddAddressScreen()
                //OrderDetailsScreen()
                //RatingScreen()

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

