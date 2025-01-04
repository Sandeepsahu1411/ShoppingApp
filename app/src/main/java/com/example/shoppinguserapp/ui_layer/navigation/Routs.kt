package com.example.shoppinguserapp.ui_layer.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{
    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object LoginSignUpScreen : SubNavigation()
}

sealed class Routes {

    @Serializable
    object SignInScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object CartScreen

    @Serializable
    object WishListScreen

    @Serializable
    object ShippingScreen

    @Serializable
    object PaymentScreen

    @Serializable
    object SeeMoreProductsScreen


    @Serializable
    data class EachProductDetailScreen(
        val productId: String
    )
    @Serializable
    object PaymentSuccessScreen

    @Serializable
    object NotificationScreen


}
