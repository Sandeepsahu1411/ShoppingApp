package com.example.shoppinguserapp.domen_layer.repo

import android.net.Uri
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.domen_layer.data_model.Category
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.data_model.WishListDataModel
import kotlinx.coroutines.flow.Flow


interface Repo {

    fun registerUser(userData: UserData): Flow<ResultState<String>>
    fun loginUser(userEmail: String, userPassword: String): Flow<ResultState<String>>

    fun getUserDetails(userId: String): Flow<ResultState<UserData>>
    fun updateUserDetails(userId: String, userData: UserData): Flow<ResultState<String>>

    fun getCategories(): Flow<ResultState<List<Category>>>
    fun getProducts(): Flow<ResultState<List<Products>>>
    fun getProductById(productID: String): Flow<ResultState<Products>>

//    fun addWishListRepo(userID: String, productID: String): Flow<ResultState<String>>
//    fun getWishListRepo(userID: String): Flow<ResultState<List<String>>>

    fun addWishListRepo(products: Products): Flow<ResultState<String>>
    fun checkWishlistRepo(productId: String): Flow<ResultState<Boolean>>
    fun getWishListRepo(): Flow<ResultState<List<Products>>>

    fun uploadImage(imageUri: Uri): Flow<ResultState<String>>


}