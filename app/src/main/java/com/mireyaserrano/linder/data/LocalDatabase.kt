package com.mireyaserrano.linder.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

object LocalDatabase {

    private const val PREFS_NAME = "LinderDB"
    private const val KEY_USERS = "users_json"
    private const val KEY_CURRENT_USER = "current_user_phone"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    private var usersMap: MutableMap<String, UserAccount> = mutableMapOf()
    private var currentUser: UserAccount? = null

    /**
     * Inicializa las SharedPreferences.
     * Se debe llamar en LinderApp o en el onCreate de las Actividades.
     */
    fun init(context: Context) {
        if (this::prefs.isInitialized) return
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Intentamos cargar lo que ya hay guardado en el móvil
        loadUsersFromDisk()

        // 2. MODIFICACIÓN AQUÍ:
        // Si el mapa tiene menos de 6 usuarios (porque has añadido nuevos al código),
        // forzamos que se ejecute preLoadUsers para rellenar los que faltan.
        if (usersMap.size < 6) {
            preLoadUsers(context)
        }

        loadCurrentUser()
    }

    private fun preLoadUsers(context: Context) {
        val seedUsers = mutableListOf<UserAccount>()
        val pkg = context.packageName

        // USUARIO 1 - Isabel
        seedUsers.add(UserAccount(
            dniNumber = "50626408G",
            phoneNumber = "644012665",
            password = "password123",
            username = "Isabel",
            birthDate = "12/05/1950",
            sexualOrientation = SexualOrientation.ASEXUAL,
            intent = Intent.ROLLO_UNA_NOCHE,
            distancePreferenceKm = 100,
            habits = "Me encanta el robar y apoyar genocidios.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_isabel_1","android.resource://$pkg/drawable/user_isabel_2","android.resource://$pkg/drawable/user_isabel_3"),
            likedByUsers = mutableListOf("612983744", "699231002", "654112233")
        ))

