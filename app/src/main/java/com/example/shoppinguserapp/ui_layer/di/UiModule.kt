package com.example.shoppinguserapp.ui_layer.di

import com.example.shoppinguserapp.data_layer.repoimple.Repoimple
import com.example.shoppinguserapp.domen_layer.repo.Repo
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        firebaseAuth: FirebaseAuth
    ): Repo{
        return Repoimple(firebaseFireStore, firebaseAuth)

    }


}