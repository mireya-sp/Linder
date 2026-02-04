package com.mireyaserrano.linder.data

import java.util.Date

enum class SexualOrientation { LESBIANA, BISEXUAL, ASEXUAL }
enum class Intent { RELACION_SERIA, ROLLO_UNA_NOCHE, HACER_AMIGAS }
enum class SubscriptionType { NINGUNA, PLUS }

data class SubscriptionData(
    val type: SubscriptionType = SubscriptionType.NINGUNA,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val purchaseToken: String? = null
)

data class UserAccount(
    //Datos del DNI
    var phoneNumber: String? = null,
    var password: String? = null,
    var dniNumber: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,

    //Perfil
    var username: String? = null,
    var email: String? = null,
    var sexualOrientation: SexualOrientation? = null,
    var intent: Intent? = null,
    var habits: String? = null,
    var distancePreferenceKm: Int = 20, //Por defecto 20km

    //Listas y Multimedia
    var userPhotos: MutableList<String> = mutableListOf(), //Rutas de fotos
    var friendReferences: MutableList<String> = mutableListOf(), //IDs de amigas (máx 3)
    var isVerified: Boolean = false,

    //Suscripción
    var subscription: SubscriptionData = SubscriptionData()
)