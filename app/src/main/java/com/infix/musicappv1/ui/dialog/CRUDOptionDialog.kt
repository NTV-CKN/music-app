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

    fun setData(data: T?) {
        this.data = data
    }
}