        // USUARIO 2 - Marta
        seedUsers.add(UserAccount(
            dniNumber = "48392105M",
            phoneNumber = "612983744",
            password = "password123",
            username = "Marta",
            birthDate = "22/03/1995",
            sexualOrientation = SexualOrientation.BISEXUAL,
            intent = Intent.RELACION_SERIA,
            distancePreferenceKm = 50,
            habits = "Amante de la pizza, el cine de terror y viajar sola.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_marta_1","android.resource://$pkg/drawable/user_marta_2"),
            likedByUsers = mutableListOf("644012665", "677889900", "622334455")
        ))

        // USUARIO 3 - Elena
        seedUsers.add(UserAccount(
            dniNumber = "71455623S",
            phoneNumber = "699231002",
            password = "password123",
            username = "Elena",
            birthDate = "05/11/1992",
            sexualOrientation = SexualOrientation.ASEXUAL,
            intent = Intent.HACER_AMIGAS,
            distancePreferenceKm = 25,
            habits = "Busco compañera para ir a museos y conciertos de jazz.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_elena_1"),
            likedByUsers = mutableListOf("666777888", "644012665")
        ))

        // USUARIO 4 - Leticia
        seedUsers.add(UserAccount(
            dniNumber = "03948571X",
            phoneNumber = "654112233",
            password = "password123",
            username = "Leticia",
            birthDate = "14/07/1953",
            sexualOrientation = SexualOrientation.LESBIANA,
            intent = Intent.RELACION_SERIA,
            distancePreferenceKm = 80,
            habits = "Estudiante de artes, apasionada de la pintura y la playa.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_leticia_1","android.resource://$pkg/drawable/user_leticia_2","android.resource://$pkg/drawable/user_leticia_3","android.resource://$pkg/drawable/user_leticia_4","android.resource://$pkg/drawable/user_leticia_5","android.resource://$pkg/drawable/user_leticia_6"),
            likedByUsers = mutableListOf("677889900", "612983744")
        ))

        // USUARIO 5 - Carmen
        seedUsers.add(UserAccount(
            dniNumber = "29485763B",
            phoneNumber = "677889900",
            password = "password123",
            username = "Carmen",
            birthDate = "30/01/1988",
            sexualOrientation = SexualOrientation.BISEXUAL,
            intent = Intent.HACER_AMIGAS,
            distancePreferenceKm = 15,
            habits = "Adoro cocinar recetas nuevas y el vino tinto.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_carmen_1"),
            likedByUsers = mutableListOf("622334455", "699231002", "654112233", "666777888")
        ))

        // USUARIO 6 - Carla
        seedUsers.add(UserAccount(
            dniNumber = "51029384Z",
            phoneNumber = "622334455",
            password = "password123",
            username = "Carla",
            birthDate = "19/09/1997",
            sexualOrientation = SexualOrientation.LESBIANA,
            intent = Intent.ROLLO_UNA_NOCHE,
            distancePreferenceKm = 40,
            habits = "Crossfitera y fanática de la montaña.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_carla_1","android.resource://$pkg/drawable/user_carla_2","android.resource://$pkg/drawable/user_carla_3"),
            likedByUsers = mutableListOf("644012665")
        ))

        // USUARIO 6 - Hermenigilda
        seedUsers.add(UserAccount(
            dniNumber = "51037384Z",
            phoneNumber = "666777888",
            password = "password123",
            username = "Hermenigilda",
            birthDate = "19/09/1926",
            sexualOrientation = SexualOrientation.LESBIANA,
            intent = Intent.ROLLO_UNA_NOCHE,
            distancePreferenceKm = 40,
            habits = "Tejo puntadas y cocino para mis nietos.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_hermenigilda_1"),
            likedByUsers = mutableListOf("622334455", "612983744", "699231002")
        ))

        // USUARIO 7 - Eduard
        seedUsers.add(UserAccount(
            dniNumber = "58579384Z",
            phoneNumber = "123456789",
            password = "password123",
            username = "Eduard",
            birthDate = "19/09/1702",
            sexualOrientation = SexualOrientation.ASEXUAL,
            intent = Intent.HACER_AMIGAS,
            distancePreferenceKm = 100,
            habits = "Me gustan mucho las interfaces y los juegos de ponis uwu.",
            userPhotos = mutableListOf("android.resource://$pkg/drawable/user_profe"),
            likedByUsers = mutableListOf("622334455", "612983744", "699231002")
        ))

        seedUsers.forEach { user ->
            user.phoneNumber?.let { phone ->
                usersMap[phone] = user
            }
        }
        saveUsersToDisk()
    }

    /**
     * Guarda un usuario y lo persiste en el disco inmediatamente.
     */
    fun saveUser(user: UserAccount) {
        val key = user.phoneNumber ?: return
        usersMap[key] = user

        // Guardamos en el almacenamiento físico
        saveUsersToDisk()

        // Establecemos como usuario activo (Login automático)
        setCurrentUser(user)
    }

    fun getUserByPhone(phone: String): UserAccount? {
        return usersMap[phone]
    }

    fun getCurrentUser(): UserAccount? {
        return currentUser
    }

    /**
     * Elimina la sesión activa del disco.
     */
    fun logout() {
        currentUser = null
        if (this::prefs.isInitialized) {
            prefs.edit().remove(KEY_CURRENT_USER).apply()
        }
    }

    // --- MÉTODOS PRIVADOS DE PERSISTENCIA ---

    private fun saveUsersToDisk() {
        // Verificación de seguridad para evitar el crash de lateinit
        if (!this::prefs.isInitialized) return

        val json = gson.toJson(usersMap)
        prefs.edit().putString(KEY_USERS, json).apply()
    }

    private fun loadUsersFromDisk() {
        if (!this::prefs.isInitialized) return

        val json = prefs.getString(KEY_USERS, null)
        if (json != null) {
            try {
                val type = object : TypeToken<MutableMap<String, UserAccount>>() {}.type
                usersMap = gson.fromJson(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
                usersMap = mutableMapOf()
            }
        }
    }

    private fun setCurrentUser(user: UserAccount) {
        currentUser = user
        if (this::prefs.isInitialized) {
            user.phoneNumber?.let {
                prefs.edit().putString(KEY_CURRENT_USER, it).apply()
            }
        }
    }

    private fun loadCurrentUser() {
        if (!this::prefs.isInitialized) return

        val phone = prefs.getString(KEY_CURRENT_USER, null)
        if (phone != null) {
            currentUser = usersMap[phone]
        }
    }

    fun importImageToApp(context: Context, uri: Uri): String? {
        return try {
            // Creamos una carpeta interna llamada 'photos'
            val folder = File(context.filesDir, "photos")
            if (!folder.exists()) folder.mkdirs()

            // Generamos un nombre único
            val fileName = "img_${System.currentTimeMillis()}_${(1..1000).random()}.jpg"
            val destFile = File(folder, fileName)

            // Copiamos el contenido del Uri al archivo nuevo
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            // Devolvemos la ruta absoluta donde se ha guardado
            destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getAllUsers(): Map<String, UserAccount> {
        return usersMap
    }
}