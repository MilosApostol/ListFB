package com.example.listfirebase.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.R
import com.example.listfirebase.data.firebasedata.registerlogin.RegisterViewModel
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFireBase(
    navController: NavHostController = rememberNavController(),
    registerViewModel: RegisterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences(stringResource(R.string.app_prefs), Context.MODE_PRIVATE)
    val key = "prboa"


    if (registerViewModel.isNetworkAvailable()) {
        // if you don't sign out user, it will always be something inside of current user, it will always go into listscreen
        if (registerViewModel.isUserLoggedIn()) {
            Toast.makeText(LocalContext.current, "true", Toast.LENGTH_LONG).show()
            navController.navigate(Screens.ListScreenFire.name + "/$key")
        }
    } else {
        Firebase.auth.signOut()
        Toast.makeText(LocalContext.current, "not true", Toast.LENGTH_LONG).show()

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var check by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()


        Column(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
                title = { Text("LogInScreen") },
            )
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) { padding ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp),
                            label = { Text("Email") },
                        )

                        TextField(modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                                        else Icons.Default.Visibility,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            })
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Checkbox(checked = check,
                                onCheckedChange = {
                                    check = it
                                })
                            Text(text = "Stay Logged")

                        }
                        Button(
                            onClick = {
                                //room
                                scope.launch {
                                    if (!(registerViewModel.isNetworkAvailable())) {
                                        userViewModel.getUserByUserNameAndPass(email, password)
                                    } else {
                                        //firebase
                                        registerViewModel.logIn(email, password)
                                    }
                                }
                                navController.navigate(Screens.ListScreenFire.name + "/$key")
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text("Log In")
                        }

                        Button(
                            onClick = { navController.navigate(Screens.RegisterScreenFire.name) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(text = "Register")
                        }
                    }
                }
            }
        }
    }
}