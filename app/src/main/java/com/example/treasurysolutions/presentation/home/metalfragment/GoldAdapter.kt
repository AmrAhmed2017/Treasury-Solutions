package com.example.treasurysolutions.presentation.home.metalfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.GoldModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.MetalItemBinding

class GoldAdapter(
    private val context: Context,
    private val goldList: List<GoldModel>,
) :
    RecyclerView.Adapter<GoldAdapter.GoldViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoldViewHolder {
        return GoldViewHolder(
            MetalItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GoldViewHolder, position: Int) {
        holder.bind(goldList[position])
    }

    override fun getItemCount() = goldList.size

    override fun getItemViewType(position: Int) = position

    inner class GoldViewHolder(private val binding: MetalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GoldModel) {

            binding.textTitle.text = item.name
            binding.textName.text = item.name
            binding.textPrice.text = item.price

            Glide.with(context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)
        }
    }
}