package com.example.listfirebase.data.firebasedata.registerlogin

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository
) {
    private val _isUserLoggedInState = MutableStateFlow(false)
    val isUserLoggedInState = _isUserLoggedInState.asStateFlow()
    val inProgress = mutableStateOf(false)
    private val signedIn = mutableStateOf(false)
    suspend fun signUp(email: String, password: String, key: String, navController: NavController) {
        inProgress.value = true

        auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isUserLoggedInState.value = true
                    signedIn.value = true
                    navController.navigate(
                        Screens.ListScreenFire.name +
                                "/$key"
                    ) {
                        popUpTo(Screens.ListScreenFire.name) {
                            inclusive = true
                        }
                    }
                } else {
                    signedIn.value = false
                }
                inProgress.value = false

            }
    }

    suspend fun logIn(email: String, password: String): Boolean {
        val resultDeferred = CompletableDeferred<Boolean>()

        inProgress.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isUserLoggedInState.value = true
                    resultDeferred.complete(true)
                } else {
                    resultDeferred.complete(false)
                }

                inProgress.value = false
            }

        return resultDeferred.await()
    }
}
