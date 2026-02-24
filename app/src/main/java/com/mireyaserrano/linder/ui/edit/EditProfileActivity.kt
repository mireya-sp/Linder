package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.Intent // Tu enum
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.SexualOrientation // Tu enum

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etName: EditText

    private lateinit var tvLookingForValue: TextView
    private lateinit var tvAboutMeValue: TextView
    private lateinit var tvSexualOrientationValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Inicializar vistas
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        etEmail = findViewById(R.id.etEmail)
        etName = findViewById(R.id.etName)
        tvLookingForValue = findViewById(R.id.tvLookingForValue)
        tvAboutMeValue = findViewById(R.id.tvAboutMeValue)
        tvSexualOrientationValue = findViewById(R.id.tvSexualOrientationValue)

        val btnImages = findViewById<RelativeLayout>(R.id.btnImages)
        val btnLookingFor = findViewById<RelativeLayout>(R.id.btnLookingFor)
        val btnAboutMe = findViewById<RelativeLayout>(R.id.btnAboutMe)
        val btnSexualOrientation = findViewById<RelativeLayout>(R.id.btnSexualOrientation)

        // Botón atrás
        btnBack.setOnClickListener { finish() }

        // Navegación a las sub-pantallas
        btnImages.setOnClickListener {
            // Ejemplo de navegación si las conviertes a Activities:
            // val intent = android.content.Intent(this, EditPhotosActivity::class.java)
            // startActivity(intent)
        }

        btnLookingFor.setOnClickListener {
            // Aquí llamarías a tu pantalla de fragment_edit_searching.xml
        }

        btnAboutMe.setOnClickListener {
            // Aquí llamarías a tu pantalla de fragment_edit_habits.xml
        }

        btnSexualOrientation.setOnClickListener {
            // Aquí llamarías a tu pantalla de fragment_edit_sexual_orientation.xml
        }
    }

    // Usamos onResume para cargar/refrescar los datos siempre que la pantalla sea visible
    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    // Usamos onPause para guardar silenciosamente el Nombre y Email al salir
    override fun onPause() {
        super.onPause()
        saveBaseProfileData()
    }

    private fun loadUserProfile() {
        val currentUser = LocalDatabase.getCurrentUser() ?: return

        etEmail.setText(currentUser.email)
        etName.setText(currentUser.username)

        // Formatear el texto de Hábitos / Sobre mí
        tvAboutMeValue.text = if (currentUser.habits.isNullOrEmpty()) {
            "Cuéntales algo sobre ti..."
        } else {
            currentUser.habits
        }

        // Traducir el Enum de Intent a texto legible
        tvLookingForValue.text = when(currentUser.intent) {
            Intent.RELACION_SERIA -> "Una relación seria"
            Intent.ROLLO_UNA_NOCHE -> "Un rollo de una noche"
            Intent.HACER_AMIGAS -> "Hacer amigas"
            else -> "No especificado"
        }

        // Traducir el Enum de Orientación Sexual a texto legible
        tvSexualOrientationValue.text = when(currentUser.sexualOrientation) {
            SexualOrientation.LESBIANA -> "Lesbiana"
            SexualOrientation.BISEXUAL -> "Bisexual"
            SexualOrientation.ASEXUAL -> "Asexual"
            else -> "No especificado"
        }
    }

    private fun saveBaseProfileData() {
        val currentUser = LocalDatabase.getCurrentUser() ?: return

        currentUser.email = etEmail.text.toString().trim()
        currentUser.username = etName.text.toString().trim()

        LocalDatabase.updateUser(currentUser)
    }
}