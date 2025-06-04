package com.example.projetofinal.screens

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.projetofinal.data.model.Usuario
import com.example.projetofinal.data.UsuarioDatabase
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, paddingValues: PaddingValues, database: UsuarioDatabase, onLoginSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var confirmPassword by remember {mutableStateOf("")}
    var passwordVisible by remember {mutableStateOf(false)}
    var confirmPasswordVisible by remember {mutableStateOf(false)}

    var nameError by remember {mutableStateOf("")}
    var emailError by remember {mutableStateOf("")}
    var passwordError by remember {mutableStateOf("")}
    var confirmPasswordError by remember {mutableStateOf("")}

    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Crie uma conta",
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth().padding(start = 25.dp, top = 15.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = {name = it},
            label = { Text(nameError.ifEmpty{"Nome"}, color = if (nameError.isNotEmpty()) Color.Red else Color.Unspecified)},
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(320.dp).padding(vertical = 6.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(emailError.ifEmpty{"E-mail"}, color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified)},
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(320.dp).padding(vertical = 6.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(passwordError.ifEmpty{"Senha"}, color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified)},
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                Icon(
                    imageVector = image,
                    contentDescription = "",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                        .width(320.dp)
                        .padding(vertical = 6.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            label = { Text(confirmPasswordError.ifEmpty{"Confirmar senha"}, color = if (confirmPasswordError.isNotEmpty()) Color.Red else Color.Unspecified)},
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    Icon(
                        imageVector = image,
                        contentDescription = "",
                        modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible}
                    )
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(320.dp)
                .padding(vertical = 6.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nameError = if (name.isBlank()) "Nome obrigatório" else ""
                emailError = if (email.isBlank()) {
                    "Email obrigatório"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    "Email inválido"
                } else {
                    ""
                }
                passwordError = when {
                    password.isBlank() -> "Senha obrigatória"
                    password.length < 8 -> "Senha tem que ter 8 caracteres"
                    else -> ""
                }
                confirmPasswordError = when {
                    confirmPassword.isBlank() -> "Confirme a senha"
                    password != confirmPassword -> "Senhas não batem"
                    else -> ""
                }

                if (nameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
                    coroutineScope.launch {
                        val userDao = database.userDao()
                        val existingUser = userDao.getUserByEmail(email)

                        if (existingUser != null) {
                            emailError = "Este e-mail já existe"
                        } else {
                            val newUsuario = Usuario(name = name, email = email, password = password)
                            userDao.insertUser(newUsuario)
                            navController.navigate("login")
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
            Text("Cadastre-se")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {navController.navigate("login")}) {
            Text(
                text = "Faça Login",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}







