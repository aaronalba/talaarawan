package com.aaron.talaarawan.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

/**
 * Clears the error of an EditText when the user starts typing on the field.
 * @return The TextWatcher that will clear errors on the field
 */
fun clearErrorAfterTypeWatcher(layout: TextInputLayout): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            layout.error = null
        }
    }
}
