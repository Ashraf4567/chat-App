package com.example.chatapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment

fun Fragment.showMessage(
    message: String,
    posActionName: String? = null,
    posAction: DialogInterface.OnClickListener? = null,
    nagActionName: String? = null,
    negAction: DialogInterface.OnClickListener? = null

): AlertDialog {
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setMessage(message)
    if (posActionName != null) {
        dialogBuilder.setPositiveButton(posActionName, posAction)
    }
    if (nagActionName != null) {
        dialogBuilder.setNegativeButton(nagActionName, negAction)
    }

    return dialogBuilder.show()
}

fun Activity.showMessage(
    message: String,
    posActionName: String? = null,
    posAction: OnDialogActionClick? = null,
    nagActionName: String? = null,
    negAction: OnDialogActionClick? = null,
    isCancelable: Boolean = true

): AlertDialog {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setMessage(message)
    if (posActionName != null) {
        dialogBuilder.setPositiveButton(
            posActionName
        ) { dialog, i ->
            dialog.dismiss()
            posAction?.onActionClick()
        }
    }
    if (nagActionName != null) {
        dialogBuilder.setNegativeButton(nagActionName) { dialog, id ->
            dialog.dismiss()
            negAction?.onActionClick()
        }
    }
    dialogBuilder.setCancelable(isCancelable)
    return dialogBuilder.show()
}

fun Activity.showLoadingProgressDialog(
    message: String,
    isCancelable: Boolean = true
): AlertDialog {
    val alertDialog = ProgressDialog(this)
    alertDialog.setMessage(message)
    alertDialog.setCancelable(isCancelable)
    return alertDialog
}