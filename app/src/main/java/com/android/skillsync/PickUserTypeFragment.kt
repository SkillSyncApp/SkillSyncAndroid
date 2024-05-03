package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.android.skillsync.Navigations.navigate
import com.android.skillsync.databinding.FragmentPickUserTypeBinding

class PickUserTypeFragment : BaseFragment() {

    private lateinit var companyCard: CardView
    private lateinit var studentCard: CardView

    private lateinit var view: View
    private var _binding: FragmentPickUserTypeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickUserTypeBinding.inflate(inflater, container, false)
        view = binding.root
        setEventListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.back_button)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setEventListeners() {
        companyCard = view.findViewById(R.id.company_card)
        studentCard = view.findViewById(R.id.student_card)

        // TODO from navigation
        companyCard.setOnClickListener {
            view.navigate(R.id.action_pickUserTypeFragment_to_signUpCompanyFragment)
        }

        studentCard.setOnClickListener {
            view.navigate(R.id.action_pickUserTypeFragment_to_signUpStudentFragment)
        }
    }
}