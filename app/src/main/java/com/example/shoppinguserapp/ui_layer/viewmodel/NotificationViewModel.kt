package com.example.shoppinguserapp.ui_layer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinguserapp.common.ResultState
import com.example.shoppinguserapp.domen_layer.data_model.NotificationModel
import com.example.shoppinguserapp.domen_layer.use_case.UseCase
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private val _notificationState = MutableStateFlow(NotificationState())
    val notificationState = _notificationState.asStateFlow()

    fun getNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getNotificationUseCase().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _notificationState.value = NotificationState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _notificationState.value = NotificationState(success = it.data)
                    }

                    is ResultState.Error -> {
                        _notificationState.value =
                            NotificationState(error = it.exception.message.toString())
                    }

                }
            }

        }
    }
    fun markSeen(notificationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.markNotificationAsReadUseCase(notificationId).collect()
        }
    }

    fun addNotification(notifications: NotificationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.addNotificationUseCase(notifications).collect()
        }
    }


}


data class NotificationState(
    val isLoading: Boolean = false,
    val success: List<NotificationModel>? = emptyList(),
    val error: String? = null
)