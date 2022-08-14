package com.example.treasurysolutions.presentation.home.metalfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.SilverModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.MetalItemBinding

class SilverAdapter(
    private val context: Context,
    private val silverList: List<SilverModel>,
) :
    RecyclerView.Adapter<SilverAdapter.GoldViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoldViewHolder {
        return GoldViewHolder(
            MetalItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GoldViewHolder, position: Int) {
        holder.bind(silverList[position])
    }

    override fun getItemCount() = silverList.size

    override fun getItemViewType(position: Int) = position

    inner class GoldViewHolder(private val binding: MetalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SilverModel) {

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