package com.mireyaserrano.linder.ui.edit

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat // IMPORTANTE PARA EL TINTE SEGURO
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.SubscriptionType

class SubscriptionActivity : AppCompatActivity() {

    private var selectedPlan: SubscriptionType = SubscriptionType.YEARLY
    private var priceToPay: String = "630,30€"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Si la app crashea aquí, es 100% seguro que te faltan estos IDs en tu XML
        val cardWeekly = findViewById<View>(R.id.cardWeekly)
        val cardMonthly = findViewById<View>(R.id.cardMonthly)
        val cardYearly = findViewById<View>(R.id.cardYearly)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        btnBack.setOnClickListener { finish() }

        fun updateSelection(type: SubscriptionType, priceText: String) {
            selectedPlan = type
            priceToPay = priceText
            btnContinue.text = "Continuar por $priceToPay"

            // 1. Limpiamos colores de forma segura compatible con cualquier Android
            ViewCompat.setBackgroundTintList(cardWeekly, null)
            ViewCompat.setBackgroundTintList(cardMonthly, null)
            ViewCompat.setBackgroundTintList(cardYearly, null)

            // 2. Aplicamos el color morado solo al seleccionado
            val selectedColor = ColorStateList.valueOf(Color.parseColor("#4A2A6A"))
            when (type) {
                SubscriptionType.WEEKLY -> {
                    cardWeekly.background.mutate()
                    ViewCompat.setBackgroundTintList(cardWeekly, selectedColor)
                }
                SubscriptionType.MONTHLY -> {
                    cardMonthly.background.mutate()
                    ViewCompat.setBackgroundTintList(cardMonthly, selectedColor)
                }
                SubscriptionType.YEARLY -> {
                    cardYearly.background.mutate()
                    ViewCompat.setBackgroundTintList(cardYearly, selectedColor)
                }
                else -> {}
            }
        }

        cardWeekly.setOnClickListener { updateSelection(SubscriptionType.WEEKLY, "29,99€") }
        cardMonthly.setOnClickListener { updateSelection(SubscriptionType.MONTHLY, "79,96€") }
        cardYearly.setOnClickListener { updateSelection(SubscriptionType.YEARLY, "519,48€") }

        // Inicializamos
        updateSelection(SubscriptionType.YEARLY, "519,48€")

        btnContinue.setOnClickListener {
            simulatePayment()
        }
    }

    private fun simulatePayment() {
        val currentUser = LocalDatabase.getCurrentUser()
        if (currentUser != null) {
            val now = System.currentTimeMillis()
            val extraTime: Long = when (selectedPlan) {
                SubscriptionType.WEEKLY -> 7L * 24 * 60 * 60 * 1000
                SubscriptionType.MONTHLY -> 30L * 24 * 60 * 60 * 1000
                SubscriptionType.YEARLY -> 365L * 24 * 60 * 60 * 1000
                else -> 0L
            }

            val currentEnd = if (currentUser.subscriptionEndDate > now) currentUser.subscriptionEndDate else now

            currentUser.subscriptionType = selectedPlan
            currentUser.subscriptionEndDate = currentEnd + extraTime

            LocalDatabase.updateUser(currentUser)

            Toast.makeText(this, "¡Pago simulado con éxito!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}