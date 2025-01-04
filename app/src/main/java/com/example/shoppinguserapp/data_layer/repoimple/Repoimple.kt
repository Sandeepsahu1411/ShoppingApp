package com.example.shoppinguserapp.data_layer.repoimple

import com.example.shoppinguserapp.common.CATEGORY
import com.example.shoppinguserapp.common.PRODUCTS
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.common.USERS
import com.example.shoppinguserapp.common.WISHLIST
import com.example.shoppinguserapp.domen_layer.data_model.Category
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.domen_layer.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class Repoimple @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore, private val firebaseAuth: FirebaseAuth
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
        userId: String,
        userData: UserData
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

    override fun wishList(
        userID: String,
        productID: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(WISHLIST).add(
            hashMapOf(
                "userID"
                        to userID,
                "productID" to productID,
            )
        ).addOnSuccessListener {
            trySend(ResultState.Success("Product Added To WishList"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it))
        }
        awaitClose {
            close()
        }
    }


}
