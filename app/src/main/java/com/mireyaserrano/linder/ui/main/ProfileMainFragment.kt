package com.mireyaserrano.linder.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import java.text.SimpleDateFormat
import java.util.*

class ProfileMainFragment : Fragment(R.layout.fragment_profile_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configuración de la Top Bar centralizada
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        // 2. Vincular las vistas del XML
        val imgProfilePhoto = view.findViewById<ShapeableImageView>(R.id.imgProfilePhoto)
        val tvProfileName = view.findViewById<TextView>(R.id.tvProfileName)
        val btnVerify = view.findViewById<ImageButton>(R.id.btnVerify)
        val llManageDouble = view.findViewById<View>(R.id.ll_manage_double)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizar)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile) // ¡AÑADIDO!

        // 3. Cargar datos del usuario
        val currentUser = LocalDatabase.getCurrentUser()

        if (currentUser != null) {
            // Cargar foto principal (la primera de la lista)
            val firstPhoto = currentUser.userPhotos.firstOrNull()
            if (firstPhoto != null) {
                Glide.with(this)
                    .load(firstPhoto)
                    .placeholder(R.drawable.user_thumb)
                    .centerCrop()
                    .into(imgProfilePhoto)
            }

            // Calcular edad y mostrar nombre
            val age = calculateAgeFromDate(currentUser.birthDate.toString())
            tvProfileName.text = "${currentUser.username}, $age"

            // Lógica de Verificación (Icono y Click)
            if (currentUser.isVerified) {
                btnVerify.setImageResource(R.drawable.ic_verified)
                btnVerify.setOnClickListener(null)
            } else {
                btnVerify.setImageResource(R.drawable.ic_non_verified)
                btnVerify.setOnClickListener {
                    showVerificationDialog()
                }
            }
        }

        // 4. Lógica de Navegación a otras pantallas (¡YA ACTIVADAS!)

        btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), com.mireyaserrano.linder.ui.edit.EditProfileActivity::class.java)
            startActivity(intent)
        }

        llManageDouble.setOnClickListener {
            val intent = Intent(requireContext(), com.mireyaserrano.linder.ui.edit.ManageDoubleActivity::class.java)
            startActivity(intent)
        }

        btnActualizar.setOnClickListener {
            val intent = Intent(requireContext(), com.mireyaserrano.linder.ui.edit.SubscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Calcula la edad a partir de un string de fecha (ej: "15/05/2000").
     */
    private fun calculateAgeFromDate(birthDateStr: String): Int {
        if (birthDateStr.isEmpty()) return 0
        try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate = format.parse(birthDateStr) ?: return 0

            val today = Calendar.getInstance()
            val dob = Calendar.getInstance()
            dob.time = birthDate

            var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return age
        } catch (e: Exception) {
            return 0
        }
    }

    /**
     * Muestra el diálogo para verificar las imágenes.
     */
    private fun showVerificationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_verify_images, null)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}