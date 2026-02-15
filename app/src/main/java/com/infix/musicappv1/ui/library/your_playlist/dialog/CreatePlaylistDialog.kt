package com.infix.musicappv1.ui.library.your_playlist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.DialogFragmentCreatePlaylistBinding
import com.infix.musicappv1.ui.library.your_playlist.YourPlaylistFragment

class CreatePlaylistDialog : DialogFragment() {
    private lateinit var binding: DialogFragmentCreatePlaylistBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentCreatePlaylistBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.txt_message_dialog_create_playlist))
            .setPositiveButton(getString(R.string.txt_create), null)//prevent dismiss
            .setNegativeButton(R.string.txt_cancel) { _, _ -> dismiss() }
            .setView(binding.root)
            .create()

        dialog.setOnShowListener {
            val createButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            createButton.setOnClickListener {
                createPlaylist()
            }
        }

        return dialog
    }

    private fun createPlaylist() {
        if (binding.inputTextNamePlaylistDialog.text != null && binding.inputTextNamePlaylistDialog.text.toString()
                .isNotEmpty()
        ) {
            requireActivity().supportFragmentManager.setFragmentResult(
                YourPlaylistFragment.CREATE_PLAYLIST_REQUEST_KEY,
                bundleOf(
                    YourPlaylistFragment.KEY_NAME_PLAYLIST to binding.inputTextNamePlaylistDialog.text!!.toString()
                )
            )
            dismiss()
        } else {
            binding.tilNamePlaylistDialog.error = getString(R.string.txt_name_playlist_is_empty)
        }
    }
}
