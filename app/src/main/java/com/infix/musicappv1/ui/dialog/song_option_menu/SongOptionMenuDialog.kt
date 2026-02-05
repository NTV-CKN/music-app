package com.infix.musicappv1.ui.dialog.song_option_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.option_menu.SongOptionMenuItem
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.DialogFragmentSongOptionMenuBinding
import com.infix.musicappv1.databinding.ItemOptionMenuSongBinding
import com.infix.musicappv1.enums.SongMenuOptionEnum
import com.infix.musicappv1.ui.dialog.song_info.SongInfoDialog
import com.infix.musicappv1.ui.dialog.song_info.SongInfoDialogViewModel

class SongOptionMenuDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentSongOptionMenuBinding
    private lateinit var adapter: SongMenuOptionAdapter
    private val songOptionMenuViewModel: SongOptionMenuViewModel by activityViewModels()
    private val songInfoViewModel: SongInfoDialogViewModel by activityViewModels()

    //class adapter
    class SongMenuOptionAdapter(
        private val menuItems: MutableList<SongOptionMenuItem> = mutableListOf(),
        private val menuClick: MenuItemClick
    ) : RecyclerView.Adapter<SongMenuOptionAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: ItemOptionMenuSongBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(menuItem: SongOptionMenuItem) {
                val context = binding.root.context
                binding.tvNameItemOptionBottomSheet.text = context.getString(menuItem.title)
                Glide.with(binding.root)
                    .load(menuItem.icon)
                    .error(R.drawable.ic_song_24)
                    .into(binding.imgItemOptionBottomSheet)
                //event click
                binding.root.setOnClickListener {
                    menuClick.onClick(menuItem)
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val binding = ItemOptionMenuSongBinding.inflate(
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
            holder.bind(menuItems[position])
        }

        override fun getItemCount() = menuItems.size

        fun updateMenuItems(menuItems: List<SongOptionMenuItem>) {
            val oldSize = this.menuItems.size
            this.menuItems.clear()
            this.menuItems.addAll(menuItems)

            if (oldSize > this.menuItems.size)
                notifyItemRangeRemoved(0, oldSize)
            notifyItemRangeChanged(0, this.menuItems.size)
        }

        interface MenuItemClick {
            fun onClick(menuItem: SongOptionMenuItem)
        }
    }//End class adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentSongOptionMenuBinding.inflate(
            inflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObserveSongOptionMenuViewModel()
    }

    private fun setupRecyclerView() {
        adapter = SongMenuOptionAdapter(
            mutableListOf(),
            object : SongMenuOptionAdapter.MenuItemClick {
                override fun onClick(menuItem: SongOptionMenuItem) {
                    onMenuItemClick(menuItem)
                }
            }
        )

        binding.rvSongOptionMenu.adapter = adapter
    }

    private fun setupObserveSongOptionMenuViewModel() {
        //update menu items
        songOptionMenuViewModel.menuItems.observe(viewLifecycleOwner) { menuItems ->
            adapter.updateMenuItems(
                menuItems
            )
        }

        //update UI show info song header bottom sheet
        songOptionMenuViewModel.song.observe(viewLifecycleOwner) { song ->
            binding.includeShowSongOptionMenu.tvItemSongTitleBottomSheet.text = song.title
            binding.includeShowSongOptionMenu.tvItemSongArtistBottomSheet.text = song.artist
            Glide.with(binding.root)
                .load(song.image)
                .error(R.drawable.ic_song_24)
                .into(binding.includeShowSongOptionMenu.imgItemSongBottomSheet)

            //TODO: event share click
            binding.includeShowSongOptionMenu.btnItemSongShareBottomSheet.setOnClickListener {

            }
        }
    }

    private fun onMenuItemClick(menuItem: SongOptionMenuItem) {
        when (menuItem.option) {
            SongMenuOptionEnum.ADD_TO_FAVORITES -> {}
            SongMenuOptionEnum.VIEW_SONG_INFO -> showSongInfo()
            SongMenuOptionEnum.VIEW_ARTIST -> {}
            SongMenuOptionEnum.REPORT_ERROR -> {}
            SongMenuOptionEnum.ADD_TO_PLAYLIST -> {}
            SongMenuOptionEnum.DOWNLOAD -> {}
            SongMenuOptionEnum.BLOCK_SONG -> {}
            SongMenuOptionEnum.VIEW_ALBUM -> {}
            SongMenuOptionEnum.PLAY_NEXT -> {}
        }
        dismiss()
    }

    private fun showSongInfo() {
        songInfoViewModel.setSong(songOptionMenuViewModel.song.value ?: Song())
        SongInfoDialog().show(
            requireActivity().supportFragmentManager,
            SongInfoDialog.TAG
        )
    }

    companion object {
        const val TAG = "SongOptionMenuDialog"
    }
}