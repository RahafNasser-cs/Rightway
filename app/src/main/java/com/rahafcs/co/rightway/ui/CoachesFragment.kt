package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rahafcs.co.rightway.databinding.FragmentCoachesBinding
import com.rahafcs.co.rightway.viewmodels.CoachViewModel

class CoachesFragment : Fragment() {
    private var binding: FragmentCoachesBinding? = null
    private val viewModel: CoachViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoachesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            coachesFragment = this@CoachesFragment
            coachViewModel = viewModel
            recyclerview.adapter = CoachAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
