package com.example.listfirebase.di

import android.content.Context
import androidx.room.Room
import com.example.listfirebase.data.AppDatabase
import com.example.listfirebase.data.firebasedata.items.ItemsRepository
import com.example.listfirebase.data.room.additems.ItemsDao
import com.example.listfirebase.data.room.additems.ItemsRoomRepository
import com.example.listfirebase.data.room.additems.ItemsRoomViewModel
import com.example.listfirebase.data.room.addlist.ListDao
import com.example.listfirebase.data.room.addlist.ListRoomRepository
import com.example.listfirebase.data.room.loginregister.LoginDao
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.session.ListSession
import com.example.listfirebase.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesDataBase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, AppDatabase.DATABASE
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth


}
