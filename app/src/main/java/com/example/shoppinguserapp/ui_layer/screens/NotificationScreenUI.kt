package com.example.shoppinguserapp.ui_layer.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shoppinguserapp.R

@Composable
fun NotificationScreenUI(navController: NavController) {


    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Notifications",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth().clickable {
                navController.navigateUp()
            }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                Modifier.size(20.dp),
                tint = Color(0xFFF68B8B)
            )
            Text(
                text = "notification",
                color = Color(0xFFF68B8B),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noti_1),
                            contentDescription = null,
                            modifier = Modifier.size(70.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(text = "Successful purchase!",  color = if(isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                                Text(
                                    text = "Just Now",  color = if(isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                )
                            }

                        }
                    }


                }
            }
            item {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noti_2),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp, 90.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,

                            )
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(text = "Embroidered Linen Top GF2261",
                                color = if(isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                                Text(
                                    text = "1:30 PM", color = if(isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                )
                            }

                        }
                    }


                }
            }
            item {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noti_3),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp, 90.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,

                            )
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(text = "One Shoulder Linen Dress", color = if(isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                                Text(
                                    text = "5:20 PM", color = if(isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                )
                            }

                        }
                    }


                }
            }
            item {
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noti_3),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp, 90.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,

                            )
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Order Cancelled !",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                                Text(
                                    text = "5:10 PM", color = if(isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}