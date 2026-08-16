package com.infix.musicappv1.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.databinding.ItemSubscriptionAdminBinding
import com.infix.musicappv1.databinding.ItemSubscriptionBinding

class SubscriptionPagingDataAdapter(
    private val isAdmin: Boolean = false,
    private val onSubscriptionClick: ((subscription: Subscription) -> Unit)? = null,
    private val onOptionClick: ((subscription: Subscription) -> Unit)? = null
) : PagingDataAdapter<Subscription, RecyclerView.ViewHolder>(DiffUtils()) {

    companion object {
        private const val VIEW_TYPE_ADMIN = 1
        private const val VIEW_TYPE_USER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (isAdmin) VIEW_TYPE_ADMIN else VIEW_TYPE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ADMIN) {
            val binding = ItemSubscriptionAdminBinding.inflate(inflater, parent, false)
            AdminViewHolder(binding)
        } else {
            val binding = ItemSubscriptionBinding.inflate(inflater, parent, false)
            UserViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        when (holder) {
            is AdminViewHolder -> holder.bind(item)
            is UserViewHolder -> holder.bind(item)
        }
    }

    class DiffUtils : DiffUtil.ItemCallback<Subscription>() {
        override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem == newItem
        }
    }

    //for Admin
    inner class AdminViewHolder(private val binding: ItemSubscriptionAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val subscription = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                onSubscriptionClick?.invoke(subscription)
            }

            binding.btnOption.setOnClickListener {
                val subscription = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                onOptionClick?.invoke(subscription)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Subscription) {
            binding.apply {
                tvName.text = item.name
                tvDescription.text = item.description
                tvPrice.text = root.context.getString(
                    R.string.txt_price_with_vnd,
                    item.price.toString()
                )

                tvDuration.text = root.context.getString(
                    R.string.txt_duration_days_args,
                    item.durationDays.toString()
                )

                if (item.isActive) {
                    chipStatus.text = root.context.getString(R.string.txt_state_active)
                    chipStatus.setChipBackgroundColorResource(android.R.color.holo_green_light)
                } else {
                    chipStatus.text = root.context.getString(
                        R.string.txt_state_unactive
                    )
                    chipStatus.setChipBackgroundColorResource(android.R.color.holo_red_light)
                }
            }
        }
    }

    // for User
    inner class UserViewHolder(private val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            val clickAction = {
                getItem(bindingAdapterPosition)?.let { subscription ->
                    onSubscriptionClick?.invoke(subscription)
                }
            }
            binding.root.setOnClickListener { clickAction() }
            binding.btnSelect.setOnClickListener { clickAction() }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Subscription) {
            val context = binding.root.context

            binding.apply {
                tvName.text = item.name
                tvDescription.text = item.description

                tvPrice.text = context.getString(
                    R.string.txt_price_with_vnd,
                    item.price.toString()
                )

                tvDuration.text = "/ ${context.getString(
                    R.string.txt_duration_days_args,
                    item.durationDays.toString()
                )}"

                val isHighValue = item.price >= 500_000.0

                val colorPrimary = com.google.android.material.color.MaterialColors.getColor(
                    root, com.google.android.material.R.attr.colorPrimarySurface
                )
                val colorOnPrimary = com.google.android.material.color.MaterialColors.getColor(
                    root, com.google.android.material.R.attr.colorOnPrimary
                )
                val colorOutlineVariant = com.google.android.material.color.MaterialColors.getColor(
                    root, com.google.android.material.R.attr.colorOutlineVariant
                )
                val colorGold = "#FFD700".toColorInt()

                if (isHighValue) {
                    cardSubscription.strokeColor = colorGold
                    cardSubscription.strokeWidth = 3.dpToPx(context)

                    tvPrice.setTextColor(colorGold)
                    btnSelect.backgroundTintList = android.content.res.ColorStateList.valueOf(colorGold)
                    btnSelect.setTextColor(android.graphics.Color.BLACK)
                } else {
                    cardSubscription.strokeColor = colorOutlineVariant
                    cardSubscription.strokeWidth = 1.dpToPx(context)

                    tvPrice.setTextColor(colorPrimary)
                    btnSelect.backgroundTintList = android.content.res.ColorStateList.valueOf(colorPrimary)
                    btnSelect.setTextColor(colorOnPrimary)
                }
            }
        }

        private fun Int.dpToPx(context: android.content.Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }
    }
}