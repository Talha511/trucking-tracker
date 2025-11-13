package com.example.truckingtracker.appnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.truckingtracker.screen.BookingDetailsScreen
import com.example.truckingtracker.screen.BookingRecordScreen
import com.example.truckingtracker.screen.ChangePasswordScreen
import com.example.truckingtracker.screen.EditProfileScreen
import com.example.truckingtracker.screen.ForgotPasswordScreen
import com.example.truckingtracker.screen.LoginScreen
import com.example.truckingtracker.screen.OrderAlertSuccessScreen
import com.example.truckingtracker.screen.ProfileScreen
import com.example.truckingtracker.screen.RouteMapScreenGoogle
import com.example.truckingtracker.screen.SignUpScreen
import com.example.truckingtracker.screen.SplashScreen

sealed class routes(val route: String) {
    object splashscreen: routes("SplashScreen")
    object loginscreen: routes("LoginScreen")
    object registrationscreen: routes("RegistrationScreen")
    object forgotpasswardscreen: routes("ForgotPasswordScreen")
    object homescreen: routes("HomeScreen")
    object changepasswordscreen : routes("ChangePasswordScreen")
    object profilescreen : routes("ProfileScreen")
    object editprofilescreen : routes("EditProfileScreen")

    object bookingRecordScreen : routes("BookingRecordScreen")
    object routeMapScreenGoogle : routes("RouteMapScreenGoogle/{vehicleName}/{vehicleType}/{imageRes}") {
        fun createRoute(vehicleName: String, vehicleType: String, imageRes: Int) =
            "RouteMapScreenGoogle/$vehicleName/$vehicleType/$imageRes"
    }

    object bookingDetailsScreen :
        routes("BookingDetailsScreen/{pickup}/{dropoff}/{date}/{time}/{vehicleName}/{vehicleType}/{imageRes}") { // ✨ ADDED ARGS
        fun createRoute(
            pickup: String,
            dropoff: String,
            date: String,
            time: String,
            vehicleName: String, // ✨ ADDED
            vehicleType: String, // ✨ ADDED
            imageRes: Int, // ✨ ADDED
        ) = "BookingDetailsScreen/$pickup/$dropoff/$date/$time/$vehicleName/$vehicleType/$imageRes"
    }
    object orderAlertSuccessScreen : routes("OrderAlertSuccessScreen")
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val currentBackStack = navController.currentBackStackEntryFlow
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    NavHost(navController = navController, startDestination = routes.splashscreen.route) {

        // 🔹 Splash
        composable(route = routes.splashscreen.route) {
            SplashScreen {
                navController.navigate(routes.loginscreen.route)
            }
        }

        // 🔹 Login
        composable(route = routes.loginscreen.route) {
            LoginScreen(
                onBackClick = {},
                onSuccess = { navController.navigate(routes.homescreen.route) },
                onSignUpClick = { navController.navigate(routes.registrationscreen.route) },
                onForgotPasswordClick = { navController.navigate(routes.forgotpasswardscreen.route) }
            )
        }

        // 🔹 Sign Up
        composable(route = routes.registrationscreen.route) {
            SignUpScreen(
                onLoginClick = { navController.navigate(routes.loginscreen.route) },
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack()
                    navController.navigate(routes.loginscreen.route)
                }
            )
        }

        // 🔹 Forgot Password
        composable(route = routes.forgotpasswardscreen.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onResetRequestClick = { navController.navigate(routes.loginscreen.route) }
            )
        }

        // 🔹 Home (with Drawer)
        composable(route = routes.homescreen.route) {
            DrawerWithHomeScreen(
                navController = navController,
                moveToRouteMapScreen = { vehicleName, vehicleType, imageRes ->
                    navController.navigate(
                        routes.routeMapScreenGoogle.createRoute(
                            vehicleName = vehicleName,
                            vehicleType = vehicleType,
                            imageRes = imageRes
                        )
                    )
                }
            )
            }

        // 🔹 Change Password
        composable(route = routes.changepasswordscreen.route) {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onChangePasswordClick = { oldPassword, newPassword, confirmPassword ->
                    navController.popBackStack()
                }
            )
        }

        // 🔹 Profile Screen
        composable(route = routes.profilescreen.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = { navController.navigate(routes.editprofilescreen.route) }
            )
        }

        // 🔹 Edit Profile Screen
        composable(route = routes.editprofilescreen.route) {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { name, email, cnic ->
                    navController.popBackStack()
                }
            )
        }
// 🔹 routeMapScreenGoogle
        composable(
            route = routes.routeMapScreenGoogle.route,
            arguments = listOf(
                navArgument("vehicleName") { type = NavType.StringType },
                navArgument("vehicleType") { type = NavType.StringType },
                navArgument("imageRes") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val vehicleName = backStackEntry.arguments?.getString("vehicleName") ?: ""
            val vehicleType = backStackEntry.arguments?.getString("vehicleType") ?: ""
            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: 0

            RouteMapScreenGoogle(
                onBackClick = { navController.popBackStack() },
                vehicleName = vehicleName,
                vehicleType = vehicleType,
                imageRes = imageRes,
                onVehicleSelected = { pickup, dropoff, dateTime ->
                    // split date & time for cleaner display
                    val (date, time) = dateTime.split(" ")
                    navController.navigate(
                        routes.bookingDetailsScreen.createRoute(
                            pickup = pickup,
                            dropoff = dropoff,
                            date = date,
                            time = time,
                            vehicleName = vehicleName,
                            vehicleType = vehicleType,
                            imageRes = imageRes
                        )
                    )
                }
            )
        }

        // 🔹 bookingDetailsScreen
        composable(
            route = routes.bookingDetailsScreen.route,
            arguments = listOf(
                navArgument("pickup") { type = NavType.StringType },
                navArgument("dropoff") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("vehicleName") { type = NavType.StringType },
                navArgument("vehicleType") { type = NavType.StringType },
                navArgument("imageRes") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val pickup = backStackEntry.arguments?.getString("pickup") ?: ""
            val dropoff = backStackEntry.arguments?.getString("dropoff") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val vehicleName = backStackEntry.arguments?.getString("vehicleName") ?: ""
            val vehicleType = backStackEntry.arguments?.getString("vehicleType") ?: ""
            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: 0

            BookingDetailsScreen(
                pickupAddress = pickup,
                dropOffAddress = dropoff,
                date = date,
                time = time,
                vehicleName = vehicleName,
                vehicleType = vehicleType,
                vehicleImageRes = imageRes,
                onBack = { navController.popBackStack() },
                onSubmit = { navController.navigate(routes.orderAlertSuccessScreen.route) }
            )
        }

        // 🔹 orderAlertSuccessScreen
        composable(route = routes.orderAlertSuccessScreen.route) {
            OrderAlertSuccessScreen (
                onDoneClick = { navController.navigate(routes.bookingRecordScreen.route)},
            )
        }
        // 🔹 bookingRecordScreen
        composable(route = routes.bookingRecordScreen.route) {
            BookingRecordScreen (
                onBackClick = { navController.popBackStack()},
            )
        }


    }
}
