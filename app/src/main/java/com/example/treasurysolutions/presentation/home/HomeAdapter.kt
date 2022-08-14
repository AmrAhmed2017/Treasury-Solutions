package com.example.treasurysolutions.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.HomeItemBinding
import com.example.sharedpreference.model.ToCurrencyModel

class HomeAdapter(
    private val context: Context,
//    private val currencies: List<ToCurrencyModel>,
    private val onClick: (ToCurrencyModel) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var currencies = mutableListOf<ToCurrencyModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            HomeItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount() = currencies.size

    override fun getItemViewType(position: Int) = position

    inner class HomeViewHolder(private val binding: HomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToCurrencyModel) {

            binding.textRate.text = String.format("%.2f", item.sellPrice)
            binding.textName.text = item.name
            binding.textExchange.text = String.format(context.getString(R.string._1_s_1_egp), item.name)

            Glide.with(context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)

            binding.container.setOnClickListener {
                onClick.invoke(item)
            }
        }

    }
}