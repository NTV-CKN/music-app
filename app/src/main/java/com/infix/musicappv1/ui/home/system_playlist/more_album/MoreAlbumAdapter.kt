package com.infix.musicappv1.ui.home.system_playlist.more_album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.databinding.ItemMoreAlbumBinding

class MoreAlbumAdapter(
    private val onAlbumClick: AlbumClickListener
) : RecyclerView.Adapter<MoreAlbumAdapter.ViewHolder>() {
    private val playlists = mutableListOf<Playlist>()

    inner class ViewHolder(private val binding: ItemMoreAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.tvTitleAlbumMoreAlbum.text = playlist.namePlaylist
            Glide.with(binding.root)
                .load(playlist.artwork)
                .error(R.drawable.ic_song_24)
                .into(binding.imgArtworkMoreAlbum)
            //click listener
            binding.root.setOnClickListener {
                onAlbumClick.onClick(playlist)
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
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun updateAlbums(playlists: List<Playlist>) {
        val oldSize = this.playlists.size
        this.playlists.clear()
        this.playlists.addAll(playlists)
        //remove old list album
        if (oldSize > this.playlists.size)
            notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeChanged(0, this.playlists.size)
    }

    interface AlbumClickListener {
        fun onClick(playlist: Playlist)
    }
}