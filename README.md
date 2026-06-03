# 📱 Linder

![Android](https://img.shields.io/badge/Plataforma-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Lenguaje-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)

## 📝 Descripción

**Linder** es una aplicación nativa para Android desarrollada en Kotlin[cite: 1]. Diseñada como una plataforma integral para conectar personas, la aplicación incluye un sistema avanzado de perfiles, emparejamiento, mensajería interna y gestión de suscripciones[cite: 1]. 

## ✨ Características Principales

* **Registro Seguro y Detallado:** Flujo de *onboarding* de 9 pasos que garantiza la autenticidad del usuario, incluyendo verificación por DNI, validación con *selfie*, preferencias de orientación sexual, intenciones y hábitos[cite: 1].
* **Exploración y Emparejamiento:** Módulos dedicados para descubrir nuevos perfiles, gestionar "Me gusta" y navegar por vistas de inicio tanto en modalidad individual como doble[cite: 1].
* **Chat Integrado:** Sistema de mensajería en tiempo real con bandeja de entrada y vistas de chat individuales[cite: 1].
* **Gestión de Perfil Premium:** Edición detallada de perfil, ajustes de parámetros de búsqueda y un módulo integrado de suscripciones[cite: 1].
* **Panel de Administración:** Dashboard exclusivo para administrar la plataforma y gestionar cuentas[cite: 1].
* **Almacenamiento Local:** Uso de base de datos local para gestionar de manera eficiente el historial de mensajes y los datos de las cuentas de usuario[cite: 1].

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Kotlin[cite: 1].
* **Entorno de Desarrollo:** Android Studio / IntelliJ[cite: 1].
* **Build System:** Gradle implementado con Kotlin DSL (`build.gradle.kts`)[cite: 1].

## 📁 Estructura del Proyecto

El código fuente sigue una arquitectura organizada por funcionalidades dentro de `app/src/main/java/com/mireyaserrano/linder/`[cite: 1]:

* `data/`: Modelos de datos (`ChatMessage`, `UserAccount`) y configuración de la base de datos local[cite: 1].
* `ui/auth/`: Flujo completo de registro, inicio de sesión y validación de identidad[cite: 1].
* `ui/main/`: Interfaz principal de la aplicación, incluyendo inicio, exploración, likes y chat[cite: 1].
* `ui/edit/`: Pantallas para la configuración general, edición de perfil y planes de suscripción[cite: 1].
* `ui/admin/`: Vistas dedicadas al panel de administración[cite: 1].
* `res/drawable/`: Recursos visuales personalizados de la interfaz, como iconos de verificación, insignias y gradientes[cite: 1].

## 🚀 Instalación y ejecución

1. **Clona este repositorio:**
```bash
   git clone [https://github.com/mireya-sp/Linder.git](https://github.com/mireya-sp/Linder.git)
   ```
2. **Abre el proyecto:** Ejecuta Android Studio y selecciona la carpeta clonada[cite: 1].
3. **Sincroniza el proyecto:** Permite que Gradle descargue las dependencias necesarias[cite: 1].
4. **Ejecuta la app:** Conecta un dispositivo o inicia un emulador y presiona "Run"[cite: 1].

---
**Desarrollado por [Mireya Serrano](https://github.com/mireya-sp), [Luka Lopez](https://github.com/luklpz), [Nico Hustea](https://github.com/Hustea)**
