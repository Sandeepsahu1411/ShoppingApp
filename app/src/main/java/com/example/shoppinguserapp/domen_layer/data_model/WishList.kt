package com.example.shoppinguserapp.domen_layer.data_model

data class WishListDataModel(
    val userID: String = "",
    var productID: String = "",
    val date: Long = System.currentTimeMillis()

)
