package com.example.ungdungbanthietbi_iot.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.models.Category
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun ParentCategoryItem(
    category: Category,
    navController: NavController,
    username: String?,
    idCustomer: String?,
    token: String?,
    depth: Int,
    navdrawerState: DrawerState,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val isSelected = category.name == selectedCategory

    Column {
        NavigationDrawerItem(
            label = {
                Text(
                    text = category.name,
                    color = if (isSelected) Color(0xFF1E88E5) else Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            selected = isSelected,
            onClick = {
                if (category.children.isNotEmpty()) {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        onCategorySelected(category.name)
                    } else {
                        onCategorySelected(null)
                    }
                } else {
                    scope.launch {
                        navdrawerState.close()
                        val route = if (username != null && idCustomer != null)
                            Screen.Category_Screen.route + "?category=${category.name}&username=${username}&idCustomer=$idCustomer&token=$token"
                        else
                            Screen.Category_Screen.route + "?category=${category.name}"
                        navController.navigate(route)
                    }
                }
            },
            modifier = Modifier
                .padding(start = (depth * 24).dp),
            badge = {
                if (category.children.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng",
                        tint = if (isSelected) Color(0xFF5D9EFF) else Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(if (isExpanded) 180f else 0f)
                    )
                }
            }
        )
        if (isExpanded && category.children.isNotEmpty()) {
            category.children.filter { !it.is_hide }.forEach { child ->
                ChildCategoryItem(
                    category = child,
                    navController = navController,
                    username = username,
                    idCustomer = idCustomer,
                    token = token,
                    depth = depth + 1,
                    navdrawerState = navdrawerState,
                    isParentSelected = isSelected
                )
            }
        }
    }
}

@Composable
fun ChildCategoryItem(
    category: Category,
    navController: NavController,
    username: String?,
    idCustomer: String?,
    token: String?,
    depth: Int,
    navdrawerState: DrawerState,
    isParentSelected: Boolean
) {
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        label = {
            Text(
                text = category.name,
                color = if (isParentSelected) Color(0xFF1E88E5) else Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },
        selected = false,
        onClick = {
            scope.launch {
                navdrawerState.close()
                val route = if (username != null && idCustomer != null)
                    Screen.Category_Screen.route + "?category=${category.name}&username=${username}&idCustomer=$idCustomer&token=$token"
                else
                    Screen.Category_Screen.route + "?category=${category.name}"
                navController.navigate(route)
            }
        },
        modifier = Modifier
            .padding(start = (depth * 24).dp)
    )
}
