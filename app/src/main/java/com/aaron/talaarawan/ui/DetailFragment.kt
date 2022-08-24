package com.aaron.talaarawan.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aaron.talaarawan.R
import com.aaron.talaarawan.data.getDayOfWeek
import com.aaron.talaarawan.data.getFormattedDate
import com.aaron.talaarawan.databinding.FragmentDetailBinding
import com.aaron.talaarawan.util.removeTitle
import com.aaron.talaarawan.util.showAppbar
import com.aaron.talaarawan.viewmodels.JournalViewModel
import com.aaron.talaarawan.viewmodels.JournalViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * The screen for updating the values of an entry.
 */
class DetailFragment : Fragment() {

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
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // observe the selected entry from the viewModel and update UI appropriately
        viewModel.entry.observe(viewLifecycleOwner) { entry ->
            binding.detailDate.text = entry.getFormattedDate()
            binding.detailDay.text = entry.getDayOfWeek()
            binding.detailTitle.text = entry.entryTitle
            binding.detailBody.text = entry.entryBody
            binding.titleInputEdit.setText(entry.entryTitle)
            binding.bodyInputEdit.setText(entry.entryBody)
        }

        // dynamically change the UI based on the editing mode
        viewModel.isEditing.observe(viewLifecycleOwner) { isEditing ->
            if (isEditing) {
                binding.titleInputLayout.visibility = View.VISIBLE
                binding.bodyInputLayout.visibility = View.VISIBLE
                binding.detailTitle.visibility = View.INVISIBLE
                binding.detailBodyHolder.visibility = View.INVISIBLE
            } else {
                binding.titleInputLayout.visibility = View.INVISIBLE
                binding.bodyInputLayout.visibility = View.INVISIBLE
                binding.detailTitle.visibility = View.VISIBLE
                binding.detailBodyHolder.visibility = View.VISIBLE
            }
        }

        // initialize the menu
        initializeMenu()

        // initialize the app bar
        showAppbar(requireActivity())
        removeTitle(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Shows a snackbar with the given message. Use it only when the view is inflated.
     */
    private fun showSnackbar(message: String, duration: Int = 1000) {
        Snackbar.make(binding.root, message, duration).show()
    }

    /**
     * Initializes the menu managed by the hosting activity.
     */
    private fun initializeMenu() {
        val menuProvider = object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
                viewModel.isEditing.value?.let { isEditing ->
                    if (isEditing) {
                        menu.findItem(R.id.action_done).isVisible = true
                        menu.findItem(R.id.action_delete).isVisible = false
                        menu.findItem(R.id.action_edit).isVisible = false
                    } else {
                        menu.findItem(R.id.action_done).isVisible = false
                        menu.findItem(R.id.action_delete).isVisible = true
                        menu.findItem(R.id.action_edit).isVisible = true
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.action_done -> {
                        // Save the entry and close editing mode.
                        showSnackbar(getString(R.string.entry_saved))
                        viewModel.setEditing(false)

                        // save the newly edited entry to the database
                        viewModel.saveEntry(
                            binding.titleInputEdit.text.toString(),
                            binding.bodyInputEdit.text.toString()
                        )
                    }
                    R.id.action_delete -> {
                        // delete the entry from the database
                        lifecycleScope.launch {
                            viewModel.deleteEntry()

                            showSnackbar(
                                "Entry ${viewModel.entry.value?.entryTitle} deleted!",
                                Snackbar.LENGTH_SHORT
                            )

                            // navigate back to the list
                            viewModel.setEditing(false)
                            findNavController().navigateUp()
                        }
                    }
                    R.id.action_edit -> {
                        showSnackbar(getString(R.string.edit_mode))
                        viewModel.setEditing(true)
                    }
                }
                requireActivity().invalidateMenu()  // update the menu items
                return true
            }
        }
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }
}