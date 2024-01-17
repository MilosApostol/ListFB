package com.example.listfirebase.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.firebasedata.items.ItemsViewModel
import com.example.listfirebase.data.room.items.ItemsRoomViewModel
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.predefinedlook.ItemsList
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    id: String = "",
    navController: NavController = rememberNavController(),
    listViewModel: ListViewModel = hiltViewModel(),
    itemsViewModel: ItemsViewModel = hiltViewModel(),
    itemsRoomViewModel: ItemsRoomViewModel = hiltViewModel(),
    listRoomViewModel: ListRoomViewModel = hiltViewModel()
) {

    //firebase
    val newItems = itemsViewModel.getAllItems.collectAsState(initial = emptyList()).value
    val roomItems = itemsRoomViewModel.getAllItems.collectAsState(emptyList()).value
    val scope = rememberCoroutineScope()
    var list = ListEntity()
    var offList = ListEntity()
    val parentList = listViewModel.getAllLists.collectAsState(initial = emptyList()).value
    val dismissStates = remember { mutableStateMapOf<String, DismissState>() }

    val roomList = listRoomViewModel.getAllLists.collectAsState(initial = emptyList()).value
    val ref = Firebase.database.getReference(Constants.Items)
        .child(Firebase.auth.currentUser?.uid!!)
    val itemsToUpload = getItemsToUpload(roomItems)
    val context = LocalContext.current
    LaunchedEffect(itemsToUpload) {
        if (listViewModel.isNetworkAvailable()) {
            for (item in itemsToUpload) {
                val reference = ref.child(item.itemId)
                reference.setValue(item.copy(sync = "1")) { _, ref ->
                    scope.launch {
                        itemsViewModel.syncUpdate(reference, item) {
                            Toast.makeText(context,  "sync", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }


    if (listViewModel.isNetworkAvailable()) {
        for (item in parentList) {
            when (item.id) {
                //parent ListID
                id -> {
                    list = item
                }
            }
        }
    } else {
        for (item in roomList) {
            when (item.listCreatorId) {
                id -> {
                    offList = item
                }
            }
        }
    }
    LaunchedEffect(Unit) {

    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (listViewModel.isNetworkAvailable()) {
                        Text(text = list.listName)
                    } else {
                        Text(text = offList.listName)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()

                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (listViewModel.isNetworkAvailable()) {
                        navController.navigate(Screens.AddItems.name + "/${list.id}")
                    } else {
                        navController.navigate(Screens.AddItems.name + "/${offList.id}")
                    }
                },
            ) {
                Icon(Icons.Filled.Add, "Add List")
            }
        },
    )
    { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            if (listViewModel.isNetworkAvailable()) {

                items(newItems.filter { it.itemCreatorId == list.id }, key = { item -> item.itemId }) { item ->
                    val dismissState = dismissStates.getOrPut(item.itemId) {
                        rememberDismissState()
                    }
                    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                        Toast.makeText(LocalContext.current, "Delete", Toast.LENGTH_SHORT).show()
                        itemsViewModel.removeItem(itemId = item.itemId)
                        itemsRoomViewModel.removeItem(item)
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(
                            DismissDirection.EndToStart
                        ),
                        background = {
                            val color by animateColorAsState(
                                targetValue =
                                if (dismissState.dismissDirection ==
                                    DismissDirection.EndToStart
                                ) Color.Red
                                else Color.Transparent,
                                label = ""
                            )
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        dismissThresholds = { FractionalThreshold(0.5f) },
                        dismissContent = {
                            ItemsList(item = item)
                        })
                }
            } else {
                items(roomItems.filter { it.itemCreatorId == list.id },
                    key = { item -> item.itemId } // it has to have a key, swipe wouldn't work without it
                ) { item ->
                    val dismissState = dismissStates.getOrPut(item.itemId) {
                        rememberDismissState()
                    }
                    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                        itemsRoomViewModel.removeItem(item)
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(
                            DismissDirection.EndToStart
                        ),
                        background = {
                            val color by animateColorAsState(
                                targetValue =
                                if (dismissState.dismissDirection ==
                                    DismissDirection.EndToStart
                                ) Color.Red
                                else Color.Transparent,
                                label = ""
                            )
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        dismissThresholds = { FractionalThreshold(0.5f) },
                        dismissContent = {
                            ItemsList(item = item)
                        })
                }
            }
        }
    }
}

    fun getItemsToUpload(itemsList: List<ItemsEntity>): List<ItemsEntity> {
        return itemsList.filter { it.sync == "0" }
    }