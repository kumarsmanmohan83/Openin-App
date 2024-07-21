package com.example.openin_app.view.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openin_app.databinding.ItemLinksDetailsBinding
import com.example.openin_app.model.LinkResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TopLinksAdapter(private val list: List<LinkResponse>, var listener: OnItemClickListener) :
    RecyclerView.Adapter<TopLinksAdapter.TopClicksViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    class TopClicksViewHolder(val binding: ItemLinksDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: LinkResponse) {
            // Set image using Glide
            item.original_image.let {
                Glide.with(binding.root)
                    .load(it)
                    .into(binding.ivClicks)
            }

            // Set advantage heading and description
            binding.tvName.text = item.app
            binding.tvDescription.text = formatDate(item.created_at)
            binding.tvClickCount.text = item.total_clicks.toString()
            binding.tvLinks.text = item.smart_link

        }
        private fun formatDate(inputDate: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            val parsedDate = ZonedDateTime.parse(inputDate, inputFormatter)
            return parsedDate.format(outputFormatter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopClicksViewHolder {
        val binding = ItemLinksDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.tvLinks.setOnClickListener {
            listener.onItemClick(list[0].smart_link)
        }
        return TopClicksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopClicksViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(link: String)
    }
}