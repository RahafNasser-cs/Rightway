package com.rahafcs.co.rightway.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentHomeBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.ACTIVITY_LEVEL
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.FIRSTNAME
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.GENDER
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.HEIGHT
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SIGN_IN
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SIGN_UP
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SUPERSCRIPTION
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.USERID
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.WEIGHT
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory

class HomeFragment : Fragment() {
    var binding: FragmentHomeBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    val viewModel: SignUpViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            homeFragment = this@HomeFragment
        }
        saveUserInfo()
        showUserInfo()
    }

    private fun saveUserInfo() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        if (sharedPreferences.getBoolean(SIGN_UP, false)) {
            viewModel.userInfo(getUserInfo())
            val editor = sharedPreferences.edit()
            editor.putBoolean(SIGN_UP, false)
            editor.apply()
        }
    }

    fun readUserInfo() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        viewModel.readUserInfo(sharedPreferences.getString(FIRSTNAME, "")!!)
    }

    fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.putBoolean(SIGN_IN, false)
        editor.apply()
        requireContext().toast("${FirebaseAuth.getInstance().currentUser?.email}")
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.registrationFragment)
    }

    private fun getUserInfo(): User {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        return User(
            id = sharedPreferences.getString(USERID, "")!!,
            firstName = sharedPreferences.getString(FIRSTNAME, "")!!,
            lastName = "",
            // subscriptionStatus = sharedPreferences.getString(SUPERSCRIPTION, "") as SubscriptionStatus,
            weight = sharedPreferences.getString(WEIGHT, "")!!,
            height = sharedPreferences.getString(HEIGHT, "")!!,
            gender = sharedPreferences.getString(GENDER, "")!!,
            activity = sharedPreferences.getString(ACTIVITY_LEVEL, "")!!
        )
    }

    private fun showUserInfo() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val message = "User Name: ${
        sharedPreferences.getString(
            FIRSTNAME,
            "defName"
        )
        }\nUser id: ${
        sharedPreferences.getString(
            USERID,
            "defUserId"
        )
        }\nUser status: ${sharedPreferences.getString(SUPERSCRIPTION, "defStatus")}"
        binding?.userInfoTextview?.text = message
    }

    fun goToHomePage() {
        findNavController().navigate(R.id.action_homeFragment_to_homeActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
