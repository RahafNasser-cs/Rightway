package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentGenderBinding

class GenderFragment : Fragment() {
    private var binding: FragmentGenderBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGenderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            genderFragment = this@GenderFragment
        }
    }

    private fun goToHeightPage() {
        findNavController().navigate(R.id.action_genderFragment_to_heightFragment)
    }

    fun userGenderIsFemale() {
        // send info "female" to viewModel TODO()
        goToHeightPage()
    }

    fun userGenderIsMale() {
        // send info "male" to viewModel TODO()
        goToHeightPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
