package com.infix.musicappv1.ui.home.album.more_album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.databinding.ItemMoreAlbumBinding

class MoreAlbumAdapter(
    private val onAlbumClick: AlbumClickListener
) : RecyclerView.Adapter<MoreAlbumAdapter.ViewHolder>() {
    private val albums = mutableListOf<Album>()

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
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    fun updateAlbums(albums: List<Album>) {
        val oldSize = this.albums.size
        this.albums.clear()
        this.albums.addAll(albums)
        //remove old list album
        if (oldSize > this.albums.size)
            notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeChanged(0, this.albums.size)
    }

    interface AlbumClickListener {
        fun onClick(album: Album)
    }
}