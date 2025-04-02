package com.example.shoppinguserapp.ui_layer.screens.other_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes


@Composable
fun PaymentSuccessScreenUI(navController: NavController) {
    // Box to arrange items vertically

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // Lottie Animation
        LottieAnimation(
            modifier = Modifier
                .size(350.dp),
            resId = R.raw.order_placed
        )

        Text(
            text = "Order Placed",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF68B8B)
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Start Shopping Button
        Button(
            onClick = {
                navController.navigate(Routes.HomeScreen) {
                    popUpTo(Routes.HomeScreen) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF68B8B), contentColor = Color.White
            )
        ) {
            Text(
                text = "Start Shopping",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }

}

// Lottie Animation Composable
@Composable
fun LottieAnimation(modifier: Modifier = Modifier, resId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
