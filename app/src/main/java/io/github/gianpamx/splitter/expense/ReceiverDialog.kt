package io.github.gianpamx.splitter.expense

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import io.github.gianpamx.splitter.R

private const val RECEIVER_MODEL = "ReceiverModel"

class ReceiverDialog : DialogFragment() {
    private var listener: Listener? = null

    private var receiverModel: ReceiverModel? = null

    val view by lazy {
        (View.inflate(activity, R.layout.expense_receiver_dialog, null) as ViewGroup).apply {
            findViewById<EditText>(R.id.nameEditText).setText(receiverModel?.name)
        }
    }

    companion object {
        fun newInstance(receiverModel: ReceiverModel): ReceiverDialog {
            val args = Bundle()
            args.putParcelable(RECEIVER_MODEL, receiverModel)

            val dialog = ReceiverDialog()
            dialog.arguments = args

            return dialog
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is Listener) listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        receiverModel = arguments?.getParcelable(RECEIVER_MODEL)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(activity)
            .setTitle(R.string.expense_receiver_dialog_title)
            .setView(view)
            .setPositiveButton(R.string.expense_receiver_dialog_save_button, { _, _ ->
                receiverModel?.apply {
                    name = view.findViewById<EditText>(R.id.nameEditText).text.toString()
                    listener?.onSave(this)
                }
            })
            .setNegativeButton(R.string.expense_receiver_dialog_cancel_button, { _, _ ->
                listener?.onCancel()
            })
            .create()

    interface Listener {
        fun onSave(receiverModel: ReceiverModel)
        fun onCancel()
    }
}
