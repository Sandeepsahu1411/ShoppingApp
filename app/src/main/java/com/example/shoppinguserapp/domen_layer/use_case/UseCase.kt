package com.example.shoppinguserapp.domen_layer.use_case

import android.net.Uri
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
import com.example.shoppinguserapp.domen_layer.data_model.NotificationModel
import com.example.shoppinguserapp.domen_layer.data_model.OrderModel
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.ShippingModel
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.repo.Repo
import javax.inject.Inject

class UseCase @Inject constructor(private val repo: Repo) {

//products & category
    fun getAllCategory() = repo.getCategories()
    fun getAllProducts() = repo.getProducts()
    fun getProductById(productID: String) = repo.getProductById(productID)
//User Auth
    fun registerUser(userData: UserData) = repo.registerUser(userData)
    fun loginUser(userEmail: String, userPassword: String) =
        repo.loginUser(userEmail, userPassword)
    fun getUserDetails(userId: String) = repo.getUserDetails(userId)
    fun updateUserDetails(userId: String, userData: UserData) = repo.updateUserDetails(userId, userData)
    fun uploadImageUseCase(imageUri: Uri) = repo.uploadImage(imageUri)
//Wishlist
    fun addWishListUseCase(products: Products) = repo.addWishListRepo(products)
    fun checkWishlistUseCase(productId: String) = repo.checkWishlistRepo(productId)
    fun getWishListUseCase() = repo.getWishListRepo()
    fun deleteWishListUseCase(productId: String) = repo.deleteWishListRepo(productId)
//Cart
    fun productCartUseCase(cartModel: CartModel) = repo.productCartRepo(cartModel)
    fun checkProductCartUseCase(productId: String) = repo.checkProductCartRepo(productId)
    fun getCartUseCase() = repo.getProductsCartRepo()
    fun deleteProductCartUseCase() = repo.deleteProductCartRepo()
    fun updateProductCartUseCase(productId: String, newQty: Int) = repo.updateProductCartRepo(productId, newQty)


//shipping
    fun addShippingUseCase(shippingModel: ShippingModel) = repo.addShippingRepo(shippingModel)
    fun getShippingUseCase() = repo.getShippingRepo()

// Order

    fun addOrderUseCase(orderModel: OrderModel) = repo.addOrderRepo(orderModel)
    fun getOrderUseCase() = repo.getOrderRepo()

//Notifications

    fun addNotificationUseCase(notificationModel: NotificationModel) = repo.addNotificationRepo(notificationModel)
    fun getNotificationUseCase() = repo.getNotificationsRepo()
    fun markNotificationAsReadUseCase(notificationId: String) =
        repo.markNotificationAsSeenRepo(notificationId)








}