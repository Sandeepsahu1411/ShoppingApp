package com.example.shoppinguserapp.ui_layer.screens.other_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.domen_layer.data_model.OrderModel
import com.example.shoppinguserapp.domen_layer.data_model.ProductItem
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen.hexToColor
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun OrderScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {
    val getOrderState = viewModel.getOrderState.collectAsStateWithLifecycle()
    val getOrderData = getOrderState.value.success

    LaunchedEffect(Unit) {
        viewModel.getOrder()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.15f)) {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.6f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "My Orders",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.HomeScreen)
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            Modifier.size(15.dp)
                        )
                        Text(
                            text = "Continue Shopping",
                            color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.sign_top),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxSize(),
                    alignment = Alignment.TopEnd
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .weight(0.85f), horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when {
                getOrderState.value.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                getOrderState.value.error.isNotEmpty() -> {
                    Toast.makeText(
                        LocalContext.current, getOrderState.value.error, Toast.LENGTH_SHORT
                    ).show()
                }

                getOrderData.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Items Found",
                            fontSize = 20.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        )
                    }
                }

                getOrderState.value.success.isNotEmpty() -> {
                    LazyColumn {
                        items(getOrderData.sortedByDescending { it.date }) { data ->
                            OrdersCard(data)

                        }
                    }

                }
            }
        }
    }
}


@Composable
fun OrdersCard(order: OrderModel) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val totalPrice = order.products.sumOf { it.productFinalPrice.toInt() * it.productQty.toInt() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { expanded = !expanded }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
    ) {
        Column(
            modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    buildAnnotatedString {

                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) { append("Order ID: ") }
                        append(order.orderId)
                    },

                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = getTimeAgo(order.date),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
            order.products.forEach { product ->
                ProductCard(product = product)
            }
            HorizontalDivider(thickness = 1.dp,color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray)
            Column {
                Row {
                    Text(text = "Total Price", fontSize = 16.sp, modifier = Modifier.weight(1f))
                    Text(
                        text = "RS.${totalPrice}",
                        color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    Text(text = "Shipping Charge", fontSize = 16.sp, modifier = Modifier.weight(1f))
                    Text(
                        text = if (totalPrice > 3000) "Free" else "Rs: 100",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(thickness = 2.dp,color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray)
            Row {
                Text(
                    text = "Sub Total",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF68B8B),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "RS.${order.totalPrice}",
                    color = Color(0xFFF68B8B),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (expanded) {
                HorizontalDivider(thickness = 2.dp,color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray)
                Column {
                    Text(
                        text = "Receiver Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Text(
                        text = "${order.firstName} ${order.lastName}, Ph. ${order.mobileNo}",
                        fontSize = 16.sp
                    )
                }
                Column {
                    Text(
                        text = "Delivery Address",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Text(
                        text = "${order.userAddress} ${order.city},  ${order.countryRegion},  ${order.postalCode}",
                        fontSize = 16.sp
                    )
                }
                Row {
                    Text(
                        text = "Payment Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (order.transactionMethod == "COD") "Cash on Delivery" else "Paid via Online",
                        fontSize = 16.sp,
                        color = Color(0xFFF68B8B),
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    Text(
                        text = "Delivery Status",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(30.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF68B8B))
                            .padding(horizontal = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = order.status,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                    }
                }
                OrderProgressTracker(order.status)

            }

        }
    }

}

@Composable
fun ProductCard(product: ProductItem) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        SubcomposeAsyncImage(
            model = if (product.productImageUrl.isNotEmpty()) product.productImageUrl else R.drawable.product_frock,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp, 70.dp)
                .clip(RoundedCornerShape(10.dp)),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        color = Color.Red
                    )
                }
            })
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.productName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Qty: ${product.productQty}  Color: ",
                    fontSize = 14.sp,
                )
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                5.dp
                            )
                        )
                        .size(15.dp)
                        .background(hexToColor(product.color))
                )

            }
            Text(
                buildAnnotatedString {
                    append("Price: ")
                    withStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.LineThrough, color = Color.Gray
                        )
                    ) {
                        append("₹${product.productPrice}")
                    }

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFF68B8B)
                        )
                    ) {
                        append("  ₹${product.productFinalPrice}")
                    }
                }, fontSize = 14.sp
            )
        }
    }
}

@Composable
fun OrderProgressTracker(currentStatus: String) {
    val statuses = listOf("Processed", "Shipped", "En Route", "Delivered")
    val currentIndex = statuses.indexOf(currentStatus)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            statuses.forEachIndexed { index, status ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            if (index <= currentIndex) Color(0xFFF68B8B) else Color.LightGray,
                            shape = CircleShape
                        )
                )
                if (index < statuses.lastIndex) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .background(if (index < currentIndex) Color(0xFFF68B8B) else Color.LightGray)
                    )
                }
            }
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            statuses.forEachIndexed { index, status ->
                Text(
                    text = status,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = if (index <= currentIndex) FontWeight.Bold else FontWeight.Normal,
                    color = if (index <= currentIndex) Color(0xFFF68B8B) else Color.LightGray,
                    modifier = Modifier.width(70.dp)
                )
            }

        }
    }
}


fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Now"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} min ago"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hr ago"
        diff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
        else -> {  // If it's older than 2 days
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}