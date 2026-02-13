package com.infix.musicappv1.ui.library.recent_song

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.ItemSongBinding
import com.infix.musicappv1.utils.MusicAppUtils

class RecentSongAdapter(
    private val songRecentClick: OnRecentSongClick,
    private val onMenuClick: OnMenuOptionClick
) : RecyclerView.Adapter<RecentSongAdapter.ViewHolder>() {
    private val songRecents = mutableListOf<SongRecent>()

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(songRecent: SongRecent, pos: Int) {
            binding.tvItemSongTitle.text = songRecent.title
            binding.tvItemSongArtist.text = songRecent.artist
            Glide.with(binding.root)
                .load(songRecent.image)
                .error(R.drawable.ic_song_24)
                .into(binding.imgItemSong)

            //song recent clicked
            binding.root.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationGranted =
                        checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    PermissionRepository.getInstance().setGrantedNotification(notificationGranted)
                    if (notificationGranted)
                        songRecentClick.onClick(songRecent, pos)

                    PermissionRepository.getInstance()
                        .setAskPermissionNotification(!notificationGranted)
                } else
                    songRecentClick.onClick(songRecent, pos)
            }
            //option menu clicked
            binding.btnItemSongOption.setOnClickListener { onMenuClick.onClick(songRecent) }
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

        val vto = binding.root.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.measuredWidth
                val clipWidthPixel = (MusicAppUtils.density * 32).toInt()

                binding.layoutListSong.layoutParams.width = width - clipWidthPixel
            }
        })
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(songRecents[position], position)
    }

    override fun getItemCount() = songRecents.size

    fun updateSongRecent(songRecents: List<SongRecent>) {
        val oldSize = this.songRecents.size
        this.songRecents.clear()
        this.songRecents.addAll(songRecents)
        if (oldSize > this.songRecents.size)
            notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeChanged(0, this.songRecents.size)
    }

    interface OnRecentSongClick {
        fun onClick(recentSong: SongRecent, pos: Int)
    }

    interface OnMenuOptionClick {
        fun onClick(recentSong: SongRecent)
    }
}