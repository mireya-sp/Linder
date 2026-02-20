package com.mireyaserrano.linder

import android.graphics.Color
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
import com.mireyaserrano.linder.data.Intent as UserIntent
import android.content.Intent as AndroidIntent

class Reg9PhotosFragment : Fragment(R.layout.fragment_reg9_photos) {

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

    private val photoUris = mutableListOf<Uri>()

    private lateinit var imageViews: List<ImageView>
    private lateinit var deleteButtons: List<ImageButton>
    private lateinit var btnFinish: MaterialButton

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            photoUris.add(uri)
            rearrangePhotos()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        btnFinish = view.findViewById(R.id.btn_finish)

        imageViews = listOf(
            view.findViewById(R.id.iv_photo_1),
            view.findViewById(R.id.iv_photo_2),
            view.findViewById(R.id.iv_photo_3),
            view.findViewById(R.id.iv_photo_4),
            view.findViewById(R.id.iv_photo_5),
            view.findViewById(R.id.iv_photo_6)
        )

        deleteButtons = listOf(
            view.findViewById(R.id.btn_delete_1),
            view.findViewById(R.id.btn_delete_2),
            view.findViewById(R.id.btn_delete_3),
            view.findViewById(R.id.btn_delete_4),
            view.findViewById(R.id.btn_delete_5),
            view.findViewById(R.id.btn_delete_6)
        )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (index >= photoUris.size && photoUris.size < 6) {
                    pickImageLauncher.launch("image/*")
                }
            }
        }

        deleteButtons.forEachIndexed { index, btnDelete ->
            btnDelete.setOnClickListener {
                if (index < photoUris.size) {
                    photoUris.removeAt(index)
                    rearrangePhotos()
                }
            }
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        btnFinish.setOnClickListener { completeRegistration() }

        rearrangePhotos()
    }

    private fun rearrangePhotos() {
        // Revertimos el padding a 35dp exactos para que la cámara no se deforme
        val paddingPx = (35 * resources.displayMetrics.density).toInt()

        for (i in 0 until 6) {
            val iv = imageViews[i]
            val btnDel = deleteButtons[i]

            if (i < photoUris.size) {
                iv.setImageURI(photoUris[i])
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                iv.imageTintList = null
                iv.setPadding(0, 0, 0, 0)
                btnDel.visibility = View.VISIBLE
            } else {
                iv.setImageResource(android.R.drawable.ic_menu_camera)
                // Usamos FIT_CENTER para recuperar tu diseño original
                iv.scaleType = ImageView.ScaleType.FIT_CENTER
                iv.imageTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#80FFFFFF"))
                iv.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
                btnDel.visibility = View.GONE
            }
        }

        updateFinishButton()
    }

    private fun updateFinishButton() {
        if (photoUris.isNotEmpty()) {
            btnFinish.isEnabled = true
            btnFinish.alpha = 1.0f
            btnFinish.setBackgroundColor(Color.parseColor("#CC99FF"))
            btnFinish.setTextColor(Color.WHITE)
        } else {
            btnFinish.isEnabled = false
            btnFinish.alpha = 0.5f
            btnFinish.setBackgroundColor(Color.parseColor("#C4C4C4"))
            btnFinish.setTextColor(Color.parseColor("#202124"))
        }
    }

    private fun completeRegistration() {
        val context = requireContext()

        val savedPhotoPaths = photoUris.mapNotNull { uri ->
            LocalDatabase.importImageToApp(context, uri)
        }

        if (savedPhotoPaths.isEmpty()) {
            Toast.makeText(context, "Por favor, añade al menos una foto", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val newUser = UserAccount(
                dniNumber = receivedDni,
                phoneNumber = receivedPhone,
                password = receivedPass,
                birthDate = receivedBirthDate,
                username = receivedUsername,
                intent = UserIntent.valueOf(receivedIntent),
                sexualOrientation = SexualOrientation.valueOf(receivedOrientation),
                habits = receivedHabits,
                distancePreferenceKm = receivedDistance,
                userPhotos = savedPhotoPaths.toMutableList()
            )

            LocalDatabase.saveUser(newUser)

            val nextScreen = AndroidIntent(requireContext(), MainActivity::class.java)
            nextScreen.flags = AndroidIntent.FLAG_ACTIVITY_NEW_TASK or AndroidIntent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(nextScreen)
            requireActivity().finish()

        } catch (e: Exception) {
            Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}