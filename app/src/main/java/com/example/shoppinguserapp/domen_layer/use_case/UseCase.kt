package com.example.shoppinguserapp.domen_layer.use_case

import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.repo.Repo
import javax.inject.Inject

class UseCase @Inject constructor(private val repo: Repo) {

    fun getAllCategory() = repo.getCategories()
    fun getAllProducts() = repo.getProducts()

    fun registerUser(userData: UserData) = repo.registerUser(userData)
    fun loginUser(userEmail: String, userPassword: String) =
        repo.loginUser(userEmail, userPassword)

    fun getUserDetails(userId: String) = repo.getUserDetails(userId)
    fun updateUserDetails(userId: String, userData: UserData) = repo.updateUserDetails(userId, userData)

    fun getProductById(productID: String) = repo.getProductById(productID)

}