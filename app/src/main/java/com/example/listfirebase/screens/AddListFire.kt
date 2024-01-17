package com.example.listfirebase.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.launch
import java.util.UUID

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
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                    .background(Color.White)
            ) {
                ListNameTextField(
                    label = "ListName",
                    value = listName,
                    onValueChanged = { listName = it },
                )

                Button(
                    onClick = {
                        if (id.isEmpty()) {
//TODO update Screen
                        } else {
                            if (listName.isNotEmpty()) {
                                if (listViewModel.isNetworkAvailable()) {

                                    val reference =
                                        FirebaseDatabase.getInstance().getReference(Constants.Lists)
                                    val key = reference.key!!
                                    val list = ListEntity(
                                        listName = listName,
                                        id = UUID.randomUUID().toString(),
                                        listCreatorId = Firebase.auth.currentUser?.uid!!,
                                        sync = "1"
                                    )
                                    val newRef = reference.push()
                                        .setValue(list) { error, ref ->
                                            val key = ref.key
                                            list.id = key!!
                                            scope.launch {
                                                listViewModel.saveData(
                                                    ref,
                                                    list,
                                                    key
                                                ) { isSucces ->
                                                    navController.navigate(
                                                        Screens.ListScreenFire.name
                                                                + "/$id"
                                                    )
                                                }
                                            }
                                        }
                                } else {
                                    val list = ListEntity(
                                        listName = listName,
                                        id = UUID.randomUUID().toString(),
                                        sync = "0"
                                    )
                                    scope.launch {
                                        listRoomViewModel.insertListOffline(list)
                                        navController.navigate(
                                            Screens.ListScreenFire.name
                                                    + "/$id")
                                    }

                                }
                            } else {
                                Toast.makeText(context, "add value", Toast.LENGTH_LONG).show()
                            }

                        }
                    }, modifier = Modifier
                        .width(200.dp)
                        .padding(20.dp)
                        .height(56.dp)
                        .align(Alignment.End),
                    content = {
                        Text(
                            "Add List",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    })
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
                text = label,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}