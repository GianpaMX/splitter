package io.github.gianpamx.splitter.settleup

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.settleup.model.CardModel
import kotlinx.android.synthetic.main.settle_up_card.view.*
import java.text.NumberFormat
import kotlin.math.absoluteValue

class CardsAdapter(private val currencyFormat: NumberFormat) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
    private val differ = AsyncListDiffer<CardModel>(this, NewCallback())

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = differ.currentList[position].personId

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun replaceCards(cards: List<CardModel>) {
        differ.submitList(cards)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.settle_up_card, parent, false)) {
        fun bind(card: CardModel) {
            itemView.personNameTextView.text = card.personName
            itemView.descriptionTextView.text = itemView.resources.getString(
                    R.string.settle_up_card_description,
                    currencyFormat.format(card.paid),
                    currencyFormat.format(card.spent)
            )
            itemView.breakdownTextView.text = card.breakdownRows.joinToString(separator = "\n") {
                itemView.resources.getString(
                        if (it.first > 0) R.string.settle_up_card_breakdown_collect_row else R.string.settle_up_card_breakdown_pay_row,
                        currencyFormat.format(it.first.absoluteValue),
                        it.second
                )
            }
        }
    }

    class NewCallback : DiffUtil.ItemCallback<CardModel>() {
        override fun areItemsTheSame(oldItem: CardModel, newItem: CardModel) = oldItem.personId == newItem.personId
        override fun areContentsTheSame(oldItem: CardModel, newItem: CardModel) = oldItem == newItem
    }
}
