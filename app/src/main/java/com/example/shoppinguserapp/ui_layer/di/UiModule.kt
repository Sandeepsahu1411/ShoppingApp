package com.example.shoppinguserapp.ui_layer.di

import com.example.shoppinguserapp.data_layer.repoimple.Repoimple
import com.example.shoppinguserapp.domen_layer.repo.Repo
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    fun provideRepo(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
        firebaseMessaging:FirebaseMessaging
    ): Repo{
        return Repoimple(firebaseFireStore, firebaseAuth, firebaseStorage,firebaseMessaging)

    }


}