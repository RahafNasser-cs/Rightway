package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentWeightBinding

class WeightFragment : Fragment() {
    private var binding: FragmentWeightBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeightBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            weightFragment = this@WeightFragment
        }
    }

    private fun goToAgePage() {
        findNavController().navigate(R.id.action_weightFragment_to_ageFragment)
    }

    fun userWeightByPound() {
        // send user weight to viewModel TODO()
        goToAgePage()
    }

    fun userWeightByKilogram() {
        // send user weight to viewModel TODO()
        goToAgePage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
