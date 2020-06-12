package ru.delivery.system.android.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

fun snackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}