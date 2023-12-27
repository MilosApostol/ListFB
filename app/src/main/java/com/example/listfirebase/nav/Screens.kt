package com.example.listfirebase.nav


enum class Screens{
    AddListFire,
    ListScreenFire,
    LoginFireBase,
    RegisterScreenFire,
    ItemsScreenFire,
    AddItemsFire
}
sealed class Screen(
    val title: String,
    val route: String
) {

    sealed class DrawerScreen(val dTitle: String, val dRoute: String) :
        Screen(title = dTitle, route = dRoute) {
        data object List : DrawerScreen("ListScreen", "list_route")
        data object Add : DrawerScreen("AddScreen", "add_route")
    }
}

val screensInDrawer = listOf(
    Screen.DrawerScreen.List,
    Screen.DrawerScreen.Add,
)



