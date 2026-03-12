package com.infix.musicappv1.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.databinding.ItemSectionListAlbumHomeBinding

class SectionAlbumListAdapter : RecyclerView.Adapter<SectionAlbumListAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(val binding: ItemSectionListAlbumHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemSectionListAlbumHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 1
}