package com.android.skillsync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.models.ComapnyPost

class CarouselAdapter(private val posts: List<ComapnyPost>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val post = posts[position]
        holder.companyName.text = post.companyName
        holder.description.text = post.description
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyName: TextView = itemView.findViewById(R.id.companyName);
        val description: TextView = itemView.findViewById(R.id.descriptionPost);
    }
}
