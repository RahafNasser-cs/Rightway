package com.rahafcs.co.rightway.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentUserInfoSettingsBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.FIRST_NAME
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SIGN_IN
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SUPERSCRIPTION
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.USERID
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class UserInfoSettingsFragment : Fragment() {
    var binding: FragmentUserInfoSettingsBinding? = null
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
        binding = FragmentUserInfoSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signOut.setOnClickListener { signOut() }
            goToHome.setOnClickListener { goToHomePage() }
        }
        readUserInfo()
        // showUserInfo()
    }

    private fun readUserInfo() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                viewModel.setUserInfo()

                viewModel.readUserInfo(sharedPreferences.getString(FIRST_NAME, "")!!).collect {
                    Log.d("UserInfoSettingsFragment", "readUserInfo: $it")
                    // viewModel.setUserInfo(it)
                    showUserInfo(it)
                }
            }
        }
    }

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.putBoolean(SIGN_IN, false)
        editor.apply()
        requireContext().toast("${FirebaseAuth.getInstance().currentUser?.email}")
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.registrationFragment)
    }

    private fun showUserInfo(userInfo: User) {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val message = "User Name: ${
        sharedPreferences.getString(
            FIRST_NAME,
            "defName"
        )
        }\nUser id: ${
        sharedPreferences.getString(
            USERID,
            "defUserId"
        )
        }\nUser status: ${sharedPreferences.getString(SUPERSCRIPTION, "defStatus")}"
        binding?.apply {
            userNameTextview.text = userInfo.firstName
            userHeight.text = userInfo.height
            userWeight.text = userInfo.weight
            userAge.text = userInfo.age
            userGender.text = userInfo.gender
            userActivityLeve.text = userInfo.activity
            subscriptionStatus.text = userInfo.subscriptionStatus
        }
        // binding?.userInfoTextview?.text = message
    }

    private fun goToHomePage() {
        // findNavController().navigate(R.id.action_homeFragment_to_homeActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
