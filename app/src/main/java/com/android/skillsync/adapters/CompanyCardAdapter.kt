package com.android.skillsync.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.CompaniesCardRecyclerView
import com.android.skillsync.R
import com.android.skillsync.models.Comapny.Company

class CompanyCardAdapter(var companies: MutableList<Company>?) : RecyclerView.Adapter<CompanyViewHolder>() {

    var listener: CompaniesCardRecyclerView.OnCompanyItemClickListener? = null

    override fun getItemCount(): Int = companies?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false);
        return CompanyViewHolder(itemView, listener, companies);
    }


    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companies?.get(position)
        holder.bind(company)
    }
}
