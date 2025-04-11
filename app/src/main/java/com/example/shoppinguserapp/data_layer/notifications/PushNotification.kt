package com.example.shoppinguserapp.data_layer.notifications

import android.content.Context
import android.util.Log
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.common.USER_TOKEN
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class PushNotification @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var accessToken: String? = null

    init {
        scope.launch {
            updateAccessToken()
        }
    }

    fun sendNotification(
        title: String, message: String
    ) {
        scope.launch {
            try {
                if (accessToken == null) {
                    updateAccessToken()
                }
                val token = getUserToke()
                Log.d("fcm", "sendNotification: $token")
                setUpNotification(token, title, message)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }


    private suspend fun getUserToke() = withContext(Dispatchers.IO) {
        val snap =
            firestore.collection(USER_TOKEN).document(firebaseAuth.currentUser?.uid.toString())
                .get().await()
        snap.getString("token").toString()

    }

    private suspend fun updateAccessToken() = withContext(Dispatchers.IO) {
        try {
            val stream = context.resources.openRawResource(R.raw.shopping_app_key)
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(("https://www.googleapis.com/auth/firebase.messaging"))
            credentials.refresh()
            accessToken = credentials.accessToken.tokenValue
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun setUpNotification(
        token: String, title: String, message: String
    ) {
        try {
            val json = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", token)
                    put("notification", JSONObject().apply {
                        put("title", title)
                        put("body", message)
                    })
                    put("android", JSONObject().apply {
                        put("priority", "high")
                    })
                })
            }
            val body =
                json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().header("Authorization", "Bearer $accessToken")
                .url("https://fcm.googleapis.com/v1/projects/${context.getString(R.string.project_id)}/messages:send")

                .post(body)
                .build()

            val response = withContext(Dispatchers.IO) {
                OkHttpClient().newCall(request).execute()
            }
            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                throw Exception("Error sending notification: $errorBody")
            } else {
                Log.d("fcm", "setUoNotification: ${response.body?.string()} ")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}