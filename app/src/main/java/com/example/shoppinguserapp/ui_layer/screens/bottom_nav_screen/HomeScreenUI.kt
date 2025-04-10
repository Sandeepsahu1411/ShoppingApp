package com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.screens.BannerSection
import com.example.shoppinguserapp.ui_layer.screens.SearchBox
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.example.shoppinguserapp.ui_layer.viewmodel.NotificationViewModel
import com.google.android.play.integrity.internal.f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(
    viewModel: AppViewModel = hiltViewModel(),
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val homeState by viewModel.homeScreenState.collectAsStateWithLifecycle()
    var seeAllCategory by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    val unseenCountState = notificationViewModel.notificationState.collectAsStateWithLifecycle()
    val unseenCount = unseenCountState.value.success?.count {
        !it.seen
    } ?: 0

    LaunchedEffect(Unit) {
        notificationViewModel.getNotification()
    }
    Column(
        modifier = Modifier.fillMaxSize()

    ) {
        // Search Box And Notification Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp, 0.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SearchBox(value = search, onValueChange = { search = it })
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate(Routes.NotificationScreen)
                    }

            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "notification",
                    tint = Color(0xFFF68B8B),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp)
                )
                if (unseenCount > 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 0.dp, y = (4).dp)
                    ) {
                        Text(unseenCount.toString(), fontSize = 10.sp)
                    }
                }
            }


        }
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
                //Main Body Content LazyColumn
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Category And See All Row
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Categories",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "See All",
                                fontSize = 16.sp,
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
                                items(
                                    homeState.category?.take(6) ?: emptyList()
                                ) { category ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(CircleShape)
                                                .background(Color.White, shape = CircleShape)
                                                .border(1.dp, Color.Gray, shape = CircleShape)
                                                .clickable {
                                                    navController.navigate(
                                                        Routes.EachCategoryScreen(
                                                            categoryName = category.name
                                                        )
                                                    )
                                                }, contentAlignment = Alignment.Center
                                        ) {
                                            if (category.imageUrl.isNotEmpty()) {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    SubcomposeAsyncImage(
                                                        model = category.imageUrl,
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize(),
                                                        loading = {
                                                            Box(
                                                                modifier = Modifier.fillMaxSize(),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                CircularProgressIndicator(
                                                                    modifier = Modifier
                                                                        .size(20.dp)
                                                                        .align(Alignment.Center),
                                                                    color = Color.Red
                                                                )
                                                            }
                                                        },

                                                        )

                                                }

                                            } else {

                                                Image(
                                                    painter = painterResource(id = R.drawable.frock),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }

                                        }
                                        Text(text = category.name, fontSize = 14.sp)
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
                                    items(
                                        homeState.category ?: emptyList()
                                    ) { category ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(70.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White, shape = CircleShape)
                                                    .border(1.dp, Color.Gray, shape = CircleShape)
                                                    .clickable {
                                                        navController.navigate(
                                                            Routes.EachCategoryScreen(
                                                                categoryName = category.name
                                                            )
                                                        )
                                                    }, contentAlignment = Alignment.Center
                                            ) {
                                                if (category.imageUrl.isNotEmpty()) {
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        SubcomposeAsyncImage(
                                                            model = category.imageUrl,
                                                            contentDescription = null,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize(),
                                                            loading = {
                                                                Box(
                                                                    modifier = Modifier.fillMaxSize(),
                                                                    contentAlignment = Alignment.Center,

                                                                    ) {
                                                                    CircularProgressIndicator(
                                                                        modifier = Modifier
                                                                            .size(20.dp)
                                                                            .align(Alignment.Center),
                                                                        color = Color.Red
                                                                    )
                                                                }
                                                            },

                                                            )
                                                    }
                                                } else {

                                                    Image(
                                                        painter = painterResource(id = R.drawable.frock),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(text = category.name, fontSize = 14.sp)
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
                                .padding(14.dp, 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Flash Sale",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "See All",
                                fontSize = 16.sp,
                                color = Color(0xFFF68B8B),
                                modifier = Modifier.clickable {
                                    navController.navigate(Routes.SeeMoreProductsScreen)
                                })
                        }
                    }
                    //Product Lazy Row
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(homeState.products?.filter { it.category == "Gown" || it.category == "Top" || it.category == "Dress" }
                                ?.take(8) ?: emptyList()) { product ->
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
                                            .height(180.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                width = 2.dp,
                                                color = Color.Yellow,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    ) {
                                        if (product.image.isNotEmpty()) {
                                            Box(modifier = Modifier.fillMaxSize()) {
                                                SubcomposeAsyncImage(
                                                    model = ImageRequest.Builder(
                                                        LocalContext.current
                                                    ).data(product.image).crossfade(true).build(),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize(),
                                                    loading = {
                                                        Box(
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            CircularProgressIndicator(
                                                                modifier = Modifier
                                                                    .size(50.dp)
                                                                    .align(Alignment.Center),
                                                                color = Color.Red
                                                            )
                                                        }
                                                    })
                                            }

                                        } else {

                                            Image(
                                                painter = painterResource(id = R.drawable.product_frock_3),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .verticalScroll(rememberScrollState())
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
                                                .weight(0.35f),
                                            text = product.name,
                                            color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color(
                                                0xFF8C8585
                                            ),
                                            lineHeight = 16.sp,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold

                                        )
                                        Column(
                                            Modifier.weight(0.65f)
                                        ) {
                                            Text(text = product.category, fontSize = 14.sp)
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
                                                    fontSize = 18.sp,
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
}

