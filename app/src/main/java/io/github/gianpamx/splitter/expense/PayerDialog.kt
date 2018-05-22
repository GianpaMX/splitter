package io.github.gianpamx.splitter.expense

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import io.github.gianpamx.splitter.R

private const val PAYER_MODEL = "PayerModel"

class PayerDialog : DialogFragment() {
    private var listener: Listener? = null

    private var payerModel: PayerModel? = null

    val view by lazy {
        (View.inflate(activity, R.layout.expense_payer_dialog, null) as ViewGroup).apply {
            findViewById<EditText>(R.id.nameEditText).setText(payerModel?.name)
            findViewById<EditText>(R.id.amountEditText).setText(payerModel?.amount)
        }
    }

    companion object {
        fun newInstance(payerModel: PayerModel): PayerDialog {
            val args = Bundle()
            args.putParcelable(PAYER_MODEL, payerModel)

            val payerDialog = PayerDialog()
            payerDialog.arguments = args

            return payerDialog
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is Listener) listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        payerModel = arguments?.getParcelable(PAYER_MODEL)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(activity)
            .setTitle(R.string.expense_payer_dialog_title)
            .setView(view)
            .setPositiveButton(R.string.expense_payer_dialog_save_button, { _, _ ->
                payerModel?.apply {
                    name = view.findViewById<EditText>(R.id.nameEditText).text.toString()
                    amount = view.findViewById<EditText>(R.id.amountEditText).text.toString()
                    listener?.onSave(this)
                }
            })
            .setNegativeButton(R.string.expense_payer_dialog_cancel_button, { _, _ ->
                listener?.onCancel()
            })
            .create()

    interface Listener {
        fun onSave(payerModel: PayerModel)
        fun onCancel()
    }
}
