package com.example.listfirebase.data.room.additems

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsRoomViewModel @Inject constructor
    (val itemsRoomRepository: ItemsRoomRepository): ViewModel() {


}