package com.example.shoppinguserapp.ui_layer.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes

@Composable
fun PaymentScreenUI(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Payments",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.clickable {
                navController.navigate(Routes.CartScreen)
            })
        {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                Modifier.size(15.dp)
            )
            Text(
                text = "Return to Shipping",
                color = if (isSystemInDarkTheme()) Color( 0xFFF68B8B) else Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                ProductDetail()
            }
            item {
                var selectedMethod by remember { mutableStateOf("Free") }
                PaymentMethod(
                    selectedMethod = selectedMethod,
                    onMethodSelected = { selectedMethod = it })
            }
            item {
                var selectedAddress by remember { mutableStateOf("Same") }
                BillingAddress(
                    selectedMethod = selectedAddress,
                    onMethodSelected = { selectedAddress = it })
            }
            item {
                Button(
                    onClick = {
                        navController.navigate(Routes.PaymentSuccessScreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8c8585), contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Pay Now",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethod(selectedMethod: String, onMethodSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Shipping Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = "All transactions are secure and encrypted.",
            fontSize = 12.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),

            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(
                        if (isSystemInDarkTheme()) Color.DarkGray else Color.White,
                    ),
                verticalArrangement = Arrangement.spacedBy(5.dp)

            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onMethodSelected("Free") }) {
                    RadioButton(
                        selected = selectedMethod == "Free",
                        onClick = { onMethodSelected("Free") },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                    )
                    Text(
                        text = "Sampath Bank IPG",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.7f)

                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.visa),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.mastercard),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.unionpay),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )

                    }
                }

                Card(
                    modifier = Modifier
                        .padding(5.dp), colors = CardDefaults.cardColors(
                        containerColor =
                         if (isSystemInDarkTheme()) Color(0x7EF5F2F1) else Color(0xFFF5F2F1),



                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(15.dp, 5.dp)

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.card_payment),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            text = "After clicking “Pay now”, you will be redirected to Sampath Bank IPG to complete your purchase securely.",
                            fontSize = 12.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),

                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }

                HorizontalDivider(thickness = 2.dp)
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onMethodSelected("COD") }) {
                    RadioButton(
                        selected = selectedMethod == "COD",
                        onClick = { onMethodSelected("COD") },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                    )
                    Text(
                        text = "Cash on delivery (COD)",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.85f)
                    )


                }


            }
        }
    }
}

@Composable
fun BillingAddress(selectedMethod: String, onMethodSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Shipping Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),

            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)

            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onMethodSelected("Same") }) {
                    RadioButton(
                        selected = selectedMethod == "Same",
                        onClick = { onMethodSelected("Same") },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                    )
                    Text(
                        text = "Same as shipping address",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.8f)

                    )
                    Text(
                        text = "Change",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(0.2f)

                    )
                }
                HorizontalDivider(thickness = 2.dp)
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onMethodSelected("Different") }) {
                    RadioButton(
                        selected = selectedMethod == "Different",
                        onClick = { onMethodSelected("Different") },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                    )
                    Text(
                        text = "Use a different billing address",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.8f)
                    )

                    Text(
                        text = "Change",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .weight(0.2f)
                            .clickable {}

                    )
                }


            }
        }
    }


}
