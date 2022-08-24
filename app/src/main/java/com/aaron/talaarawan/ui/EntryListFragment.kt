package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaron.talaarawan.databinding.FragmentEntryListBinding
import com.aaron.talaarawan.util.hideAppbar
import com.aaron.talaarawan.util.removeTitle
import com.aaron.talaarawan.viewmodels.JournalViewModel
import com.aaron.talaarawan.viewmodels.JournalViewModelFactory

/**
 * The screen that shows a list of saved journal entries.
 */
class EntryListFragment : Fragment() {

    private val viewModel: JournalViewModel by activityViewModels {
        val application = requireActivity().application as JournalApplication
        JournalViewModelFactory(
            application.database.userDao(),
            application.database.entryDao()
        )
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAppbar(requireActivity())
        removeTitle(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Navigates to the screen for adding a new journal entry.
     */
    private fun addEntry() {
        val action = EntryListFragmentDirections.actionEntryListFragmentToDetailFragment()
        findNavController().navigate(action)
        viewModel.createNewEntry()
        viewModel.setEditing(true)
    }
}