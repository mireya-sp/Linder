package com.mireyaserrano.linder.data

import java.util.Date

// Enums definidos
enum class SexualOrientation { LESBIANA, BISEXUAL, ASEXUAL }
enum class Intent { RELACION_SERIA, ROLLO_UNA_NOCHE, HACER_AMIGAS }
enum class SubscriptionType { NINGUNA, PLUS }

// Datos de suscripción
data class SubscriptionData(
    val type: SubscriptionType = SubscriptionType.NINGUNA,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val purchaseToken: String? = null
)

// La clase principal de Usuario
data class UserAccount(
    // Identificador único (Clave del Map)
    var dniNumber: String? = null,

    // Datos Login / Personales
    var phoneNumber: String? = null,
    var password: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,

    // Perfil
    var username: String? = null,
    var email: String? = null,
    var sexualOrientation: SexualOrientation? = null,
    var intent: Intent? = null,
    var habits: String? = null,
    var distancePreferenceKm: Int = 20,

    // Listas y Multimedia
    var userPhotos: MutableList<String> = mutableListOf(),
    var friendReferences: MutableList<String> = mutableListOf(),
    var isVerified: Boolean = false,

    // Suscripción
    var subscription: SubscriptionData = SubscriptionData(),

    var likedByUsers: MutableList<String> = mutableListOf()

)