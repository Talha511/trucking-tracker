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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.truckingtracker.reterofit.RegisterRequest
import com.example.truckingtracker.viewmodels.AuthViewModel
import org.json.JSONObject
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onSuccess:()-> Unit,
    onSignUpClick: (String, String, String) -> Unit = { _, _, _ -> },
    onLoginClick: () -> Unit = {},
    onTermsClick: () -> Unit = { /* Handle navigation to Terms screen */ }
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cnic by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var cnicError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val emailPattern = remember { Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    ) }
    val cnicPattern = remember {
        Pattern.compile("^\\d{5}-\\d{7}-\\d{1}$")
    }

    val regResponse by viewModel.regResponse.collectAsState()


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
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    Text(
                        text = "Sign up to start your journey with us.",
                        fontSize = 16.sp,
                        color = secondaryTextColor,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                item {
                    Text(
                        text = "Full Name*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = null
                        },
                        placeholder = { Text("Enter your full name") },
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
                            keyboardType = KeyboardType.Text
                        ),
                        isError = nameError != null,
                        supportingText = {
                            if (nameError != null) {
                                Text(
                                    text = nameError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

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

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Text(
                        text = "CNIC Number*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = cnic,
                        onValueChange = {
                            cnic = it
                            cnicError = null
                        },
                        placeholder = { Text("Enter your CNIC Number") },
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
                        isError = cnicError != null,
                        supportingText = {
                            if (cnicError != null) {
                                Text(
                                    text = cnicError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

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
                        placeholder = { Text("Create password") },
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
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description, tint = secondaryTextColor)
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

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Text(
                        text = "Confirm Password*",
                        fontSize = 14.sp,
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = null
                        },
                        placeholder = { Text("Confirm your password") },
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
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = description, tint = secondaryTextColor)
                            }
                        },
                        isError = confirmPasswordError != null,
                        supportingText = {
                            if (confirmPasswordError != null) {
                                Text(
                                    text = confirmPasswordError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    agreedToTerms = !agreedToTerms
                                    termsError = null
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = agreedToTerms,
                                onCheckedChange = {
                                    agreedToTerms = it
                                    termsError = null
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = primaryColor,
                                    uncheckedColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("I agree to the ")
                                    withStyle(
                                        style = SpanStyle(
                                            color = primaryColor,
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    ) {
                                        append("Terms & Conditions")
                                    }
                                },
                                fontSize = 15.sp,
                                color = onSurfaceColor,
                                modifier = Modifier.clickable(onClick = onTermsClick)
                            )
                        }

                        if (termsError != null) {
                            Text(
                                text = termsError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
                item {
                    Column {
                        Button(
                            onClick = {
                                nameError = null
                                emailError = null
                                cnicError = null
                                passwordError = null
                                confirmPasswordError = null
                                termsError = null

                                when {
                                    name.isBlank() -> {
                                        nameError = "Name field cannot be empty."
                                    }
                                    email.isBlank() -> {
                                        emailError = "Email field cannot be empty."
                                    }
                                    !emailPattern.matcher(email).matches() -> {
                                        emailError = "Please enter a valid email format."
                                    }
                                    cnic.isBlank() || cnic.length < 13 -> {
                                        cnicError = "Cnic field cannot be empty."
                                    }
                                    !cnicPattern.matcher(cnic).matches() -> {
                                        cnicError = "Please enter a valid Cnic format in dashes format."
                                    }
                                    password.isBlank() || password.length < 6 -> {
                                        passwordError = "Password must be at least 6 characters long."
                                    }
                                    confirmPassword.isBlank() -> {
                                        confirmPasswordError = "Confirm password field cannot be empty."
                                    }
                                    password != confirmPassword -> {
                                        confirmPasswordError = "Passwords do not match."
                                    }
                                    !agreedToTerms -> {
                                        termsError = "You must agree to the Terms & Conditions."
                                        Toast.makeText(context, "Please agree to the terms.", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        viewModel.register(
                                            RegisterRequest(name = name, email = email, cnic = cnic, password = password)
                                        )
                                    }
                                }
                            },
                            enabled = agreedToTerms,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                disabledContainerColor = primaryColor.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "Sign Up",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
                regResponse?.let { response ->
                    if (response.isSuccessful) {
                        val body = response.body()
                        Toast.makeText(
                            context,
                            "Welcome ${body?.user?.name ?: "User"}! Your account has been created successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                        onSuccess()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val message = try {
                            JSONObject(errorBody ?: "").optString("message", "Registration failed")
                        } catch (e: Exception) {
                            "Registration failed"
                        }
                        Log.d("SignUpScreen", "Error: $message")
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 15.sp,
                            color = secondaryTextColor
                        )
                        Text(
                            text = "Login",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            modifier = Modifier.clickable(onClick = onLoginClick)
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    com.example.truckingtracker.ui.theme.TruckingTrackerTheme {
        SignUpScreen(
            onBackClick = {},
            onSignUpClick = { _, _, _ -> },
            onLoginClick = {},
            onSuccess = {}
        )
    }
}
