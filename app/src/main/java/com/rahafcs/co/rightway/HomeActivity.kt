package com.rahafcs.co.rightway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rahafcs.co.rightway.databinding.ActivityHomeBinding
import com.rahafcs.co.rightway.ui.BrowsFragment
import com.rahafcs.co.rightway.ui.CoachesFragment
import com.rahafcs.co.rightway.ui.WorkoutsFragment

class HomeActivity : AppCompatActivity() {
    private val workoutsFragment = WorkoutsFragment()
    private val browsFragment = BrowsFragment()
    private val coachesFragment = CoachesFragment()
    //private val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
    private var binding: ActivityHomeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbar)
        binding?.tabLayout?.setupWithViewPager(binding?.viewPager)
        //viewPagerAdapter.addFragment(workoutsFragment, "Workouts")
        //viewPagerAdapter.addFragment(browsFragment, "Brows")
       // viewPagerAdapter.addFragment(coachesFragment, "Coaches")
        //binding?.viewPager?.adapter = viewPagerAdapter
    }

//    private class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
//        FragmentPagerAdapter(fm, behavior) {
//
//        private val fragments = ArrayList<Fragment>()
//        private val fragmentTitle = ArrayList<String>()
//
//        override fun getCount(): Int {
//            return fragments.size
//        }
//
//        fun addFragment(fragment: Fragment, title: String) {
//            fragments.add(fragment)
//            fragmentTitle.add(title)
//        }
//
//        override fun getItem(position: Int): Fragment {
//            return fragments[position]
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            return fragmentTitle[position]
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
