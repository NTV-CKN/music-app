package com.infix.musicappv1.ui.adapter.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.databinding.ItemMoreAlbumBinding

class MoreAlbumPagingDataAdapter(
    private val onAlbumClick: AlbumClickListener
) : PagingDataAdapter<Album, MoreAlbumPagingDataAdapter.ViewHolder>(DiffUtils()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMoreAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val album = getItem(position)
        album?.let {
            holder.bind(it)
        }
    }

    class DiffUtils : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(
            oldItem: Album,
            newItem: Album
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Album,
            newItem: Album
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: ItemMoreAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.tvTitleAlbumMoreAlbum.text = album.name
            Glide.with(binding.root)
                .load(album.artwork)
                .error(R.drawable.ic_song_24)
                .into(binding.imgArtworkMoreAlbum)
            //click listener
            binding.root.setOnClickListener {
                onAlbumClick.onClick(album)
            }
        }
    }

    interface AlbumClickListener {
        fun onClick(album: Album)
    }
}