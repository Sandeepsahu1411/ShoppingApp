package com.example.shoppinguserapp.ui_layer.screens

import android.R.attr.contentDescription
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.SubcomposeAsyncImage
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.domen_layer.data_model.UserData
import com.example.shoppinguserapp.ui_layer.navigation.Routes
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
                    Row(modifier = Modifier.weight(0.25f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f),
                            contentAlignment = Alignment.Center

                        ) {
                            SubcomposeAsyncImage(
                                model =
                                if (imageUrl.isNotEmpty()) imageUrl else R.drawable.default_profile,
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
                                            modifier = Modifier.size(40.dp),
                                            color = Color(0xFFF68B8B)
                                        )
                                    }
                                })
                            if (updateImageState.value.isLoading) {
                                Box(
                                    modifier = Modifier
                                        .size(130.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        color = Color(0xFFF68B8B)
                                    )
                                }
                            }

                            if (isEditable) {
                                Box(
                                    modifier = Modifier.size(140.dp),
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
                                .weight(0.5f)
                                .size(200.dp),
                            alignment = Alignment.TopEnd
                        )

                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .weight(0.7f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
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
                                isEditable = isEditable
                            )
                            CustomOutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholderText = "Last Name",
                                modifier = Modifier.weight(1f),
                                isEditable = isEditable

                            )
                        }

                        CustomOutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholderText = "Email",
                            Modifier.fillMaxWidth(),
                            isEditable = isEditable
                        )
                        CustomOutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            placeholderText = "Phone Number",
                            Modifier.fillMaxWidth(),
                            isEditable = isEditable


                        )
                        CustomOutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholderText = "Address",
                            Modifier.fillMaxWidth(),
                            isEditable = isEditable

                        )
                        if (!isEditable) {

                            Button(
                                onClick = {
                                    showDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF68B8B), contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Log Out",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            OutlinedIconButton(
                                onClick = {
                                    isEditable = !isEditable
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .border(
                                        BorderStroke(2.dp, Color(0xFFF68B8B)),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                shape = RoundedCornerShape(18.dp),

                                ) {
                                Text(
                                    text = "Edit Profile",
                                    fontSize = 16.sp,
                                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                            }
                        } else {
                            Button(
                                onClick = {
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
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF68B8B), contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Update Profile",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
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
                            .weight(0.2f),
                        alignment = Alignment.BottomStart
                    )
                }
                LogoutDialog(showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onLogout = {
                        showDialog = false
                        firebaseAuth.signOut()
                        navController.navigate(Routes.SignInScreen) {
                            popUpTo(Routes.SignInScreen) {
                                inclusive = true
                            }
                        }
                    })
            }

        }
    }


}