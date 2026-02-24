package com.infix.musicappv1.ui.adapter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.ItemSongBinding
import com.infix.musicappv1.ui.adapter.SongAdapter.OptionSongClickListener
import com.infix.musicappv1.ui.adapter.SongAdapter.SongClickListener

class SongPagingDataAdapter(
    private val onSongClick: SongClickListener,
    private val onOptionClick: OptionSongClickListener
) : PagingDataAdapter<Song, SongPagingDataAdapter.ViewHolder>(DiffUtils()) {
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
        val song = getItem(position)
        song?.let {
            holder.bind(it, position)
        }
    }

    class DiffUtils : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(
            oldItem: Song,
            newItem: Song
        ): Boolean {
//            Log.d("SSSS", "" + oldItem + " " + newItem)
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Song,
            newItem: Song
        ): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.title == newItem.title
                    && oldItem.album == newItem.album
        }

    }

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
            binding.root.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationGranted =
                        ContextCompat.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    PermissionRepository.Companion.getInstance()
                        .setGrantedNotification(notificationGranted)
                    if (notificationGranted)
                        onSongClick.onSongClick(song, position)

                    PermissionRepository.Companion.getInstance()
                        .setAskPermissionNotification(!notificationGranted)
                } else
                    onSongClick.onSongClick(song, position)
            }
            //option
            binding.btnItemSongOption.setOnClickListener {
                onOptionClick.onOptionClick(song)
            }
        }
    }
}