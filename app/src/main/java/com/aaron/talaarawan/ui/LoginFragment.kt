package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aaron.talaarawan.JournalViewModel
import com.aaron.talaarawan.JournalViewModelFactory
import com.aaron.talaarawan.R
import com.aaron.talaarawan.data.Entry
import com.aaron.talaarawan.data.User
import com.aaron.talaarawan.databinding.FragmentLoginBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                    // show and set the username text view
                    val name: String = list[0].userFullName.split(" ").first()
                    username.visibility = View.VISIBLE
                    username.text = name

                    // greet user
                    header.text = getString(R.string.welcome_back)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**
     * Hides the app bar
     */
    private fun hideAppbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}