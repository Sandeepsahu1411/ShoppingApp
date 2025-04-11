package com.example.shoppinguserapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppinguserapp.data_layer.notifications.createNotificationChannel
import com.example.shoppinguserapp.data_layer.notifications.sendNotification
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
import com.example.shoppinguserapp.domen_layer.data_model.OrderModel
import com.example.shoppinguserapp.domen_layer.data_model.ProductItem
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.ShippingModel
import com.example.shoppinguserapp.ui.theme.ShoppingUserAppTheme
import com.example.shoppinguserapp.ui_layer.navigation.AppNavigation
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.example.shoppinguserapp.ui_layer.viewmodel.NotificationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    private lateinit var navController: NavController

    @Inject
    lateinit var context: Activity

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    var productId: String? = null
    var productData: Products? = null
    var productQty: String = "1"
    var getCartData: List<CartModel> = emptyList()
    var getShippingData: ShippingModel? = null
    var subTotal: Int = 0
    var productColor: String = ""
    var productSize: String = ""

    private val viewModel: AppViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            ShoppingUserAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding()
                ) {
                    AppNavigation(firebaseAuth , navController = navController as NavHostController)
                }
            }
        }
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("")

    }

    fun startPayment(
        userName: String,
        userEmail: String,
        userPhoneNo: String,
        amount: Double,
        productId: String?,
        productSize: String,
        productColor: String,
        productData: Products?,
        productQty: String,
        cartData: List<CartModel>,
        shippingData: ShippingModel?,
        subTotal: Int

    ) {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        this.productId = productId
        this.productData = productData
        this.productQty = productQty
        this.getCartData = cartData
        this.getShippingData = shippingData
        this.subTotal = subTotal
        this.productColor = productColor
        this.productSize = productSize

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

            options.put("amount", amount * 100)

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
        val orderProducts = mutableListOf<ProductItem>()

        if (!productId.isNullOrEmpty() && productData != null) {
            orderProducts.add(
                ProductItem(
                    productId = productData?.productId ?: "",
                    productName = productData?.name ?: "",
                    productDes = productData?.description ?: "",
                    productQty = productQty,
                    productPrice = productData?.price ?: "",
                    productFinalPrice = productData?.finalPrice ?: "",
                    productCategory = productData?.category ?: "",
                    productImageUrl = productData?.image ?: "",
                    color = productColor,
                    size = productSize
                )
            )
        }
        else if (getCartData.isNotEmpty()) {
            orderProducts.addAll(
                getCartData.map { cartItem ->
                    ProductItem(
                        productId = cartItem.productId,
                        productName = cartItem.name,
                        productDes = cartItem.description,
                        productQty = cartItem.qty.toString(),
                        productPrice = cartItem.price,
                        productFinalPrice = cartItem.finalPrice,
                        productCategory = cartItem.category,
                        productImageUrl = cartItem.imageUrl,
                        color = cartItem.color,
                        size = cartItem.size
                    )
                }
            )
        }

        viewModel.addOrder(
            orderModel = OrderModel(
                orderId = UUID.randomUUID().toString().take(12)
                    .uppercase(),
                products = orderProducts,
                totalPrice = if (subTotal > 3000) subTotal.toString() else (subTotal + 100).toString(),
                transactionMethod = "Online",
                transactionId = p1?.paymentId.toString(),
                userAddress = getShippingData?.address.toString(),
                city = getShippingData?.city.toString(),
                countryRegion = getShippingData?.countryRegion.toString(),
                userEmail = getShippingData?.email.toString(),
                firstName = getShippingData?.firstName.toString(),
                lastName = getShippingData?.lastName.toString(),
                mobileNo = getShippingData?.mobileNo.toString(),
                postalCode = getShippingData?.pinCode.toString(),
            )
        )
        createNotificationChannel(context)
        sendNotification(context, notificationViewModel)
        if (productId.isNullOrEmpty()) {
            viewModel.deleteProductCart()
        }
        navController.navigate(Routes.PaymentSuccessScreen){
            popUpTo(Routes.HomeScreen){
                inclusive = false
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

