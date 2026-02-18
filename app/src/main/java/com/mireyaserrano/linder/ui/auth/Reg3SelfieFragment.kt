package com.mireyaserrano.linder.ui.auth

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.Reg4NameFragment
import java.io.File

class Reg3SelfieFragment : Fragment(R.layout.fragment_reg3_selfie) {

    // Datos acumulados
    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null

    // URI temporal para la foto
    private var selfieUri: Uri? = null
    private var isFaceValid = false

    // Vistas
    private lateinit var ivPreview: ImageView
    private lateinit var tvError: TextView
    private lateinit var btnNext: Button
    private lateinit var btnTake: Button

    // Permisos
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) takeSelfieHD() else showError("Necesitamos la cámara para verificar tu identidad.")
        }

    // Lanzador Cámara
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && selfieUri != null) {
            ivPreview.setImageURI(selfieUri) // Mostrar foto en el círculo
            ivPreview.imageTintList = null // Quitar el tinte gris del placeholder
            detectFace(selfieUri!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar datos
        arguments?.let {
            receivedPhone = it.getString("phone")
            receivedPass = it.getString("password")
            receivedDni = it.getString("dni")
            receivedBirthDate = it.getString("birthDate")
        }

        // Inicializar vistas
        btnNext = view.findViewById(R.id.btn_next_selfie)
        btnTake = view.findViewById(R.id.btn_take_selfie)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        ivPreview = view.findViewById(R.id.iv_selfie_preview)
        tvError = view.findViewById(R.id.tv_error_selfie)

        disableNextButton()

        btnTake.setOnClickListener {
            hideError()
            checkPermissionAndTake()
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        btnNext.setOnClickListener {
            if (isFaceValid) {
                navigateToNextStep()
            }
        }
    }

    private fun checkPermissionAndTake() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takeSelfieHD()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun takeSelfieHD() {
        try {
            val tmpFile = File.createTempFile("selfie_photo", ".jpg", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            selfieUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                tmpFile
            )
            takePictureLauncher.launch(selfieUri)
        } catch (e: Exception) {
            showError("Error al iniciar la cámara.")
        }
    }

    private fun detectFace(uri: Uri) {
        Toast.makeText(requireContext(), "Verificando rostro...", Toast.LENGTH_SHORT).show()

        val image: InputImage
        try {
            image = InputImage.fromFilePath(requireContext(), uri)
        } catch (e: Exception) {
            showError("Error al procesar la imagen.")
            return
        }

        // Configuramos el detector para que sea preciso
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // Para detectar ojos abiertos/sonrisa
            .build()

        val detector =  FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    showError("No se ha detectado ningún rostro. Inténtalo de nuevo.")
                    isFaceValid = false
                    disableNextButton()
                } else if (faces.size > 1) {
                    showError("Hay más de una persona en la foto. Debes salir solo tú.")
                    isFaceValid = false
                    disableNextButton()
                } else {
                    // Solo hay 1 cara. Verificamos calidad
                    val face = faces[0]

                    // Probabilidad de ojos abiertos (0.0 a 1.0)
                    val leftEyeOpen = face.leftEyeOpenProbability ?: 0f
                    val rightEyeOpen = face.rightEyeOpenProbability ?: 0f

                    if (leftEyeOpen < 0.4 || rightEyeOpen < 0.4) {
                        showError("Parece que tienes los ojos cerrados. Ábrelos bien.")
                        isFaceValid = false
                        disableNextButton()
                    } else {
                        // ÉXITO
                        isFaceValid = true
                        hideError()
                        enableNextButton()
                        Toast.makeText(requireContext(), "Identidad verificada correctamente.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {
                showError("Error al analizar el rostro.")
            }
    }

    private fun navigateToNextStep() {
        // Creamos el siguiente fragmento (asegúrate de crear el archivo Reg4NameFragment vacío si no existe)
        val nextFragment = Reg4NameFragment()

        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", receivedDni)
            putString("birthDate", receivedBirthDate)
            // Aquí podríamos pasar la URI del selfie si la necesitamos para el perfil final
            putString("selfieUri", selfieUri.toString())
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }

    // --- UI HELPERS ---
    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvError.visibility = View.GONE
    }

    private fun enableNextButton() {
        btnNext.isEnabled = true
        btnNext.alpha = 1.0f
    }

    private fun disableNextButton() {
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f
    }
}