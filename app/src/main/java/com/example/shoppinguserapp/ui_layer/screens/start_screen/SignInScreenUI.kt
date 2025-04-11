package com.example.shoppinguserapp.ui_layer.screens.start_screen

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.navigation.SubNavigation
import com.example.shoppinguserapp.ui_layer.screens.CustomButton
import com.example.shoppinguserapp.ui_layer.screens.CustomOutlinedTextField
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.google.common.math.LinearTransformation.horizontal

@Composable
fun SignInScreenUI(
    viewModel: AppViewModel = hiltViewModel(),
    navController: NavController,
) {

    val loginState = viewModel.loginUserState.collectAsState()
    val context = LocalContext.current
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    when {
        loginState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        loginState.value.error != null -> {
            Toast.makeText(context, loginState.value.error, Toast.LENGTH_SHORT).show()
            viewModel.clearLoginState()
        }

        loginState.value.success != null -> {
            Toast.makeText(context, loginState.value.success, Toast.LENGTH_SHORT).show()
            viewModel.clearLoginState()
            navController.navigate(Routes.HomeScreen) {
                popUpTo(SubNavigation.LoginSignUpScreen) {
                    inclusive = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(0.2f)) {
                Text(
                    text = "Login",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(30.dp)
                        .align(Alignment.Bottom),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,

                    )
                Image(
                    painter = painterResource(id = R.drawable.sign_top),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .size(200.dp),
                    alignment = Alignment.TopEnd
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .weight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                CustomOutlinedTextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    placeholderText = "Email",
                    Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                )

                CustomOutlinedTextField(
                    value = userPassword,
                    onValueChange = { userPassword = it },
                    placeholderText = "Password",
                    Modifier.fillMaxWidth(),
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                )
                Text(
                    text = "Forgot Password?",
                    fontSize = 18.sp,
                    color = Color(0xFFF68B8B),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(0.dp))
                CustomButton(
                    text= "Login",
                    onClick= {
                        if (userEmail.isEmpty() || userPassword.isEmpty()) {
                            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT)
                                .show()
                            return@CustomButton
                        }
                        viewModel.loginUser(userEmail, userPassword)
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? ",
                        color = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Sign Up",
                        color = Color(0xFFF68B8B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.SignUpScreen)
                        }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp), color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray
                    )
                    Text(
                        text = " Or ".uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp),
                        color = Color.Gray
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp),color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray
                    )
                }
                OutlinedIconButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp),
                            )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Login with Facebook",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            )
                    }
                }
                OutlinedIconButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Login with Google",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                        )
                    }
                }

            }
            Image(
                painter = painterResource(id = R.drawable.sign_bottom),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .size(150.dp)
                    .weight(0.15f),
                alignment = Alignment.BottomStart
            )
        }
    }
}

