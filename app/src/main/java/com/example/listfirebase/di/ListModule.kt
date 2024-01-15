package com.example.listfirebase.di

import android.net.ConnectivityManager
import com.example.listfirebase.data.AppDatabase
import com.example.listfirebase.data.firebasedata.listfirebase.ListRepository
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.room.addlist.ListDao
import com.example.listfirebase.data.room.addlist.ListRoomRepository
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.session.ListSession
import com.example.listfirebase.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ListModule {
    //room
    @Provides
    @Singleton
    fun providesListRoomDao(database: AppDatabase) = database.listDao()

    @Provides
    @Singleton
    fun providesListRoomRepository(dao: ListDao) = ListRoomRepository(dao)

    @Provides
    @Singleton
    fun providesListRoomViewModel(
        repository: ListRoomRepository,
        userSessionManager: UserSessionManager,
        listSession: ListSession,
        userRepository: UserRepository
    ) = ListRoomViewModel(repository, userSessionManager, listSession, userRepository)

    @Provides
    @Singleton
    fun providesListSession(): ListSession {
        return ListSession()
    }

    //firebase
    @Provides
    @Singleton
    fun provideListRepository(userSessionManager: UserSessionManager, dao: ListDao): ListRepository {
        return ListRepository(userSessionManager, dao)
    }

    @Provides
    @Singleton
    fun providesListViewModel(
        repository: ListRepository,
        connectivityManager: ConnectivityManager,
        userSessionManager: UserSessionManager,
        userRepository: UserRepository,
        listRoomRepository: ListRoomRepository
    ) = ListViewModel(
        repository,
        connectivityManager,
        userSessionManager,
        userRepository,
        listRoomRepository
    )

    @Provides
    @Singleton
    fun providesListItemsViewModel(listSession: ListSession) = ListItemsViewModel(listSession)
}