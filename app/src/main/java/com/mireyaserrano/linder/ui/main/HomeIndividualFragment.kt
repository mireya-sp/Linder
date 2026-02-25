package com.mireyaserrano.linder.ui.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.Intent
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.UserAccount
import kotlin.math.abs

class HomeIndividualFragment : Fragment() {

    private var usersList: List<UserAccount> = emptyList()
    private var currentUserIndex: Int = 0
    private var currentPhotoIndex: Int = 0
    private val historyStack = mutableListOf<Int>()

    private lateinit var swipeableCard: View
    private lateinit var ivMainPhoto: ImageView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileBio: TextView
    private lateinit var llPhotoIndicators: LinearLayout
    private lateinit var btnRewind: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_individual, container, false)

        TopBarManager.setup(this, view, TopBarManager.ScreenType.HOME_INDIVIDUAL)

        swipeableCard = view.findViewById(R.id.swipeable_card)
        ivMainPhoto = view.findViewById(R.id.iv_main_photo)
        tvProfileName = view.findViewById(R.id.tv_profile_name)
        tvProfileBio = view.findViewById(R.id.tv_profile_bio)
        llPhotoIndicators = view.findViewById(R.id.ll_photo_indicators)
        btnRewind = view.findViewById(R.id.btn_rewind)

        val btnLike = view.findViewById<ImageButton>(R.id.btn_like)
        val btnDislike = view.findViewById<ImageButton>(R.id.btn_dislike)

        setupTouchAndSwipeListeners()
        loadUsers()

        btnLike.setOnClickListener { animateCardOffScreen(true) }
        btnDislike.setOnClickListener { animateCardOffScreen(false) }
        btnRewind.setOnClickListener { processRewind() }

        return view
    }

    private fun loadUsers() {
        val loggedInUser = LocalDatabase.getCurrentUser() ?: return
        val allUsersMap = LocalDatabase.getAllUsers()

        // 1. Obtener el filtro enviado desde ExploreFragment
        val categoryFilter = arguments?.getString("FILTER_CATEGORY")

        // 2. Mapear el texto al enum Intent
        val intentFilter = when (categoryFilter) {
            "Relación estable" -> Intent.RELACION_SERIA
            "Libre esta noche" -> Intent.ROLLO_UNA_NOCHE
            "Hacer amigos"     -> Intent.HACER_AMIGAS
            else -> null
        }

        usersList = allUsersMap.values.filter { targetUser ->
            val isNotMe = targetUser.phoneNumber != loggedInUser.phoneNumber
            val isNotAMatch = !loggedInUser.matches.contains(targetUser.phoneNumber)
            val isNotAnActiveChat = !loggedInUser.activeChats.contains(targetUser.phoneNumber)

            // 3. Aplicar el filtro de intención si existe
            val matchesIntent = if (intentFilter != null) {
                targetUser.intent == intentFilter
            } else {
                true // Si no hay filtro, pasan todos
            }

            isNotMe && isNotAMatch && isNotAnActiveChat && matchesIntent
        }

        currentUserIndex = 0
        historyStack.clear()

        showCurrentUser()
    }

    private fun showCurrentUser() {
        swipeableCard.translationX = 0f
        swipeableCard.translationY = 0f
        swipeableCard.rotation = 0f
        swipeableCard.alpha = 1f

        if (currentUserIndex >= usersList.size) {
            val categoryFilter = arguments?.getString("FILTER_CATEGORY")

            if (categoryFilter != null) {
                tvProfileName.text = "No hay resultados"
                tvProfileBio.text = "Nadie busca '$categoryFilter' cerca de ti por ahora."
            } else {
                tvProfileName.text = "No hay más usuarios"
                tvProfileBio.text = "Vuelve más tarde para descubrir gente nueva."
            }

            ivMainPhoto.setImageResource(R.drawable.profile_placeholder)
            llPhotoIndicators.removeAllViews()
            swipeableCard.setOnTouchListener(null)
            return
        }

        val user = usersList[currentUserIndex]
        currentPhotoIndex = 0

        tvProfileName.text = user.username
        tvProfileBio.text = user.habits

        updatePhotoView(user)
        updateRewindButton()
    }

    private fun updatePhotoView(user: UserAccount) {
        val photos = user.userPhotos

        if (photos.isNotEmpty() && currentPhotoIndex < photos.size) {
            val uriString = photos[currentPhotoIndex]
            Glide.with(this).load(Uri.parse(uriString)).into(ivMainPhoto)
        } else {
            ivMainPhoto.setImageResource(R.drawable.profile_placeholder)
        }

        llPhotoIndicators.removeAllViews()
        val dotSize = (8 * resources.displayMetrics.density).toInt()
        val margin = (4 * resources.displayMetrics.density).toInt()

        for (i in photos.indices) {
            val dot = ImageView(requireContext())
            val params = LinearLayout.LayoutParams(dotSize, dotSize)
            params.setMargins(margin, 0, margin, 0)
            dot.layoutParams = params

            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            if (i == currentPhotoIndex) {
                shape.setColor(Color.WHITE)
            } else {
                shape.setColor(Color.TRANSPARENT)
                shape.setStroke(2, Color.WHITE)
            }
            dot.background = shape

            llPhotoIndicators.addView(dot)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchAndSwipeListeners() {
        var startX = 0f
        var startY = 0f
        val swipeThreshold = 300f

        swipeableCard.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    startY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - startX
                    val deltaY = event.rawY - startY
                    v.translationX = deltaX
                    v.translationY = deltaY

                    v.rotation = deltaX * 0.05f
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - startX
                    val deltaY = event.rawY - startY

                    if (deltaX > swipeThreshold) {
                        animateCardOffScreen(isLike = true)
                    } else if (deltaX < -swipeThreshold) {
                        animateCardOffScreen(isLike = false)
                    } else if (abs(deltaX) < 15f && abs(deltaY) < 15f) {
                        handleTapToChangePhoto(event.x, v.width)
                        resetCardPosition()
                    } else {
                        resetCardPosition()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun handleTapToChangePhoto(tapX: Float, viewWidth: Int) {
        val user = usersList.getOrNull(currentUserIndex) ?: return
        if (user.userPhotos.isEmpty()) return

        if (tapX < viewWidth / 2) {
            if (currentPhotoIndex > 0) {
                currentPhotoIndex--
                updatePhotoView(user)
            }
        } else {
            if (currentPhotoIndex < user.userPhotos.size - 1) {
                currentPhotoIndex++
                updatePhotoView(user)
            }
        }
    }

    private fun resetCardPosition() {
        swipeableCard.animate()
            .translationX(0f)
            .translationY(0f)
            .rotation(0f)
            .setDuration(300)
            .start()
    }

    private fun animateCardOffScreen(isLike: Boolean) {
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val targetX = if (isLike) screenWidth else -screenWidth
        val targetRotation = if (isLike) 20f else -20f

        swipeableCard.animate()
            .translationX(targetX)
            .rotation(targetRotation)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                if (isLike) processLike() else processPass()
            }
            .start()
    }

    private fun processLike() {
        if (currentUserIndex >= usersList.size) return

        val loggedInUser = LocalDatabase.getCurrentUser() ?: return
        val likedUser = usersList[currentUserIndex]

        if (loggedInUser.likedByUsers.contains(likedUser.phoneNumber)) {
            loggedInUser.matches.add(likedUser.phoneNumber ?: "")
            likedUser.matches.add(loggedInUser.phoneNumber ?: "")

            LocalDatabase.updateUser(loggedInUser)
            LocalDatabase.updateUser(likedUser)

            showMatchDialog(likedUser)
        } else {
            likedUser.likedByUsers.add(loggedInUser.phoneNumber ?: "")

            LocalDatabase.updateUser(likedUser)

            Toast.makeText(requireContext(), "¡Le has dado Like a ${likedUser.username}!", Toast.LENGTH_SHORT).show()
            moveToNextUser()
        }
    }

    private fun showMatchDialog(matchedUser: UserAccount) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())

        builder.setTitle("¡Es un Match!")
        builder.setMessage("Tú y ${matchedUser.username} os habéis gustado.")

        builder.setPositiveButton("Iniciar Chat") { dialog, _ ->
            dialog.dismiss()

            moveToNextUser()

            matchedUser.phoneNumber?.let { phone ->
                val chatFragment = ChatFragment.newInstance(phone)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        builder.setNegativeButton("Seguir deslizando") { dialog, _ ->
            dialog.dismiss()
            moveToNextUser()
        }

        builder.setCancelable(false)
        builder.show()
    }
    private fun processPass() {
        if (currentUserIndex >= usersList.size) return

        val passedUser = usersList[currentUserIndex]
        Toast.makeText(requireContext(), "Has pasado de ${passedUser.username}", Toast.LENGTH_SHORT).show()

        moveToNextUser()
    }

    private fun moveToNextUser() {
        historyStack.add(currentUserIndex)
        currentUserIndex++
        showCurrentUser()
    }

    private fun processRewind() {
        if (historyStack.isNotEmpty()) {
            currentUserIndex = historyStack.removeAt(historyStack.size - 1)
            showCurrentUser()
        }
    }

    private fun updateRewindButton() {
        if (historyStack.isNotEmpty()) {
            btnRewind.alpha = 1.0f
            btnRewind.isEnabled = true
        } else {
            btnRewind.alpha = 0.5f
            btnRewind.isEnabled = false
        }
    }


}