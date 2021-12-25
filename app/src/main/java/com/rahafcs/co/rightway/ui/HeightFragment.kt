package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentHeightBinding

class HeightFragment : Fragment() {
    private var binding: FragmentHeightBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeightBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            heightFragment = this@HeightFragment
        }
    }

    private fun goToWeightPage() {
        findNavController().navigate(R.id.action_heightFragment_to_weightFragment)
    }

    fun userHeightByFeet() {
        if (binding?.heightEditText?.text.toString().isNotEmpty()) {
            // send to viewModel TODO()
        }
        goToWeightPage()
    }

    fun userHeightByCentimeter() {
        if (binding?.heightEditText?.text.toString().isNotEmpty()) {
            // send to viewModel TODO()
        }
        goToWeightPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
