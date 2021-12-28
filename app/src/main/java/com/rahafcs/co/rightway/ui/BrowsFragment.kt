package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentBrowsBinding
import com.rahafcs.co.rightway.utility.toast

class BrowsFragment : Fragment() {

    private var binding: FragmentBrowsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        }
        binding?.equipmentCard?.setOnClickListener {
            showMenu()
//            shownMenu(it, R.menu.equipment_menu)
        }
        binding?.barbell?.setOnClickListener {  showWorkoutWithBarbell()}
        binding?.cable?.setOnClickListener { showWorkoutWithCable() }
        binding?.allEquipment?.setOnClickListener { showWorkoutWithAllEquipment() }
        binding?.kettleBell?.setOnClickListener { showWorkoutWithKettleBell() }
    }

    private fun showMenu() {
        if (binding?.expandMenu?.visibility == View.GONE) {
            binding?.expandMenu?.visibility = View.VISIBLE
        } else {
            binding?.expandMenu?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.equipment_menu, menu)
    }

    private fun showWorkoutWithAllEquipment() {
        requireContext().toast("Show workout with all equipment")
    }

    private fun showWorkoutWithBarbell() {
        requireContext().toast("Show workout with barbell")
    }

    private fun showWorkoutWithCable() {
        requireContext().toast("Show workout with cable")
    }

    private fun showWorkoutWithDumbbell() {
        requireContext().toast("Show workout with dumbbell")
    }

    private fun showWorkoutWithKettleBell() {
        requireContext().toast("Show workout with Kettle bell")
    }

    private fun showWorkoutWithResistanceBand() {
        requireContext().toast("Show workout with Resistance band")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }
}
