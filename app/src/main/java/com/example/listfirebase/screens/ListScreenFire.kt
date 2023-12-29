package com.example.listfirebase.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.registerlogin.RegisterViewModel
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.example.listfirebase.nav.Screen
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.nav.screensInDrawer
import com.example.listfirebase.predefinedlook.AppBarView
import com.example.listfirebase.predefinedlook.BottomDrawerContent
import com.example.listfirebase.predefinedlook.DrawerItem
import com.example.listfirebase.predefinedlook.Lists
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
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

    val roomListFlow = listRoomViewModel.getAllLists
    val roomList = roomListFlow.collectAsState(initial = emptyList()).value

    val sharedPreferences =
        context.getSharedPreferences(stringResource(R.string.app_prefs), Context.MODE_PRIVATE)

    val scope = rememberCoroutineScope()
    val id = 0
    val drawerScaffoldState = rememberScaffoldState()
    val bottomScaffoldState = rememberScaffoldState()
    val currentRoute = navController.currentDestination?.route
    var userId: String? = ""

    if (registerViewModel.isNetworkAvailable()) {
        //changing ID of the room databaseID
        LaunchedEffect(key1 = Unit, key2 = FirebaseAuth.getInstance().currentUser) {

            if (Firebase.auth.currentUser != null) {
                val user = userViewModel.getUserDetails()
                if (user != null) {
                    registerViewModel.logInAfterOffline(user.userEmail, user.userPassword)
                   val listsToUpload = compareListsAndIdentifyItemsForUpload(list, roomList)
                  listViewModel.uploadData(listsToUpload)
                }
            } else {
                userViewModel.updateRoomUserIdAfterLogin(Firebase.auth.currentUser?.email.toString())
            }
        }
    } else {
        //if network is off, taking the ID for saved user
        LaunchedEffect(key1 = Unit) {
            userViewModel.getUserId()
        }
    }
    userId = userViewModel.userIdState
    Scaffold(
        scaffoldState = drawerScaffoldState,
        topBar = {
            AppBarView(
                title = "ListScreen",
                onMenuNavClicked = {
                    scope.launch {
                        drawerScaffoldState.drawerState.apply {
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
                            drawerScaffoldState.drawerState.close()
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
        Column(modifier = Modifier.fillMaxSize()) {
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
                Lists(
                    list,
                    listViewModel,
                    navController,
                    paddingValues,
                    userViewModel,
                    listRoomViewModel,
                    userId,
                    //  bottomScaffoldState.drawerState,
                    //   scope
                )
            } else {
                Lists(
                    roomList,
                    listViewModel,
                    navController,
                    paddingValues,
                    userViewModel,
                    listRoomViewModel,
                    userId,
                    //    bottomScaffoldState.drawerState,
                    //    scope
                )
            }
        }
    }
}

fun compareListsAndIdentifyItemsForUpload(
    remoteList: List<ListEntity>,
    localList: List<ListEntity>
): List<ListEntity> {
    // Assuming a unique ID property for each item
    val remoteItemIds = remoteList.map { it.sync }.toSet()//goes through id on firebase
    return localList.filter { it.sync !in remoteItemIds } // comparing id vs firebaseID  and returns a list of the items which arent in that list
}

