package com.rahafcs.co.rightway.ui.workout

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.ViewPagerFragmentDirections
import com.rahafcs.co.rightway.databinding.FragmentBrowsBinding
import com.rahafcs.co.rightway.utility.Constant.BARBELL
import com.rahafcs.co.rightway.utility.Constant.BODY_WEIGHT
import com.rahafcs.co.rightway.utility.Constant.CABLE
import com.rahafcs.co.rightway.utility.Constant.DUMBBELL
import com.rahafcs.co.rightway.utility.Constant.KETTLE_BELL
import com.rahafcs.co.rightway.utility.Constant.RESISTANCE_BAND
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
    }

    // Handel back press.
    private fun onBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // To show workout with all equipment.
    private fun showWorkoutWithAllEquipment() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment("")
        findNavController().navigate(action)
    }

    // To show workout with barbell.
    private fun showWorkoutWithBarbell() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment(
                BARBELL
            )
        findNavController().navigate(action)
    }

    // To show workout with cable.
    private fun showWorkoutWithCable() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment(
                CABLE
            )
        findNavController().navigate(action)
    }

    // To show workout with dumbbell.
    private fun showWorkoutWithDumbbell() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment(
                DUMBBELL
            )
        findNavController().navigate(action)
    }

    // To show workout with kettle bell.
    private fun showWorkoutWithKettleBell() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment(
                KETTLE_BELL
            )
        findNavController().navigate(action)
    }

    // To show workout with resistance band.
    private fun showWorkoutWithResistanceBand() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment(
                RESISTANCE_BAND
            )
        findNavController().navigate(action)
    }

    // To show workout with body weight. 
    private fun showWorkoutWithBodyWeight() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToShowWorkoutsByEquipmentFragment(
                BODY_WEIGHT
            )
        findNavController().navigate(action)
    }
}
