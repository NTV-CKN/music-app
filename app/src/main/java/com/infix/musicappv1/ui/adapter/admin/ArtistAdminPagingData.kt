package com.infix.musicappv1.ui.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.ItemArtistBinding

class ArtistAdminPagingDataAdapter(
    private val onOptionClick: (artist: Artist) -> Unit,
    private val onArtistClick: ((artist: Artist) -> Unit)? = null
) : PagingDataAdapter<Artist, ArtistAdminPagingDataAdapter.ViewHolder>(DiffUtils()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemArtistBinding.inflate(
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

    class DiffUtils : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.avatar == newItem.avatar
        }
    }

    inner class ViewHolder(private val binding: ItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnInterestArtist.visibility = View.GONE
            binding.btnOptionArtist.visibility = View.VISIBLE

            binding.btnOptionArtist.setOnClickListener {
                val artist = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                onOptionClick(artist)
            }
        }

        fun bind() {
            val artist = getItem(bindingAdapterPosition) ?: return
            binding.tvNameArtist.text = artist.name
            binding.tvNumberInterested.text = binding.root.context.getString(
                R.string.txt_amount_interested, artist.amountInterested
            )

            Glide.with(binding.root)
                .load(artist.avatar)
                .error(R.drawable.ic_song_24)
                .into(binding.imgArtist)
        }
    }
}