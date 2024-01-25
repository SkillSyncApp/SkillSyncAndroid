package com.android.skillsync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.adapters.CompanyCardAdapter
import com.android.skillsync.databinding.FragmentCompaniesCardRecyclerViewBinding
import com.android.skillsync.models.Comapny.Company

class CompaniesCardRecyclerView : Fragment() {

    private var onCompanyItemClickListener: OnCompanyItemClickListener? = null
    private var companyViewModel: CompanyViewModel? = null
    private var companiesRecyclerView: RecyclerView? = null
    private var adapter: CompanyCardAdapter? = null

    private var _binding: FragmentCompaniesCardRecyclerViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompaniesCardRecyclerViewBinding.inflate(inflater, container, false)
        val view = binding.root

        companyViewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)
        companiesRecyclerView = view.findViewById(R.id.cardsCompanyRecyclerView)

        companiesRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        adapter = CompanyCardAdapter(mutableListOf())
        companiesRecyclerView?.adapter = adapter

        Log.d("COUNT ITEM", " "+ adapter?.itemCount)

        companyViewModel?.getAllCompanies()?.observe(viewLifecycleOwner) {
            adapter?.companies = it
        }

        adapter?.listener = object : OnCompanyItemClickListener {
            override fun onCompanyClicked(company: Company?) {
                onCompanyItemClickListener?.onCompanyClicked(company)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    private fun reloadData() {
        companyViewModel?.refreshCompanies()
        adapter?.companies = companyViewModel?.getAllCompanies()?.value

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnCompanyItemClickListener {
        fun onCompanyClicked(company: Company?)
    }
}
