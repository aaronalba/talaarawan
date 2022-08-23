package com.aaron.talaarawan.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaron.talaarawan.R
import com.aaron.talaarawan.RegisterViewModel
import com.aaron.talaarawan.RegisterViewModelFactory
import com.aaron.talaarawan.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * The register screen of the application. Shown during first start.
 */
class RegisterFragment : Fragment() {

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(
            (activity?.application as JournalApplication).database.userDao()
        )
    }

    /**
     * View binding property used to access views and is valid only
     * between onCreateView() and onDestroyView()
     */
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureBackPressCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // register click listener
        binding.registerBtn.setOnClickListener { registerUser() }

        /**
         * input field error watcher.
         * removes the shown error when the user types on the field
         */
        binding.apply {
            nameInputEdit.addTextChangedListener(
                clearErrorAfterTypeWatcher(nameInputLayout)
            )
            confirmInputEdit.addTextChangedListener(
                clearErrorAfterTypeWatcher(confirmInputLayout)
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Registers the entered user to the app's database
     */
    private fun registerUser() {
        val fullName = binding.nameInputEdit.text.toString()
        if (registerViewModel.isUserValid(fullName)) {
            val pin = binding.pinInputEdit.text.toString()
            val pin2 = binding.confirmInputEdit.text.toString()

            if (registerViewModel.isPinValid(pin, pin2)) {
                // save the user to the database
                registerViewModel.registerUser(fullName, pin)
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            } else {
                // pins do not match
                binding.confirmInputLayout.error = getString(R.string.pins_do_not_match)
            }
        } else {
            // name field is empty
            binding.nameInputLayout.error = getString(R.string.please_enter_your_name)
        }
    }

    /**
     * Adds a custom back press callback on the activity.
     */
    private fun configureBackPressCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            // close the app when the back button is pressed
            requireActivity().finish()
        }
    }

    /**
     * Clears the error of an EditText when the user starts typing on the field.
     * @return The TextWatcher that will clear errors on the field
     */
    private fun clearErrorAfterTypeWatcher(
        layout: TextInputLayout,
    ): TextWatcher =
        object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                layout.error = null
            }
        }
}