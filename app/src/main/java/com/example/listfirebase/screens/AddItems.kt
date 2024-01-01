package com.example.listfirebase.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsViewModel
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.firebasedata.items.ItemsViewModel
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.room.additems.ItemsRoomViewModel
import com.example.listfirebase.data.room.additems.ListItemsViewModel
import com.example.listfirebase.data.room.additemscustom.AddItemsCustomViewM
import com.example.listfirebase.di.CustomAdd
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.predefinedlook.SearchItems
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItems(
    id: String,
    navController: NavController = rememberNavController(),
    addItemsViewModel: AddItemsViewModel = hiltViewModel(),
    itemsViewModel: ItemsViewModel = hiltViewModel(),
    itemsRoomViewModel: ItemsRoomViewModel = hiltViewModel(),
    listViewModel: ListViewModel = hiltViewModel(),
    addItemsCustomViewM: AddItemsCustomViewM = hiltViewModel(),
    listItemsViewModel: ListItemsViewModel = hiltViewModel()
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = addItemsViewModel.addItem.collectAsState()
    val scope = rememberCoroutineScope()
    val key = ""

    //get all custom items from before
    val allItems by addItemsCustomViewM.gettCustomItems.collectAsState(initial = listOf())
    // have no idea what this does
    val filteredItems = allItems.filter { it.title.contains(text, ignoreCase = true) }
    // val list = listItemsViewModel.getList()

    if (listViewModel.isNetworkAvailable()) {

        LaunchedEffect(Unit) {
            scope.launch {
                addItemsViewModel.getItems()
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TopAppBar(title = { Text(text = "AddItems") },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Menu")
                    }
                })
            DockedSearchBar(
                modifier = Modifier
                    .semantics { traversalIndex = -1f }
                    .fillMaxWidth(),
                query = text,
                onQueryChange = {
                    text = it
                    addItemsViewModel.onSearchChange(text)
                },
                onSearch = {
                    if (listViewModel.isNetworkAvailable()) {
                        val item = AddItemsData(
                            title = text,
                            id = UUID.randomUUID().toString(),
                            listCreatorId = id
                        )
                        addItemsCustomViewM.addItem(item)
                        saveData(
                            itemName = text,
                            id,
                            navController,
                            itemsViewModel,
                            itemsRoomViewModel,
                            scope
                        )
                        active = false
                    } else {
                        addItemsCustomViewM.addItem(AddItemsData(title = text))
                    }
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Hinted search text") },
                leadingIcon = {
                    if (!active) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    } else {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.clickable { active = false })
                    }
                },
                trailingIcon = {
                    if (active) {
                        Icon(Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                            }
                        )
                    }
                }) {
                LazyColumn {
                    items(filteredItems) { items ->
                        CustomAdd(customItem = items, onClick = {
                            addItemsCustomViewM.removeItem(items)
                        }, onRowClick = {
                            addItemsCustomViewM.addToSelectedItems(items)
                            saveData(
                                itemName = items.title,
                                id,
                                navController,
                                itemsViewModel,
                                itemsRoomViewModel,
                                scope
                            )
                            active = false
                        })
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(Alignment.Top)
                ) {

                    items(items.value) { item ->
                        SearchItems(itemsData = item, onClick = {
                            addItemsViewModel.addToSelectedItems(item)
                            active = false
                            saveData(
                                itemName = item.title,
                                id,
                                navController,
                                itemsViewModel,
                                itemsRoomViewModel,
                                scope
                            )

                        })
                    }
                }
            }
        }
    }
}


fun saveData(
    itemName: String,
    id: String,
    navController: NavController,
    itemsViewModel: ItemsViewModel,
    itemsRoomViewModel: ItemsRoomViewModel,
    scope: CoroutineScope,
) {
    val db = Firebase.database
    val ref = db.getReference(Constants.Items)
        .child(Firebase.auth.currentUser?.uid!!)
    val newItem = ItemsEntity(
        itemName = itemName,
        itemId = UUID.randomUUID().toString(),
        itemCreatorId = id,
    )
    val key = ref.key!!
    val newValue = ref.push()
    newValue.setValue(newItem) { error, ref ->
        val key = ref.key
        newItem.itemId = key!!
        itemsViewModel.updateData(ref, newItem, key)
        scope.launch {
            itemsRoomViewModel.insertItemsOnline(newItem)
        }
    }
    navController.navigate(Screens.ItemsScreenFire.name + "/$id") {
        popUpTo(Screens.AddItems.name + "$id") {
            inclusive = true
        }
    }
}


