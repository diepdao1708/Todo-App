package com.android.diepdao1708.todo4.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


//https://stackoverflow.com/questions/22830675/hide-soft-keyboard-on-activity-where-no-keyboard-operation-found
fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}