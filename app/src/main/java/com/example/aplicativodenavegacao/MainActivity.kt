package com.example.aplicativodenavegacao

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { username ->
                navController.navigate("password/$username")
            })
        }
        composable("password/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            PasswordScreen(username = username, onPasswordEntered = {
                navController.navigate("loggedIn/$username")
            })
        }
        composable("loggedIn/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            LoggedInScreen(username = username)
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo do Aplicativo",
                    modifier = Modifier
                        .size(510.dp)
                        .padding(bottom = 16.dp)

                )

                TextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = if (it.length < 3) {
                            "O nome de usuário deve ter pelo menos 3 caracteres"
                        } else {
                            ""
                        }
                    },
                    label = { Text("Nome de Usuário") },
                    isError = errorMessage.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Button(
                    onClick = {
                        if (username.length >= 3) {
                            onLoginSuccess(username)
                        } else {
                            errorMessage = "Digite um nome de usuário válido"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    enabled = username.isNotEmpty() && errorMessage.isEmpty()
                ) {
                    Text("Próximo")
                }
            }
        }
    )
}

@Composable
fun PasswordScreen(username: String, onPasswordEntered: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Olá, $username! Digite sua senha para continuar.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onPasswordEntered() }) {
                        Text("Entrar")
                    }
                    TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Text(if (isPasswordVisible) "Ocultar" else "Mostrar")
                    }
                }
            }
        }
    )
}

@Composable
fun LoggedInScreen(username: String) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bem-vindo, $username! Você está logado.",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        AppNavigation()
    }
}
