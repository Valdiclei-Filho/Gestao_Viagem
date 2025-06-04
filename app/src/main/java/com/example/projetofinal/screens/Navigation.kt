package com.example.projetofinal.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.projetofinal.data.repository.ViagemRepository
import com.example.projetofinal.data.UsuarioDatabase
import com.example.projetofinal.data.model.NavigationItems
import com.example.projetofinal.viewmodel.ViagemViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(paddingValues: PaddingValues, database: UsuarioDatabase) {
    val navController = rememberNavController()
    var isAuthenticated by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        NavigationItems("Início", Icons.Filled.Home),
        NavigationItems("Nova viagem", Icons.Filled.Flight),
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val screenTitle = when (selectedItemIndex) {
        0 -> "Início"
        1 -> "Nova viagem"
        else -> "Início"
    }

    var userId by rememberSaveable { mutableStateOf<Int?>(null) }

    val viagemRepository = ViagemRepository(database.travelDao())
    val viagemViewModel = ViagemViewModel(viagemRepository)

    Scaffold(
        topBar = {
            if (isAuthenticated) {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Green),
                    title = { Text(text = screenTitle) },
                    actions = {
                        IconButton(onClick = {
                            isAuthenticated = false
                            userId = null
                            navController.navigate("login") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Sair",
                                tint = Color.Black
                            )
                        }
                    }
                )

            }
        },
        bottomBar = {
            if (isAuthenticated) {
                NavigationBar(
                    items = items,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { index, route ->
                        selectedItemIndex = index
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = "login", modifier = Modifier.padding(padding)) {
            composable("login") {
                LoginScreen(navController, paddingValues, database) { id ->
                    isAuthenticated = true
                    userId = id
                    navController.navigate("Início") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable("register") {
                RegisterScreen(navController, paddingValues, database) {
                    isAuthenticated = true
                    navController.navigate("Início") {
                        popUpTo("register") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable("Início") {
                userId?.let { HomeScreen(navController, viewModel = viagemViewModel, userId = it) }
            }
            composable("Nova viagem") {
                userId?.let { TravelsScreen(navController, viewModel = viagemViewModel, userId = it) }
            }
            composable("editTravel/{travelId}") { backStackEntry ->
                val travelId = backStackEntry.arguments?.getString("travelId")?.toIntOrNull() ?: return@composable
                EditTravelScreen(navController, viewModel = viagemViewModel, travelId = travelId)
            }
            composable("roteiroIA/{roteiro}") { backStackEntry ->
                val roteiro = backStackEntry.arguments?.getString("roteiro") ?: ""
                RoteiroIAScreen(roteiro = roteiro) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun NavigationBar(
    items: List<NavigationItems>,
    selectedItemIndex: Int,
    onItemSelected: (Int, String) -> Unit
) {
    NavigationBar(
        containerColor = Color.Green,
        modifier = Modifier.padding(top = 2.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = { onItemSelected(index, item.name) },
                icon = {
                    Icon(
                        imageVector = item.logo,
                        contentDescription = item.name,
                        tint = if (selectedItemIndex == index) Color.Green else Color.Black,
                        modifier = Modifier.size(if (selectedItemIndex == index) 35.dp else 30.dp)
                    )
                }
            )
        }
    }
}