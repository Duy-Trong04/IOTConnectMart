package com.example.ungdungbanthietbi_iot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.navigation.NavGraph
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

