package com.example.ungdungbanthietbi_iot

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController:NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Check_Out.route
    ) {
        composable(
            route = Screen.Check_Out.route
        ){
            CheckoutScreen(navController)
        }
        composable(
            route = Screen.Add_Address.route
        ){
            AddAddressScreen(navController)
        }
        composable(
            route = Screen.Address_Selection.route
        ){
            AddressSelectionScreen(navController)
        }
        composable(
            route = Screen.Order_Detail.route
        ){
            OrderDetailsScreen(navController)
        }
    }
}