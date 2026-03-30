package com.blimas.mycryptolog.di

import com.blimas.mycryptolog.data.repository.TransactionRepositoryImpl
import com.blimas.mycryptolog.data.repository.WalletRepositoryImpl
import com.blimas.mycryptolog.domain.repository.TransactionRepository
import com.blimas.mycryptolog.domain.repository.WalletRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideWalletRepository(
        db: DatabaseReference,
        auth: FirebaseAuth
    ): WalletRepository = WalletRepositoryImpl(db, auth)

    @Provides
    @Singleton
    fun provideTransactionRepository(
        db: DatabaseReference,
        auth: FirebaseAuth
    ): TransactionRepository = TransactionRepositoryImpl(db, auth)
}
