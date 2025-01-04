package com.example.shoppinguserapp.domen_layer.data_model


data class Products (
    val name: String ="",
    val description: String ="",
    val price: String ="",
    val finalPrice: String ="",
    val category: String ="",
    val date: Long =System.currentTimeMillis(),
    val image: String ="",
    val availableUnits: Int =0,
    var productId: String =""
)