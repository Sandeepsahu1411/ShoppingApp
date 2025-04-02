package com.example.shoppinguserapp.ui_layer.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import kotlin.collections.forEachIndexed

@Composable
fun BottomNavigation(
    navController: NavController, selectedItemIndex: Int, onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavigationItem("Wishlist", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavigationItem("Cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavigationItem("Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
    )
    val isGestureNavigation = isGestureNavigationEnabled()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!isGestureNavigation) Modifier.navigationBarsPadding() else Modifier)
            .height(70.dp)
            .background(Color(0xFFEEEBD8), RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable {
                            onItemSelected(index)
                            when (index) {
                                0 -> navController.navigate(Routes.HomeScreen) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }

                                1 -> navController.navigate(Routes.WishListScreen)
                                2 -> navController.navigate(Routes.CartScreen)
                                3 -> navController.navigate(Routes.ProfileScreen)

                            }
                        }, contentAlignment = Alignment.Companion.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.Companion.CenterHorizontally,
                        modifier = if (selectedItemIndex == index) Modifier.offset(y = (-10).dp) else Modifier
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (selectedItemIndex == index) Color(0xFFf68b8b) else Color.Companion.Transparent,
                                    RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
                                )
                                .size(50.dp), contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = if (selectedItemIndex == index) Color.Companion.White else Color.Companion.Black
                            )
                        }
                        AnimatedVisibility(selectedItemIndex == index) {
                            Text(
                                text = item.title,
                                color = if (selectedItemIndex == index) Color.Companion.Black else Color.Companion.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


data class BottomNavigationItem(
    val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector
)

@Composable
fun isGestureNavigationEnabled(): Boolean {
    val context = LocalContext.current
    val resources = context.resources
    val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")

    // 2 => Gesture Navigation, 1 => 2-Button Nav, 0 => 3-Button Nav
    return if (resourceId > 0) {
        resources.getInteger(resourceId) == 2
    } else {
        false
    }
}
