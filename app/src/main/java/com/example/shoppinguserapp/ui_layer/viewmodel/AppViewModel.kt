package com.example.shoppinguserapp.ui_layer.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
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

    private val _addWishListState = MutableStateFlow(WishListState())
    val addWishListState = _addWishListState.asStateFlow()

    private val _checkWishlistState = MutableStateFlow(CheckWishlistState())
    val checkWishlistState = _checkWishlistState.asStateFlow()

    private val _getWishListState = MutableStateFlow(GetWishListState())
    val getWishlistState = _getWishListState.asStateFlow()

    private val _uploadImageState = MutableStateFlow(ImageUploadState())
    val uploadImageState = _uploadImageState.asStateFlow()

    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState = _addToCartState.asStateFlow()

    private val _checkProductCartState = MutableStateFlow(CheckProductCartState())
    val checkProductCartState = _checkProductCartState.asStateFlow()

    private val _getCartState = MutableStateFlow(GetProductsCartState())
    val getCartState = _getCartState.asStateFlow()

    private val _updateProductCartState = MutableStateFlow(UpdateProductCartState())


    private val _deleteProductCartState = MutableStateFlow(DeleteProductCartState())
    val deleteProductCartState = _deleteProductCartState.asStateFlow()


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

    fun addWishListModel(products: Products) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.addWishListUseCase(products).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _addWishListState.value = WishListState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _addWishListState.value = WishListState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _addWishListState.value =
                            WishListState(error = it.exception.message.toString())
                    }
                }

            }
        }
    }

    fun checkWishlistModel(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.checkWishlistUseCase(productId).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _checkWishlistState.value = CheckWishlistState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _checkWishlistState.value = CheckWishlistState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _checkWishlistState.value =
                            CheckWishlistState(error = it.exception.message.toString())
                    }
                }

            }
        }
    }

    fun getWishlistModel() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getWishListUseCase().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getWishListState.value = GetWishListState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getWishListState.value = GetWishListState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _getWishListState.value =
                            GetWishListState(error = it.exception.message.toString())
                    }
                }
            }

        }

    }

    fun resetWishlistState() {
        _addWishListState.value = WishListState()
        _checkWishlistState.value = CheckWishlistState()
        _getWishListState.value = GetWishListState()
    }

    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.uploadImageUseCase(imageUri).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _uploadImageState.value = ImageUploadState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _uploadImageState.value = ImageUploadState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _uploadImageState.value =
                            ImageUploadState(error = it.exception.message.toString())
                    }
                }
            }
        }
    }

    fun addProductCart(cartModel: CartModel) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.productCartUseCase(cartModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _addToCartState.value = AddToCartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _addToCartState.value = AddToCartState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _addToCartState.value =
                            AddToCartState(error = it.exception.message.toString())
                    }
                }
            }
        }
    }

    fun checkProductCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.checkProductCartUseCase(productId).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _checkProductCartState.value = CheckProductCartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _checkProductCartState.value = CheckProductCartState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _checkProductCartState.value =
                            CheckProductCartState(error = it.exception.message.toString())
                    }
                }
            }
        }

    }

    fun getProductsCart() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getCartUseCase().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getCartState.value = GetProductsCartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getCartState.value = GetProductsCartState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _getCartState.value =
                            GetProductsCartState(error = it.exception.message.toString())
                    }
                }
            }
        }

    }

    fun updateProductCart(productId: String, newQty: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateProductCartUseCase(productId, newQty).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _updateProductCartState.value = UpdateProductCartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _updateProductCartState.value = UpdateProductCartState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _updateProductCartState.value =
                            UpdateProductCartState(error = it.exception.message.toString())
                    }
                }
            }
        }

    }

    fun deleteProductCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.deleteProductCartUseCase(productId).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _deleteProductCartState.value = DeleteProductCartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _deleteProductCartState.value = DeleteProductCartState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _deleteProductCartState.value =
                            DeleteProductCartState(error = it.exception.message.toString())
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

data class WishListState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String = ""

)

data class CheckWishlistState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String = ""
)

data class GetWishListState(
    val isLoading: Boolean = false,
    val success: List<Products> = emptyList(),
    val error: String = ""
)

data class ImageUploadState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String? = null
)

data class AddToCartState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String? = null
)

data class CheckProductCartState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String = ""
)

data class GetProductsCartState(
    val isLoading: Boolean = false,
    val success: List<CartModel> = emptyList(),
    val error: String? = null
)

data class UpdateProductCartState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String? = null
)

data class DeleteProductCartState(
    val isLoading: Boolean = false,
    var success: String? = null,
    var error: String? = null
)