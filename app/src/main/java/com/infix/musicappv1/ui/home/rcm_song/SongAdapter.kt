package com.infix.musicappv1.ui.home.rcm_song

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.ItemSongBinding

class SongAdapter(
    private val onSongClick: SongClickListener,
    private val onOptionClick: OptionSongClickListener
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private val songs = mutableListOf<Song>()

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song, position: Int) {
            binding.tvItemSongTitle.text = song.title
            binding.tvItemSongArtist.text = song.artist
            Glide.with(binding.root)
                .load(song.image)
                .error(R.drawable.ic_song_24)
                .into(binding.imgItemSong)
            //song click listener
            onSongClick.onSongClick(song)
            //option
            binding.btnItemSongOption.setOnClickListener {
                onOptionClick.onOptionClick(song)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSongBinding.inflate(
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
        holder.bind(songs[position], position)
    }

    override fun getItemCount(): Int = songs.size

    fun updateSongs(songs: List<Song>) {
        val oldSize = this.songs.size
        this.songs.clear()
        this.songs.addAll(songs)

        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, this.songs.size)
    }

    interface SongClickListener {
        fun onSongClick(song: Song)
    }

    interface OptionSongClickListener {
        fun onOptionClick(song: Song)
    }
}