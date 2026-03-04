package com.mireyaserrano.linder.data

import java.util.Date

enum class SexualOrientation { LESBIANA, BISEXUAL, ASEXUAL }
enum class Intent { RELACION_SERIA, ROLLO_UNA_NOCHE, HACER_AMIGAS }
enum class SubscriptionType { NONE, WEEKLY, MONTHLY, YEARLY }
data class SubscriptionData(
    val type: SubscriptionType = SubscriptionType.NONE,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val purchaseToken: String? = null
)

data class UserMetrics(
    // Historial de compras de este usuario
    var subWeeklyPurchases: Int = 0,
    var subMonthlyPurchases: Int = 0,
    var subYearlyPurchases: Int = 0,

    var interestTravel: Boolean = false,
    var interestEvents: Boolean = false,
    var interestNature: Boolean = false,
    var interestBeach: Boolean = false
)

data class UserAccount(
    var dniNumber: String? = null,

    var phoneNumber: String? = null,
    var password: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,

    var username: String? = null,
    var email: String? = null,
    var sexualOrientation: SexualOrientation? = null,
    var intent: Intent? = null,
    var habits: String? = null,
    var distancePreferenceKm: Int = 20,

    var location: String = "",
    var minAgePreference: Int = 18,
    var maxAgePreference: Int = 100,

    // Listas y Multimedia
    var userPhotos: MutableList<String> = mutableListOf(),
    var friendReferences: MutableList<String> = mutableListOf(),
    var isVerified: Boolean = false,

    var subscriptionType: SubscriptionType = SubscriptionType.NONE,
    var subscriptionEndDate: Long = 0L,

    var likedByUsers: MutableList<String> = mutableListOf(),
    val matches: MutableList<String> = mutableListOf(),
    val activeChats: MutableList<String> = mutableListOf(),

    var hasCompletedTutorial: Boolean = false,
    var hasSeenExploreTutorial: Boolean = false,
    var hasSeenLikesTutorial: Boolean = false,
    var hasSeenChatsTutorial: Boolean = false,
    var hasSeenProfileTutorial: Boolean = false,

    var metrics: UserMetrics = UserMetrics()
)