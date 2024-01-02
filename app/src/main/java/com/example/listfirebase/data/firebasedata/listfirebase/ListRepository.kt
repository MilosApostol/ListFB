package com.example.listfirebase.data.firebasedata.listfirebase

import android.os.Parcel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.room.addlist.ListDao
import com.example.listfirebase.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ListRepository @Inject constructor(
    val userSessionManager: UserSessionManager,
    val dao: ListDao
) {

    private val _list = MutableStateFlow<List<ListEntity>>(emptyList())
    val list: StateFlow<List<ListEntity>> = _list

    private var job: Job? = null

    private val reference = FirebaseDatabase.getInstance().getReference(Constants.Lists)

    private val _listFlow = MutableStateFlow<List<ListEntity>>(emptyList())


    fun readData() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listNew = mutableListOf<ListEntity>()
                for (itemSnapshot in snapshot.children) {
                    val list: ListEntity? = itemSnapshot.getValue(ListEntity::class.java)
                    list?.let { listNew.add(it) }
                }
                _listFlow.value = listNew
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        job = Job()

        job?.let { current ->
            reference.addValueEventListener(valueEventListener)

            current.invokeOnCompletion {
                if (it is CancellationException) {
                    // Handle cancellation logic, e.g., log a message
                }
                reference.removeEventListener(valueEventListener)
            }
        }
    }

    suspend fun getLists(): StateFlow<List<ListEntity>> {
        return _listFlow
    }

    suspend fun uploadData(
        itemsToUpload: List<ListEntity>, callback: (Boolean) -> Unit
    ) {
        val reference = FirebaseDatabase.getInstance().getReference(Constants.Lists)
        for (item in itemsToUpload) {
            reference.setValue(item).addOnCompleteListener {
                val success = it.isSuccessful
                callback(success)
            }
        }
    }

    suspend fun saveData(
        ref: DatabaseReference,
        list: ListEntity,
        key: String,
        callback: (Boolean) -> Unit
    ) {
        val updateList = list.copy(id = key)
        dao.insertList(list = updateList)

        ref.setValue(updateList).addOnCompleteListener { task ->
            val success = task.isSuccessful
            callback(success)
        }
    }

    fun saveItems(ref: DatabaseReference, list: ItemsEntity, key: String) {
        ref.setValue(list).addOnCompleteListener { task ->
            list.copy(itemId = key)
            if (task.isSuccessful) {

            } else {
            }
        }
    }

    fun deleteAll() {
        reference.removeValue()
    }
}
