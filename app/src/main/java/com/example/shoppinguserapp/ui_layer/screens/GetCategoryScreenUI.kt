package com.example.shoppinguserapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel

@Composable
fun GetCategoryScreenUI(viewModel: AppViewModel = hiltViewModel()) {
    val categoryState = viewModel.getCategoryState.collectAsState()
    val context = LocalContext.current

    when {
        categoryState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }
        categoryState.value.error.isNotEmpty() -> {
            Toast.makeText(context, categoryState.value.error, Toast.LENGTH_SHORT).show()

        }
        categoryState.value.success.isNotEmpty() -> {
            Toast.makeText(context, categoryState.value.success.toString(), Toast.LENGTH_SHORT)
                .show()

        }
    }
    LaunchedEffect(Unit) {
        viewModel.getAllCategory()
    }

    LazyRow(){
        items(categoryState.value.success){
            Text(text = it!!.name)
            Text(text = it.imageUrl)
        }

        }


}