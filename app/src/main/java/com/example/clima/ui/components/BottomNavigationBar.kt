package com.example.clima.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clima.navigation.Screen

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Aforo") },
            label = { Text("Aforo") },
            selected = currentRoute == Screen.Aforo.route,
            onClick = { onNavigate(Screen.Aforo.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "FAQ") },
            label = { Text("FAQ") },
            selected = currentRoute == Screen.FAQ.route,
            onClick = { onNavigate(Screen.FAQ.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.MoreVert, contentDescription = "Más") },
            label = { Text("Más") },
            selected = currentRoute == Screen.More.route,
            onClick = { onNavigate(Screen.More.route) }
        )
    }
}

