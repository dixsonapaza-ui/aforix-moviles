package com.example.clima.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.viewmodel.AforoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AforoScreen(
    viewModel: AforoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val aforoData = uiState.aforoData
    
    // Estados para diálogos y selección
    var showCapacityDialog by remember { mutableStateOf(false) }
    var showLocationMenu by remember { mutableStateOf(false) }
    var newCapacity by remember { mutableStateOf(aforoData.maxCapacity.toString()) }
    
    // Lista de ubicaciones disponibles
    val locations = remember {
        listOf("Principal", "Tecup", "Sala de Conferencias", "Área Común")
    }
    
    // Calcular porcentaje
    val percentage = if (aforoData.maxCapacity > 0) {
        (aforoData.currentOccupancy.toFloat() / aforoData.maxCapacity.toFloat()) * 100f
    } else {
        0f
    }
    
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1000),
        label = "percentage"
    )
    
    // Determinar color según porcentaje
    val progressColor = when {
        percentage >= 90 -> MaterialTheme.colorScheme.error
        percentage >= 70 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    
    LaunchedEffect(uiState.selectedLocation) {
        viewModel.loadAforoData(uiState.selectedLocation)
    }
    
    // Mostrar mensajes
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null || uiState.successMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header con título y botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Control de Aforo",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = { showCapacityDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Selector de ubicación
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ubicación:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                var expanded by remember { mutableStateOf(false) }
                Box {
                    TextButton(
                        onClick = { expanded = true }
                    ) {
                        Text(
                            text = aforoData.location,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        locations.forEach { location ->
                            DropdownMenuItem(
                                text = { Text(location) },
                                onClick = {
                                    viewModel.changeLocation(location)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // Card principal con información
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Indicador circular de porcentaje
                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { animatedPercentage / 100f },
                        modifier = Modifier.size(200.dp),
                        strokeWidth = 16.dp,
                        color = progressColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Animación del número de ocupación
                        val animatedOccupancy by animateIntAsState(
                            targetValue = aforoData.currentOccupancy,
                            animationSpec = tween(durationMillis = 500),
                            label = "occupancy"
                        )
                        Text(
                            text = "$animatedOccupancy",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "de ${aforoData.maxCapacity}",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${animatedPercentage.toInt()}%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = progressColor
                        )
                    }
                }
                
                // Información adicional
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoCard(
                        title = "Ocupación",
                        value = "${aforoData.currentOccupancy}",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    InfoCard(
                        title = "Capacidad",
                        value = "${aforoData.maxCapacity}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        
        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { 
                    viewModel.incrementAforo(uiState.selectedLocation)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                enabled = !uiState.isLoading && aforoData.currentOccupancy < aforoData.maxCapacity,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Entrar",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Entrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Button(
                onClick = { 
                    viewModel.decrementAforo(uiState.selectedLocation)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                enabled = !uiState.isLoading && aforoData.currentOccupancy > 0,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Salir",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Salir",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        // Información de otras ubicaciones
        Text(
            text = "Otras Ubicaciones",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        
        // Card de ubicaciones adicionales
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ubicación Tecup
                LocationInfoCard(
                    locationName = "Tecup",
                    currentOccupancy = 45,
                    maxCapacity = 80,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Separador
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                
                // Otras ubicaciones de ejemplo
                LocationInfoCard(
                    locationName = "Sala de Conferencias",
                    currentOccupancy = 12,
                    maxCapacity = 30,
                    modifier = Modifier.fillMaxWidth()
                )
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                
                LocationInfoCard(
                    locationName = "Área Común",
                    currentOccupancy = 28,
                    maxCapacity = 50,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Mensajes con animación
        AnimatedVisibility(
            visible = uiState.errorMessage != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        AnimatedVisibility(
            visible = uiState.successMessage != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            uiState.successMessage?.let { success ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = success,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    
    // Diálogo para configurar capacidad máxima
    if (showCapacityDialog) {
        AlertDialog(
            onDismissRequest = { showCapacityDialog = false },
            title = {
                Text(
                    text = "Configurar Capacidad Máxima",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Ubicación: ${aforoData.location}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = newCapacity,
                        onValueChange = { 
                            if (it.all { char -> char.isDigit() }) {
                                newCapacity = it
                            }
                        },
                        label = { Text("Capacidad máxima") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val capacity = newCapacity.toIntOrNull() ?: aforoData.maxCapacity
                        viewModel.setMaxCapacity(capacity, aforoData.location)
                        showCapacityDialog = false
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showCapacityDialog = false
                        newCapacity = aforoData.maxCapacity.toString()
                    }
                ) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun LocationInfoCard(
    locationName: String,
    currentOccupancy: Int,
    maxCapacity: Int,
    modifier: Modifier = Modifier
) {
    val percentage = if (maxCapacity > 0) {
        (currentOccupancy.toFloat() / maxCapacity.toFloat()) * 100f
    } else {
        0f
    }
    
    val statusColor = when {
        percentage >= 90 -> MaterialTheme.colorScheme.error
        percentage >= 70 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = locationName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$currentOccupancy / $maxCapacity personas",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${percentage.toInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .width(80.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = statusColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

