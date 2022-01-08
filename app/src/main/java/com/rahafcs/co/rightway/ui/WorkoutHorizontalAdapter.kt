package com.rahafcs.co.rightway.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.ViewPagerFragmentDirections
import com.rahafcs.co.rightway.databinding.NestedItemBinding
import com.rahafcs.co.rightway.ui.WorkoutsFragment.Companion.listOfSavedWorkouts
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ui.findUrlGlide

class WorkoutHorizontalAdapter(var itemClickListener: (WorkoutsInfoUiState) -> Boolean) :
    ListAdapter<WorkoutsInfoUiState, WorkoutHorizontalAdapter.WorkoutViewHolder>(
        HorizontalDiffCallback
    ) {

    inner class WorkoutViewHolder(private val binding: NestedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutsInfoUiState) {
            binding.bodyTargetTextview.text = item.name
            binding.workoutGif.findUrlGlide(item.gifUrl)
            binding.workoutCardView.setOnClickListener {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragment2ToWorkoutDetailsFragment2(
                        item
                    )
                binding.root.findNavController().navigate(action)
            }
            if (isSaved(item)) {
                binding.bookmark.setImageResource(R.drawable.bookmark_filled)
            }
            binding.bookmark.setOnClickListener {
                if (itemClickListener(item)) {
                    binding.bookmark.setImageResource(R.drawable.bookmark_filled)
                } else {
                    binding.bookmark.setImageResource(R.drawable.bookmark)
                }
            }
        }

        private fun isSaved(workoutsInfoUiState: WorkoutsInfoUiState) =
            listOfSavedWorkouts.contains(workoutsInfoUiState)
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
