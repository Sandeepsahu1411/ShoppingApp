package com.example.shoppinguserapp.ui_layer.screens

//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.navigation.NavController
//import com.example.shoppinguserapp.R
//import com.example.shoppinguserapp.ui_layer.navigation.Routes
//import kotlinx.coroutines.delay
//
//@Composable
//fun SplashScreenUI(navController: NavController) {
//
//    LaunchedEffect(key1 = true) {
//        delay(2000)
//        navController.navigate(Routes.SignInScreen) {
//            popUpTo(Routes.SplashScreen) {
//                inclusive = true
//            }
//
//        }
//    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color(0xfff68b8b)),
//        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
//        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.splash_screen),
//            contentDescription = "Splash",
//            modifier = Modifier,
//            contentScale = ContentScale.Companion.Crop
//        )
//    }
//
//}