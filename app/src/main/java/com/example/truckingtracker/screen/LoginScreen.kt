package com.example.truckingtracker.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.truckingtracker.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit ,
    onSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val loginResponse by viewModel.loginResponseX.collectAsState()

    val emailPattern = remember {
        Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    }

    val primaryColor = Color(0xFFEB5757)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val inputBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = onSurfaceColor
                        )
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
                        text = "Welcome Here!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    Text(
                        text = "Login to your account to continue your journey.",
                        fontSize = 16.sp,
                        color = secondaryTextColor,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                // ---- Email Field ----
                item {
                    Text(
                        text = "Email Address*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        placeholder = { Text("Enter your email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = inputBackgroundColor,
                            unfocusedContainerColor = inputBackgroundColor
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        isError = emailError != null,
                        supportingText = {
                            if (emailError != null) {
                                Text(
                                    text = emailError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // ---- Password Field ----
                item {
                    Text(
                        text = "Password*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        placeholder = { Text("Enter your password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = inputBackgroundColor,
                            unfocusedContainerColor = inputBackgroundColor
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = null,
                                    tint = secondaryTextColor
                                )
                            }
                        },
                        isError = passwordError != null,
                        supportingText = {
                            if (passwordError != null) {
                                Text(
                                    text = passwordError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // ---- Login Button ----
                item {
                    Button(
                        onClick = {
                            emailError = null
                            passwordError = null

                            when {
                                email.isBlank() -> emailError = "Email cannot be empty"
                                !emailPattern.matcher(email).matches() -> emailError = "Invalid email"
                                password.isBlank() -> passwordError = "Password cannot be empty"
                                else -> {
                                    coroutineScope.launch {
                                        viewModel.login(context = context, email = email, password = password)
                                    }
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
                            text = "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp)) }

                // ---- Signup ----
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            fontSize = 15.sp,
                            color = secondaryTextColor
                        )
                        Text(
                            text = "Sign Up",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            modifier = Modifier.clickable(onClick = onSignUpClick)
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // ---- Forgot Password ----
                item {
                    Text(
                        text = "Forgot Password?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = secondaryTextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .clickable(onClick = onForgotPasswordClick)
                    )
                }
            }
        }
    )

    // ---- Observe API Response ----
    loginResponse?.let { response ->
        if (response.isSuccessful) {
            val body = response.body()
            Toast.makeText(context, "Welcome ${body?.user?.name ?: "User"}", Toast.LENGTH_LONG).show()
            Log.d("xyz","${body?.user?.name}")
            onSuccess()
        } else {
            Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
            Log.d("xyz","${response.message()}")
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    com.example.truckingtracker.ui.theme.TruckingTrackerTheme {
        LoginScreen(
            onBackClick = {}, onSignUpClick = {}, onSuccess = {}, onForgotPasswordClick = {})
    }
}
