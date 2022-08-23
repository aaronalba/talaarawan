package com.aaron.talaarawan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaron.talaarawan.JournalViewModel
import com.aaron.talaarawan.JournalViewModelFactory
import com.aaron.talaarawan.data.Entry
import com.aaron.talaarawan.data.User
import com.aaron.talaarawan.databinding.FragmentLoginBinding
import com.aaron.talaarawan.viewmodels.LoginViewModel
import com.aaron.talaarawan.viewmodels.LoginViewModelFactory

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
        viewModel.allUsers.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                // not registered yet
                val action =
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            } else {
                // hide the loading indicator and show login field
                binding.apply {
                    progressBar.visibility = View.GONE
                    username.visibility = View.VISIBLE
                    pinInputLayout.visibility = View.VISIBLE
                    loginBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}