package io.github.gianpamx.splitter.groupexpenses

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.groupexpenses.model.ExpenseItem
import kotlinx.android.synthetic.main.group_expenses_item.view.*
import java.text.NumberFormat

class ExpensesAdapter(private val currencyFormat: NumberFormat) : RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {
    private val differ = AsyncListDiffer<ExpenseItem>(this, NewCallback())

    var onClickListener: ((ExpenseItem) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = differ.currentList[position].id

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun replaceExpenses(expenses: List<ExpenseItem>) {
        differ.submitList(expenses)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.group_expenses_item, parent, false)) {
        init {
            itemView.setOnClickListener {
                onClickListener?.invoke(differ.currentList[adapterPosition].copy())
            }
        }

        fun bind(expenseItem: ExpenseItem) {
            itemView.titleTextView.text = expenseItem.title
            itemView.totalExpenseTextView.text = if (expenseItem.total > 0.0) currencyFormat.format(expenseItem.total) else ""
        }
    }

    class NewCallback : DiffUtil.ItemCallback<ExpenseItem>() {
        override fun areItemsTheSame(oldItem: ExpenseItem?, newItem: ExpenseItem?) = oldItem?.id == newItem?.id
        override fun areContentsTheSame(oldItem: ExpenseItem?, newItem: ExpenseItem?) = oldItem == newItem
    }
}
