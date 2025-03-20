package com.example.shoppinguserapp.domen_layer.data_model




data class Products(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val finalPrice: String = "",
    val category: String = "",
    val date: Long = System.currentTimeMillis(),
    val image: String = "",
    val availableUnits: Int = 0,
    var productId: String = ""
)

fun Products.toCartModel(
    qty: Int = 1,
    color: String = "",
    size: String = ""

) : CartModel {
    return CartModel(

        productId = this.productId,
        name = this.name,
        description = this.description,
        price = this.price,
        finalPrice = this.finalPrice,
        category = this.category,
        date = this.date,
        imageUrl = this.image,
        qty = qty,
        color = color,
        size = size
    )
}

