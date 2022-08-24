package com.aaron.talaarawan.util

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity


/**
 * Shows the app bar of the activity.
 */
fun showAppbar(activity: Activity) {
    (activity as AppCompatActivity).supportActionBar?.show()
}

/**
 * Hides the app bar of the activity.
 */
fun hideAppbar(activity: Activity) {
    (activity as AppCompatActivity).supportActionBar?.hide()
}

/**
 * Removes the title that is shown in the app bar.
 */
fun removeTitle(activity: Activity) {
    (activity as AppCompatActivity).supportActionBar?.title = ""
}