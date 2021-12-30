package com.rahafcs.co.rightway.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rahafcs.co.rightway.databinding.NestedItemBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ui.findUrlGlide

class WorkoutHorizontalAdapter :
    ListAdapter<WorkoutsInfoUiState, WorkoutHorizontalAdapter.WorkoutViewHolder>(
        HorizontalDiffCallback
    ) {

    class WorkoutViewHolder(private val binding: NestedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutsInfoUiState) {
            binding.bodyTargetTextview.text = item.name
            binding.workoutGif.findUrlGlide(item.gifUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(NestedItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object HorizontalDiffCallback : DiffUtil.ItemCallback<WorkoutsInfoUiState>() {
        override fun areItemsTheSame(
            oldItem: WorkoutsInfoUiState,
            newItem: WorkoutsInfoUiState
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: WorkoutsInfoUiState,
            newItem: WorkoutsInfoUiState
        ): Boolean {
            return oldItem.name == oldItem.name || oldItem.equipment == oldItem.equipment || oldItem.gifUrl == oldItem.gifUrl || oldItem.target == oldItem.target
        }
    }
}
