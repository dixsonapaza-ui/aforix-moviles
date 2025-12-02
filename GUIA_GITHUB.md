# Guia para Subir el Proyecto AFORIX a GitHub

## Paso 1: Crear Repositorio en GitHub

1. Ve a [GitHub](https://github.com) e inicia sesion
2. Haz clic en el boton "+" (arriba a la derecha) y selecciona "New repository"
3. Completa los datos:
   - **Repository name:** `forix-control-aforo` (o el nombre que prefieras)
   - **Description:** "Aplicacion Android para control de aforo de personas en tiempo real con Firebase"
   - **Visibility:** Public o Private (tu eleccion)
   - **NO marques** "Initialize this repository with a README" (ya tenemos uno)
4. Haz clic en "Create repository"

## Paso 2: Inicializar Git en el Proyecto

Abre una terminal en la carpeta del proyecto y ejecuta:

```bash
# Inicializar repositorio git
git init

# Agregar todos los archivos (excepto los del .gitignore)
git add .

# Hacer el primer commit
git commit -m "Initial commit: AFORIX - Control de Aforo v1.0.0"

# Agregar el repositorio remoto (reemplaza TU_USUARIO con tu usuario de GitHub)
git remote add origin https://github.com/TU_USUARIO/forix-control-aforo.git

# Cambiar a la rama main (si es necesario)
git branch -M main

# Subir el codigo
git push -u origin main
```

## Paso 3: Verificar que google-services.json NO se suba

IMPORTANTE: El archivo `google-services.json` contiene credenciales de Firebase y NO debe subirse a GitHub.

El archivo `.gitignore` ya esta configurado para excluirlo. Verifica que:

1. El archivo `app/google-services.json` NO aparezca en `git status`
2. Solo se suba `app/google-services.json.example` (la plantilla)

## Paso 4: Agregar Descripcion al Repositorio

Una vez subido, agrega esta descripcion en GitHub:

```
Aplicacion Android moderna para control de aforo de personas en tiempo real. 
Desarrollada con Jetpack Compose y Firebase.

Caracteristicas:
- Autenticacion con Firebase
- Control de aforo en tiempo real
- Multiples ubicaciones
- Interfaz moderna con Material Design 3
- Sincronizacion en la nube
```

## Paso 5: Agregar Topics (Opcional)

En la pagina del repositorio, haz clic en el engranaje junto a "About" y agrega estos topics:

- android
- kotlin
- jetpack-compose
- firebase
- material-design
- mvvm
- aforo
- control-capacidad

## Notas Importantes

- El archivo `google-services.json` NO se subira (esta en .gitignore)
- Los usuarios que clonen el repositorio necesitaran crear su propio archivo `google-services.json`
- El archivo `google-services.json.example` sirve como plantilla
- Lee `FIREBASE_SETUP.md` para instrucciones de configuracion

## Comandos Utiles

```bash
# Ver estado de los archivos
git status

# Ver que archivos se van a subir
git ls-files

# Verificar que google-services.json NO esta incluido
git ls-files | grep google-services.json

# Si necesitas actualizar el repositorio en el futuro
git add .
git commit -m "Descripcion del cambio"
git push
```

