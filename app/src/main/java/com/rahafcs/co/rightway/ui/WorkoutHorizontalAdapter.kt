package com.rahafcs.co.rightway.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.databinding.NestedItemBinding

class WorkoutHorizontalAdapter :
    ListAdapter<Workout, WorkoutHorizontalAdapter.WorkoutViewHolder>(HorizontalDiffCallback) {

    class WorkoutViewHolder(private val binding: NestedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Workout) {
            binding.bodyTargetTextview.text = item.name ?: "Not found"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(NestedItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object HorizontalDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.name == oldItem.name
        }
    }
}
