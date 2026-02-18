package com.mireyaserrano.linder.ui.auth

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mireyaserrano.linder.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

class Reg2DniFragment : Fragment(R.layout.fragment_reg2_dni) {

    // Variables de datos
    private var receivedPhone: String? = null
    private var receivedPass: String? = null

    // Datos extraídos
    private var extractedDni: String? = null
    private var extractedBirthDate: String? = null

    // Variable temporal para guardar la ruta de la foto HD
    private var latestTmpUri: Uri? = null

    // Vistas
    private lateinit var tvError: TextView
    private lateinit var btnNext: Button

    // --- PERMISOS ---
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                takePhotoHD()
            } else {
                showError("Es necesario el permiso de cámara para escanear el DNI")
            }
        }

    // --- LANZADORES ---

    // Galería (Ya te funciona bien)
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) analyzeImageFromUri(uri)
    }

    // Cámara HD (NUEVO: Devuelve un booleano, la imagen se guarda en latestTmpUri)
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && latestTmpUri != null) {
            // Reutilizamos la lógica de la galería porque ahora tenemos una URI de alta calidad
            analyzeImageFromUri(latestTmpUri!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            receivedPhone = it.getString("phone")
            receivedPass = it.getString("password")
        }

        // Inicializar vistas con tipos explícitos
        btnNext = view.findViewById<Button>(R.id.btn_next_dni)
        val btnUpload = view.findViewById<Button>(R.id.btn_upload_photo)
        val btnTake = view.findViewById<Button>(R.id.btn_take_photo)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        tvError = view.findViewById<TextView>(R.id.tv_error_dni)

        disableNextButton()

        // Listeners
        btnUpload.setOnClickListener {
            hideError()
            pickImageLauncher.launch("image/*")
        }

        btnTake.setOnClickListener {
            hideError()
            checkCameraPermissionAndLaunch()
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        btnNext.setOnClickListener {
            if (extractedDni != null && extractedBirthDate != null) {
                navigateToSelfie()
            }
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhotoHD()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // --- LÓGICA CÁMARA HD ---

    private fun takePhotoHD() {
        // 1. Crear un archivo temporal vacío en la caché de la app
        val tmpFile = File.createTempFile("dni_photo", ".jpg", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        // 2. Obtener la URI segura usando FileProvider
        latestTmpUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider", // Debe coincidir con el authorities del Manifest
            tmpFile
        )

        // 3. Lanzar la cámara pasándole esa URI para que guarde ahí la foto HD
        takePictureLauncher.launch(latestTmpUri)
    }

    // --- ANÁLISIS (Ahora siempre recibe URI) ---

    private fun analyzeImageFromUri(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(requireContext(), uri)
            processImageWithMLKit(image)
        } catch (e: Exception) {
            showError("Error al cargar la imagen")
        }
    }

    private fun processImageWithMLKit(image: InputImage) {
        Toast.makeText(requireContext(), "Analizando imagen HD...", Toast.LENGTH_SHORT).show()

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                validateData(visionText.text)
            }
            .addOnFailureListener {
                showError("No se ha podido leer el texto de la imagen")
            }
    }

    private fun validateData(fullText: String) {
        // 1. Regex DNI
        val dniPattern = Pattern.compile("\\b[0-9]{8}[-\\s]?[A-Z]\\b")
        val dniMatcher = dniPattern.matcher(fullText)

        var tempDni: String? = null
        if (dniMatcher.find()) {
            tempDni = dniMatcher.group(0)
        }

        if (tempDni == null) {
            showError("No se ha podido leer el DNI. Asegúrate de que haya buena luz.")
            disableNextButton()
            return
        }

        // 2. Regex Fechas
        val datePattern = Pattern.compile("\\b(\\d{2})[\\s/\\-\\.](\\d{2})[\\s/\\-\\.](\\d{4})\\b")
        val dateMatcher = datePattern.matcher(fullText)

        val foundDates = ArrayList<Date>()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        while (dateMatcher.find()) {
            val day = dateMatcher.group(1)
            val month = dateMatcher.group(2)
            val year = dateMatcher.group(3)
            try {
                val date = sdf.parse("$day/$month/$year")
                if (date != null) foundDates.add(date)
            } catch (e: Exception) { }
        }

        if (foundDates.isEmpty()) {
            showError("No se ha encontrado ninguna fecha válida.")
            disableNextButton()
            return
        }

        // 3. La fecha más antigua es la de nacimiento
        val birthDate = foundDates.minOrNull()

        if (birthDate == null) {
            showError("Error procesando las fechas.")
            return
        }

        // 4. Verificar edad
        if (isAdult(birthDate)) {
            extractedDni = tempDni
            extractedBirthDate = sdf.format(birthDate)
            hideError()
            enableNextButton()
            Toast.makeText(requireContext(), "Datos verificados correctamente.", Toast.LENGTH_SHORT).show()
        } else {
            showError("Debes ser mayor de 18 años para registrarte.")
            disableNextButton()
        }
    }

    private fun isAdult(birthDate: Date): Boolean {
        val dob = Calendar.getInstance().apply { time = birthDate }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age >= 18
    }

    // --- UI HELPERS ---

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
        disableNextButton()
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

    private fun navigateToSelfie() {
        val nextFragment = Reg3SelfieFragment()
        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", extractedDni)
            putString("birthDate", extractedBirthDate)
        }
        nextFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}