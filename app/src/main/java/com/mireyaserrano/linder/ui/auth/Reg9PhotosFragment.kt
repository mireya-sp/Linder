package com.mireyaserrano.linder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
// IMPORTANTE: Asegúrate de importar tu MainActivity
import com.mireyaserrano.linder.MainActivity

class Reg9PhotosFragment : Fragment() {

    private var hasPhoto = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg9_photos, container, false)

        // Referencia a los 6 espacios (puedes añadir los otros 5 siguiendo este esquema)
        val ivPhoto1 = view.findViewById<ImageView>(R.id.iv_photo_1)
        val btnFinish = view.findViewById<Button>(R.id.btn_finish)

        // Simulación: al pulsar la primera foto, habilitamos el botón
        ivPhoto1.setOnClickListener {
            hasPhoto = true
            // CORRECCIÓN: Usamos android.R.drawable para iconos del sistema
            ivPhoto1.setImageResource(android.R.drawable.ic_menu_gallery)
            ivPhoto1.setPadding(0,0,0,0) // Quitamos el padding para que la "foto" ocupe todo
            updateButtonState(btnFinish)
        }

        btnFinish.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            requireActivity().finish()
        }
        return view
    }

    private fun updateButtonState(btn: Button) {
        if (hasPhoto) {
            btn.alpha = 1.0f
            btn.isEnabled = true
        }
    }
}