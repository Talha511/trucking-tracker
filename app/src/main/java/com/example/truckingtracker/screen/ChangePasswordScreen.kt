package com.example.truckingtracker.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.truckingtracker.reterofit.ChangePasswordRequest
import com.example.truckingtracker.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onChangePasswordClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordconfirmation by remember { mutableStateOf("") }

    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordconfirmationVisible by remember { mutableStateOf(false) }

    var oldPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordconfirmationError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val changePasswordResponse by viewModel.changepasswordResponse.collectAsState()

    val primaryColor = Color(0xFFEB5757)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val inputBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh

    LaunchedEffect(changePasswordResponse) {
        changePasswordResponse?.let { response ->
            Log.d("hhh",response.toString())
            Log.d("ChangePasswordAPI", "Response code: ${response.code()}")
            Log.d("ChangePasswordAPI", "Response body: ${response.body()}")
            Log.d("ChangePasswordAPI", "Error body: ${response.errorBody()?.string()}")

            when {
                response.isSuccessful -> {
                    Toast.makeText(
                        context,
                        response.body()?.msg ?: "Password changed successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                    onBackClick()
                }
                else -> {
                    Toast.makeText(
                        context,
                        "Error ${response.code()}: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = onSurfaceColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Text(
                        text = "Change Password",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    Text(
                        text = "Enter your old and new password below to update your credentials.",
                        fontSize = 16.sp,
                        color = secondaryTextColor,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                // OLD PASSWORD
                item {
                    Text(
                        text = "Old Password*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = {
                            oldPassword = it
                            oldPasswordError = null
                        },
                        placeholder = { Text("Enter your old password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = inputBackgroundColor,
                            unfocusedContainerColor = inputBackgroundColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true,
                        visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (oldPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                                Icon(image, contentDescription = null, tint = secondaryTextColor)
                            }
                        },
                        isError = oldPasswordError != null,
                        supportingText = {
                            oldPasswordError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // NEW PASSWORD
                item {
                    Text(
                        text = "New Password*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            newPasswordError = null
                        },
                        placeholder = { Text("Enter new password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = inputBackgroundColor,
                            unfocusedContainerColor = inputBackgroundColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true,
                        visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (newPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                Icon(image, contentDescription = null, tint = secondaryTextColor)
                            }
                        },
                        isError = newPasswordError != null,
                        supportingText = {
                            newPasswordError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // CONFIRM NEW PASSWORD
                item {
                    Text(
                        text = "Confirm New Password*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = newPasswordconfirmation,
                        onValueChange = {
                            newPasswordconfirmation = it
                            newPasswordconfirmationError = null
                        },
                        placeholder = { Text("Re-enter new password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = inputBackgroundColor,
                            unfocusedContainerColor = inputBackgroundColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true,
                        visualTransformation = if (newPasswordconfirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (newPasswordconfirmationVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { newPasswordconfirmationVisible = !newPasswordconfirmationVisible }) {
                                Icon(image, contentDescription = null, tint = secondaryTextColor)
                            }
                        },
                        isError = newPasswordconfirmationError != null,
                        supportingText = {
                            newPasswordconfirmationError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // SUBMIT BUTTON
                item {
                    Button(
                        onClick = {
                            oldPasswordError = null
                            newPasswordError = null
                            newPasswordconfirmationError = null

                            when {
                                oldPassword.isBlank() -> oldPasswordError = "Old password cannot be empty."
                                newPassword.isBlank() || newPassword.length < 6 ->
                                    newPasswordError = "New password must be at least 6 characters long."
                                newPasswordconfirmation.isBlank() ->
                                    newPasswordconfirmationError = "Confirm password cannot be empty."
                                newPassword != newPasswordconfirmation ->
                                    newPasswordconfirmationError = "Passwords do not match."
                                else -> {
                                    viewModel.changePassword(
                                        context = context,
                                        data = ChangePasswordRequest(
                                            old_password = oldPassword,
                                            new_password = newPassword,
                                            new_password_confirmation = newPasswordconfirmation
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Change Password",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Text(
                        text = "Back to Profile",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = secondaryTextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .clickable(onClick = onBackClick)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChangePasswordScreen() {
    com.example.truckingtracker.ui.theme.TruckingTrackerTheme {
        ChangePasswordScreen()
    }
}
