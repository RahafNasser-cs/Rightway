package com.rahafcs.co.rightway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.rahafcs.co.rightway.databinding.FragmentViewPagerBinding
import com.rahafcs.co.rightway.ui.BrowsFragment
import com.rahafcs.co.rightway.ui.CoachesFragment
import com.rahafcs.co.rightway.ui.WorkoutsFragment

class ViewPagerFragment : Fragment() {
    var binding: FragmentViewPagerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val adapter = ViewPagerAdapter(
            getFragmentList(),
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding?.viewPager?.adapter = adapter

        TabLayoutMediator(binding?.tabLayout!!, binding?.viewPager!!) { tab, position ->
            when (position) {
                0 -> tab.text = "Workouts"
                1 -> tab.text = "Brows"
                2 -> tab.text = "Coaches"
            }
        }.attach()

        return binding?.root
    }

    private fun getFragmentList(): ArrayList<Fragment> = arrayListOf(
        WorkoutsFragment(),
        BrowsFragment(),
        CoachesFragment()
    )
}
