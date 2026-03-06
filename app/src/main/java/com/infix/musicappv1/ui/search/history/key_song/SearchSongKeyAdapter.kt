package com.infix.musicappv1.ui.search.history.key_song

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.data.model.search.SearchKeySong
import com.infix.musicappv1.databinding.ItemKeySearchSongBinding

class SearchSongKeyAdapter(
    private val onClick: OnSearchKeySongClick
) : RecyclerView.Adapter<SearchSongKeyAdapter.ViewHolder>() {
    private val searchKeySongs = mutableListOf<SearchKeySong>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemKeySearchSongBinding.inflate(
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
        holder.bind(searchKeySongs[position])
    }

    override fun getItemCount() = searchKeySongs.size

    fun updateSearchKeySongs(searchKeySongs: List<SearchKeySong>) {
        val oldSize = this.searchKeySongs.size
        this.searchKeySongs.clear()
        this.searchKeySongs.addAll(searchKeySongs)
        if (oldSize > this.searchKeySongs.size)
            notifyItemRangeRemoved(0, oldSize)

        notifyItemRangeChanged(0, this.searchKeySongs.size)
    }

    inner class ViewHolder(private val binding: ItemKeySearchSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchKeySong: SearchKeySong) {
            binding.tvNameKeySearchSong.text = searchKeySong.key
            binding.root.setOnClickListener {
                onClick.onClick(searchKeySong)
            }
        }
    }

    fun interface OnSearchKeySongClick {
        fun onClick(searchKeySong: SearchKeySong)
    }
}