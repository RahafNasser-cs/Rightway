package com.rahafcs.co.rightway.ui.trainee

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentSendEmailBinding
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendEmailFragment : Fragment() {
    private var binding: FragmentSendEmailBinding? = null
    private val emailViewModel by activityViewModels<EmailViewModel>()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            emailViewModel = this@SendEmailFragment.emailViewModel
            backArrow.setOnClickListener { this@SendEmailFragment.upToTop() }
            emailAddressEditText.setText(args.coachEmail)
            sendBtn.setOnClickListener {
                sendEmail()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
    }

    // Handel back press.
    private fun onBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    // To complete send email process.
    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail() {
        val address = getEmail().split(",".toRegex()).toTypedArray()
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, address)
            putExtra(Intent.EXTRA_SUBJECT, getSubject())
            putExtra(Intent.EXTRA_TEXT, getMessage())
            type = "message/rfc822"
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            requireContext().toast(getString(R.string.required_app))
        }
    }

    // Get data from views.
    private fun getMessage() = binding?.messageEditText?.text.toString()
    private fun getSubject() = binding?.subjectEditText?.text.toString()
    private fun getEmail() = binding?.emailAddressEditText?.text.toString()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
