package com.example.truckingtracker.appnavigation

import android.widget.Toast
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.truckingtracker.screen.SjTransportersScreen
import com.example.truckingtracker.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun DrawerWithHomeScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
    moveToRouteMapScreen: (vehicleName: String, vehicleType: String, imageRes: Int) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        modifier = Modifier.safeDrawingPadding(),
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = routes.homescreen.route,
                onItemClick = { item ->
                    when (item) {
                        is DrawerItem.Home -> navController.navigate(routes.homescreen.route)
                        is DrawerItem.Profile -> { navController.navigate(routes.profilescreen.route)}
                        is DrawerItem.ChangePassword -> {navController.navigate(routes.changepasswordscreen.route)}
                        is DrawerItem.Logout -> { showLogoutDialog = true }
                    }
                    scope.launch { drawerState.close() }
                },
                onCloseDrawer = { // 🔹 Close arrow callback
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        SjTransportersScreen(
            moveToRouteMapScreen = { vehicleName, vehicleType, imageRes ->
                moveToRouteMapScreen(vehicleName, vehicleType, imageRes)
            },
            onClickDrawer =  {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open()
                    else drawerState.close()
                }
            }
        )
        // 🔒 Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Confirm Logout",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to log out?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            // 🔹 Clear session safely
                            if(viewModel.deleteToken(context = ctx)){
                                navController.navigate(routes.loginscreen.route) {
                                    popUpTo(0)
                                }
                            }else{
                                Toast.makeText(ctx,"Try again",Toast.LENGTH_LONG).show()
                            }
                        }
                    ) {
                        Text(
                            "Logout",
                            color = primaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
            )
        }
    }

}
