package com.example.shoppinguserapp.domen_layer.data_model

data class ShippingModel(
    val email : String = "",
    val mobileNo : String = "",
    val countryRegion : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val address : String = "",
    val city : String = "",
    val pinCode : String = "",
    val saveForNextTime : Boolean = false
)
