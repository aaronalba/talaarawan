package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aaron.talaarawan.databinding.FragmentEntryListBinding

/**
 * The screen that shows a list of saved journal entries.
 */
class EntryListFragment : Fragment() {

    /**
     * View binding property used to access the views and is valid only
     * between onViewCreate() and onDestroyView()
     */
    private var _binding: FragmentEntryListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}