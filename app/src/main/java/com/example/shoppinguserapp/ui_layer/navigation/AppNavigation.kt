package com.example.shoppinguserapp.ui_layer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.shoppinguserapp.ui_layer.permission.NotificationPermission
import com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen.CartScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.EachCategoryScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.EachProductDetailScreenUI
import com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen.HomeScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.NotificationScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.OrderScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.PaymentScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.PaymentSuccessScreenUI
import com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen.ProfileScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.SeeMoreProductScreenUI
import com.example.shoppinguserapp.ui_layer.screens.other_screen.ShippingScreenUI
import com.example.shoppinguserapp.ui_layer.screens.start_screen.SignInScreenUI
import com.example.shoppinguserapp.ui_layer.screens.start_screen.SignUpScreenUI
import com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen.WishListScreenUI
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val currentDestination by navController.currentBackStackEntryAsState()

    val screensWithBottomBar = listOf(
        Routes.HomeScreen::class.qualifiedName,
        Routes.WishListScreen::class.qualifiedName,
        Routes.CartScreen::class.qualifiedName,
        Routes.ProfileScreen::class.qualifiedName
    )

    val shouldShowBottomBar = currentDestination?.destination?.route in screensWithBottomBar

    val shouldUseFullScreen = currentDestination?.destination?.route in listOf(
        Routes.SignInScreen::class.qualifiedName,
        Routes.SignUpScreen::class.qualifiedName
    )

    LaunchedEffect(currentDestination) {
        selectedItemIndex = when (currentDestination?.destination?.route) {
            Routes.HomeScreen::class.qualifiedName -> 0
            Routes.WishListScreen::class.qualifiedName -> 1
            Routes.CartScreen::class.qualifiedName -> 2
            Routes.ProfileScreen::class.qualifiedName -> 3
            else -> selectedItemIndex
        }
    }

    val startScreen = if (firebaseAuth.currentUser != null) {
        SubNavigation.MainHomeScreen
    } else {
        SubNavigation.LoginSignUpScreen
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigation(
                    navController = navController,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { index ->
                        selectedItemIndex = index
                        when (index) {
                            0 -> navController.navigate(Routes.HomeScreen::class.qualifiedName!!)
                            1 -> navController.navigate(Routes.WishListScreen::class.qualifiedName!!)
                            2 -> navController.navigate(Routes.CartScreen::class.qualifiedName!!)
                            3 -> navController.navigate(Routes.ProfileScreen::class.qualifiedName!!)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (shouldUseFullScreen) Modifier
                    else Modifier.padding(innerPadding)
                )
        ) {
            NavHost(navController = navController, startDestination = startScreen) {

                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.SignInScreen) {
                    composable<Routes.SignInScreen> {
                        SignInScreenUI(
                            navController = navController,
                        )
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreenUI(navController = navController, firebaseAuth = firebaseAuth)
                    }
                }
                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen> {
                        NotificationPermission()
                        HomeScreenUI(navController = navController)
                    }
                    composable<Routes.EachProductDetailScreen> {
                        val productId = it.arguments?.getString("productId")
//                        val data = it.toRoute<Routes.EachProductDetailScreen>()
                        EachProductDetailScreenUI(
                            navController = navController,
                            productId = productId.toString(),
                        )
                    }
                    composable<Routes.EachCategoryScreen> {
                        val categoryName = it.arguments?.getString("categoryName")
                        EachCategoryScreenUI(navController, categoryName.toString())
                    }
                    composable<Routes.WishListScreen> {
                        WishListScreenUI(navController = navController)

                    }

                    composable<Routes.CartScreen> {
                        CartScreenUI(navController = navController)

                    }
                    composable<Routes.ProfileScreen> {
                        ProfileScreenUI(navController = navController, firebaseAuth = firebaseAuth)

                    }
                    composable<Routes.SeeMoreProductsScreen> {
                        SeeMoreProductScreenUI(navController = navController)
                    }
                    composable<Routes.ShippingScreen> {
                        val data = it.toRoute<Routes.ShippingScreen>()
                        val productId = it.arguments?.getString("productId")
                        val productSize = it.arguments?.getString("productSize")
                        val productColor = it.arguments?.getString("productColor")
                        val productQty = it.arguments?.getString("productQty")
                        ShippingScreenUI(
                            navController = navController,
                            productId.toString(),
                            productSize.toString(),
                            productColor.toString(),
                            productQty.toString()
                        )
                    }

                    composable<Routes.PaymentScreen> {
                        PaymentScreenUI(navController)
                    }
                    composable<Routes.PaymentSuccessScreen> {

                        PaymentSuccessScreenUI(navController)
                    }
                    composable<Routes.NotificationScreen> {
                        NotificationScreenUI(navController)
                    }
                    composable<Routes.OrderScreen>{
                        OrderScreenUI(navController)
                    }


                }
            }
        }
    }
}


