package com.example.shoppinguserapp.ui_layer.screens

import android.R.attr.category
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.google.android.play.integrity.internal.s
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.domen_layer.data_model.toCartModel
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.google.android.play.integrity.internal.z
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateField.count

@Composable
fun EachProductDetailScreenUI(
    viewModel: AppViewModel = hiltViewModel(),
    navController: NavController,
    productId: String,
    firebaseAuth: FirebaseAuth,


    ) {
    val eachProductDetailState = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    var count by rememberSaveable { mutableIntStateOf(1) }

    var selectedSize by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId)
    }
    val context = LocalContext.current

    when {
        eachProductDetailState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }

        eachProductDetailState.value.error.isNotEmpty() -> {
            Toast.makeText(context, eachProductDetailState.value.error, Toast.LENGTH_SHORT).show()
        }

        eachProductDetailState.value.success != null -> {
            val productData = eachProductDetailState.value.success
            eachProductDetailState.value.success?.let {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .height(550.dp)
                    ) {
                        // Background Image

                        if (productData?.image?.isNotEmpty()!!) {
                            var isLoading by remember { mutableStateOf(true) }

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                                    .data(productData.image).crossfade(true).build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    onSuccess = { isLoading = false },
                                    onError = { isLoading = false })
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .align(Alignment.Center),
                                        color = Color.Red
                                    )
                                }
                            }
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.cheakout_img),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            if (isSystemInDarkTheme()) Color.Black else Color.White.copy(
                                                alpha = 1f
                                            )
                                        ), startY = 0f, endY = 250f
                                    )
                                )
                                .clip(shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Text(
                                    text = productData?.name ?: "NA",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,

                                    )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,

                                    ) {
                                    repeat(4) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Star",
                                            tint = Color.Yellow,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.StarBorder,
                                        contentDescription = "Half Star",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("Rs. ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSystemInDarkTheme()) Color.White else Color.Black

                                        )
                                    ) {
                                        append(it.finalPrice)
                                    }

                                })
                            }
                        }
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(30.dp)
                                .background(
                                    if (isSystemInDarkTheme()) Color.Black else Color(0xFFF68B8B),
                                    CircleShape
                                )
                                .clickable {
                                    navController.navigateUp()

                                }, contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null,
                                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            )

                        }
                    }

                    // Content Below Image
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
                            .padding(vertical = 10.dp, horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)

                    ) {
                        // Size Options
                        Text(
                            text = "Size",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("M", "L", "XL", "XXL").forEach { size ->
                                Box(modifier = Modifier
                                    .border(
                                        1.dp,
                                        if (selectedSize == size) Color.Blue else Color.Gray,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        if (selectedSize == size) Color.LightGray else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedSize = size }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)) {
                                    Text(text = size)
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IncreaseDecreesRow(count,
                                onIncrease = { count++ },
                                onDecrease = { if (count > 1) count-- })
                        }
                        // Color Options
                        Text(
                            text = "Color",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,

                            )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Color.Green, Color.Cyan, Color.Yellow, Color.Blue
                            ).forEach { color ->
                                Box(modifier = Modifier
                                    .size(40.dp)
                                    .clickable { selectedColor = color }
                                    .background(color, RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 1.dp,
                                        color = if (selectedColor == color) Color.Black else Color.Gray,
                                        shape = RoundedCornerShape(8.dp)
                                    ))
                            }
                        }
                        // Specification Section
                        SpecificationText(eachProductDetailState.value.success!!)
                        // Buy Now Button
                        ButtonsContent(
                            navController,
                            viewModel,
                            productData,
                            productId,
                            selectedColor,
                            selectedSize,
                            count
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun IncreaseDecreesRow(count: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(onClick = { onDecrease() }) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = null)
        }
        Box(
            modifier = Modifier

                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Text(text = "$count", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = { onIncrease() }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }

}

@Composable
fun ButtonsContent(
    navController: NavController,
    viewModel: AppViewModel,

    productsData: Products?,
    productId: String,
    selectedColor: Color,
    selectedSize: String,
    count: Int

) {
    val context = LocalContext.current
    val addWishlistState = viewModel.addWishListState.collectAsStateWithLifecycle()
    val checkWishlistState = viewModel.checkWishlistState.collectAsStateWithLifecycle()

    val addToCartState = viewModel.addToCartState.collectAsStateWithLifecycle()
    val checkCartState = viewModel.checkProductCartState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.checkWishlistModel(productId)
        viewModel.checkProductCart(productId)
    }

    if (addWishlistState.value.success != null) {
//        Toast.makeText(LocalContext.current, addWishlistState.value.success, Toast.LENGTH_SHORT)
//            .show()
        viewModel.checkWishlistModel(productId)
        viewModel.resetWishlistState()

    } else if (addWishlistState.value.error.isNotEmpty()) {
        Toast.makeText(LocalContext.current, addWishlistState.value.error, Toast.LENGTH_SHORT)
            .show()
        viewModel.resetWishlistState()
    }
    if (addToCartState.value.success != null) {
        Toast.makeText(LocalContext.current, addToCartState.value.success, Toast.LENGTH_SHORT)
            .show()
        viewModel.checkProductCart(productId)
        addToCartState.value.success = null


    } else if (addToCartState.value.error != null) {
        Toast.makeText(LocalContext.current, addToCartState.value.error, Toast.LENGTH_SHORT).show()

    }

    Column(
        modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = { navController.navigate(Routes.ShippingScreen) },
            colors = ButtonDefaults.buttonColors(
                Color(0xfff68b8b), contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = "Buy now", fontSize = 20.sp, modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        if (checkCartState.value.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xff2112e2))
            }
        } else {
            Button(
                onClick = {
                    if (checkCartState.value.success) {
                        navController.navigate(Routes.CartScreen)
                    } else {
                        if (selectedSize.isNotEmpty() && selectedColor != Color.Transparent) {
                            val cartModel = productsData?.toCartModel(
                                qty = count,
                                color = colorToHex(selectedColor),
                                size = selectedSize
                            )
                            viewModel.addProductCart(cartModel!!)

                        } else {
                            Toast.makeText(
                                context, "Please select size and color", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8c8585), contentColor = Color.White
                )
            ) {
                Text(
                    text = if (checkCartState.value.success) "Go to Cart " else "Add to Cart ",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }

        }
        Button(
            onClick = {
                viewModel.addWishListModel(productsData!!)

            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color(0xfff68b8b)
            ),
        ) {
            if (addWishlistState.value.isLoading) {
                CircularProgressIndicator(
                    color = Color(0xfff68b8b), modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = if (checkWishlistState.value.success) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Wishlist Icon",
                    tint = Color(0xfff68b8b),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (checkWishlistState.value.success) "Remove from Wishlist" else "Add to Wishlist",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xfff68b8b)
                )


            }


        }

    }
}

@Composable
fun SpecificationText(products: Products) {

    Text(
        text = "Specification",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = products.description)
        Text(text = "Material: Linen")
        Text(
            text = "Material Composition: 100% Linen", style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Please bear in mind that the photo may be slightly different from the actual item in terms of color due to lighting conditions or the display used to View...",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
    }
}

// Function to convert Color to HEX String
fun colorToHex(color: Color): String {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}







