package com.example.openin_app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openin_app.databinding.ItemClicksHorizontalBinding
import com.example.openin_app.model.TopClicksResponse

class TopClicksAdapter(private val list: List<TopClicksResponse>) :
    RecyclerView.Adapter<TopClicksAdapter.TopClicksViewHolder>() {

    class TopClicksViewHolder(val binding: ItemClicksHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopClicksResponse) {
            // Set image using Glide
            item.image.let {
                if (it != null) {
                    binding.ivClicks.setImageResource(it)
                }
            }

            // Set advantage heading and description
            binding.tvName.text = item.title
            binding.tvDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopClicksViewHolder {
        val binding = ItemClicksHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopClicksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopClicksViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            // Handle item click if needed
            // Example: notify the click to a callback or activity
        }
    }

    override fun getItemCount(): Int = list.size
}
