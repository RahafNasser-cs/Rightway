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
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SIGN_IN
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collect
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
            logoutImg.setOnClickListener { signOut() }
            homeImg.setOnClickListener { goToHomePage() }
            saveBtn.setOnClickListener { readUserInfo() }
        }
        readUserInfo()
    }

    private fun readUserInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.readUserInfo().collect {
                    Log.d("UserInfoSettingsFragment", "readUserInfo: $it")
                    showUserInfo(it)
//                    viewModel.setUserInfo(it)
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
        binding?.apply {
            userNameTextview.text = userInfo.firstName
            userHeightEditText.setText(userInfo.height)
            userWeightEditText.setText(userInfo.weight)
            userAgeEditText.setText(userInfo.age)
            userGenderEditText.setText(userInfo.gender)
            userActivityLeveEditText.setText(userInfo.activity)
            subscriptionStatus.text = userInfo.subscriptionStatus
        }
        updateUserInfo(userInfo)
    }

    private fun updateUserInfo(userInfo: User) {
        if (checkIfUserInfoChanged(userInfo)) {
            binding?.saveBtn?.isEnabled = true
            binding?.saveBtn?.setOnClickListener {
                saveUserInfo(userInfo)
                requireContext().toast("Saved changes")
            }
        }
    }

    private fun saveUserInfo(userInfo: User) {
        viewModel.userInfo(getUpdatedUserInfo(userInfo))
    }

    private fun getUpdatedUserInfo(oldUserInfo: User): User {
        return oldUserInfo.copy(
            height = binding?.userHeightEditText?.text.toString(),
            weight = binding?.userWeightEditText?.text.toString(),
            age = binding?.userAgeEditText?.text.toString()
        )
    }

    private fun checkIfUserInfoChanged(userInfo: User): Boolean {
        return binding?.let {
            if (userInfo.height != it.userHeightEditText.text.toString()) {
                true
            } else if (userInfo.weight != it.userWeightEditText.text.toString()) {
                true
            } else if (userInfo.age != it.userAgeEditText.text.toString()) {
                true
            } else {
                false
            }
        } ?: false
    }

    private fun goToHomePage() {
        findNavController().navigate(R.id.action_userInfoSettingsFragment2_to_viewPagerFragment2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
