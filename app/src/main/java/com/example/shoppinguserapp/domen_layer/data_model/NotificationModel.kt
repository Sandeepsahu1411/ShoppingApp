package com.example.shoppinguserapp.domen_layer.data_model



data class NotificationModel(
    val notificationId: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val seen: Boolean = false,
    val imageUrl: String = ""
)
