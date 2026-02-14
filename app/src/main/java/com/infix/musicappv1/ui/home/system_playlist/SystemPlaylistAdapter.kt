package com.infix.musicappv1.ui.home.system_playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.infix.musicappv1.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.databinding.ItemSystemPlaylistBinding

class SystemPlaylistAdapter(
    private val onSystemPlaylistClick: SystemPlaylistClick
) : RecyclerView.Adapter<SystemPlaylistAdapter.ViewHolder>() {
    private val playlists = mutableListOf<Playlist>()

    inner class ViewHolder(private val binding: ItemSystemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.tvTitleSystemPlaylist.text = playlist.namePlaylist
            Glide.with(binding.root)
                .load(playlist.artwork)
                .error(R.drawable.ic_song_24)
                .into(binding.imgItemSystemPlaylist)
            //click listener
            binding.root.setOnClickListener {
                onSystemPlaylistClick.onClick(playlist)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSystemPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun updatePlaylists(playlists: List<Playlist>) {
        val oldSize = this.playlists.size
        this.playlists.clear()
        this.playlists.addAll(playlists)
        //remove old list album
        if (oldSize > this.playlists.size)
            notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeChanged(0, this.playlists.size)
    }

    interface SystemPlaylistClick {
        fun onClick(playlist: Playlist)
    }
}