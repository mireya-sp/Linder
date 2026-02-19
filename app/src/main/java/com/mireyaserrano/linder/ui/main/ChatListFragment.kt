package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos la Top Bar en modo "Otras pantallas"
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        // Aquí irá tu RecyclerView con los chats en el futuro
    }
}