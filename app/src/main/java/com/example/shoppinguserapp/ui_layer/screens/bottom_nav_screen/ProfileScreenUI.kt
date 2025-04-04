package com.example.shoppinguserapp.ui_layer.screens.bottom_nav_screen

import android.R.attr.text
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.screens.CustomButton
import com.example.shoppinguserapp.ui_layer.screens.CustomOutlineButton
import com.example.shoppinguserapp.ui_layer.screens.CustomOutlinedTextField
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreenUI(
    navController: NavController,
    firebaseAuth: FirebaseAuth,
    viewModel: AppViewModel = hiltViewModel(),
) {
    var showDialog by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    val userState = viewModel.getUserDetailsState.collectAsStateWithLifecycle()
    val updateUserState = viewModel.updateUserDetailsState.collectAsStateWithLifecycle()
    val updateImageState = viewModel.uploadImageState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            viewModel.uploadImage(it)
            imageUri = it
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getUserDetails(firebaseAuth.currentUser?.uid.toString())
    }
    LaunchedEffect(userState.value.success) {
        userState.value.success.let {
            firstName = it?.firstName ?: ""
            lastName = it?.lastName ?: ""
            email = it?.email ?: ""
            phoneNumber = it?.phoneNumber ?: ""
            address = it?.address ?: ""
            password = it?.password ?: ""
            imageUrl = it?.profilePicture ?: ""

        }
    }

    if (updateImageState.value.success != null) {
        imageUrl = updateImageState.value.success.toString()
        updateImageState.value.success = null
    }
    when {
        updateUserState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        updateUserState.value.error.isNotEmpty() -> {
            Toast.makeText(LocalContext.current, updateUserState.value.error, Toast.LENGTH_SHORT)
                .show()
        }

        updateUserState.value.success != null -> {
            Toast.makeText(LocalContext.current, updateUserState.value.success, Toast.LENGTH_SHORT)
                .show()
            updateUserState.value.success = null
        }
    }
    when {
        userState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        userState.value.error.isNotEmpty() -> {
            Toast.makeText(LocalContext.current, userState.value.error, Toast.LENGTH_SHORT).show()
        }

        userState.value.success != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(0.2f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.6f),
                            contentAlignment = Alignment.Center

                        ) {
                            SubcomposeAsyncImage(
                                model = if (imageUrl.isNotEmpty()) imageUrl else R.drawable.default_profile,
                                contentDescription = null,
                                modifier = Modifier
                                    .border(4.dp, Color(0xFFF68B8B), CircleShape)
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(40.dp),
                                            color = Color(0xFFF68B8B)
                                        )
                                    }
                                })
                            if (updateImageState.value.isLoading) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp), color = Color(0xFFF68B8B)
                                    )
                                }
                            }

                            if (isEditable) {
                                Box(
                                    modifier = Modifier.size(130.dp),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    IconButton(onClick = {
                                        launcher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .align(Alignment.Center)
                                                .background(Color(0xFFF68B8B), CircleShape)
                                        )
                                    }
                                }
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .weight(0.7f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            CustomOutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                placeholderText = "First Name",
                                modifier = Modifier.weight(1f),
                                isEditable = isEditable,
                                imeAction = ImeAction.Next
                            )
                            CustomOutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholderText = "Last Name",
                                modifier = Modifier.weight(1f),
                                isEditable = isEditable,
                                imeAction = ImeAction.Next
                            )
                        }

                        CustomOutlinedTextField(
                            value = email,
                            onValueChange = { email = it.lowercase() },
                            placeholderText = "Email",
                            Modifier.fillMaxWidth(),
                            isEditable = isEditable,
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        )
                        CustomOutlinedTextField(
                            value = phoneNumber,
                            onValueChange = {
                                if (it.length <= 10) {
                                    phoneNumber = it
                                }
                            },
                            placeholderText = "Phone Number",
                            Modifier.fillMaxWidth(),
                            isEditable = isEditable,
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Phone
                        )
                        CustomOutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholderText = "Address",
                            Modifier.fillMaxWidth(),
                            isEditable = isEditable,
                        )
                        if (!isEditable) {
                            CustomButton(
                                text = "Log Out",
                                onClick = {
                                    showDialog = true
                                }
                            )
                            CustomOutlineButton(
                                text="Edit Profile",
                                onClick = {
                                    isEditable = !isEditable
                                }
                            )
                            CustomButton(
                                text = "My Orders",
                                onClick = {
                                    navController.navigate(Routes.OrderScreen)
                                }
                            )

                        } else {
                            CustomButton(
                                text = "Update Profile",
                                onClick = {
                                    val emailPattern =
                                        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,3}\$")
                                    if (!emailPattern.matches(email)) {
                                        Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT)
                                            .show()
                                        return@CustomButton
                                    }
                                    if (phoneNumber.length != 10 || !phoneNumber.all { it.isDigit() }) {
                                        Toast.makeText(
                                            context,
                                            "Invalid phone number",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@CustomButton
                                    }
                                    viewModel.updateUserDetails(
                                        firebaseAuth.currentUser?.uid.toString(), UserData(
                                            firstName = firstName,
                                            lastName = lastName,
                                            email = email,
                                            phoneNumber = phoneNumber,
                                            address = address,
                                            password = password,
                                            profilePicture = imageUrl

                                        )
                                    )
                                    isEditable = !isEditable
                                }
                            )
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.sign_bottom),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .size(150.dp)
                            .weight(0.1f),
                        alignment = Alignment.BottomStart
                    )
                }
                LogoutDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onLogout = {
                        showDialog = false
                        firebaseAuth.signOut()
                        navController.navigate(Routes.SignInScreen) {
                            popUpTo(Routes.SignInScreen) {
                                inclusive = true
                            }
                        }
                    },
                    imageUrl = imageUrl
                )
            }

        }
    }


}

@Composable
fun LogoutDialog(
    showDialog: Boolean, onDismiss: () -> Unit, onLogout: () -> Unit, imageUrl: String
) {
    if (showDialog) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            AlertDialog(
                onDismissRequest = onDismiss, confirmButton = {
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

                        SubcomposeAsyncImage(
                            model = if (imageUrl.isNotEmpty()) imageUrl else R.drawable.default_profile,
                            contentDescription = null,
                            modifier = Modifier
                                .border(4.dp, Color(0xFFF68B8B), CircleShape)
                                .size(130.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp), color = Color(0xFFF68B8B)
                                    )
                                }
                            })

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