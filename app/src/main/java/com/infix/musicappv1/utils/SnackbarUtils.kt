package com.infix.musicappv1.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    fun showBaseSnackbar(view: View, msg: String, duration: Int) {
        Snackbar.make(
            view,
            msg,
            duration
        ).show()
    }

    fun showSnackbarWithAction(view: View, msg: String, titleAction: String, duration: Int, action: ()-> Unit) {
        Snackbar.make(
            view,
            msg,
            duration
        )
            .setAction(titleAction) {action.invoke()}
            .show()
    }
}