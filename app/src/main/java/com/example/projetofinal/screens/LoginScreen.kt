package com.example.projetofinal.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.projetofinal.data.UsuarioDatabase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    database: UsuarioDatabase,
    onLoginSuccess: (Int) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    emailError.ifEmpty { "E-mail" },
                    color = if (emailError.isNotEmpty()) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(320.dp)
                .padding(vertical = 6.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    passwordError.ifEmpty { "Senha" },
                    color = if (passwordError.isNotEmpty()) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(320.dp)
                .padding(vertical = 6.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        if (loginError.isNotEmpty()) {
            Text(
                text = loginError,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                emailError = if (email.isBlank()) "E-mail é obrigatório" else ""
                passwordError = if (password.isBlank()) "Senha é obrigatória" else ""

                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    coroutineScope.launch {
                        val userDao = database.userDao()
                        val user = userDao.getUser(email, password)
                        if (user != null) {
                            onLoginSuccess(user.id)
                            navController.navigate("Início") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            loginError = "E-mail ou senha incorretos!"
                        }
                    }
                }
            },
            modifier = Modifier
                .width(300.dp)
                .padding(top = 20.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Entrar",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 6.dp),
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row {
            Text(
                text = "Cadastre-se",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}
