package com.infix.musicappv1.ui.adapter.admin

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.ItemSongBinding
import com.infix.musicappv1.ui.adapter.song.SongAdapter

class SongAdminPagingDataAdapter(
    private val onSongClick: SongAdapter.SongClickListener,
    private val onOptionClick: SongAdapter.OptionSongClickListener,
) : PagingDataAdapter<Song, SongAdminPagingDataAdapter.ViewHolder>(DiffUtils()) {
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
        holder.bind()
    }

    class DiffUtils : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(
            oldItem: Song,
            newItem: Song
        ): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Song,
            newItem: Song
        ): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.artist == newItem.artist &&
                    oldItem.image == newItem.image &&
                    oldItem.favorite == newItem.favorite
        }
    }

    class WrapContentLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        init {
            isItemPrefetchEnabled = false
        }

        override fun onLayoutChildren(
            recycler: RecyclerView.Recycler?,
            state: RecyclerView.State?
        ) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
            }
        }

        override fun scrollVerticallyBy(
            dy: Int,
            recycler: RecyclerView.Recycler?,
            state: RecyclerView.State?
        ): Int {
            return try {
                super.scrollVerticallyBy(dy, recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                dy
            }
        }

        override fun supportsPredictiveItemAnimations(): Boolean = false
    }

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                //option
                binding.btnItemSongOption.setOnClickListener {
                    val song = getItem(bindingAdapterPosition)?:return@setOnClickListener
                    onOptionClick.onOptionClick(song)
                }
            }

        @SuppressLint("SuspiciousIndentation")
        fun bind() {
            val song = getItem(bindingAdapterPosition) ?: return
            binding.tvItemSongTitle.text = song.title
            binding.tvItemSongArtist.text = song.artist
            Glide.with(binding.root)
                .load(song.image)
                .error(R.drawable.ic_song_24)
                .into(binding.imgItemSong)
            //song click listener
        }
    }
}