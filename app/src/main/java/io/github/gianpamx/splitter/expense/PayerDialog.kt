package io.github.gianpamx.splitter.expense

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import io.github.gianpamx.splitter.R
import kotlinx.android.synthetic.main.expense_payer_dialog.*

private const val PAYER_MODEL = "PayerModel"

class PayerDialog : DialogFragment() {
    private var listener: Listener? = null

    private lateinit var payerModel: PayerModel

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

    override fun onCreateDialog(savedInstanceState: Bundle?) =
            AlertDialog.Builder(activity)
                    .setTitle(R.string.expense_payer_dialog_title)
                    .setView(activity?.layoutInflater?.inflate(R.layout.expense_payer_dialog, null))
                    .setPositiveButton(R.string.expense_payer_dialog_save_button, { _, _ ->
                        payerModel.name = nameEditText.text.toString()
                        payerModel.amount = amountEditText.text.toString()
                        listener?.onSave(payerModel)
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
