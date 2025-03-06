package com.example.shoppinguserapp.domen_layer.use_case

import android.net.Uri
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
import com.example.shoppinguserapp.domen_layer.data_model.Products
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
//Cart
    fun productCartUseCase(cartModel: CartModel) = repo.productCartRepo(cartModel)
    fun checkProductCartUseCase(productId: String) = repo.checkProductCartRepo(productId)
    fun getCartUseCase() = repo.getProductsCartRepo()




}