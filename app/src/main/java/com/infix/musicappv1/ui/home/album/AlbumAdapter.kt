package com.infix.musicappv1.ui.home.album

import android.view.LayoutInflater
import android.view.ViewGroup
import com.infix.musicappv1.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.databinding.ItemAlbumBinding

class AlbumAdapter(
    private val onAlbumClick: AlbumAdapter.AlbumClickListener
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    private val albums = mutableListOf<Album>()

    inner class ViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.tvItemTitle.text = album.name
            Glide.with(binding.root)
                .load(album.artwork)
                .error(R.drawable.ic_song_24)
                .into(binding.imgItemAlbum)
            //click listener
            binding.root.setOnClickListener {
                onAlbumClick.onAlbumClick(album)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        fun onAlbumClick(album: Album)
    }
}