package com.infix.musicappv1.ui.adapter.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.ItemArtistBinding

class ArtistAdapter(
    private val onArtistClick: OnArtistClick,
    private val onInterestClick: OnInterestClick
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {
    private val artists = mutableListOf<Artist>()

    inner class ViewHolder(private val binding: ItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            binding.tvNameArtist.text = artist.name
            binding.tvNumberInterested.text =
                binding.root.context.getString(R.string.txt_amount_of_interested, artist.interested)
            Glide.with(binding.root)
                .load(artist.avatar)
                .error(R.drawable.ic_artist_24px)
                .circleCrop()
                .into(binding.imgArtist)

            val icInterested = if (artist.isInterested)
                R.drawable.ic_interest_on
            else
                R.drawable.ic_interest_off
            binding.btnInterestArtist.setImageResource(icInterested)

            binding.root.setOnClickListener { onArtistClick.onClick(artist) }
            binding.btnInterestArtist.setOnClickListener { onInterestClick.onClick(artist) }
        }
    }

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
        holder.bind(artists[position])
    }

    override fun getItemCount() = artists.size

    fun updateArtists(artists: List<Artist>) {
        val oldSize = this.artists.size
        this.artists.clear()
        this.artists.addAll(artists)
        if (oldSize > this.artists.size)
            notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeChanged(0, this.artists.size)
    }

    interface OnArtistClick {
        fun onClick(artist: Artist)
    }

    interface OnInterestClick {
        fun onClick(artist: Artist)
    }
}