package com.example.listfirebase.data.firebasedata.items

import com.example.listfirebase.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.cancellation.CancellationException

class ItemsRepository() {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constants.Items)
            .child(Firebase.auth.currentUser?.uid!!)

    val _itemsFlow = MutableStateFlow<List<ItemsEntity>>(emptyList())

    private var job: Job? = null


    fun getItemsFlow(): StateFlow<List<ItemsEntity>> {
        return _itemsFlow
    }

    fun readData() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemsList = mutableListOf<ItemsEntity>()
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item: ItemsEntity? = itemSnapshot.getValue(ItemsEntity::class.java)
                        item?.let { itemsList.add(it) }
                    }
                    _itemsFlow.value = itemsList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        job = Job()

        job?.let { current ->
            databaseReference.addValueEventListener(valueEventListener)

            current.invokeOnCompletion {
                if (it is CancellationException) {
                    databaseReference.removeEventListener(valueEventListener)
                }
            }
        }
    }

    fun removeItem(itemId: String) {
        databaseReference.child(itemId).removeValue()
    }
    fun deleteAll() {
        databaseReference.removeValue()
    }

    fun updateItem(reference: DatabaseReference, itemsEntity: ItemsEntity, key: String) {
        reference.setValue(itemsEntity).addOnCompleteListener { task ->
            itemsEntity.copy(itemId = key)
            if (task.isSuccessful) {

            } else {
            }
        }
    }
}