package com.example.treasurysolutions.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.BankModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.HomeItemBinding
import com.example.sharedpreference.model.ToCurrencyModel
import com.example.treasurysolutions.databinding.BankItemBinding

class BankAdapter(
    private val context: Context,
    private val banks: List<BankModel>,
    private val onClick: (bankModel: BankModel) -> Unit
) :
    RecyclerView.Adapter<BankAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            BankItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(banks[position])
    }

    override fun getItemCount() = banks.size

    override fun getItemViewType(position: Int) = position

    inner class HomeViewHolder(private val binding: BankItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BankModel) {

            binding.textAbb.text = item.abbreviation
            binding.textName.text = item.name
            binding.container.setOnClickListener {
                onClick.invoke(item)
            }
            Glide.with(context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)

        }
    }
}