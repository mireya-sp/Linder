package com.mireyaserrano.linder

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.SexualOrientation
import com.mireyaserrano.linder.data.UserAccount
// ALIAS IMPORTANTE: Usamos 'UserIntent' para referirnos a TU enum de datos
import com.mireyaserrano.linder.data.Intent as UserIntent
// ALIAS IMPORTANTE: Usamos 'AndroidIntent' para navegar entre pantallas
import android.content.Intent as AndroidIntent

class Reg9PhotosFragment : Fragment(R.layout.fragment_reg9_photos) {

    // Datos recibidos
    private var receivedPhone: String = ""
    private var receivedPass: String = ""
    private var receivedDni: String = ""
    private var receivedBirthDate: String = ""
    private var receivedSelfieUri: String? = null
    private var receivedUsername: String = ""
    private var receivedOrientation: String = ""
    private var receivedIntent: String = ""
    private var receivedDistance: Int = 10
    private var receivedHabits: String = ""

    // Fotos
    private val photoUris = arrayOfNulls<Uri>(6)
    private lateinit var imageViews: List<ImageView>
    private var targetSlotIndex: Int = -1

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null && targetSlotIndex != -1) {
            savePhotoInSlot(uri, targetSlotIndex)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar argumentos
        arguments?.let {
            receivedPhone = it.getString("phone") ?: ""
            receivedPass = it.getString("password") ?: ""
            receivedDni = it.getString("dni") ?: ""
            receivedBirthDate = it.getString("birthDate") ?: ""
            receivedSelfieUri = it.getString("selfieUri")
            receivedUsername = it.getString("username") ?: ""
            receivedOrientation = it.getString("sexualOrientation") ?: ""
            receivedIntent = it.getString("userIntent") ?: ""
            receivedDistance = it.getInt("distancePreference", 10)
            receivedHabits = it.getString("userHabits") ?: ""
        }

        // Vistas
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val btnFinish = view.findViewById<MaterialButton>(R.id.btn_finish)

        imageViews = listOf(
            view.findViewById(R.id.iv_photo_1),
            view.findViewById(R.id.iv_photo_2),
            view.findViewById(R.id.iv_photo_3),
            view.findViewById(R.id.iv_photo_4),
            view.findViewById(R.id.iv_photo_5),
            view.findViewById(R.id.iv_photo_6)
        )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener { handlePhotoClick(index) }
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        btnFinish.setOnClickListener { completeRegistration() }
    }

    private fun handlePhotoClick(clickedIndex: Int) {
        if (photoUris[clickedIndex] != null) {
            targetSlotIndex = clickedIndex
            pickImageLauncher.launch("image/*")
        } else {
            val firstEmptyIndex = photoUris.indexOfFirst { it == null }
            if (firstEmptyIndex != -1) {
                targetSlotIndex = firstEmptyIndex
                pickImageLauncher.launch("image/*")
            } else {
                Toast.makeText(requireContext(), "Máximo 6 fotos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePhotoInSlot(uri: Uri, index: Int) {
        photoUris[index] = uri
        val imageView = imageViews[index]
        imageView.setImageURI(uri)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.imageTintList = null
        imageView.setPadding(0, 0, 0, 0)

        checkFinishButton()
    }

    private fun checkFinishButton() {
        val hasPhotos = photoUris.any { it != null }
        val btnFinish = view?.findViewById<MaterialButton>(R.id.btn_finish)
        btnFinish?.isEnabled = hasPhotos
        btnFinish?.alpha = if (hasPhotos) 1.0f else 0.5f
    }

    private fun completeRegistration() {
        // Convertimos las URIs a Strings para guardarlas
        val finalPhotos = photoUris.filterNotNull().map { it.toString() }.toMutableList()

        try {
            // Creamos el usuario usando los nombres EXACTOS de tu UserAccount
            val newUser = UserAccount(
                dniNumber = receivedDni,
                phoneNumber = receivedPhone,
                password = receivedPass,
                birthDate = receivedBirthDate,
                username = receivedUsername,
                sexualOrientation = SexualOrientation.valueOf(receivedOrientation), // String a Enum
                intent = UserIntent.valueOf(receivedIntent), // String a Enum
                habits = receivedHabits,
                distancePreferenceKm = receivedDistance,
                userPhotos = finalPhotos,
                // gender = ... (si lo pedimos antes, si no, null por defecto)
                // subscription = ... (por defecto ya es NINGUNA)
            )

            // Guardar en BD (persistencia en disco)
            LocalDatabase.saveUser(newUser)

            Toast.makeText(requireContext(), "¡Bienvenido a Linder!", Toast.LENGTH_LONG).show()

            // Navegar a Main usando el alias 'AndroidIntent'
            val intent = AndroidIntent(requireContext(), MainActivity::class.java)
            intent.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK or AndroidIntent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}