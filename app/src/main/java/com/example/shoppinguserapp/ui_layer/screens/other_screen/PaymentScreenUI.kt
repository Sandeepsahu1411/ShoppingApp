package com.example.shoppinguserapp.ui_layer.screens.other_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppinguserapp.MainActivity
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.data_layer.notifications.createNotificationChannel
import com.example.shoppinguserapp.data_layer.notifications.sendNotification
import com.example.shoppinguserapp.domen_layer.data_model.OrderModel
import com.example.shoppinguserapp.domen_layer.data_model.ProductItem
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.screens.CustomButton
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.example.shoppinguserapp.ui_layer.viewmodel.NotificationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@Composable
fun PaymentScreenUI(
    navController: NavController,
    productId: String,
    productSize: String,
    productColor: String,
    productQty: String,
    viewModel: AppViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()

) {

    val getCartState = viewModel.getCartState.collectAsStateWithLifecycle()
    val getCartData = getCartState.value.success

    val getShippingAddress = viewModel.getShippingState.collectAsStateWithLifecycle()
    val getShippingData = getShippingAddress.value.success

    val getProductByIdState = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val productData = getProductByIdState.value.success

    var selectedMethod by remember { mutableStateOf("") }
    var selectedAddress by remember { mutableStateOf("Same") }

    val addOrderState = viewModel.addOrderState.collectAsStateWithLifecycle()

    val activity = LocalContext.current as MainActivity
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.getProductsCart()
        viewModel.getShippingAddress()
    }
    LaunchedEffect(productId) {
        if (productId.isNotEmpty()) {
            viewModel.getProductById(productId)
        }
    }
    when {
        addOrderState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        addOrderState.value.error != null -> {
            Toast.makeText(LocalContext.current, addOrderState.value.error, Toast.LENGTH_SHORT)
                .show()
        }

        addOrderState.value.success != null -> {
            if (productId.isEmpty()) {
                viewModel.deleteProductCart()
            }
            createNotificationChannel(context)
            sendNotification(context, notificationViewModel)
            navController.navigate(Routes.PaymentSuccessScreen) {
                popUpTo(Routes.HomeScreen) {
                    inclusive = false
                }
            }
            addOrderState.value.success = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Payments",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.clickable {
                navController.navigateUp()
            })
        {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                Modifier.size(15.dp)
            )
            Text(
                text = "Return to Shipping",
                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        when {
            getCartState.value.isLoading || getProductByIdState.value.isLoading || getShippingAddress.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            getCartState.value.error != null || getProductByIdState.value.error.isNotEmpty() || getShippingAddress.value.error.isNotEmpty() -> {
                Toast.makeText(
                    LocalContext.current,
                    "Error: ${getCartState.value.error ?: getProductByIdState.value.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            getCartState.value.success.isNotEmpty() || getProductByIdState.value.success != null || getShippingAddress.value.success != null -> {
                val subTotal =
                    if (productId.isEmpty()) {
                        getCartData.sumOf { it.finalPrice.toInt() * it.qty.toInt() }
                    } else {
                        (productData?.finalPrice?.toInt() ?: 0) * (productQty.toIntOrNull() ?: 1)

                    }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        if (productId.isNotEmpty()) {
                            EachBuyProductDetail(
                                productSize, productColor, productQty, productData
                            )
                        } else {
                            CartProductDetail(getCartData)
                        }
                    }
                    item {
                        PaymentMethod(
                            selectedMethod = selectedMethod,
                            onMethodSelected = { selectedMethod = it })
                    }
                    item {
                        BillingAddress(
                            selectedMethod = selectedAddress,
                            onMethodSelected = { selectedAddress = it })
                    }
                    item {
                        CustomButton(
                            text = "Place Order",
                            onClick = {
                                if (selectedMethod == "") {
                                    Toast.makeText(
                                        context,
                                        "Select payment method",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@CustomButton
                                }
                                if (selectedMethod == "Online") {
                                    activity.startPayment(
                                        userName = "sandeep",
                                        userEmail = "Sandeep@gmail.com",
                                        userPhoneNo = "8773454678",
                                        amount = if (subTotal > 3000) subTotal.toDouble() else subTotal.toDouble() + 100,
                                        productId = productId,
                                        productData = productData,
                                        productQty = productQty,
                                        cartData = getCartData,
                                        shippingData = getShippingData,
                                        subTotal = subTotal,
                                        productColor = productColor,
                                        productSize = productSize
                                    )
                                } else {
                                    val orderProducts = if (productId.isNotEmpty()) {
                                        // ✅ Single Buy Order
                                        listOf(
                                            ProductItem(
                                                productId = productData?.productId ?: productId,
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
                                    } else {
                                        // ✅ Cart Buy Order
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
                                    }
                                    viewModel.addOrder(
                                        orderModel = OrderModel(
                                            orderId = UUID.randomUUID().toString().take(12)
                                                .uppercase(),
                                            products = orderProducts,
                                            totalPrice = if (subTotal > 3000) subTotal.toString() else (subTotal + 100).toString(),
                                            transactionMethod = selectedMethod,
                                            transactionId = "123456789",
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

                                }
                            },
                            containerColor = Color(0xFF8c8585)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethod(selectedMethod: String, onMethodSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Payment Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),

            )
        Text(
            text = "All transactions are secure and encrypted.",
            fontSize = 14.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),

            modifier = Modifier.padding(vertical = 10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(18.dp))
                .background(
                    if (isSystemInDarkTheme()) Color.DarkGray else Color.White,
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onMethodSelected("Online") }) {
                RadioButton(
                    selected = selectedMethod == "Online",
                    onClick = { onMethodSelected("Online") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                )
                Text(
                    text = "Sampath Bank IPG",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.7f)

                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.visa),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.mastercard),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.unionpay),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )

                }
            }

            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        onMethodSelected("Online")
                    }, colors = CardDefaults.cardColors(
                    containerColor =
                        if (isSystemInDarkTheme()) Color(0xE68D7171) else Color(0xFFF5F2F1)

                )


            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(15.dp, 5.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.card_payment),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757)
                    )
                    Text(
                        text = "After clicking “Pay now”, you will be redirected to Sampath Bank IPG to complete your purchase securely.",
                        fontSize = 12.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = if (isSystemInDarkTheme()) Color.White else Color(0xFFF68B8B)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onMethodSelected("COD") }) {
                RadioButton(
                    selected = selectedMethod == "COD",
                    onClick = { onMethodSelected("COD") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                )
                Text(
                    text = "Cash on delivery (COD)",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.85f)
                )


            }


        }

    }
}

@Composable
fun BillingAddress(selectedMethod: String, onMethodSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Billing Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),

            modifier = Modifier.padding(vertical = 10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(18.dp)),
            verticalArrangement = Arrangement.spacedBy(0.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onMethodSelected("Same") }) {
                RadioButton(
                    selected = selectedMethod == "Same",
                    onClick = { onMethodSelected("Same") },
                    modifier = Modifier.weight(0.1f),

                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                )
                Text(
                    text = "Same as shipping address",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.7f)

                )
                Text(
                    text = "Change",
                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(0.2f)
                        .clickable {}

                )
            }
            HorizontalDivider(thickness = 2.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onMethodSelected("Different") }) {
                RadioButton(
                    selected = selectedMethod == "Different",
                    onClick = { onMethodSelected("Different") },
                    modifier = Modifier.weight(0.1f),
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                )
                Text(
                    text = "Use a different billing address",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.7f)
                )

                Text(
                    text = "Change",
                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(0.2f)
                        .clickable {}

                )
            }


        }

    }


}
