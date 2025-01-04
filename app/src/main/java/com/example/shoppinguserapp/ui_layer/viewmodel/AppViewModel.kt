package com.example.shoppinguserapp.ui_layer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.domen_layer.data_model.Category
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.use_case.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    private val _registerUserState = MutableStateFlow(RegisterUserState())
    val registerUserState = _registerUserState.asStateFlow()

    private val _loginUserState = MutableStateFlow(LoginUserState())
    val loginUserState = _loginUserState.asStateFlow()

    private val _getCategoryState = MutableStateFlow(GetCategoryState())
    val getCategoryState = _getCategoryState.asStateFlow()

    private val _getProductsState = MutableStateFlow(GetProductsState())
    val getProductsState = _getProductsState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val _getUserDetailsState = MutableStateFlow(GetUserDetailsState())
    val getUserDetailsState = _getUserDetailsState.asStateFlow()

    private val _updateUserDetailsState = MutableStateFlow(UpdateUserDetailsState())
    val updateUserDetailsState = _updateUserDetailsState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductsByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()


    fun registerUser(userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.registerUser(userData).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _registerUserState.value = RegisterUserState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _registerUserState.value =
                            RegisterUserState(isLoading = false, success = it.data)
                    }

                    is ResultState.Error -> {
                        _registerUserState.value =
                            RegisterUserState(error = it.exception.message.toString())
                    }
                }

            }
        }

    }

    fun clearSignUpState() {
        _registerUserState.value = _registerUserState.value.copy(success = null, error = null)
    }


    fun loginUser(userEmail: String, userPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.loginUser(userEmail, userPassword).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _loginUserState.value = LoginUserState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _loginUserState.value = LoginUserState(isLoading = false, success = it.data)
                    }

                    is ResultState.Error -> {
                        _loginUserState.value =
                            LoginUserState(error = it.exception.message.toString())
                    }
                }
            }

        }
    }

    fun clearLoginState() {
        _loginUserState.value = _loginUserState.value.copy(error = null, success = null)
    }

    fun getUserDetails(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getUserDetails(userId).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getUserDetailsState.value = GetUserDetailsState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getUserDetailsState.value = GetUserDetailsState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _getUserDetailsState.value =
                            GetUserDetailsState(error = it.exception.message.toString())
                    }
                }
            }
        }
    }

    fun updateUserDetails(userId: String, userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateUserDetails(userId, userData).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _updateUserDetailsState.value = UpdateUserDetailsState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _updateUserDetailsState.value = UpdateUserDetailsState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _updateUserDetailsState.value =
                            UpdateUserDetailsState(error = it.exception.message.toString())
                    }
                }
            }
        }
    }

    fun getAllCategory() {
        viewModelScope.launch {
            useCase.getAllCategory().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getCategoryState.value = GetCategoryState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getCategoryState.value = GetCategoryState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _getCategoryState.value =
                            GetCategoryState(error = it.exception.message.toString())
                    }
                }
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            useCase.getAllProducts().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getProductsState.value = GetProductsState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getProductsState.value = GetProductsState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _getProductsState.value =
                            GetProductsState(error = it.exception.message.toString())
                    }
                }
            }

        }
    }

    init {
        getHomeScreenData()
    }

    fun getHomeScreenData() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                useCase.getAllCategory(), useCase.getAllProducts()
            ) { categoryState, productState ->
                when {
                    categoryState is ResultState.Loading || productState is ResultState.Loading -> {
                        HomeScreenState(isLoading = true)
                    }

                    categoryState is ResultState.Success && productState is ResultState.Success -> {
                        HomeScreenState(
                            category = categoryState.data, products = productState.data
                        )
                    }

                    categoryState is ResultState.Error -> {

                        HomeScreenState(error = categoryState.exception.message.toString())
                    }

                    productState is ResultState.Error -> {

                        HomeScreenState(error = productState.exception.message.toString())
                    }

                    else -> HomeScreenState()

                }
            }.collect { state ->
                _homeScreenState.value = state
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getProductById(productId).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getProductByIdState.value = GetProductsByIdState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getProductByIdState.value = GetProductsByIdState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _getProductByIdState.value =
                            GetProductsByIdState(error = it.exception.message.toString())
                    }

                }
            }

        }

    }


}


data class GetCategoryState(
    val isLoading: Boolean = false,
    val success: List<Category?> = emptyList(),
    val error: String = ""

)

data class GetProductsState(
    val isLoading: Boolean = false,
    var success: List<Products?> = emptyList(),
    val error: String = ""
)

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val category: List<Category>? = null,
    val products: List<Products>? = null
)

data class RegisterUserState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String? = null
)

data class LoginUserState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String? = null
)

data class GetProductsByIdState(
    val isLoading: Boolean = false,
    val success: Products? = null,
    val error: String = ""
)

data class GetUserDetailsState(
    val isLoading: Boolean = false,
    val success: UserData? = null,
    val error: String = ""
)

data class UpdateUserDetailsState(
    val isLoading: Boolean = false,
    var success: String? = null,
    val error: String = ""
)