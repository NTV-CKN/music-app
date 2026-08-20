package com.infix.musicappv1.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.data.model.ai_rcm.AiMoodUiState
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentSectionAiRecommendationBinding
import com.infix.musicappv1.ui.adapter.song.AIRecommendSongAdapter

class SectionAiRecommendationAdapter(
    private val onSuggestClick: (String) -> Unit,
    private val onSongClick: AIRecommendSongAdapter.SongClickListener,
    private val onOptionClick: AIRecommendSongAdapter.OptionSongClickListener,
    private val permissionRepository: PermissionRepository
) : RecyclerView.Adapter<SectionAiRecommendationAdapter.AiViewHolder>() {

    private var currentState: AiMoodUiState = AiMoodUiState.Idle

    fun updateState(state: AiMoodUiState) {
        this.currentState = state
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiViewHolder {
        val binding = FragmentSectionAiRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AiViewHolder, position: Int) {
        holder.bind(currentState)
    }

    override fun getItemCount(): Int = 1

    inner class AiViewHolder(val binding: FragmentSectionAiRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnSuggest.setOnClickListener {
                val prompt = binding.etMoodInput.text?.toString()?.trim().orEmpty()
                if (prompt.isNotEmpty()) {
                    onSuggestClick(prompt)
                }
            }
        }

        fun bind(state: AiMoodUiState) {
            when (state) {
                is AiMoodUiState.Idle -> {
                    binding.progressAiLoading.visibility = View.GONE
                    binding.cardAiMessage.visibility = View.GONE
                    binding.rvAiSongs.visibility = View.GONE
                }

                is AiMoodUiState.Loading -> {
                    binding.progressAiLoading.visibility = View.VISIBLE
                    binding.cardAiMessage.visibility = View.GONE
                    binding.rvAiSongs.visibility = View.GONE

                    binding.btnSuggest.isEnabled = false
                }

                is AiMoodUiState.Success -> {
                    binding.progressAiLoading.visibility = View.GONE

                    binding.tvAiMessage.text = state.response.aiMessage
                    binding.cardAiMessage.visibility = View.VISIBLE

                    binding.rvAiSongs.visibility = View.VISIBLE

                    if (binding.rvAiSongs.adapter == null) {
                        binding.rvAiSongs.adapter = AIRecommendSongAdapter(
                            onSongClick = onSongClick,
                            onOptionClick = onOptionClick,
                            permissionRepository
                        )
                    }
                    (binding.rvAiSongs.adapter as? AIRecommendSongAdapter)?.updateSongs(state.response.songs)
                }

                is AiMoodUiState.Error -> {
                    binding.progressAiLoading.visibility = View.GONE
                    Toast.makeText(binding.root.context, state.message, Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnSuggest.isEnabled = true
        }
    }
}