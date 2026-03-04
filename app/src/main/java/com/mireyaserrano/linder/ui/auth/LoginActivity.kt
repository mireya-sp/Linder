package com.mireyaserrano.linder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase // Importante añadir esto

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LocalDatabase.loadMetrics(this)

        val btnGoogle = findViewById<MaterialButton>(R.id.btn_google)
        val btnFacebook = findViewById<MaterialButton>(R.id.btn_facebook)
        val btnPhone = findViewById<MaterialButton>(R.id.btn_phone)
        val btnTroubles = findViewById<MaterialButton>(R.id.btn_troubles)

        btnGoogle.setOnClickListener { loginWithGoogle() }
        btnFacebook.setOnClickListener { loginWithFacebook() }
        btnTroubles.setOnClickListener { contactWithSupport() }

        btnPhone.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginWithFacebook() {
        LocalDatabase.globalMetrics.loginFacebook++
        LocalDatabase.saveMetrics(this)
        Toast.makeText(this, "Opción no disponible todavía", Toast.LENGTH_SHORT).show()
    }

    private fun loginWithGoogle() {
        LocalDatabase.globalMetrics.loginGoogle++
        LocalDatabase.saveMetrics(this)
        Toast.makeText(this, "Opción no disponible todavía", Toast.LENGTH_SHORT).show()
    }

    private fun contactWithSupport() {
        //TODO: Solicitar ayuda al soporte
        Toast.makeText(this, "Opción no disponible todavía", Toast.LENGTH_SHORT).show()
    }
}