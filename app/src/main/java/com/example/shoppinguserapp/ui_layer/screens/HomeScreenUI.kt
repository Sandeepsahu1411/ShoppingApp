package com.example.shoppinguserapp.ui_layer.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(viewModel: AppViewModel = hiltViewModel(), navController: NavController) {

    val context = LocalContext.current
    val homeState by viewModel.homeScreenState.collectAsStateWithLifecycle()
    var seeAllCategory by remember { mutableStateOf(false) }

    // Static Images List  CategoryS And Products
    val categoryImages = listOf(
        R.drawable.frock,
        R.drawable.frock_2,
        R.drawable.frock_3,
        R.drawable.frock_4,
        R.drawable.frock_2,
    )
    val productImages = listOf(
        R.drawable.product_frock,
        R.drawable.product_frock_2,
        R.drawable.product_frock_3,
        R.drawable.prouct_frock_4,
        R.drawable.product_frock_2,
        )


    when {
        homeState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        homeState.error != null -> {
            Toast.makeText(context, homeState.error, Toast.LENGTH_SHORT).show()
        }

        homeState.category != null && homeState.products != null -> {
            Column(
                modifier = Modifier.fillMaxSize()

            ) {
                // Search Box And Notification Icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) { SearchBox() }
                    IconButton(onClick = {
                        navController.navigate(
                            Routes.NotificationScreen
                        )
                    }) {
                        Icon(
                            Icons.Outlined.NotificationAdd, contentDescription = "notification",
                            tint = Color(0xFFF68B8B),
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }

                //Main Body Content LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    //Category And See All Row
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Categories",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "See All",
                                fontSize = 18.sp,
                                color = Color(0xFFF68B8B),
                                modifier = Modifier.clickable { seeAllCategory = !seeAllCategory })
                        }
                    }
                    //Category Lazy Row And Grid
                    item {
                        if (!seeAllCategory) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                itemsIndexed(
                                    homeState.category ?: emptyList()
                                ) { index, category ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .background(Color.White, shape = CircleShape)
                                                .border(1.dp, Color.Gray, shape = CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(id = categoryImages[index % categoryImages.size]),
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(text = category.name)
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                                    .height(250.dp)
                            ) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 5.dp)
                                ) {
                                    itemsIndexed(
                                        homeState.category ?: emptyList()
                                    ) { index, category ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .background(Color.White, shape = CircleShape)
                                                    .border(1.dp, Color.Gray, shape = CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = categoryImages[index % categoryImages.size]),
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(text = category.name)
                                        }
                                    }

                                }
                            }
                        }
                    }
                    //Slider Banner
                    item { BannerSection() }
                    //Flash Sale And See All Row
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Flash Sale",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "See All",
                                fontSize = 18.sp,
                                color = Color(0xFFF68B8B),
                                modifier = Modifier.clickable {
                                    navController.navigate(Routes.SeeMoreProductsScreen)
                                })
                        }
                    }
                    //Product Lazy Row
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(
                                homeState.products ?: emptyList()

                            ) { index, product ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .width(140.dp)
                                        .clickable {
                                            navController.navigate(
                                                Routes.EachProductDetailScreen(productId = product.productId)
                                            )
                                        }, verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(200.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                width = 2.dp,
                                                color = Color.Yellow,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(id = productImages[index % productImages.size]),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()

                                            .border(
                                                2.dp,
                                                Color.LightGray,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 5.dp),
                                            text = product.name,
                                            color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color(
                                                0xFF8C8585
                                            ),
                                            fontSize = 20.sp,
                                        )
                                        Text(text = product.description, fontSize = 16.sp)
                                        Row {
                                            Text(
                                                text = "Rs:",
                                                color = Color(0xFFFF4081),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = " ${product.finalPrice}",
                                                color = Color(0xFFFF4081),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                        ) {
                                            Text(
                                                text = "Rs:",
                                                color = if (isSystemInDarkTheme()) Color.White else Color(
                                                    0xFFFF4081
                                                ),

                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = " ${product.price} ",
                                                fontSize = 16.sp,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "20% off",
                                                color = Color(0xFFFF4081),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold

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
    }
}

