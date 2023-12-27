package com.example.listfirebase.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.listfirebase.Constants
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.nav.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListFire(
    id: String,
    navController: NavController,
    listViewModel: ListViewModel = hiltViewModel(),
    listRoomViewModel: ListRoomViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var listName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val db = Firebase.database
    val usersRef = db.getReference("users")

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "AddListScreen") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            }
        )
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                ListNameTextField(
                    label = "ListName",
                    value = listName,
                    onValueChanged = { listName = it }
                )

                Button(
                    onClick = {
                        if (id.isEmpty()) {
//TODO update
                        } else {
                            if (listName.isNotEmpty()) {
                                if (listViewModel.isNetworkAvailable()) {
                                    val reference =
                                        FirebaseDatabase.getInstance().getReference(Constants.Lists)
                                            .child(
                                                FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                            )
                                    val key = reference.key!!
                                    val list = ListEntity(
                                        listName = listName,
                                        id = "temporary_id",
                                        listCreatorId = Firebase.auth.currentUser!!.uid
                                    )
                                    navController.navigate(Screens.ListScreenFire.name + "/$id")
                                    val newRef = reference.push()
                                        .setValue(list) { error, ref ->
                                            val key = ref.key
                                            Toast.makeText(context, "$key", Toast.LENGTH_LONG)
                                                .show()
                                            list.id = key!!

                                            listViewModel.saveData(ref, list, key)
                                        listRoomViewModel.insertList(ListEntity(listName))//you have problem with ID
                                        }
                                    navController.navigate(Screens.ListScreenFire.name + "/$id")
                                } else {
                                    Log.e("AddList", "Failed to get key after saving to Firebase")
                                }
                            } else {
                       listRoomViewModel.insertList(ListEntity(listName))//you have problem with ID
                            }
                        }

                    }){
                    Text(text = "Click")
                }
            }
        }
    }
}

@Composable
fun ListNameTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChanged,
        label = {
            Text(
                text = label, color = Color.Black
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}