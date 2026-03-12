package com.infix.musicappv1.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.databinding.ItemSectionHeaderHomeBinding

class SectionHeaderHomeAdapter(
    private val onSearchClick: () -> Unit
) : RecyclerView.Adapter<SectionHeaderHomeAdapter.HeaderViewHolder>() {

    inner class HeaderViewHolder(private val binding: ItemSectionHeaderHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.btnSearchHome.setOnClickListener { onSearchClick.invoke() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(
            ItemSectionHeaderHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}