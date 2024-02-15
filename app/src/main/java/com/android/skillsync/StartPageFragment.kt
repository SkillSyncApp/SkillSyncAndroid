package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class StartPageFragment : BaseFragment() {

    var signInButton: Button? = null
    var signUpButton: Button? = null

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_start_page, container, false)

        setEventListeners()

        return view
    }

    private fun setEventListeners() {
        signInButton = view.findViewById(R.id.button_sign_in)
        signUpButton = view.findViewById(R.id.button_sign_up)

        signInButton?.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_startPageFragment_to_signInFragment)
        }

        signUpButton?.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_startPageFragment_to_pickUserTypeFragment)
        }
    }
}
