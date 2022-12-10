package com.sena.mygamesapp.Dialogs

import android.app.AlertDialog
import android.content.Context

class CustomDialog(context: Context) : AlertDialog.Builder(context) {

    fun show(title: String, message: String) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()

        // Set other dialog properties
        alertDialog.setCancelable(true)

        alertDialog.show()

    }

}