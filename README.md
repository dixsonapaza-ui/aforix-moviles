# FORIX - Control de Aforo

Aplicación Android moderna para el control de aforo de personas en tiempo real, desarrollada con Jetpack Compose y Firebase.

## Características

- 🔐 **Autenticación**: Sistema de login y registro con Firebase Authentication
- 📊 **Control de Aforo**: Monitoreo en tiempo real de la ocupación de espacios
- 🎨 **Diseño Elegante**: Interfaz moderna con paleta de colores profesional
- 📱 **Navegación Intuitiva**: Navegación por pestañas inferior
- ❓ **FAQ**: Preguntas frecuentes integradas
- ⚙️ **Configuración**: Panel de opciones y configuración

## Tecnologías Utilizadas

- **Jetpack Compose**: UI moderna y declarativa
- **Firebase Authentication**: Autenticación de usuarios
- **Firebase Firestore**: Base de datos en tiempo real
- **Material Design 3**: Componentes de diseño modernos
- **Navigation Compose**: Navegación entre pantallas
- **ViewModel**: Gestión de estado

## Configuración de Firebase

### Paso 1: Crear proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Agrega una aplicación Android:
   - Nombre del paquete: `com.example.clima`
   - Descarga el archivo `google-services.json`

### Paso 2: Configurar Firebase en el proyecto

1. Coloca el archivo `google-services.json` en la carpeta `app/`
2. Asegúrate de que el archivo `build.gradle.kts` del nivel superior tenga el plugin de Google Services (ya está configurado)
3. Asegúrate de que el archivo `app/build.gradle.kts` tenga el plugin aplicado (ya está configurado)

### Paso 3: Habilitar servicios en Firebase

1. **Firebase Authentication**:
   - Ve a Authentication > Sign-in method
   - Habilita "Email/Password"

2. **Firebase Firestore**:
   - Ve a Firestore Database
   - Crea una base de datos en modo de prueba
   - Las reglas de seguridad se configurarán automáticamente

### Paso 4: Reglas de Firestore (Opcional - para producción)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /aforo/{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## Estructura del Proyecto

```
app/src/main/java/com/example/clima/
├── data/
│   ├── AuthRepository.kt       # Repositorio de autenticación
│   └── AforoRepository.kt      # Repositorio de datos de aforo
├── viewmodel/
│   ├── AuthViewModel.kt        # ViewModel de autenticación
│   └── AforoViewModel.kt        # ViewModel de aforo
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt      # Pantalla de login
│   │   ├── RegisterScreen.kt   # Pantalla de registro
│   │   ├── AforoScreen.kt      # Pantalla principal de aforo
│   │   ├── FAQScreen.kt        # Pantalla de preguntas frecuentes
│   │   └── MoreScreen.kt       # Pantalla de más opciones
│   ├── components/
│   │   └── BottomNavigationBar.kt  # Barra de navegación inferior
│   └── theme/
│       ├── Color.kt             # Paleta de colores
│       ├── Theme.kt             # Tema de la aplicación
│       └── Type.kt              # Tipografía
├── navigation/
│   └── NavGraph.kt             # Configuración de navegación
└── MainActivity.kt              # Actividad principal
```

## Paleta de Colores

La aplicación utiliza una paleta de colores elegante y profesional:

- **Primary**: Indigo (#6366F1)
- **Secondary**: Púrpura (#8B5CF6)
- **Accent**: Cyan (#06B6D4)
- **Success**: Verde (#10B981)
- **Warning**: Amarillo (#F59E0B)
- **Error**: Rojo (#EF4444)

## Funcionalidades

### Autenticación
- Registro de nuevos usuarios
- Inicio de sesión
- Cierre de sesión

### Control de Aforo
- Visualización de ocupación actual
- Indicador circular con porcentaje
- Botones para incrementar/decrementar aforo
- Validación de capacidad máxima
- Sincronización en tiempo real con Firebase

### Navegación
- Pantalla principal de aforo
- Preguntas frecuentes (FAQ)
- Panel de más opciones

## Compilación

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Configura Firebase (ver sección anterior)
4. Sincroniza el proyecto con Gradle
5. Ejecuta la aplicación

## Requisitos

- Android Studio Hedgehog o superior
- Min SDK: 24
- Target SDK: 36
- Kotlin 2.0.21
- JDK 11

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## Autor

Desarrollado para FORIX - Control de Aforo

