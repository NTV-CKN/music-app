package com.infix.musicappv1.ui.admin.bottom_sheet

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.ItemSaaPickerBinding

class SAAPickerPagingDataAdapter<T : Any>(
    private val onItemClick: OnItemClick<T>,
) : PagingDataAdapter<T, SAAViewHolder<T>>(DiffUtils<T>()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SAAViewHolder<T> {
        val binding = ItemSaaPickerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SAAViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: SAAViewHolder<T>, position: Int) {
        Log.d("SAAPickerPagingDataAdapter", "pos $position")
        val data = getItem(position)
        if (data != null)
            holder.bind(data)
    }

    fun interface OnItemClick<T> {
        fun onClick(data: T)
    }

    class DiffUtils<T : Any> : DiffUtil.ItemCallback<T>() {
        //if true => check content => false (rebind this viewHolder) => true (nothing)
        //if false => remove old item and insert new item
        override fun areItemsTheSame(
            oldItem: T,
            newItem: T
        ): Boolean {
            return when {
                oldItem is Song && newItem is Song -> oldItem.id == newItem.id
                oldItem is Artist && newItem is Artist -> oldItem.id == newItem.id
                oldItem is Album && newItem is Album -> oldItem.id == newItem.id
                else -> oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: T,
            newItem: T
        ): Boolean {
            return oldItem == newItem
        }
    }

    //this layout manager will set for recycler view has use this adapter. Cause during user scroll,
    //recycler view may predict next item but data of adapter still not next item, so app will crash
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
}

class SAAViewHolder<T>(
    private val binding: ItemSaaPickerBinding,
    private val onItemClick: SAAPickerPagingDataAdapter.OnItemClick<T>,
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SuspiciousIndentation")
    fun bind(data: T) {
        binding.root.setOnClickListener {
            onItemClick.onClick(data)
        }

        var title = ""
        var subtitle = ""
        var img = ""
        when (data) {
            is Song -> {
                title = data.title
                subtitle = data.artist
                img = data.image
            }

            is Artist -> {
                title = data.name
                subtitle = data.amountInterested.toString()
                img = data.avatar
            }

            is Album -> {
                title = data.name
                subtitle = data.size.toString()
                img = data.artwork
            }
        }

        binding.tvItemSsaPickerTitle.text = title
        binding.tvItemSsaPickerSubtitle.text = subtitle
        Glide.with(binding.root)
            .load(img)
            .error(R.drawable.ic_song_24)
            .into(binding.imgItemSong)
    }
}