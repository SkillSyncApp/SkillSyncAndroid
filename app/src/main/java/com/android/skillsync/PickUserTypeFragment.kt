package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation

class PickUserTypeFragment : Fragment() {

    private lateinit var companyCard: CardView
    private lateinit var studentCard: CardView

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
        companyCard = view.findViewById(R.id.company_card)
        studentCard = view.findViewById(R.id.student_card)

        // TODO from navigation
        companyCard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_pickUserTypeFragment_to_signUpCompanyFragment)
        }

        studentCard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_pickUserTypeFragment_to_signUpStudentFragment)
        }
    }
}
