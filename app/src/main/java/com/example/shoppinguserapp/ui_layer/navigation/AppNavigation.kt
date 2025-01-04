package com.example.shoppinguserapp.ui_layer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
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
import com.example.shoppinguserapp.ui_layer.screens.CartScreenUI
import com.example.shoppinguserapp.ui_layer.screens.EachProductDetailScreenUI
import com.example.shoppinguserapp.ui_layer.screens.HomeScreenUI
import com.example.shoppinguserapp.ui_layer.screens.NotificationScreenUI
import com.example.shoppinguserapp.ui_layer.screens.PaymentScreenUI
import com.example.shoppinguserapp.ui_layer.screens.PaymentSuccessScreenUI
import com.example.shoppinguserapp.ui_layer.screens.ProfileScreenUI
import com.example.shoppinguserapp.ui_layer.screens.SeeMoreProductScreenUI
import com.example.shoppinguserapp.ui_layer.screens.ShippingScreenUI
import com.example.shoppinguserapp.ui_layer.screens.SignInScreenUI
import com.example.shoppinguserapp.ui_layer.screens.SignUpScreenUI
import com.example.shoppinguserapp.ui_layer.screens.WishListScreenUI
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val currentDestinationAsState = navController.currentBackStackEntryAsState()
    val currentDestination = currentDestinationAsState.value?.destination?.route
    val shouldShowBottomBar = remember(currentDestination) {
        mutableStateOf(
            when (currentDestination) {
                Routes.SignInScreen::class.qualifiedName,
                Routes.SignUpScreen::class.qualifiedName -> false
                else -> true
            }
        )
    }

    val isUserLoggedIn = remember {  mutableStateOf(firebaseAuth.currentUser != null) }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routes.SignInScreen::class.qualifiedName, Routes.SignUpScreen::class.qualifiedName -> false
            else -> true
        }
    }

    val startScreen = if (isUserLoggedIn.value) {
        SubNavigation.MainHomeScreen
    } else {
        SubNavigation.LoginSignUpScreen
    }

    Scaffold(
//            containerColor = Color(0xFFFEEEBD8),
        bottomBar = {
            if (shouldShowBottomBar.value) {
                BottomNavigation(navController = navController,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { selectedItemIndex = it })
            }
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (shouldShowBottomBar.value) Modifier.padding(it) else Modifier)
        ) {
            NavHost(navController = navController, startDestination = startScreen) {

                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.SignInScreen) {
                    composable<Routes.SignInScreen> {
                        SignInScreenUI(
                            navController = navController, isUserLoggedIn = isUserLoggedIn
                        )
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreenUI(navController = navController)
                    }
                }
                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen> {
                        HomeScreenUI(navController = navController)
                    }
                    composable<Routes.EachProductDetailScreen> {
                        val productId = it.arguments?.getString("productId")
//                        val data = it.toRoute<Routes.EachProductDetailScreen>()
                        EachProductDetailScreenUI(
                            navController = navController, productId = productId.toString()

                        )
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
                        ShippingScreenUI(navController = navController)
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


                }
            }
        }
    }
}

