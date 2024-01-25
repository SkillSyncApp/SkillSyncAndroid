package com.android.skillsync.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.CompaniesCardRecyclerView
import com.android.skillsync.models.Comapny.Company

class CompanyViewHolder(itemView: View,
                        val listener: CompaniesCardRecyclerView.OnCompanyItemClickListener?,
                        var companies: MutableList<Company>?): RecyclerView.ViewHolder(itemView) {

    var companyName: TextView? = null
    var companyBio: TextView? = null
    var companyEmail: TextView? = null
    val companyAddress: TextView? = null

    var comapny: Company? = null

        init {
        itemView.setOnClickListener {
            val company = companies?.get(adapterPosition)
            listener?.onCompanyClicked(company)
        }
    }

    fun bind(company: Company?) {
        this.comapny = company
        companyName?.text = company?.name
        companyBio?.text = company?.bio
        companyEmail?.text = company?.email
        companyAddress?.text = company?.location?.address
    }
}
