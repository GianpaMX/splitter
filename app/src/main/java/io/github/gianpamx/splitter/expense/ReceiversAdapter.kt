package io.github.gianpamx.splitter.expense

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.expense.ReceiversAdapter.ViewHolder
import io.github.gianpamx.splitter.expense.model.ReceiverModel
import kotlinx.android.synthetic.main.expense_receiver_item.view.*

class ReceiversAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val differ = AsyncListDiffer<ReceiverModel>(this, NewCallback())

    var onCheckedChangeListener: ((ReceiverModel) -> Unit)? = null
    var onLongClickListener: ((ReceiverModel) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = differ.currentList[position].id

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun replaceReceivers(receivers: List<ReceiverModel>) {
        differ.submitList(receivers)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.expense_receiver_item, parent, false)) {
        init {
            with(itemView) {
                receiverCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    val receiverModel = differ.currentList[adapterPosition]
                    receiverModel.isChecked = isChecked
                    onCheckedChangeListener?.invoke(receiverModel.copy())
                }

                setOnClickListener {
                    receiverCheckBox.isChecked = !receiverCheckBox.isChecked
                }

                setOnLongClickListener {
                    onLongClickListener?.invoke(differ.currentList[adapterPosition].copy())
                    return@setOnLongClickListener onLongClickListener != null
                }
            }
        }

        fun bind(receiver: ReceiverModel) {
            with(itemView) {
                nameTextView.text = receiver.name
                receiverCheckBox.isChecked = receiver.isChecked
            }
        }
    }

    class NewCallback : DiffUtil.ItemCallback<ReceiverModel>() {
        override fun areItemsTheSame(oldItem: ReceiverModel?, newItem: ReceiverModel?) = oldItem?.id == newItem?.id
        override fun areContentsTheSame(oldItem: ReceiverModel?, newItem: ReceiverModel?) = oldItem == newItem
    }
}
