package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class PickUserTypeFragment : Fragment() {

    var signUpAsCompany: Button? = null
    var signUpAsGroup: Button? = null

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.fragment_pick_user_type, container, false)
        setEventListeners()

        return view
    }

    private fun setEventListeners() {
        signUpAsCompany = view.findViewById(R.id.sign_up_company)
        signUpAsGroup = view.findViewById(R.id.sign_up_group)

        signUpAsCompany?.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_pickUserTypeFragment_to_signUpCompanyFragment)
        }

        signUpAsGroup?.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_pickUserTypeFragment_to_signUpGroupFragment)
        }
    }
}
