package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.databinding.FragmentEditSexualOrientationBinding

class EditOrientationFragment : Fragment(R.layout.fragment_edit_sexual_orientation) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEditSexualOrientationBinding.bind(view)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }
}