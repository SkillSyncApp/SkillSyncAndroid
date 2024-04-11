package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

class PickUserTypeFragment : BaseFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.back_button)


        backButton.setOnClickListener {
            findNavController().navigateUp()        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
