package com.example.shoppinguserapp.data_layer.repoimple

import android.net.Uri
import com.example.shoppinguserapp.common.ADD_TO_CART
import com.example.shoppinguserapp.common.ADD_TO_CART_BY_USER
import com.example.shoppinguserapp.common.CATEGORY
import com.example.shoppinguserapp.common.ORDER
import com.example.shoppinguserapp.common.PRODUCTS
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.common.SHIPPING
import com.example.shoppinguserapp.common.USERS
import com.example.shoppinguserapp.common.USER_TOKEN
import com.example.shoppinguserapp.common.WISHLIST
import com.example.shoppinguserapp.common.WISHLIST_BY_USER
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
import com.example.shoppinguserapp.domen_layer.data_model.Category
import com.example.shoppinguserapp.domen_layer.data_model.OrderModel
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.ShippingModel
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class Repoimple @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseMessaging: FirebaseMessaging
) : Repo {
    override fun registerUser(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnSuccessListener {
                firebaseFireStore.collection(USERS).document(it.user!!.uid.toString()).set(userData)
                    .addOnSuccessListener {
                        trySend(ResultState.Success("User Registered Successfully"))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it))
                    }
                updateFcmToken(firebaseAuth.currentUser?.uid.toString())

            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose {
            close()
        }


    }

    override fun loginUser(
        userEmail: String, userPassword: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {
            trySend(ResultState.Success("User Logged In Successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it))
        }
        updateFcmToken(firebaseAuth.currentUser?.uid.toString())
        awaitClose {
            close()
        }
    }

    override fun getUserDetails(uid: String): Flow<ResultState<UserData>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(USERS).document(uid).get().addOnSuccessListener {
            val data = it.toObject(UserData::class.java)!!.apply {
                this.userId = it.id
            }
            trySend(ResultState.Success(data))
        }.addOnFailureListener {
            trySend(ResultState.Error(it))
        }
        awaitClose {
            close()
        }
    }

    override fun updateUserDetails(
        userId: String, userData: UserData
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(USERS).document(userId).set(userData).addOnSuccessListener {
            trySend(ResultState.Success("User Updated Successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it))
        }
        awaitClose {
            close()
        }
    }

    override fun getCategories(): Flow<ResultState<List<Category>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFireStore.collection(CATEGORY).get().addOnSuccessListener {
            val category = it.documents.mapNotNull {
                it.toObject(Category::class.java)
            }

            trySend(ResultState.Success(category))
        }.addOnFailureListener {
            trySend(ResultState.Error(it))
        }
        awaitClose {
            close()
        }
    }

    override fun getProducts(): Flow<ResultState<List<Products>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFireStore.collection(PRODUCTS).get().addOnSuccessListener {
            val productData = it.documents.mapNotNull { document ->
                val product = document.toObject(Products::class.java)
                product?.productId = document.id
                product
            }

            trySend(ResultState.Success(productData))
        }.addOnFailureListener { exception ->
            trySend(ResultState.Error(exception))
        }

        awaitClose { close() }
    }

    override fun getProductById(productID: String): Flow<ResultState<Products>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFireStore.collection(PRODUCTS).document(productID).get().addOnSuccessListener {
            val data = it.toObject(Products::class.java)!!.apply {
                this.productId = it.id
            }
            trySend(ResultState.Success(data))
        }.addOnFailureListener { exception ->
            trySend(ResultState.Error(exception))
        }
        awaitClose {
            close()
        }


    }

    override fun addWishListRepo(products: Products): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(WISHLIST).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                WISHLIST_BY_USER
            ).whereEqualTo("productId", products.productId).get().addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    firebaseFireStore.collection(WISHLIST)
                        .document(firebaseAuth.currentUser?.uid.toString()).collection(
                            WISHLIST_BY_USER
                        ).document(it.documents[0].id).delete().addOnSuccessListener {
                            trySend(ResultState.Success("Removed to Wishlist"))
                            close()
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it))
                            close()
                        }
                    return@addOnSuccessListener
                } else {
                    firebaseFireStore.collection(WISHLIST)
                        .document(firebaseAuth.currentUser?.uid.toString())
                        .collection(WISHLIST_BY_USER).document().set(products)
                        .addOnSuccessListener {
                            trySend(ResultState.Success("Added to Wishlist"))
                            close()
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it))
                            close()
                        }
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
                return@addOnFailureListener
            }
        awaitClose {
            close()
        }
    }

    override fun checkWishlistRepo(productId: String): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(WISHLIST).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                WISHLIST_BY_USER
            ).whereEqualTo("productId", productId).get().addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    trySend(ResultState.Success(true))
                } else {
                    trySend(ResultState.Success(false))
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose {
            close()
        }

    }

    override fun getWishListRepo(): Flow<ResultState<List<Products>>> = callbackFlow {

        trySend(ResultState.Loading)
        firebaseFireStore.collection(WISHLIST).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                WISHLIST_BY_USER
            ).get().addOnSuccessListener {
                val productData = it.documents.mapNotNull {
                    it.toObject(Products::class.java)
                }
                trySend(ResultState.Success(productData))
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose {
            close()
        }

    }

    override fun deleteWishListRepo(productId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(WISHLIST).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                WISHLIST_BY_USER
            ).whereEqualTo("productId", productId).get().addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    firebaseFireStore.collection(WISHLIST)
                        .document(firebaseAuth.currentUser?.uid.toString()).collection(
                            WISHLIST_BY_USER
                        ).document(it.documents[0].id).delete().addOnSuccessListener {
                            trySend(ResultState.Success("Removed to Wishlist"))
                            close()
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it))
                            close()
                        }
                }
            }
        awaitClose {
            close()
        }


    }

    override fun uploadImage(imageUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseStorage.reference.child("userProfile/${imageUri.lastPathSegment}")
            .putFile(imageUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    trySend(ResultState.Success(uri.toString()))
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }.addOnCanceledListener {
                trySend(ResultState.Error(Exception("Upload Cancelled")))
            }
        awaitClose {
            close()
        }

    }

    override fun productCartRepo(cartModel: CartModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString())
                .collection(
                    ADD_TO_CART_BY_USER
                ).whereEqualTo("productId", cartModel.productId).get().addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        firebaseFireStore.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .collection(ADD_TO_CART_BY_USER).document(it.documents[0].id)
                            .delete()
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Removed from Cart"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it))
                                close()
                            }
                        return@addOnSuccessListener

                    } else {
                        firebaseFireStore.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .collection(ADD_TO_CART_BY_USER).document().set(cartModel)
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Added to Cart"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it))
                                close()
                            }
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it))
                    return@addOnFailureListener
                }

            awaitClose {
                close()
            }

        }

    override fun checkProductCartRepo(productId: String): Flow<ResultState<Boolean>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString())
                .collection(ADD_TO_CART_BY_USER)
                .whereEqualTo("productId", productId).get().addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        trySend(ResultState.Success(true))
                    } else {
                        trySend(ResultState.Success(false))
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it))
                }

            awaitClose {
                close()
            }

        }

    override fun getProductsCartRepo(): Flow<ResultState<List<CartModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(ADD_TO_CART)
            .document(firebaseAuth.currentUser?.uid.toString())
            .collection(ADD_TO_CART_BY_USER).get().addOnSuccessListener {
                val data = it.documents.mapNotNull {
                    it.toObject(CartModel::class.java)
                }
                trySend(ResultState.Success(data))

            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose {
            close()
        }


    }

    override fun deleteProductCartRepo(): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString())
                .collection(ADD_TO_CART_BY_USER).get().addOnSuccessListener {
                    it.documents.forEach {
                        firebaseFireStore.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .collection(ADD_TO_CART_BY_USER).document(it.id).delete()
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Cart Cleared"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it))
                                close()
                            }
                    }
                }
            awaitClose { close() }
        }

    override fun updateProductCartRepo(
        productId: String, newQty: Int
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val userId = firebaseAuth.currentUser?.uid.toString()

        firebaseFireStore.collection(ADD_TO_CART).document(userId)
            .collection(ADD_TO_CART_BY_USER)
            .whereEqualTo("productId", productId).get().addOnSuccessListener { documents ->
                if (documents.documents.isNotEmpty()) {
                    val docId = documents.documents[0].id


                    firebaseFireStore.collection(ADD_TO_CART).document(userId)
                        .collection(ADD_TO_CART_BY_USER).document(docId).update("qty", newQty)
                        .addOnSuccessListener {
                            trySend(ResultState.Success("Quantity Updated"))
                            close()
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it))
                            close()
                        }
                } else {
                    trySend(ResultState.Error(Exception("Product not found in cart")))
                    close()
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
                close()
            }

        awaitClose {
            close()
        }
    }

    override fun addShippingRepo(shippingModel: ShippingModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(SHIPPING)
                .document(firebaseAuth.currentUser?.uid.toString()).set(shippingModel)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Address Added"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it))
                }
            awaitClose {
                close()
            }
        }

    override fun getShippingRepo(): Flow<ResultState<ShippingModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(SHIPPING)
            .document(firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val data = it.toObject(ShippingModel::class.java)
                trySend(ResultState.Success(data.let {
                    data ?: ShippingModel()
                }))
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose {
            close()
        }

    }

    override fun addOrderRepo(orderModel: OrderModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val updatedOrderModel = orderModel.copy(userId = firebaseAuth.currentUser?.uid.toString())
        firebaseFireStore.collection(ORDER)
            .add(updatedOrderModel)
            .addOnSuccessListener {
                trySend(ResultState.Success("Order Placed"))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }

        awaitClose { close() }
    }

    override fun getOrderRepo(): Flow<ResultState<List<OrderModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        firebaseFireStore.collection(ORDER)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val data = querySnapshot.documents.mapNotNull {
                    it.toObject(OrderModel::class.java)
                }
                trySend(ResultState.Success(data))
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error(exception))
            }

        awaitClose { close() }
    }

    fun updateFcmToken(userId: String) {
        firebaseMessaging.token.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result
                firebaseFireStore.collection(USER_TOKEN).document(userId).set(mapOf("token" to token))
            }
        }


    }


}