package com.mireyaserrano.linder.ui.edit

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
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

        val cardWeekly = findViewById<View>(R.id.cardWeekly)
        val cardMonthly = findViewById<View>(R.id.cardMonthly)
        val cardYearly = findViewById<View>(R.id.cardYearly)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        btnBack.setOnClickListener { finish() }

        fun updateSelection(type: SubscriptionType, priceText: String) {
            selectedPlan = type
            priceToPay = priceText
            btnContinue.text = "Continuar por $priceToPay"

            ViewCompat.setBackgroundTintList(cardWeekly, null)
            ViewCompat.setBackgroundTintList(cardMonthly, null)
            ViewCompat.setBackgroundTintList(cardYearly, null)

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

        // Asignar los listeners de selección
        cardWeekly.setOnClickListener { updateSelection(SubscriptionType.WEEKLY, "29,99€") }
        cardMonthly.setOnClickListener { updateSelection(SubscriptionType.MONTHLY, "79,96€") }
        cardYearly.setOnClickListener { updateSelection(SubscriptionType.YEARLY, "519,48€") }

        // Selección por defecto
        updateSelection(SubscriptionType.YEARLY, "519,48€")

        val btnContinuePayment = findViewById<Button>(R.id.btnContinue)
        btnContinuePayment.setOnClickListener {
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

            // Actualizamos el plan y la fecha
            currentUser.subscriptionType = selectedPlan
            currentUser.subscriptionEndDate = currentEnd + extraTime

            // --- AÑADIDO: REGISTRO DE MÉTRICAS DE COMPRA ---
            when (selectedPlan) {
                SubscriptionType.WEEKLY -> currentUser.metrics.subWeeklyPurchases++
                SubscriptionType.MONTHLY -> currentUser.metrics.subMonthlyPurchases++
                SubscriptionType.YEARLY -> currentUser.metrics.subYearlyPurchases++
                else -> {}
            }
            // -----------------------------------------------

            // Guardamos el usuario en la base de datos
            LocalDatabase.updateUser(currentUser)

            Toast.makeText(this, "¡Pago simulado con éxito!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}