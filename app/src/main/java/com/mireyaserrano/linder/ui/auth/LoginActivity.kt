package com.mireyaserrano.linder.ui.auth


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.mireyaserrano.linder.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnGoogle = findViewById<MaterialButton>(R.id.btn_google)
        val btnFacebook = findViewById<MaterialButton>(R.id.btn_facebook)
        val btnPhone = findViewById<MaterialButton>(R.id.btn_phone)
        val btnTroubles = findViewById<MaterialButton>(R.id.btn_troubles)

        val loginWithFacebook = {
            //TODO: Hacer el registro con Facebook
            Toast.makeText(this, "Opción no disponible todavía", Toast.LENGTH_SHORT).show()
        }

        val loginWithGoogle = {
            //TODO: Hacer el registro con Google
            Toast.makeText(this, "Opción no disponible todavía", Toast.LENGTH_SHORT).show()
        }

        val contactWithSupport = {
            //TODO: Solicitar ayuda al soporte
            Toast.makeText(this, "Opción no disponible todavía", Toast.LENGTH_SHORT).show()
        }

        btnGoogle.setOnClickListener { loginWithGoogle() }
        btnFacebook.setOnClickListener { loginWithFacebook() }
        btnTroubles.setOnClickListener { contactWithSupport() }

        btnPhone.setOnClickListener {
            val intent = Intent(this, Reg1PhoneFragment::class.java)
            startActivity(intent)
        }
    }
}