# 📱 Linder

![Android](https://img.shields.io/badge/Plataforma-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Lenguaje-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)

## 📝 Descripción

**Linder** es una aplicación nativa para Android desarrollada en Kotlin. Diseñada como una plataforma integral para conectar personas, la aplicación incluye un sistema avanzado de perfiles, emparejamiento, mensajería interna y gestión de suscripciones. 

## ✨ Características Principales

* **Registro Seguro y Detallado:** Flujo de *onboarding* de 9 pasos que garantiza la autenticidad del usuario, incluyendo verificación por DNI, validación con *selfie*, preferencias de orientación sexual, intenciones y hábitos.
* **Exploración y Emparejamiento:** Módulos dedicados para descubrir nuevos perfiles, gestionar "Me gusta" y navegar por vistas de inicio tanto en modalidad individual como doble.
* **Chat Integrado:** Sistema de mensajería en tiempo real con bandeja de entrada y vistas de chat individuales.
* **Gestión de Perfil Premium:** Edición detallada de perfil, ajustes de parámetros de búsqueda y un módulo integrado de suscripciones.
* **Panel de Administración:** Dashboard exclusivo para administrar la plataforma y gestionar cuentas.
* **Almacenamiento Local:** Uso de base de datos local para gestionar de manera eficiente el historial de mensajes y los datos de las cuentas de usuario.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Kotlin.
* **Entorno de Desarrollo:** Android Studio / IntelliJ.
* **Build System:** Gradle implementado con Kotlin DSL (`build.gradle.kts`).

## 📁 Estructura del Proyecto

El código fuente sigue una arquitectura organizada por funcionalidades dentro de `app/src/main/java/com/mireyaserrano/linder/`:

* `data/`: Modelos de datos (`ChatMessage`, `UserAccount`) y configuración de la base de datos local.
* `ui/auth/`: Flujo completo de registro, inicio de sesión y validación de identidad.
* `ui/main/`: Interfaz principal de la aplicación, incluyendo inicio, exploración, likes y chat.
* `ui/edit/`: Pantallas para la configuración general, edición de perfil y planes de suscripción.
* `ui/admin/`: Vistas dedicadas al panel de administración.
* `res/drawable/`: Recursos visuales personalizados de la interfaz, como iconos de verificación, insignias y gradientes.

## 🚀 Instalación y ejecución

1. **Clona este repositorio:**
```bash
   git clone [https://github.com/mireya-sp/Linder.git](https://github.com/mireya-sp/Linder.git)
   ```
2. **Abre el proyecto:** Ejecuta Android Studio y selecciona la carpeta clonada.
3. **Sincroniza el proyecto:** Permite que Gradle descargue las dependencias necesarias.
4. **Ejecuta la app:** Conecta un dispositivo o inicia un emulador y presiona "Run".

---
**Desarrollado por [Mireya Serrano](https://github.com/mireya-sp), [Luka Lopez](https://github.com/luklpz), [Nico Hustea](https://github.com/Hustea)**
