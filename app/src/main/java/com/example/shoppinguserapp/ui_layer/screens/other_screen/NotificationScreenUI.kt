package com.example.shoppinguserapp.ui_layer.screens.other_screen


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.domen_layer.data_model.NotificationModel
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen.CartTopRow
import com.example.shoppinguserapp.ui_layer.viewmodel.NotificationViewModel

@Composable
fun NotificationScreenUI(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {

    val getNotificationState = viewModel.notificationState.collectAsStateWithLifecycle()
    val notificationData = getNotificationState.value.success
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getNotification()
    }
    Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(0.15f)) {
                Row (verticalAlignment = Alignment.Top){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.6f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Notifications",
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .weight(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    getNotificationState.value.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    getNotificationState.value.error != null -> {
                        Toast.makeText(
                            context,
                            getNotificationState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    notificationData.isNullOrEmpty() ->{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Notifications",
                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            items(notificationData ?: emptyList()) {
                                NotificationCard(it) {
                                    viewModel.markSeen(it.notificationId)
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
                                            Text(
                                                text = "Embroidered Linen Top GF2261",
                                                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
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
                                                    text = "1:30 PM",
                                                    color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
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
                                                text = "One Shoulder Linen Dress",
                                                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray
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
                                                    text = "5:20 PM",
                                                    color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
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
                                                    text = "5:10 PM",
                                                    color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
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

@Composable
fun NotificationCard(notification: NotificationModel, onClick: () -> Unit = {}) {
    val backgroundColor = when {
        notification.seen -> Color(0xFFE0E0E0)
        isSystemInDarkTheme() -> Color.DarkGray
        else -> Color.White
    }
    Card(
        modifier = Modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(backgroundColor),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.noti_1),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Column {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                )
                Text(
                    text = notification.message,
                    lineHeight = 16.sp,
                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                    fontSize = 14.sp,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTimeFilled,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = getTimeAgo(notification.timestamp),
                        fontSize = 12.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                    )
                }

            }
        }
    }

}