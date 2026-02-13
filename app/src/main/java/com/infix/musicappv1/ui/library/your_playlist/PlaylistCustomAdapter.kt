package com.infix.musicappv1.ui.library.your_playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.ItemYourPlaylistBinding

class PlaylistCustomAdapter(
    private val onPlaylistClick: OnPlaylistCustomClick,
    private val onMenuClick: OnMenuOptionClick
) : RecyclerView.Adapter<PlaylistCustomAdapter.ViewHolder>() {
    private val playlistCustom = mutableListOf<PlaylistWithSongs>()

    inner class ViewHolder(private val binding: ItemYourPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlistWithSong: PlaylistWithSongs) {
            binding.tvNameCustomPlaylist.text = playlistWithSong.playlist.namePlaylist
            binding.tvNumberSongCustomPlaylist.text = binding.root.context.getString(
                R.string.txt_amount_of_songs,
                "" + playlistWithSong.songs.size
            )
            //playlist click
            binding.root.setOnClickListener { onPlaylistClick.onClick(playlistWithSong) }
            //on menu click
            binding.btnMoreMenuCustomPlaylist.setOnClickListener {
                onMenuClick.onClick(
                    playlistWithSong
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemYourPlaylistBinding.inflate(
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
        holder.bind(playlistCustom[position])
    }

    override fun getItemCount() = playlistCustom.size

    fun updatePlaylistCustoms(playlistCustoms: List<PlaylistWithSongs>) {
        val oldSize = this.playlistCustom.size
        this.playlistCustom.clear()
        this.playlistCustom.addAll(playlistCustoms)
        //remove old list album
        if (oldSize > this.playlistCustom.size)
            notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeChanged(0, this.playlistCustom.size)
    }

    interface OnPlaylistCustomClick {
        fun onClick(playlistWithSongs: PlaylistWithSongs)
    }

    interface OnMenuOptionClick {
        fun onClick(playlistWithSong: PlaylistWithSongs)
    }
}