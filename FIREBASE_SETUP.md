# Configuración de Firebase para AFORIX

## Pasos para configurar Firebase

### 1. Crear proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en "Agregar proyecto" o selecciona un proyecto existente
3. Completa el nombre del proyecto (ej: "AFORIX")
4. Sigue los pasos del asistente

### 2. Agregar aplicación Android

1. En el panel del proyecto, haz clic en el ícono de Android
2. Completa el formulario:
   - **Nombre del paquete Android**: `com.example.clima`
   - **Apodo de la app** (opcional): AFORIX
   - **Certificado de firma SHA-1** (opcional para desarrollo)
3. Haz clic en "Registrar app"
4. **Descarga el archivo `google-services.json`**
5. Coloca el archivo en la carpeta `app/` del proyecto (al mismo nivel que `build.gradle.kts`)

### 3. Habilitar Firebase Authentication

1. En el menú lateral, ve a **Authentication**
2. Haz clic en "Comenzar"
3. Ve a la pestaña **Sign-in method**
4. Habilita **Email/Password**
5. Haz clic en "Guardar"

### 4. Habilitar Cloud Firestore

1. En el menú lateral, ve a **Firestore Database**
2. Haz clic en "Crear base de datos"
3. Selecciona **Modo de prueba** (para desarrollo)
4. Elige una ubicación para la base de datos
5. Haz clic en "Habilitar"

### 5. Reglas de Firestore (Recomendado para producción)

Para desarrollo, las reglas de prueba funcionarán. Para producción, actualiza las reglas:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Solo usuarios autenticados pueden leer/escribir en aforo
    match /aforo/{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### 6. Verificar configuración

1. Asegúrate de que el archivo `google-services.json` esté en `app/google-services.json`
2. Verifica que el plugin de Google Services esté aplicado en `app/build.gradle.kts`
3. Sincroniza el proyecto con Gradle Files
4. Compila y ejecuta la aplicación

## Estructura de datos en Firestore

La aplicación creará automáticamente la siguiente estructura:

```
aforo/
  └── Principal/
      ├── currentOccupancy: number
      ├── maxCapacity: number
      └── location: string
```

## Solución de problemas

### Error: "Missing google-services.json"
- Verifica que el archivo esté en `app/google-services.json`
- Asegúrate de que el nombre del paquete coincida con `com.example.clima`

### Error de autenticación
- Verifica que Email/Password esté habilitado en Firebase Console
- Revisa que las reglas de Firestore permitan acceso autenticado

### Error de conexión
- Verifica tu conexión a internet
- Asegúrate de que Firebase esté correctamente configurado

