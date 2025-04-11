package com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen

import android.R.attr.data
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.screens.CustomButton
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel

@Composable
fun CartScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val getCartProductState = viewModel.getCartState.collectAsStateWithLifecycle()
    val getCartData = getCartProductState.value.success
    val deleteProductCartState = viewModel.addToCartState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getProductsCart()
    }
    when {
        deleteProductCartState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        deleteProductCartState.value.error != null -> {
            Toast.makeText(context, deleteProductCartState.value.error, Toast.LENGTH_SHORT).show()
        }

        deleteProductCartState.value.success != null -> {
            viewModel.getProductsCart()
            deleteProductCartState.value.success = null
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.15f)) {
            CartTopRow(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .weight(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                getCartProductState.value.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                getCartProductState.value.error != null -> {
                    Toast.makeText(
                        context, getCartProductState.value.error, Toast.LENGTH_SHORT
                    ).show()
                }

                !getCartProductState.value.isLoading && getCartData.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Items in Cart",
                            fontSize = 20.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        )
                    }
                }

                getCartProductState.value.success.isNotEmpty() -> {
                    var quantities by remember { mutableStateOf(getCartData.associate { it.productId to it.qty.toInt() }) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CartHeaderRow()
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            items(getCartData) { cartData ->

                                var quantity =
                                    quantities[cartData.productId] ?: cartData.qty.toInt()

                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    SubcomposeAsyncImage(
                                        model = if (cartData.imageUrl.isNotEmpty()) cartData.imageUrl else R.drawable.product_frock,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(70.dp, 100.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        loading = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.LightGray)
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .align(Alignment.Center),
                                                    color = Color.Red
                                                )
                                            }
                                        })

                                    Column(
                                        modifier = Modifier.weight(0.25f),
                                        verticalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Text(
                                            text = cartData.name.let {
                                                if (it.length > 15) "${
                                                    it.take(15)
                                                }..." else it
                                            },
                                            fontSize = 14.sp,
                                            lineHeight = 16.sp,
                                            color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Size : ${cartData.size}",
                                            fontSize = 14.sp,
                                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,

                                            )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Color: ",
                                                fontSize = 14.sp,
                                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,

                                                )
                                            Box(
                                                modifier = Modifier
                                                    .clip(
                                                        shape = RoundedCornerShape(
                                                            5.dp
                                                        )
                                                    )
                                                    .size(15.dp)
                                                    .background(hexToColor(cartData.color))
                                            )
                                        }
                                    }

                                    Column(
                                        modifier = Modifier.weight(0.5f),
                                        verticalArrangement = Arrangement.SpaceAround

                                    ) {
                                        Row {
                                            Text(
                                                text = cartData.finalPrice,
                                                fontSize = 14.sp,
                                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.weight(0.35f)

                                            )
                                            Text(
                                                text = quantity.toString(),
                                                fontSize = 14.sp,
                                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(0.3f),
                                                textAlign = TextAlign.Center
                                            )
                                            val total = cartData.finalPrice.toInt() * quantity
                                            Text(
                                                text = total.toString(),
                                                fontSize = 14.sp,
                                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .weight(0.35f)
                                                    .fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        Spacer(Modifier.size(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(0xFFD3BEBE))
                                                    .clickable {
                                                        if (quantity > 1) {
                                                            quantities = quantities
                                                                .toMutableMap()
                                                                .apply {
                                                                    put(
                                                                        cartData.productId,
                                                                        quantity - 1
                                                                    )
                                                                }
                                                            viewModel.updateProductCart(
                                                                cartData.productId, quantity - 1
                                                            )
                                                        } else {
                                                            viewModel.addProductCart(cartData)
//
                                                        }

                                                    }, contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Remove,
                                                    contentDescription = null,
                                                    tint = if (isSystemInDarkTheme()) Color.Black else Color.DarkGray,
                                                )
                                            }
                                            Spacer(Modifier.size(30.dp))

                                            Box(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(0xFFD3BEBE))
                                                    .clickable {

                                                        quantities = quantities
                                                            .toMutableMap()
                                                            .apply {
                                                                put(
                                                                    cartData.productId,
                                                                    quantity + 1
                                                                )
                                                            }
                                                        viewModel.updateProductCart(
                                                            cartData.productId, quantity + 1
                                                        )
                                                    }, contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Rounded.Add,
                                                    contentDescription = null,
                                                    tint = if (isSystemInDarkTheme()) Color.Black else Color.DarkGray,

                                                    )
                                            }

                                        }
                                    }
                                }
                            }
                            item {
                                val subTotal = getCartData.sumOf {
                                    it.finalPrice.toInt() * (quantities[it.productId]
                                        ?: it.qty.toInt())
                                }
                                HorizontalDivider(thickness = 2.dp, color = Color.Gray)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "Sub Total  ",
                                        fontSize = 18.sp,
                                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Text(
                                        text = "Rs: $subTotal",
                                        fontSize = 18.sp,
                                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                CustomButton(
                                    text = "Checkout",
                                    onClick = {
                                        navController.navigate(
                                            Routes.ShippingScreen(
                                                productId = "",
                                                productSize = "",
                                                productColor = "",
                                                productQty = ""
                                            )
                                        )
                                    },
                                    containerColor = Color(0xFF8c8585)
                                )

                            }
                        }
//                    }

                    }
                }
            }
        }
    }

}

fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}

@Composable
fun CartTopRow(navController: NavController) {
    Row (verticalAlignment = Alignment.Top){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Shopping Cart",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
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

@Composable
fun CartHeaderRow() {
    val headers = listOf("Items" to 0.5f, "Price" to 0.2f, "QTY" to 0.15f, "Total" to 0.15f)

    Row {
        headers.forEach { (title, weight) ->
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,

                modifier = Modifier.weight(weight)
            )
        }
    }
}