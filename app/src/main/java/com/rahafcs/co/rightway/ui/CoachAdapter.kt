package com.rahafcs.co.rightway.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rahafcs.co.rightway.databinding.CoachItemBinding
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState

class CoachAdapter :
    ListAdapter<CoachInfoUiState, CoachAdapter.CoachViewHolder>(CoachDiffCallback) {

    class CoachViewHolder(private val binding: CoachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoachInfoUiState) {
            binding.coachNameTextview.text = item.phoneNumber
            binding.experience.text = item.experience
            binding.coachNameTextview.text = item.name
            binding.price.text = item.price
        }
    }

    companion object CoachDiffCallback : DiffUtil.ItemCallback<CoachInfoUiState>() {
        override fun areItemsTheSame(
            oldItem: CoachInfoUiState,
            newItem: CoachInfoUiState
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CoachInfoUiState,
            newItem: CoachInfoUiState
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
