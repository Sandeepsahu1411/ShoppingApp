package com.example.shoppinguserapp.data_layer.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.random.Random
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.common.NOTIFICATIONS
import com.example.shoppinguserapp.domen_layer.data_model.NotificationModel
import com.example.shoppinguserapp.ui_layer.viewmodel.NotificationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
        }
    }

}

fun createNotificationChannel(
    context: Context,

    ) {
    val name = "Girl Shopping"
    val desc = "test_desc"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
        description = desc
    }
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}

fun sendNotification(
    context: Context,
    viewModel : NotificationViewModel
) {

    val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
        .setSmallIcon(R.drawable.google)
        .setContentTitle("Order")
        .setContentText("Order Created Successfully😊")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(Random.nextInt(), builder.build())

    val notificationModel = NotificationModel(
        title = "Order",
        message = "Order Created Successfully 😊",
        timestamp = System.currentTimeMillis(),
        seen =  false
    )
//    firebaseAuth.currentUser?.uid?.let { uid->
//        firestore.collection(NOTIFICATIONS).document(uid).collection("MyNotifications").add(notificationModel)
//    }
    viewModel.addNotification(notificationModel)

}
