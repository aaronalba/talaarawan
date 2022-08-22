package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaron.talaarawan.databinding.FragmentRegisterBinding

/**
 * The register screen of the application. Shown during first start.
 */
class RegisterFragment : Fragment() {


    /**
     * View binding property used to access views and is valid only
     * between onCreateView() and onDestroyView()
     */
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}