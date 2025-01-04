package com.example.shoppinguserapp.data_layer.di

import androidx.annotation.IntDef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    fun provideFirebase() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()

    }
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()

    }
}