package com.rahafcs.co.rightway.ui.brows

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.ViewPagerFragmentDirections
import com.rahafcs.co.rightway.databinding.FragmentBrowsBinding

class BrowsFragment : Fragment() {

    private var binding: FragmentBrowsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrowsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            browsFragment = this@BrowsFragment
            barbellChip.setOnClickListener { showWorkoutWithBarbell() }
            cableChip.setOnClickListener { showWorkoutWithCable() }
            allEquipmentChip.setOnClickListener { showWorkoutWithAllEquipment() }
            kettleBellChip.setOnClickListener { showWorkoutWithKettleBell() }
            resistanceBandChip.setOnClickListener { showWorkoutWithResistanceBand() }
            dumbbellChip.setOnClickListener { showWorkoutWithDumbbell() }
            noEquipmentCard.setOnClickListener { showWorkoutWithBodyWeight() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showWorkoutWithAllEquipment() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("")
        findNavController().navigate(action)
    }

    private fun showWorkoutWithBarbell() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("barbell")
        findNavController().navigate(action)
    }

    private fun showWorkoutWithCable() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("cable")
        findNavController().navigate(action)
    }

    private fun showWorkoutWithDumbbell() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("dumbbell")
        findNavController().navigate(action)
    }

    private fun showWorkoutWithKettleBell() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("kettlebell")
        findNavController().navigate(action)
    }

    private fun showWorkoutWithResistanceBand() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("resistance band")
        findNavController().navigate(action)
    }

    private fun showWorkoutWithBodyWeight() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("body weight")
        findNavController().navigate(action)
    }
}
