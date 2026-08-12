package com.infix.musicappv1.ui.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.databinding.ItemAlbumAdminBinding

class AlbumAdminPagingDataAdapter(
    private val onOptionClick: (album: Album) -> Unit,
    private val onAlbumClick: ((album: Album) -> Unit)? = null
) : PagingDataAdapter<Album, AlbumAdminPagingDataAdapter.ViewHolder>(DiffUtils()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAlbumAdminBinding.inflate(
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

    class DiffUtils : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(
            oldItem: Album,
            newItem: Album
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Album,
            newItem: Album
        ): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.size == newItem.size &&
                    oldItem.artwork == newItem.artwork
        }
    }

    inner class ViewHolder(private val binding: ItemAlbumAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val album = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                onAlbumClick?.invoke(album)
            }

            binding.btnOptionAlbum.setOnClickListener {
                val album = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                onOptionClick(album)
            }
        }

        fun bind() {
            val album = getItem(bindingAdapterPosition) ?: return
            binding.tvTitleAlbumMoreAlbum.text = album.name
            binding.tvSizeAlbumMoreAlbum.text =
                binding.root.context.getString(R.string.txt_size_album_args, album.size.toString())

            Glide.with(binding.root)
                .load(album.artwork)
                .error(R.drawable.ic_song_24)
                .into(binding.imgArtworkMoreAlbum)
        }
    }
}