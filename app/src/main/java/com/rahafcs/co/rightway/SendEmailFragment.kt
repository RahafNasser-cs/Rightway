package com.rahafcs.co.rightway

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentSendEmailBinding
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop
import com.rahafcs.co.rightway.viewmodels.EmailViewModel
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SendEmailFragment : Fragment() {
    private var binding: FragmentSendEmailBinding? = null
    private val viewModel by activityViewModels<SignUpViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }
    private val emailViewModel by activityViewModels<EmailViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }
    private val args: SendEmailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendEmailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readUserInfo()
        showPreSubject()
        binding?.backArrow?.setOnClickListener { this.upToTop() }
        binding?.emailAddressEditText?.setText(args.coachEmail)
        binding?.emailViewModel = emailViewModel
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.sendBtn?.setOnClickListener {

            val email = binding?.emailAddressEditText?.text.toString()
            val subject = binding?.subjectEditText?.text.toString()
            val message = binding?.messageEditText?.text.toString()
            val address = email.split(",".toRegex()).toTypedArray()

            val intent = Intent(Intent.ACTION_SEND).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, address)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                type = "message/rfc822"
            }

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                requireContext().toast("Required app is not installed")
            }
        }
    }

    private fun readUserInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.readUserInfo().collect {
                    showUserMessage(it)
                }
            }
        }
    }

    private fun showUserMessage(userInfo: User) {
        // binding?.messageEditText?.setText("")
        val preMessage =
            "Hi I'm ${userInfo.firstName}\nI would like to subscribe with you!" +
                "\nSome info about me:\nGender: ${userInfo.gender}" +
                "\nHeight: ${userInfo.height}\nWeight: ${userInfo.weight}" +
                "\nAge: ${userInfo.age}\nActivity level: ${userInfo.activity}\n"
        val message = preMessage + binding?.messageEditText?.text.toString()
        // emailViewModel.setPreMessage(preMessage)
        binding?.messageEditText?.setText(preMessage)
    }
    private fun showPreSubject(){
        val preSubject = "Hello, I would like to subscribe with you!"
        binding?.subjectEditText?.setText(preSubject)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
