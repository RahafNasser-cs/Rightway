package com.rahafcs.co.rightway.ui.coach

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rahafcs.co.rightway.ViewPagerFragmentDirections
import com.rahafcs.co.rightway.databinding.CoachItemBinding
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState

class CoachAdapter :
    ListAdapter<CoachInfoUiState, CoachAdapter.CoachViewHolder>(CoachDiffCallback) {

    class CoachViewHolder(private val binding: CoachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoachInfoUiState) {
            binding.coachNameTextview.text = item.name
            binding.coachExperience.text = item.experience
            binding.coachPhone.text = item.phoneNumber
            binding.coachEmail.text = item.email
            binding.coachPrice.text = item.price
            binding.emailImg.setOnClickListener {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragment2ToSendEmailFragment(item.email)
                binding.root.findNavController()
                    .navigate(action)
            }
        }
    }

    companion object CoachDiffCallback : DiffUtil.ItemCallback<CoachInfoUiState>() {
        override fun areItemsTheSame(
            oldItem: CoachInfoUiState,
            newItem: CoachInfoUiState,
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CoachInfoUiState,
            newItem: CoachInfoUiState,
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.experience == newItem.experience && oldItem.phoneNumber == newItem.phoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachViewHolder {
        return CoachViewHolder(CoachItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CoachViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
