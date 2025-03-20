package com.example.shoppinguserapp.ui_layer.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation {
    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object LoginSignUpScreen : SubNavigation()
}

sealed class Routes {

    @Serializable
    object SignInScreen : Routes()

    @Serializable
    object SignUpScreen : Routes()

    @Serializable
    object HomeScreen : Routes()

    @Serializable
    object ProfileScreen : Routes()

    @Serializable
    object CartScreen : Routes()

    @Serializable
    object WishListScreen : Routes()

    @Serializable
    data class ShippingScreen(
        val productId : String,
        val productSize : String,
        val productColor : String,
        val productQty : String
    ) : Routes()

    @Serializable
    object PaymentScreen : Routes()

    @Serializable
    object SeeMoreProductsScreen : Routes()


    @Serializable
    data class EachProductDetailScreen(
        val productId: String
    ) : Routes()

    @Serializable
    data class EachCategoryScreen(
        val categoryName: String
    ) : Routes()

    @Serializable
    object PaymentSuccessScreen : Routes()

    @Serializable
    object NotificationScreen : Routes()
}
