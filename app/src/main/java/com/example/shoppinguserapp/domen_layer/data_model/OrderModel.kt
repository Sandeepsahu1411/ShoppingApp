package com.example.shoppinguserapp.domen_layer.data_model

data class OrderModel(
    val orderId : String = "",
    val products: List<ProductItem> = emptyList(),
    val date : Long = System.currentTimeMillis(),
    val totalPrice : String = "",
    val transactionMethod : String = "",
    val transactionId : String = "",
    val userAddress : String = "",
    val city : String = "",
    val countryRegion : String = "",
    val userEmail : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val mobileNo : String = "",
    val postalCode : String = "",
    val status: String = "Processed"
)

data class ProductItem(
    val productId: String = "",
    val productName: String = "",
    val productDes: String = "",
    val productQty: String = "",
    val productPrice: String = "",
    val productFinalPrice: String = "",
    val productCategory: String = "",
    val productImageUrl: String = "",
    val color: String = "",
    val size: String = ""
)
