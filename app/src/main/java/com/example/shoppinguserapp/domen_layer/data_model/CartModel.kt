package com.example.shoppinguserapp.domen_layer.data_model

data class CartModel(
    var productId : String = "",
    val name : String = "",
    val description : String = "",
    val qty : Int = 1,
    val price : String = "",
    val finalPrice : String = "",
    val category : String = "",
    val imageUrl : String = "",
    val color : String = "",
    val size : String = "",
    val date : Long = System.currentTimeMillis()
)