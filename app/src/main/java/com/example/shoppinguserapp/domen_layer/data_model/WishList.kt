package com.example.shoppinguserapp.domen_layer.data_model

data class WishList(
    val userID: String = "",
    val productID: String = "",
    val date: Long = System.currentTimeMillis()

)
