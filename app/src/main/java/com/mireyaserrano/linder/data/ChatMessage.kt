package com.mireyaserrano.linder.data

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderPhone: String,
    val receiverPhone: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)