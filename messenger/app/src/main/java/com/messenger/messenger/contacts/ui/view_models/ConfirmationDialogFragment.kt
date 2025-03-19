package com.messenger.messenger.contacts.ui.view_models

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.messenger.messenger.R

class ConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> }
            .create()
}