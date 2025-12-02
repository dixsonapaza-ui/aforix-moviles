package com.example.clima.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FAQItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen() {
    val faqs = remember {
        listOf(
            FAQItem(
                question = "¿Qué es AFORIX?",
                answer = "AFORIX es una aplicación de control de aforo que te permite gestionar y monitorear la cantidad de personas en un espacio determinado en tiempo real."
            ),
            FAQItem(
                question = "¿Cómo registro una entrada?",
                answer = "Simplemente presiona el botón 'Entrar' en la pantalla principal. El sistema incrementará automáticamente el contador de ocupación."
            ),
            FAQItem(
                question = "¿Cómo registro una salida?",
                answer = "Presiona el botón 'Salir' en la pantalla principal. El sistema decrementará automáticamente el contador de ocupación."
            ),
            FAQItem(
                question = "¿Qué pasa si se alcanza la capacidad máxima?",
                answer = "Cuando se alcanza la capacidad máxima, el botón 'Entrar' se deshabilitará para evitar exceder el límite establecido. El indicador cambiará a color rojo para alertar."
            ),
            FAQItem(
                question = "¿Puedo cambiar la capacidad máxima?",
                answer = "Actualmente, la capacidad máxima está configurada por defecto en 100 personas. Esta funcionalidad estará disponible en futuras actualizaciones."
            ),
            FAQItem(
                question = "¿Los datos se guardan en la nube?",
                answer = "Sí, todos los datos se sincronizan automáticamente con Firebase, lo que permite acceso en tiempo real desde cualquier dispositivo."
            ),
            FAQItem(
                question = "¿Necesito conexión a internet?",
                answer = "Sí, AFORIX requiere conexión a internet para sincronizar los datos en tiempo real con Firebase."
            ),
            FAQItem(
                question = "¿Cómo creo una cuenta?",
                answer = "En la pantalla de inicio de sesión, presiona 'Regístrate' y completa el formulario con tu correo electrónico y contraseña."
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Preguntas Frecuentes",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )
        
        faqs.forEachIndexed { index, faq ->
            ExpandableFAQCard(
                faq = faq,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
fun ExpandableFAQCard(
    faq: FAQItem,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Contraer" else "Expandir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = faq.answer,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

