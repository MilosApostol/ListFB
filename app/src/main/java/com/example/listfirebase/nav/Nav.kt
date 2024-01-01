package com.example.listfirebase.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listfirebase.screens.AddItems
import com.example.listfirebase.screens.AddListFire
import com.example.listfirebase.screens.ItemsScreen
import com.example.listfirebase.screens.ListScreenFire
import com.example.listfirebase.screens.LoginFireBase
import com.example.listfirebase.screens.RegisterScreenFire

object Graph {
    const val AUTH = "auth_graph"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginFireBase.name,
    ) {
        composable(Screens.LoginFireBase.name) {
            LoginFireBase(navController = navController)
        }
        composable(Screens.RegisterScreenFire.name) {
            RegisterScreenFire(navController)
        }
        composable(
            Screens.ListScreenFire.name + "/{key}",
            arguments = listOf(
                navArgument("key") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                })
        ) {
            val key  = it.arguments?.getString("key") ?: ""
            ListScreenFire(key, navController)
        }
            composable(
                Screens.AddListFire.name + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                //  val id = if (it.arguments != null) it.arguments!!.getString("id") else ""
                AddListFire(id = id, navController = navController)
            }
            composable(
                Screens.ItemsScreenFire.name + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                ItemsScreen(id = id, navController = navController)
            }

            composable(
                Screens.AddItems.name + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                AddItems(id = id, navController = navController)
            }
        }
    }

