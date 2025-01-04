package com.example.shoppinguserapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.I
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
import androidx.navigation.NavController
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes

@Composable
fun WishListScreenUI(navController: NavController) {

}
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .height(550.dp)
//        ) {
//            // Background Image
//            Image(
//                painter = painterResource(id = R.drawable.cheakout_img),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxSize(), // Image height adjusted
//                contentScale = ContentScale.FillBounds
//            )
//            // Gradient Overlay on Image
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(110.dp)
//                    .align(Alignment.BottomCenter)
//                    .background(
//                        Brush.verticalGradient(
//                            colors = listOf(
//                                Color.Transparent,if(isSystemInDarkTheme()) Color.Black else Color.White
//                                    .copy(alpha = 1f)
//                            ),
//                            startY = 0f,
//                            endY = 250f
//                        )
//                    )
//                    .clip(shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)),
//                contentAlignment = Alignment.TopStart
//            ) {
//                Column(
//                    modifier = Modifier.padding(horizontal = 20.dp),
//                    verticalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//
//                    Text(
//                        text = "One Shoulder Linen Dress",
//                        fontSize = 25.sp,
//                        fontWeight = FontWeight.ExtraBold,
//                        color = Color.White,
//
//                        )
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//
//                        ) {
//                        repeat(4) {
//                            Icon(
//                                imageVector = Icons.Default.Star,
//                                contentDescription = "Star",
//                                tint = Color.Yellow,
//                                modifier = Modifier.size(25.dp)
//                            )
//                        }
//                        Icon(
//                            imageVector = Icons.Default.StarBorder,
//                            contentDescription = "Half Star",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                    Text(text = buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 20.sp,
//                                fontFamily = FontFamily.SansSerif,
//                                fontWeight = FontWeight.Bold
//                            )
//                        ) {
//                            append("Rs. ")
//                        }
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 30.sp,
//                                fontWeight = FontWeight.ExtraBold,
//                                color = if(isSystemInDarkTheme()) Color.White else Color.Black
//
//                            )
//                        ) {
//                            append("879")
//                        }
//
//                    })
//
//                }
//
//            }
//        }
//
//        // Content Below Image
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(if(isSystemInDarkTheme()) Color.Black else Color.White)
//                .padding(vertical = 10.dp, horizontal = 20.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//
//        ) {
//            // Size Options
//            Text(
//                text = "Size",
//                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.titleLarge,
//            )
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                listOf("M", "L", "XL", "XXL").forEach { size ->
//                    Box(
//                        modifier = Modifier
//
//                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                            .padding(horizontal = 10.dp, vertical = 8.dp)
//                    ) {
//                        Text(text = size)
//                    }
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                IncreaseDecreesRow()
//            }
//            // Color Options
//            Text(
//                text = "Color",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold,
//
//                )
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                listOf(Color.Green, Color.Cyan, Color.Yellow, Color.Blue).forEach { color ->
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .background(color, RoundedCornerShape(8.dp))
//                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                    )
//                }
//            }
//            // Specification Section
//            SpecificationText()
//            // Buy Now Button
//            ButtonsContent(navController)
//        }
//    }
//}
//
//@Composable
//fun IncreaseDecreesRow(modifier: Modifier = Modifier) {
//    var count by remember { mutableIntStateOf(1) }
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        IconButton(onClick = { if (count > 1) count-- }) {
//            Icon(imageVector = Icons.Default.Remove, contentDescription = null)
//        }
//        Box(
//            modifier = Modifier
//
//                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                .padding(horizontal = 15.dp, vertical = 10.dp)
//        ) {
//            Text(text = "$count", style = MaterialTheme.typography.bodyMedium)
//        }
//        IconButton(onClick = { count++ }) {
//            Icon(imageVector = Icons.Default.Add, contentDescription = null)
//        }
//    }
//
//}
//
//@Composable
//fun ButtonsContent(navController: NavController) {
//    val context = LocalContext.current
//    Column(
//        modifier = Modifier.padding(top = 10.dp),
//        verticalArrangement = Arrangement.spacedBy(15.dp)
//    ) {
//        Button(
//            onClick = { navController.navigate(Routes.ShippingScreen) },
//            colors = ButtonDefaults.buttonColors(
//                Color(0xfff68b8b),
//                contentColor = Color.White
//            ),
//            modifier = Modifier
//                .fillMaxWidth(),
//            shape = RoundedCornerShape(15.dp)
//        ) {
//            Text(
//                text = "Buy now", fontSize = 20.sp,
//                modifier = Modifier.padding(vertical = 5.dp)
//            )
//        }
//        Button(
//            onClick = {
//                navController.navigate(Routes.CartScreen)
//            },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(15.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF8c8585), contentColor = Color.White
//            )
//        ) {
//            Text(
//                text = "Add to Cart ",
//                fontSize = 20.sp,
//                modifier = Modifier.padding(vertical = 5.dp)
//            )
//        }
//        var wishlistIcon by remember { mutableStateOf(false) }
//        Button(
//            onClick = {
//                wishlistIcon =!wishlistIcon
//                Toast.makeText(context, if (wishlistIcon)"Added to Wishlist" else
//                    "Removed from Wishlist",Toast.LENGTH_SHORT).show()
//
//            }, modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(15.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Transparent, contentColor = Color(0xfff68b8b)
//            )
//        )
//        {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Absolute.Center
//            ) {
//
//                Icon(
//                    imageVector = if (wishlistIcon) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
//                    contentDescription = null
//                )
//                Spacer(modifier = Modifier.width(10.dp))
//                Text(
//                    text = "Add to Wishlist",
//                    fontSize = 20.sp,
//                    modifier = Modifier.padding(vertical = 5.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SpecificationText(modifier: Modifier = Modifier) {
//
//    Text(
//        text = "Specification",
//        style = MaterialTheme.typography.titleLarge,
//        fontWeight = FontWeight.Bold,
//    )
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Text(text = "Dress")
//        Text(text = "Material: Linen")
//        Text(
//            text = "Material Composition: 100% Linen",
//            style = MaterialTheme.typography.bodyLarge
//        )
//        Text(
//            text = "Please bear in mind that the photo may be slightly different from the actual item in terms of color due to lighting conditions or the display used to View...",
//            style = MaterialTheme.typography.bodyLarge,
//            textAlign = TextAlign.Justify
//        )
//    }
//}
