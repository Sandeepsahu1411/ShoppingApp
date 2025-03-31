package com.example.shoppinguserapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

import androidx.compose.ui.Modifier

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shoppinguserapp.ui.theme.ShoppingUserAppTheme
import com.example.shoppinguserapp.ui_layer.navigation.AppNavigation
import com.example.shoppinguserapp.ui_layer.navigation.Routes

import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PayloadHelper
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            ShoppingUserAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding()
                ) {
                    AppNavigation(firebaseAuth)
                }
            }
        }
        Checkout.preload(applicationContext)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID("")


    }
    fun startPayment(
        userName: String ,
        userEmail : String,
        userPhoneNo : String


    ) {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", userName)
            options.put("payment_capture", 1)
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image", "http://example.com/image/rzp.jpg")
            options.put("theme.color", "#f68b8b");
            options.put("currency", "INR");

            options.put("amount", "50000")

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", userEmail)
            prefill.put("contact", userPhoneNo)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()

        navController.navigate(Routes.HomeScreen){
            popUpTo(0){
                inclusive = true
            }
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {

        val errorMsg = when (p0) {
            Checkout.PAYMENT_CANCELED -> "Payment was cancelled. Please try again."
            Checkout.NETWORK_ERROR -> "Network issue! Please check your connection."
            Checkout.INVALID_OPTIONS -> "Invalid payment options. Contact support. $response"
            else -> "Payment failed! Reason: $response"
        }
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()

    }
}

