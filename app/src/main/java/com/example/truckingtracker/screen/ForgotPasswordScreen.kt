package com.example.truckingtracker.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onResetRequestClick: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val emailPattern = remember { Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    ) }

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
                        text = "Forgot Password?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    Text(
                        text = "Enter your email address below to receive a password reset link.",
                        fontSize = 16.sp,
                        color = secondaryTextColor,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

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
                        placeholder = { Text("Enter your register email") },
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

                item { Spacer(modifier = Modifier.height(32.dp)) }

                item {
                    Button(
                        onClick = {
                            emailError = null

                            when {
                                email.isBlank() -> {
                                    emailError = "Email field cannot be empty."
                                    Toast.makeText(context, "Please enter your email.", Toast.LENGTH_SHORT).show()
                                }
                                !emailPattern.matcher(email).matches() -> {
                                    emailError = "Please enter a valid email format."
                                    Toast.makeText(context, "Invalid email format.", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    onResetRequestClick(email)
                                    Toast.makeText(context, "Password reset link sent to $email", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Send Reset Link",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Text(
                        text = "Back to Login",
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
fun PreviewForgotPasswordScreen() {
    com.example.truckingtracker.ui.theme.TruckingTrackerTheme {
        ForgotPasswordScreen(
            onBackClick = {},
            onResetRequestClick = {}
        )
    }
}