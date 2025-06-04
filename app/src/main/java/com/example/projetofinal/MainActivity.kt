package com.example.projetofinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.projetofinal.data.UsuarioDatabase
import com.example.projetofinal.screens.AppNavigation
import com.example.projetofinal.ui.theme.ProjetoFinalTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = UsuarioDatabase.getDatabase(applicationContext)

        enableEdgeToEdge()
        setContent {
            ProjetoFinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(paddingValues = innerPadding, database = database)
                }
            }
        }
    }
}
