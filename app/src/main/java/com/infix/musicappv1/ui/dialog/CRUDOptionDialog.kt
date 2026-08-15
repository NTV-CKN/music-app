package com.infix.musicappv1.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infix.musicappv1.databinding.FragmentCrudDialogBinding

class CRUDOptionDialog<T>(
    private var data: T? = null,
    private val onUpdate: (data: T) -> Unit,
    private val onView: (data: T) -> Unit,
    private val onDelete: (data: T) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCrudDialogBinding

    private var isVisibleDelete: Boolean = true
    private var isVisibleView: Boolean = true
    private var isVisibleUpdate: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrudDialogBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvents()
        applyVisibility()
    }

    private fun setEvents() {
        //update
        binding.btnOptionUpdate.setOnClickListener {
            data?.let {
                onUpdate.invoke(it)
                dismiss()
            }
        }

        //remove
        binding.btnOptionDelete.setOnClickListener {
            data?.let {
                onDelete.invoke(it)
                dismiss()
            }
        }
    }

    private fun applyVisibility() {
        if (::binding.isInitialized) {
            binding.btnOptionDelete.visibility = if (isVisibleDelete) View.VISIBLE else View.GONE
            binding.btnOptionRead.visibility = if (isVisibleView) View.VISIBLE else View.GONE
            binding.btnOptionUpdate.visibility = if (isVisibleUpdate) View.VISIBLE else View.GONE
        }
    }

    fun setData(
        data: T?,
        isVisibleDelete: Boolean = true,
        isVisibleView: Boolean = true,
        isVisibleUpdate: Boolean = true
    ) {
        this.data = data
        this.isVisibleDelete = isVisibleDelete
        this.isVisibleView = isVisibleView
        this.isVisibleUpdate = isVisibleUpdate
    }
}