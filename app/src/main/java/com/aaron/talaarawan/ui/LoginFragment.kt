package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.aaron.talaarawan.R
import com.aaron.talaarawan.databinding.FragmentLoginBinding
import com.aaron.talaarawan.util.clearErrorAfterTypeWatcher
import com.aaron.talaarawan.viewmodels.LoginViewModel
import com.aaron.talaarawan.viewmodels.LoginViewModelFactory
import kotlinx.coroutines.launch

/**
 * The login screen of the application.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(
            (activity?.application as JournalApplication).database.userDao()
        )
    }

    /**
     * View binding property to access the views and is valid only
     * between onCreateView() and onDestroyView().
     */
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideAppbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener { login() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // create watcher that will remove any errors in the pin field when the user starts typing
        binding.pinInputEditText.addTextChangedListener(
            clearErrorAfterTypeWatcher(binding.pinInputLayout)
        )

        // asynchronously load the list of users
        viewLifecycleOwner.lifecycleScope.launch {
            val list = viewModel.getUsers()
            if (list.isEmpty()) {
                // not yet registered, launch register fragment
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            } else {
                // a user is detected, Note: currently supports only one user
                binding.apply {
                    // hide the loading indicator
                    progressBar.visibility = View.GONE

                    // show login field
                    pinInputLayout.visibility = View.VISIBLE
                    loginBtn.visibility = View.VISIBLE

                    // greet user
                    val name: String = list[0].userFullName.split(" ").first()
                    header.text = getString(R.string.hello, name)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Attempts to login to the application with the given pin
     */
    private fun login() {
        viewLifecycleOwner.lifecycleScope.launch {
            val users = viewModel.getUsers()
            if (users.first().userPin == binding.pinInputEditText.text.toString()) {
                // go to list of entries if pin is correct
                val action = LoginFragmentDirections.actionLoginFragmentToEntryListFragment()
                findNavController().navigate(action)
            } else {
                binding.pinInputLayout.error = getString(R.string.incorrect_pin)
            }
        }
    }


    /**
     * Hides the app bar
     */
    private fun hideAppbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}