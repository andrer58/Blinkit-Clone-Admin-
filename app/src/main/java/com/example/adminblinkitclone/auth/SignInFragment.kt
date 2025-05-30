package com.example.adminblinkitclone.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.databinding.FragmentSignInBinding
import com.example.adminblinkitclone.utils



class SignInFragment : Fragment() {


    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        setStatusBarColor()
        getUserNumber()
        onContinueButtonClick()
        return binding.root
    }

    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()

            if(number.isEmpty() || number.length != 10){
                utils.showToast(requireContext(),"Please enter a valid number")
            }
            else{
                val bundle = Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_signInFragment_to_otpFragment,bundle)
            }
        }
    }


    private fun getUserNumber() {
        binding.etUserNumber.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(number: CharSequence?, start: Int, before: Int, count: Int) {
                val len = number?.length

                if(len == 10) {
                    binding.btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                } else {
                    binding.btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grayish_blue
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }


    private fun setStatusBarColor(){
        activity?.window?.apply{
            val StatusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = StatusBarColors
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}


