package com.infix.musicappv1.ui.admin.album.update_add

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.ItemSongBinding

class SelectedSongAdapter(
    private val onDeleteClick: (song: Song) -> Unit
) : RecyclerView.Adapter<SelectedSongAdapter.ViewHolder>() {

    private var songList: List<Song> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateSongs(newSongs: List<Song>) {
        this.songList = newSongs
        notifyDataSetChanged()
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
        holder.bind(songList[position])
    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnItemSongOption.setImageResource(R.drawable.ic_close)

            binding.btnItemSongOption.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(songList[position])
                }
            }
        }

        fun bind(song: Song) {
            binding.tvItemSongTitle.text = song.title
            binding.tvItemSongArtist.text = song.artist

            Glide.with(binding.root)
                .load(song.image)
                .error(R.drawable.ic_song_24)
                .into(binding.imgItemSong)
        }
    }
}