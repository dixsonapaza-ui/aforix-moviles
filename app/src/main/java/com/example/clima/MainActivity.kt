package com.example.clima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clima.navigation.NavGraph
import com.example.clima.navigation.Screen
import com.example.clima.ui.components.BottomNavigationBar
import com.example.clima.ui.theme.ForixTheme
import com.example.clima.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForixTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val authUiState by authViewModel.uiState.collectAsState()
    
    // Manejar logout
    LaunchedEffect(Unit) {
        // Verificar si el usuario está autenticado al iniciar
        if (authUiState.user == null && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    val showBottomBar = currentRoute in listOf(
        Screen.Aforo.route,
        Screen.FAQ.route,
        Screen.More.route
    )
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Aforo.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onLogout = {
                authViewModel.signOut()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}