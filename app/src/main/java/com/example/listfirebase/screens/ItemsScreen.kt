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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.firebasedata.items.ItemsViewModel
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.predefinedlook.ItemsList


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    id: String,
    navController: NavController = rememberNavController(),
    listViewModel: ListViewModel = hiltViewModel(),
    itemsViewModel: ItemsViewModel = hiltViewModel()
) {

    //Toast.makeText(LocalContext.current, "$id", Toast.LENGTH_LONG).show()
    val itemsFlow = itemsViewModel.getAllItems
    val newItems = itemsFlow.collectAsState(initial = emptyList()).value
    val scope = rememberCoroutineScope()
    var list: ListEntity = ListEntity()

    val parentList = listViewModel.getAllLists.collectAsState(initial = emptyList()).value
    for (item in parentList) {
        when (item.id) {
            id -> {
                list = item
            }
        }
    }



    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = list.listName) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddItemsFire.name + "/${list.id}")
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
            items(newItems.filter { it.itemCreatorId == list.id },
                key = { item -> item.itemId } // it has to have a key, swipe wouldn't work without it

            ) { item ->
                val dismissState = androidx.compose.material.rememberDismissState()

                if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                    Toast.makeText(LocalContext.current, "Delete", Toast.LENGTH_SHORT).show()
                    itemsViewModel.removeItem(itemId = item.itemId)
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
                            ) Color.Red //red color
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