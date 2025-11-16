package com.blimas.mycryptolog.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blimas.mycryptolog.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SignUpContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        isLoading = isLoading,
        onSignUpClick = {
            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@SignUpContent
            }
            if (email.isNotBlank() && password.isNotBlank()) {
                isLoading = true
                authViewModel.signUp(email.trim(), password.trim()) { success ->
                    isLoading = false
                    if (success) {
                        Toast.makeText(context, "Sign up successful! Please check your email for verification.", Toast.LENGTH_LONG).show()
                        onLoginClick()
                    } else {
                        Toast.makeText(context, "Sign up failed. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        },
        onLoginClick = onLoginClick
    )
}

@Composable
private fun SignUpContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSignUpClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Sign Up")
            }
        }

        TextButton(onClick = onLoginClick, enabled = !isLoading) {
            Text("Already have an account? Login")
        }
    }
}


@Preview(showBackground = true, name = "SignUp Screen - Default")
@Composable
fun SignUpContentDefaultPreview() {
    MaterialTheme {
        SignUpContent(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            isLoading = false,
            onSignUpClick = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, name = "SignUp Screen - Loading")
@Composable
fun SignUpContentLoadingPreview() {
    MaterialTheme {
        SignUpContent(
            email = "user@example.com",
            onEmailChange = {},
            password = "password",
            onPasswordChange = {},
            confirmPassword = "password",
            onConfirmPasswordChange = {},
            isLoading = true,
            onSignUpClick = {},
            onLoginClick = {}
        )
    }
}