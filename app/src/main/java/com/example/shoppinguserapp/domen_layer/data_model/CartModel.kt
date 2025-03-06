package com.example.shoppinguserapp.domen_layer.data_model

data class CartModel(
    var productId : String = "",
    val productName : String = "",
    val description : String = "",
    val qty : String = "",
    val price : String = "",
    val finalPrice : String = "",
    val category : String = "",
    val imageUrl : String = "",
    val color : String = "",
    val size : String = "",
    val date : Long = System.currentTimeMillis()
)