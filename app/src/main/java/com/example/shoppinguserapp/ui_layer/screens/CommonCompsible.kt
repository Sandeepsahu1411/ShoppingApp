package com.example.shoppinguserapp.ui_layer.screens

import android.R.attr.fontStyle
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.example.shoppinguserapp.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isEditable: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
    onlyNumbers: Boolean = false

) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (!onlyNumbers || it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        label = {
            Text(
                text = placeholderText,
                color = if (isSystemInDarkTheme()) Color(0xFFEFCECE) else Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        },
        modifier = modifier.height(60.dp),
        singleLine = true,
        readOnly = !isEditable,
        textStyle = TextStyle(fontSize = 16.sp),
        visualTransformation = if (isPassword && !passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val icon =
                    if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = icon,
                        tint = Color(0xFFF68B8B),
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                        modifier = Modifier.padding(horizontal = 5.dp)

                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFFF68B8B),
            unfocusedIndicatorColor = Color(0xFFF68B8B),
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction?.invoke()
            },
            onGo = { onImeAction?.invoke() }

        )
    )
}


@Composable
fun SuccessDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    val scale = animation()
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD8D8DC)),
            contentAlignment = Alignment.Center
        ) {
            AlertDialog(onDismissRequest = onDismiss, confirmButton = {

                Button(

                    onClick = onDismiss, colors = ButtonDefaults.buttonColors(
                        Color(0xFFF68B8B)
                    ), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Done")
                }
            }, title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .scale(scale)
                            .background(color = Color(0xFFF68B8B), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(60.dp) // Adjust size of Icon, remove padding if not needed
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Success",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF68B8B),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }, text = {
                Text(
                    text = "Congratulations, you have completed your registration!",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }, shape = RoundedCornerShape(20.dp), modifier = Modifier.padding(vertical = 20.dp)
            )
        }
    }
}

@Composable
fun LogoutDialog(
    showDialog: Boolean, onDismiss: () -> Unit, onLogout: () -> Unit
) {
    if (showDialog) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            AlertDialog(onDismissRequest = onDismiss, confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = BorderStroke(1.dp, Color.Red)
                    ) {
                        Text(text = "Cancel", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF68B8B)
                        )
                    ) {
                        Text(
                            text = "Log Out", fontWeight = FontWeight.Bold, color = Color.White
                        )
                    }
                }
            }, title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile image
                    Image(
                        painter = painterResource(id = R.drawable.fake_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color(0xFFF68B8B), CircleShape)

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "LOG OUT",
                        color = Color(0xFFF68B8B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }, text = {
                Text(
                    text = "Do you Really Want To Logout!",
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color(0xFFEFCECE) else Color.DarkGray,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }, shape = RoundedCornerShape(20.dp)

            )
        }
    }
}

@Composable
fun animation(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Animation for scaling in and out
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.2f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        ), label = "zoom in-out animation"
    )
    return scale
}

@Composable
fun CartHeaderRow() {
    val headers = listOf("Items" to 0.55f, "Price" to 0.2f, "QTY" to 0.15f, "Total" to 0.15f)

    Row {
        headers.forEach { (title, weight) ->
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,

                modifier = Modifier.weight(weight)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(modifier: Modifier = Modifier) {
    var search by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = search,
        onValueChange = { search = it },
        placeholder = { Text(text = "Search", color = Color.LightGray) },
        leadingIcon = {
            Icon(
                Icons.Default.Search, contentDescription = "Search",
                tint = Color(0xFFF68B8B),
                modifier = Modifier.size(30.dp),
            )
        },
//        modifier = Modifier.weight(1f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFFF68B8B),
            unfocusedIndicatorColor = Color(0xFFF68B8B),
        )
    )

}


@Composable
fun BannerSection() {
    val bannerImages = listOf(
        R.drawable.banner_1, // Replace with your banner images
        R.drawable.banner_2,
        R.drawable.banner_3
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { bannerImages.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState, // Updated parameter
            pageSize = PageSize.Fill, // Total number of banners
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 1.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray) // Placeholder color
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = bannerImages[page]),
                    contentDescription = "Banner Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(bannerImages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier

                        .size(if (isSelected) 20.dp else 14.dp)
                        .padding(4.dp)
                        .background(
                            if (isSelected) Color(0xFFD77A7A) else Color(0xFFF68B8B).copy(),
                            shape = CircleShape
                        )
                )
            }
        }
        LaunchedEffect(pagerState) {

            while (true) {
                delay(3000)
                val nextPage = if (pagerState.currentPage == bannerImages.lastIndex) {
                    0
                } else {
                    pagerState.currentPage + 1
                }
                pagerState.scrollToPage(nextPage)


            }
        }


    }
}
