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
import com.mireyaserrano.linder.data.SubscriptionType
import com.mireyaserrano.linder.ui.edit.SubscriptionActivity
import java.text.SimpleDateFormat
import java.util.*

class ProfileMainFragment : Fragment(R.layout.fragment_profile_main) {

    private lateinit var imgProfilePhoto: ShapeableImageView
    private lateinit var tvProfileName: TextView
    private lateinit var tvVipBadge: TextView
    private lateinit var btnVerify: ImageButton
    private lateinit var btnActualizar: Button
    private lateinit var tvSubscriptionInfo: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        imgProfilePhoto = view.findViewById(R.id.imgProfilePhoto)
        tvProfileName = view.findViewById(R.id.tvProfileName)
        tvVipBadge = view.findViewById(R.id.tvVipBadge)
        btnVerify = view.findViewById(R.id.btnVerify)
        btnActualizar = view.findViewById(R.id.btnActualizar)
        tvSubscriptionInfo = view.findViewById(R.id.tvSubscriptionInfo)

        val llManageDouble = view.findViewById<View>(R.id.ll_manage_double)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)

        btnEditProfile.setOnClickListener {
            // val intent = Intent(requireContext(), com.mireyaserrano.linder.ui.edit.EditProfileActivity::class.java)
            // startActivity(intent)
        }

        llManageDouble.setOnClickListener {
            // val intent = Intent(requireContext(), com.mireyaserrano.linder.ui.edit.ManageDoubleActivity::class.java)
            // startActivity(intent)
        }

        btnActualizar.setOnClickListener {
            val intent = Intent(requireContext(), SubscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshProfileData()
    }

    private fun refreshProfileData() {
        val currentUser = LocalDatabase.getCurrentUser() ?: return

        val firstPhoto = currentUser.userPhotos.firstOrNull()
        if (firstPhoto != null) {
            Glide.with(this)
                .load(firstPhoto)
                .placeholder(R.drawable.user_thumb)
                .centerCrop()
                .into(imgProfilePhoto)
        }

        val age = calculateAgeFromDate(currentUser.birthDate.toString())
        tvProfileName.text = "${currentUser.username}, $age"

        if (currentUser.isVerified) {
            btnVerify.setImageResource(R.drawable.ic_verified)
            btnVerify.setOnClickListener(null)
        } else {
            btnVerify.setImageResource(R.drawable.ic_non_verified)
            btnVerify.setOnClickListener { showVerificationDialog() }
        }

        val now = System.currentTimeMillis()
        if (currentUser.subscriptionEndDate > now) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateStr = sdf.format(Date(currentUser.subscriptionEndDate))

            val typeStr = when(currentUser.subscriptionType) {
                SubscriptionType.WEEKLY -> "PLUS Semanal"
                SubscriptionType.MONTHLY -> "PLUS Mensual"
                SubscriptionType.YEARLY -> "PLUG anal " + "🤫"
                else -> "PLUS"
            }

            tvVipBadge.text = typeStr
            tvVipBadge.visibility = View.VISIBLE

            tvSubscriptionInfo.text = "Tu suscripción acaba el $dateStr"
            tvSubscriptionInfo.visibility = View.VISIBLE
            btnActualizar.text = "Ampliar Plan"
        } else {
            tvVipBadge.visibility = View.GONE
            tvSubscriptionInfo.visibility = View.GONE
            btnActualizar.text = "Actualizar"
        }
    }

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

    private fun showVerificationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_verify_images, null)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}