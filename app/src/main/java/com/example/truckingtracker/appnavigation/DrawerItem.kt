package com.example.truckingtracker.appnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerItem(val title: String, val route: String, val icon: ImageVector) {
    object Home : DrawerItem("Home", routes.homescreen.route, Icons.Default.Home)
    object Profile : DrawerItem("Profile", "ProfileScreen", Icons.Default.Person)
    object ChangePassword : DrawerItem("Change Password", "ChangePasswordScreen", Icons.Default.Lock)
    object Logout : DrawerItem("Logout", "Logout", Icons.AutoMirrored.Filled.ExitToApp)
}
