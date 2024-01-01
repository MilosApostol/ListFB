package com.example.listfirebase.data.room.additems

import androidx.lifecycle.ViewModel
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.session.ListSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class ListItemsViewModel @Inject constructor(
    private val listSessionManager: ListSession
) : ViewModel() {


    fun getList(): ListEntity? {
        return listSessionManager.getList()
    }
}