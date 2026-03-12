package com.infix.musicappv1.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.databinding.ItemSectionListSongHomeBinding

class SectionSongListAdapter(
    private val navigateToMoreRcm: () -> Unit
) : RecyclerView.Adapter<SectionSongListAdapter.SongTitleViewHolder>() {

    inner class SongTitleViewHolder(val binding: ItemSectionListSongHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvTitleRcmSong.setOnClickListener { navigateToMoreRcm.invoke() }
            binding.btnMoreRcmSong.setOnClickListener { navigateToMoreRcm.invoke() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongTitleViewHolder {
        return SongTitleViewHolder(
            ItemSectionListSongHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongTitleViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}