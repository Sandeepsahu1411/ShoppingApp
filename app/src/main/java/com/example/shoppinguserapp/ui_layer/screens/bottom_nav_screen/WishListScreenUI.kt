package com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun WishListScreenUI(
    navController: NavController,
    viewModel: AppViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getWishlistModel()
    }

    val getWishlistState = viewModel.getWishlistState.collectAsStateWithLifecycle()
    val wishlistData = getWishlistState.value.success

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.2f)) {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "My Wishlist",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )

                }
                Image(
                    painter = painterResource(id = R.drawable.sign_top),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp),
                    alignment = Alignment.TopEnd
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()

                .weight(0.8f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                getWishlistState.value.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                getWishlistState.value.error.isNotEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = getWishlistState.value.error.toString())
                    }
                }

                !getWishlistState.value.isLoading && wishlistData.isEmpty()  -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No items in wishlist",
                            fontSize = 20.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        )
                    }
                }

                getWishlistState.value.success.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(wishlistData) { product ->
                            WishlistItem(navController, product = product, viewModel)
                        }

                    }
                }
            }
        }

    }
}

@Composable
fun WishlistItem(navController: NavController, product: Products, viewModel: AppViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                navController.navigate(Routes.EachProductDetailScreen(productId = product.productId))
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (product.image.isNotEmpty()) {
                var isLoading by remember { mutableStateOf(true) }

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp, 80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        onSuccess = { isLoading = false },
                        onError = { isLoading = false }
                    )
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
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
                    modifier = Modifier
                        .size(50.dp, 80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }


            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = product.category, fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "₹${product.finalPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFf68b8b)
                )
            }

            IconButton(onClick = {
                viewModel.deleteWishListModel(product.productId)
                viewModel.resetWishlistState()
                navController.navigate(Routes.WishListScreen)

            }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from Wishlist",
                    tint = Color.Red
                )
            }
        }
    }
}
