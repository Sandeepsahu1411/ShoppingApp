package com.example.shoppinguserapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeMoreProductScreenUI(
    viewModel: AppViewModel = hiltViewModel(), navController: NavController
) {

    val productState = viewModel.getProductsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val productImages = listOf(
        R.drawable.product_frock,
        R.drawable.product_frock_2,
        R.drawable.product_frock_3,
        R.drawable.prouct_frock_4,
        R.drawable.product_frock_2,

        )
    var searchQuery by remember { mutableStateOf("") }


    val filteredProducts = productState.value.success.filter {
        it!!.name.contains(searchQuery, ignoreCase = true)
    }


    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }
    when {
        productState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }

        productState.value.error.isNotEmpty() -> {
            Toast.makeText(context, productState.value.error, Toast.LENGTH_SHORT).show()

        }

        productState.value.success.isNotEmpty() -> {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(0.2f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f)
                                .padding(start = 20.dp),
                            verticalArrangement = Arrangement.Center

                        ) {
                            Text(
                                text = "See More",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,

                                )
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .clickable {
                                        navController.navigate(Routes.HomeScreen)
                                    }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Continue Shopping",
                                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }
                        Image(
                            painter = painterResource(id = R.drawable.sign_top),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .size(200.dp),
                            alignment = Alignment.TopEnd
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .weight(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Items",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                text = "Price",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,

                                textAlign = TextAlign.Center,

                                modifier = Modifier.weight(0.5f)
                            )
                        }
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(text = "Search", color = Color.LightGray) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search, contentDescription = "Search",
                                    tint = Color(0xFFF68B8B),
                                    modifier = Modifier.size(30.dp),
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFFF68B8B),
                                unfocusedIndicatorColor = Color(0xFFF68B8B),
                            )
                        )

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            itemsIndexed(items = filteredProducts) { index, product ->
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.clickable {
                                        navController.navigate(
                                            Routes.EachProductDetailScreen(
                                                productId = product!!.productId
                                            )
                                        )
                                    }) {
                                    if(product?.image?.isNotEmpty()!!){
                                        var isLoading by remember { mutableStateOf(true) }

                                        Box(modifier = Modifier) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(product.image)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(100.dp, 140.dp)
                                                    .clip(RectangleShape),
                                                onSuccess = { isLoading = false },
                                                onError = { isLoading = false }
                                            )
                                            if (isLoading) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .align(Alignment.Center),
                                                    color = Color.Red
                                                )
                                            }
                                        }

                                    }else{

                                        Image(
                                            painter = painterResource(id = R.drawable.product_frock_3),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(100.dp, 140.dp)
                                                .clip(RectangleShape),
                                        )
                                    }
//                                    Image(
//                                        painter = painterResource(id = productImages[index % productImages.size]),
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .size(100.dp, 140.dp)
//                                            .clip(RectangleShape),
//                                        contentScale = ContentScale.Crop
//
//                                    )
                                    Column(
                                        modifier = Modifier.weight(0.7f),
                                        verticalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Text(
                                            text = product!!.name,
                                            fontSize = 18.sp,
                                            color = Color(0xFFF68B8B),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = product.category,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Size : UK10",
                                            fontSize = 16.sp,

                                            )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Color : ",
                                                fontSize = 16.sp,
                                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray
                                            )


                                            Box(
                                                modifier = Modifier
                                                    .clip(shape = RoundedCornerShape(5.dp))
                                                    .size(20.dp)
                                                    .background(Color.Green)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Rs: ${product?.finalPrice}",
                                        fontSize = 16.sp,
                                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(0.3f)

                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
