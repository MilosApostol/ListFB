package com.example.listfirebase.predefinedlook

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.example.listfirebase.nav.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

@Composable
fun Lists(
    listFirebase: List<ListEntity>,
    listsRoom: List<ListEntity>,
    listViewModel: ListViewModel,
    navController: NavController,
    paddingValues: PaddingValues,
    userViewModel: UserViewModel,
    listRoomViewModel: ListRoomViewModel,
    userId: String?
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .wrapContentHeight(Alignment.Top)
    )
    {
        if (listViewModel.isNetworkAvailable()) {
            items(
                listFirebase
            ) { list ->
                ListItems(
                    list = list,
                    onDeleteClick = {
                        val listKey = list.id
                        Firebase.database.getReference(Constants.Lists)
                            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                            .child(listKey)
                            .removeValue()
                        listRoomViewModel.removeList(list)
                    },
                    onTextClick = {
                        val id = list.id
                        navController.navigate(Screens.ItemsScreenFire.name + "/$id")
                    },
                    onDotsClick = {
                        listRoomViewModel.expandedStates[list.id] = true
                    }
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                            .weight(1f)
                    ) {
                        DropdownMenu(
                            expanded = listRoomViewModel.expandedStates[list.id] ?: false,
                            onDismissRequest = {
                                listRoomViewModel.expandedStates[list.id] = false
                            },
                        ) {
                            PersonItem(
                                personName = "Rename",
                                dropdownItems = listOf(DropDownItem("Rename")),
                                onItemClick = { /* Handle rename action here */ },
                                id = list.id
                            )
                            PersonItem(
                                personName = "Delete",
                                dropdownItems = listOf(DropDownItem("Delete")),
                                onItemClick = {
                                    val listKey = list.id
                                    Firebase.database.getReference(Constants.Lists)
                                        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                                        .child(listKey)
                                        .removeValue()
                                    listRoomViewModel.removeList(list)
                                },
                                id = list.id
                            )
                        }
                    }
                }
            }
        } else {
            items(
                listsRoom.filter { it.sync == "0" && it.listCreatorId == userId }) { list ->
                ListItems(
                    list = list,
                    onDeleteClick = {
                        listRoomViewModel.removeList(list)
                    },
                    onTextClick = {
                        val id = list.listCreatorId
                        navController.navigate(Screens.ItemsScreenFire.name + "/$id")
                    },
                    onDotsClick = {
                        listRoomViewModel.expandedStates[list.id] = true
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                            .weight(1f)
                    ) {
                        DropdownMenu(
                            expanded = listRoomViewModel.expandedStates[list.id] ?: false,
                            onDismissRequest = {
                                listRoomViewModel.expandedStates[list.id] = false
                            },
                        ) {
                            PersonItem(
                                personName = "Rename",
                                dropdownItems = listOf(DropDownItem("Rename")),
                                onItemClick = { /* Handle rename action here */ },
                                id = list.id
                            )
                            PersonItem(
                                personName = "Delete",
                                dropdownItems = listOf(DropDownItem("Delete")),
                                onItemClick = {
                                    listRoomViewModel.removeList(list)
                                },
                                id = list.id
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonItem(
    personName: String,
    dropdownItems: List<DropDownItem>,
    onItemClick: (DropDownItem) -> Unit,
    id: String
) {
    DropdownMenuItem(
        text = {
            Text(
                personName,
                modifier = Modifier,
                style = TextStyle(color = Color.Black)
            )
        },
        onClick = { onItemClick(DropDownItem(personName)) }
    )
}

data class DropDownItem(
    val text: String
)