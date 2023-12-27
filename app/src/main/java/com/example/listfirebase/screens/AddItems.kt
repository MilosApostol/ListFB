package com.example.listfirebase.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsViewModel
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.firebasedata.items.ItemsViewModel
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.predefinedlook.SearchItems
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItems(
    id: String,
    navController: NavController = rememberNavController(),
    addItemsViewModel: AddItemsViewModel = hiltViewModel(),
    itemsViewModel: ItemsViewModel = hiltViewModel()
) {
    val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constants.Items)
            .child(Firebase.auth.currentUser?.uid!!)
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = addItemsViewModel.addItem.collectAsState().value
    val scope = rememberCoroutineScope()
    //  val allItems by addCustomViewModel.getAllItems.collectAsState(initial = listOf()) for room
    //  val filteredItems = allItems.filter { it.title.contains(text, ignoreCase = true) }
    val context = LocalContext.current
    Toast.makeText(context, "$id", Toast.LENGTH_LONG).show()
    LaunchedEffect(Unit) {
        scope.launch {
            addItemsViewModel.getItems()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopAppBar(title = { Text(text = "AddItems") }, modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
                }
            })
        DockedSearchBar(
            modifier = Modifier
                .padding(top = 8.dp)
                .semantics { traversalIndex = -1f }
                .fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                addItemsViewModel.onSearchChange(text)
            },
            onSearch = {
                for (item in items) {
                    val key = databaseReference.key!!
                    val item =
                        ItemsEntity(
                            itemId = UUID.randomUUID().toString(), //temporary ID
                            itemName = item.title,
                            description = item.description,
                            itemCreatorId = id //parent ID
                        )
                    val newRef = databaseReference.push()
                        .setValue(item) { _, ref ->
                            val key = ref.key
                            item.itemId = key!!
                        }
                    addItemsViewModel.saveItems(databaseReference, item, key)

                    active = false
                    navController.navigate(Screens.ItemsScreenFire.name + "/$key")
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
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
            {
                items(
                    items
                ) { item ->
                    SearchItems(itemsData = item, onClick = {
                        saveData(item.title, id, item.description, navController, itemsViewModel = itemsViewModel)
                    })
                }
            }
        }

    }
}
fun saveData(
    itemName: String,
    id: String,
    description: String,
    navController: NavController,
    itemsViewModel: ItemsViewModel
) {

    val db = Firebase.database
    val ref = db.getReference(Constants.Items)
        .child(Firebase.auth.currentUser?.uid!!)
    val newItem = ItemsEntity(
        itemName = itemName,
        itemId = UUID.randomUUID().toString(),
        itemCreatorId = id.toString(),
        description = description
    )
    val key = ref.key!!
    val newValue = ref.push()
    newValue.setValue(newItem){error, ref ->
        val key = ref.key
        newItem.itemId = key!!
        itemsViewModel.updateData(ref, newItem, key)
    }
        navController.navigate(Screens.ItemsScreenFire.name + "/$id") {
            popUpTo(Screens.AddItemsFire.name + "$id") {
                inclusive = true
            }
        }
}


