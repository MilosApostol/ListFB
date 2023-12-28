package com.example.listfirebase.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.R
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.registerlogin.RegisterViewModel
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.example.listfirebase.nav.Screen
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.nav.screensInDrawer
import com.example.listfirebase.predefinedlook.AppBarView
import com.example.listfirebase.predefinedlook.DrawerItem
import com.example.listfirebase.predefinedlook.ListItems
import com.example.listfirebase.predefinedlook.Lists
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ListScreenFire(
    key: String,
    navController: NavController = rememberNavController(),
    listViewModel: ListViewModel = hiltViewModel(),
    listRoomViewModel: ListRoomViewModel = hiltViewModel(),
    registerViewModel: RegisterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val listFlow = listViewModel.getAllLists
    val list = listFlow.collectAsState(initial = emptyList()).value
    Toast.makeText(LocalContext.current, "true ${Firebase.auth.currentUser?.uid}", Toast.LENGTH_LONG).show()

    val roomListFlow = listRoomViewModel.getAllLists
    val roomList = roomListFlow.collectAsState(initial = emptyList()).value

    val scope = rememberCoroutineScope()
    val id = 0
    val scaffoldState = rememberScaffoldState()
    val currentRoute = navController.currentDestination?.route
    var userId: String = ""
    if (registerViewModel.isNetworkAvailable() && Firebase.auth.currentUser?.uid != null) {
        //changing ID of the room databaseID
        LaunchedEffect(key1 = Unit, key2 = FirebaseAuth.getInstance().currentUser) {
            scope.launch {
                userViewModel.updateRoomUserIdAfterLogin(Firebase.auth.currentUser?.email.toString())
            }
        }
    } else {
        //if network is off, taking the ID for saved user
        LaunchedEffect(key1 = Unit) {
            scope.launch {
                userId = userViewModel.getUserId().toString()
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(
                title = "ListScreen",
                onMenuNavClicked = {
                    scope.launch {
                        scaffoldState.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                onDeleteNavClicked = {
                    listViewModel.deleteAll()
                },
                onLogoutClicked = {
                    scope.launch {
                        userViewModel.logout()
                        listViewModel.signOut()
                        navController.navigate(Screens.LoginFireBase.name)
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddListFire.name + "/$id")
                },
            ) {
                Icon(Icons.Filled.Add, "Add List")
            }//drawerItems
        }, drawerContent = {
            LazyColumn(Modifier.padding(16.dp)) {
                items(screensInDrawer) { item ->
                    DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        if (item.dRoute == Screen.DrawerScreen.Add.route) {
                            navController.navigate(Screens.AddListFire.name + "/$id")
                        } else {
                            navController.navigate(item.dRoute)
                        }
                    }
                }

            }
        }
    ) { paddingValues ->
        if (!registerViewModel.isNetworkAvailable()) {
            Text(
                text = "No Internet, access limited",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red),
                color = Color.White,
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        if (registerViewModel.isNetworkAvailable()) {
            Lists(list, listViewModel, navController, paddingValues)
        } else {
            // this works, just find the right filter, it has to be in viewmodel, setListCreatorID to the userId
            LazyColumn {
                items(roomList.filter {
                    it.listCreatorId == userId
                }) { it ->
                    ListItems(it)
                }
            }
        }
    }
}

