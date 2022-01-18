package com.rahafcs.co.rightway.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentGenderBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.GENDER

class GenderFragment : Fragment() {
    private var binding: FragmentGenderBinding? = null
    lateinit var sharedPreferences: SharedPreferences

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
        addToSharedPreference("Female")
        goToHeightPage()
    }

    fun userGenderIsMale() {
        // send info "male" to viewModel TODO()
        addToSharedPreference("Male")
        goToHeightPage()
    }

    private fun addToSharedPreference(
        gender: String,
    ) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(GENDER, gender)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
