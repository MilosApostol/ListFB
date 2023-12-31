package com.example.listfirebase.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.listfirebase.data.firebasedata.registerlogin.RegisterViewModel
import com.example.listfirebase.data.room.loginregister.UserEntity
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.predefinedlook.TextFieldLookEmail
import com.example.listfirebase.predefinedlook.TextFieldLookPassword
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenFire(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val key = "pappa"

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(title = { Text("RegisterScreen") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
            }
        })
        Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
            val cardModifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)

            Column(modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = cardModifier,
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    TextFieldLookEmail(
                        text = email, onTextChange = { email = it },
                        leadingIcon = Icons.Default.Email,
                        label = "Email"
                    )
                }

                Card(
                    modifier = cardModifier,
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                {
                    TextFieldLookPassword(
                        text = password,
                        label = "Password",
                        visible = passwordVisible,
                        leadingIcon = Icons.Default.Lock,
                        trailingIconStart = Icons.Default.VisibilityOff,
                        trailingIconEnd = Icons.Default.Visibility,
                        onTextChange = { password = it },
                        onClick = { passwordVisible = !passwordVisible })
                }

                Button(
                    onClick = {
                        if (email.isEmpty() && password.isEmpty()) {
                            Toast.makeText(context, "please add value", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            scope.launch {
                                val userExist = userViewModel.userExist(email)
                                if (userExist) {
                                    Toast.makeText(context, "pickanother", Toast.LENGTH_LONG)
                                        .show()

                                } else {
                                    registerViewModel.onSignUp(
                                        email,
                                        password,
                                        key, navController

                                    )

                                }
                            }
                        }
                    }, modifier = Modifier
                        .width(180.dp)
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .height(56.dp)
                        .align(Alignment.End),
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Register",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    })
            }
        }
    }
}


