package com.infix.musicappv1.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.databinding.FragmentUserProfileFragmentBinding
import com.infix.musicappv1.ui.user.UserManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileFragmentBinding

    private val userManagementViewMode: UserManagementViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserManagementVM()
    }

    private fun observeUserManagementVM() {
        userManagementViewMode.user.observe(viewLifecycleOwner) { initData(it) }
    }

    private fun initData(user: User?) {
        user?.let { currentUser ->
            binding.edtEmailUpf.setText(currentUser.email)
            binding.edtDisplayNameUpf.setText(currentUser.displayName)

            Glide.with(this)
                .load(currentUser.avatar)
                .placeholder(R.drawable.ic_account_circle_24)
                .error(R.drawable.ic_close)
                .circleCrop()
                .into(binding.imgAvatarUpf)
        }
    }
}