package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaron.talaarawan.data.getDayOfWeek
import com.aaron.talaarawan.data.getFormattedDate
import com.aaron.talaarawan.databinding.FragmentEditBinding
import com.aaron.talaarawan.viewmodels.JournalViewModel
import com.aaron.talaarawan.viewmodels.JournalViewModelFactory

/**
 * The screen for updating the values of an entry.
 */
class EditFragment : Fragment() {

    private val viewModel: JournalViewModel by activityViewModels {
        val application = requireActivity().application as JournalApplication
        JournalViewModelFactory(
            application.database.userDao(),
            application.database.entryDao()
        )
    }

    /**
     * View binding property used to access views. Valid only between
     * onCreateView() and onDestroyView()
     */
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // observe the selected entry from the viewModel and update UI appropriately
        viewModel.entry.observe(viewLifecycleOwner) { entry ->
            binding.detailDate.text = entry.getFormattedDate()
            binding.detailDay.text = entry.getDayOfWeek()
            binding.titleInputEdit.setText(entry.entryTitle)
            binding.bodyInputEdit.setText(entry.entryBody)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}