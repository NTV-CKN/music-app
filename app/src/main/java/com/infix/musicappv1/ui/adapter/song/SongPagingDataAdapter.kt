package com.infix.musicappv1.ui.adapter.song

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.ItemSongBinding

class SongPagingDataAdapter(
    private val onSongClick: SongAdapter.SongClickListener,
    private val onOptionClick: SongAdapter.OptionSongClickListener,
    private val permissionRepository: PermissionRepository
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
        Log.d("SongPagingDataAdapter", "pos $position")
        holder.bind()
    }

    class DiffUtils : DiffUtil.ItemCallback<Song>() {
        //if true => check content => false (rebind this viewHolder) => true (nothing)
        //if false => remove old item and insert new item
        override fun areItemsTheSame(
            oldItem: Song,
            newItem: Song
        ): Boolean {
            //if load too fast and data wrong create behavior, maybe has duplicate song id
            //so this callback will return true => RecyclerView move item this song at oldpos to new pos => crash
            //We add compare with title to guarantee two songs is same and return right true or false
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

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    val song = getItem(bindingAdapterPosition)?:return@setOnClickListener
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val notificationGranted =
                            ContextCompat.checkSelfPermission(
                                binding.root.context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED

                        permissionRepository.setGrantedNotification(notificationGranted)
                        if (notificationGranted)
                            onSongClick.onSongClick(song, bindingAdapterPosition)

                        permissionRepository.setAskPermissionNotification(!notificationGranted)
                    } else
                        onSongClick.onSongClick(song, position)
                }
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