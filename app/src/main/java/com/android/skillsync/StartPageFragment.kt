package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.FragmentStartPageBinding
import com.google.firebase.auth.FirebaseAuth

class StartPageFragment : BaseFragment() {

    private var userAuthViewModel: UserAuthViewModel? = null

    private var _binding: FragmentStartPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        view = binding.root
        userAuthViewModel = UserAuthViewModel()

        setEventListeners()
        return view
    }

    private fun setEventListeners() {

        binding.buttonSignIn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_startPageFragment_to_signInFragment)
        }

        binding.buttonSignUp.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_startPageFragment_to_pickUserTypeFragment)
        }

        view.post {
            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                userAuthViewModel?.setCurrentUserId()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_startPageFragment_to_mapViewFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
