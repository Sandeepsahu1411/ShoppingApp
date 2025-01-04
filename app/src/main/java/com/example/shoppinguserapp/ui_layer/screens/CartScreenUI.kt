package com.example.shoppinguserapp.ui_layer.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes

@Composable
fun CartScreenUI(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(0.2f)) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Shopping Cart",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
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
                        .size(200.dp),
                    alignment = Alignment.TopEnd
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .weight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CartHeaderRow()
                LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(2) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.product_frock),
                                contentDescription = null,
                                modifier = Modifier

                                    .size(100.dp, 140.dp)
                                    .clip(RectangleShape),
                                contentScale = ContentScale.Crop

                            )
                            Column(
                                modifier = Modifier.weight(0.25f),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    text = "Modern Frock ",
                                    fontSize = 18.sp,
                                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,

                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "GF1025 ",
                                    fontSize = 16.sp,
                                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,

                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Size : UK10",
                                    fontSize = 16.sp,
                                    color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,

                                    )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Color : ",
                                        fontSize = 16.sp,
                                        color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,

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
                                text = "Rs: 8740",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(0.2f)

                            )
                            Text(
                                text = "1",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.15f),
                                textAlign = TextAlign.Center

                            )
                            Text(
                                text = "8740",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(0.15f)

                            )

                        }

                    }
                    item {
                        HorizontalDivider(thickness = 2.dp, color = Color.Gray)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Sub Total  ",
                                fontSize = 22.sp,
                                color = if (isSystemInDarkTheme()) Color( 0xFFF68B8B) else Color.DarkGray,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "Rs: 8740",
                                fontSize = 20.sp,
                                color = if (isSystemInDarkTheme()) Color( 0xFFF68B8B) else Color.DarkGray,
                                fontWeight = FontWeight.Bold
                            )

                        }

                        Button(
                            onClick = {
                                navController.navigate(Routes.ShippingScreen)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8c8585), contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "CheckOut",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }


                }


            }

        }


    }

}