package com.rahafcs.co.rightway.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rahafcs.co.rightway.databinding.OuterItemBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsUiState

class WorkoutVerticalAdapter :

    ListAdapter<WorkoutsUiState, WorkoutVerticalAdapter.WorkoutViewHolder>(
        VerticalDiffCallback
    ) {
    inner class WorkoutViewHolder(private val binding: OuterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutsUiState) {
            binding.muscleName.text = item.workoutBodyTargetUiState.bodyPart
            var adapter = WorkoutHorizontalAdapter()
            binding.innerRecyclerview.adapter = adapter
            adapter.submitList(item.workoutsInfoUiState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(OuterItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object VerticalDiffCallback : DiffUtil.ItemCallback<WorkoutsUiState>() {
        override fun areItemsTheSame(
            oldItem: WorkoutsUiState,
            newItem: WorkoutsUiState
        ): Boolean {
            return oldItem.workoutBodyTargetUiState.bodyPart == newItem.workoutBodyTargetUiState.bodyPart
        }

        override fun areContentsTheSame(
            oldItem: WorkoutsUiState,
            newItem: WorkoutsUiState
        ): Boolean {
            return oldItem.workoutBodyTargetUiState.bodyPart == newItem.workoutBodyTargetUiState.bodyPart
        }
    }
}
