package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        // set click listener on the fab
        binding.addBtn.setOnClickListener { addEntry() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Navigates to the screen for adding a new journal entry.
     */
    private fun addEntry() {
        val action = EntryListFragmentDirections.actionEntryListFragmentToEditFragment()
        findNavController().navigate(action)
    }
}