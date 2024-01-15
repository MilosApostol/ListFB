package com.example.listfirebase.di


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsDao
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsRepository
import com.example.listfirebase.data.retrofit.ApiItemsClient
import com.example.listfirebase.data.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesAddItemsService(): ApiService {
        return ApiItemsClient.apiService
    }

    @Provides
    @Singleton
    fun providesAddItemsRepository(apiService: ApiService, dao: AddItemsDao): AddItemsRepository {
        return AddItemsRepository(apiService, dao )
    }


    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}