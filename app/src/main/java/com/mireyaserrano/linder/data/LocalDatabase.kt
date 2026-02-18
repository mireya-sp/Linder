package com.mireyaserrano.linder.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
        // Evitamos re-inicializar si ya está lista
        if (this::prefs.isInitialized) return

        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadUsersFromDisk()
        loadCurrentUser()
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
}