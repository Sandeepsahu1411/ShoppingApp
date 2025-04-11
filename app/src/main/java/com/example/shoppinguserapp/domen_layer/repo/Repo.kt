package com.example.shoppinguserapp.domen_layer.repo

import android.app.Notification
import android.net.Uri
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
import com.example.shoppinguserapp.domen_layer.data_model.Category
import com.example.shoppinguserapp.domen_layer.data_model.NotificationModel
import com.example.shoppinguserapp.domen_layer.data_model.OrderModel
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.ShippingModel
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.data_model.WishListDataModel
import kotlinx.coroutines.flow.Flow


interface Repo {

    fun registerUser(userData: UserData): Flow<ResultState<String>>
    fun loginUser(userEmail: String, userPassword: String): Flow<ResultState<String>>

    fun getUserDetails(userId: String): Flow<ResultState<UserData>>
    fun updateUserDetails(userId: String, userData: UserData): Flow<ResultState<String>>
    fun uploadImage(imageUri: Uri): Flow<ResultState<String>>

    fun getCategories(): Flow<ResultState<List<Category>>>
    fun getProducts(): Flow<ResultState<List<Products>>>
    fun getProductById(productID: String): Flow<ResultState<Products>>

    fun addWishListRepo(products: Products): Flow<ResultState<String>>
    fun checkWishlistRepo(productId: String): Flow<ResultState<Boolean>>
    fun getWishListRepo(): Flow<ResultState<List<Products>>>
    fun deleteWishListRepo(productId: String): Flow<ResultState<String>>


    fun productCartRepo(cartModel: CartModel): Flow<ResultState<String>>
    fun checkProductCartRepo(productId: String): Flow<ResultState<Boolean>>
    fun getProductsCartRepo(): Flow<ResultState<List<CartModel>>>
    fun deleteProductCartRepo(): Flow<ResultState<String>>
    fun updateProductCartRepo(productId: String, newQty: Int): Flow<ResultState<String>>

    fun addShippingRepo(shippingModel: ShippingModel): Flow<ResultState<String>>
    fun getShippingRepo(): Flow<ResultState<ShippingModel>>

    fun addOrderRepo(orderModel: OrderModel): Flow<ResultState<String>>
    fun getOrderRepo(): Flow<ResultState<List<OrderModel>>>

    fun addNotificationRepo(notification: NotificationModel): Flow<ResultState<String>>
    fun getNotificationsRepo(): Flow<ResultState<List<NotificationModel>>>
    fun markNotificationAsSeenRepo(notificationId: String): Flow<ResultState<String>>


}