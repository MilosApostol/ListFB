package com.example.listfirebase.predefinedlook

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.nav.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

@Composable
fun Lists(
    lists: List<ListEntity>,
    listViewModel: ListViewModel,
    navController: NavController,
    paddingValues: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
    {
        items(
            lists
        ) { list ->
            ListItems(
                list = list,
                onDeleteClick = {
                    val listKey = list.id
                    Firebase.database.getReference(Constants.Lists)
                        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                        .child(listKey)
                        .removeValue()

                },
                onRenameClick = {
                    val id = list.id
                    navController.navigate(Screens.AddListFire.name + "/$id")
                },
                onTextClick = {
                    val id = list.id
                    navController.navigate(Screens.ItemsScreenFire.name + "/$id")
                }

            )
        }
    }
}
