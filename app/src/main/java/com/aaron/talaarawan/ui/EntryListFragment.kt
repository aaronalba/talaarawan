package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaron.talaarawan.adapter.EntryAdapter
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

        // configure the app bar
        hideAppbar(requireActivity())
        removeTitle(requireActivity())

        // initialize the recycler view
        val adapter = EntryAdapter { entry ->
            // update selected fragment
            viewModel.updateSelectedEntry(entry)
            viewModel.setEditing(false)

            // navigate to detail fragment
            val action = EntryListFragmentDirections.actionEntryListFragmentToDetailFragment()
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        // observe the list of entries from the database and update the list accordingly
        viewModel.entryList.observe(viewLifecycleOwner) { entryList ->
            entryList?.let {
                if (it.isNotEmpty()) {
                    // show recycler view if the list is not empty
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.entries.visibility = View.VISIBLE
                    binding.addNote.visibility = View.GONE
                    binding.addNoteLabel.visibility = View.GONE
                } else {
                    // show empty list image
                    binding.recyclerView.visibility = View.GONE
                    binding.entries.visibility = View.GONE
                    binding.addNote.visibility = View.VISIBLE
                    binding.addNoteLabel.visibility = View.VISIBLE
                }

                // update adapter with the newly received list
                adapter.submitList(it)
            }
        }
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