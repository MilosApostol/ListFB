package com.example.listfirebase.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.listfirebase.R
import com.example.listfirebase.data.firebasedata.registerlogin.RegisterViewModel
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.predefinedlook.TextFieldLookEmail
import com.example.listfirebase.predefinedlook.TextFieldLookPassword
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
    val key = "prboa"
    val scope = rememberCoroutineScope()
    val isLoggedInState = registerViewModel.isUserLoggedInState.collectAsState(initial = false)

    LaunchedEffect(key1 = Unit) {
        if (isLoggedInState.value && registerViewModel.isNetworkAvailable()) {
            navController.navigate(Screens.ListScreenFire.name + "/$key")
        } else if (userViewModel.loggingState()) {
            navController.navigate(Screens.ListScreenFire.name + "/$key")
        }
    }
    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences(stringResource(R.string.app_prefs), Context.MODE_PRIVATE)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!registerViewModel.isNetworkAvailable()) {
            Text(
                text = "No Internet, access limited",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .align(Alignment.Start), // Align to the start (left side)
                color = Color.White,
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Start), // Align to the start (left side)
                    color = Color.Black,
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        textAlign = TextAlign.Start
                    )
                )

                Text(
                    text = "Please sign in to continue",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Start), // Align to the start (left side)
                    color = Color.Gray,
                    style = MaterialTheme.typography.h3.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                )
                val cardModifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable { /* Handle click here */ }
                    .padding(16.dp)
                Card(
                    modifier = cardModifier, elevation = 8.dp, shape = RoundedCornerShape(16.dp)
                ) {
                    TextFieldLookEmail(
                        text = email,
                        leadingIcon = Icons.Default.Email,
                        label = "Email",
                        onTextChange = { email = it }
                    )
                }
                Card(
                    modifier = cardModifier, elevation = 8.dp, shape = RoundedCornerShape(16.dp)
                ) {
                    TextFieldLookPassword(
                        text = password,
                        label = "Password",
                        onTextChange = { password = it },
                        leadingIcon = Icons.Default.Lock,
                        trailingIconStart = Icons.Default.VisibilityOff,
                        trailingIconEnd = Icons.Default.Visibility,
                        onClick = { passwordVisible = !passwordVisible },
                        visible = passwordVisible
                    )
                }
                Button(
                    onClick = {
                        if (email.isEmpty() && password.isEmpty()) {
                            Toast.makeText(context, "add value", Toast.LENGTH_LONG).show()
                        } else {
                            scope.launch {
                                //room
                                if (!(registerViewModel.isNetworkAvailable())) {
                                    val user =
                                        userViewModel.getUserByUserNameAndPass(
                                            email,
                                            password
                                        )
                                    userViewModel.updateHolderId(email)
                                    if (user) {
                                        navController.popBackStack(
                                            Screens.ListScreenFire.name +
                                                    "/$key", inclusive = false
                                        )
                                    } else {
                                        Toast.makeText(
                                            context, "Something is wrong",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    //firebase

                                    val firebaseLog =
                                        registerViewModel.logIn(email, password)
                                    if (firebaseLog) {

                                        navController.popBackStack(
                                            Screens.ListScreenFire.name +
                                                    "/$key", inclusive = false
                                        )

                                    } else {
                                        Toast.makeText(
                                            context, "Something is wrong",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }, modifier = Modifier
                        .width(180.dp)
                        .padding(
                            top = 16.dp, start = 16.dp, end = 16.dp
                        )
                        .height(56.dp)
                        .align(Alignment.CenterHorizontally),
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Log In", color = Color.White, fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClickableText(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("Don't have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Blue, fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("SignUp")
                            }
                        }, onClick = {
                            if (registerViewModel.isNetworkAvailable()) {
                                // Navigate to SignUp screen
                                navController.navigate(Screens.RegisterScreenFire.name)
                            } else {
                                Toast.makeText(
                                    context,
                                    "please connect to the internet if you want to register",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                        )

                    }
                }
            }
        }
    }

}


@Preview
@Composable
fun login() {
    LoginFireBase()
}