package com.mireyaserrano.linder.data

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(), // Identificador único
    val senderPhone: String,   // Quién lo envía
    val receiverPhone: String, // Quién lo recibe
    val message: String,       // El texto
    val timestamp: Long = System.currentTimeMillis() // La hora exacta
)