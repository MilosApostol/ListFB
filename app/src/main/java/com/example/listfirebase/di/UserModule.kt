package com.example.listfirebase.di

import android.content.Context
import androidx.room.Room
import com.example.listfirebase.data.AppDatabase
import com.example.listfirebase.data.room.loginregister.LoginDao
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: LoginDao,
        userSessionManager: UserSessionManager
    ): UserRepository {
        return UserRepository(userDao, userSessionManager)
    }

    @Provides
    @Singleton
    fun providesUserSession(): UserSessionManager {
        return UserSessionManager()
    }

    @Provides
    @Singleton
    fun providesUserDao(database: AppDatabase) = database.loginDao()
}