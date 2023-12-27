package com.example.listfirebase.data.firebasedata.registerlogin

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.listfirebase.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class RegisterRepository @Inject constructor(val auth: FirebaseAuth) {

    private val signedIn = mutableStateOf(false)
    private val inProgress = mutableStateOf(false)

    suspend fun signUp(email: String, password: String, navController: NavController, key: String) {
        inProgress.value = true

        auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signedIn.value = true
                    navController.navigate(Screens.ListScreenFire.name + "/$key")
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
                    // Login successful
                    resultDeferred.complete(true)
                } else {
                    // Login failed
                    resultDeferred.complete(false)
                }

                inProgress.value = false
            }

        return resultDeferred.await()
    }
}
