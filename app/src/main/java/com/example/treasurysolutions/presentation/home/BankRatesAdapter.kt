package com.example.treasurysolutions.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.EGPCurrenciesRatesModel
import com.example.sharedpreference.model.ToCurrencyModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.BankRatesItemBinding
import kotlin.math.round

class BankRatesAdapter(
    private val context: Context,
    private val currencies: List<ToCurrencyModel>,
) :
    RecyclerView.Adapter<BankRatesAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            BankRatesItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount() = currencies.size

    override fun getItemViewType(position: Int) = position

    inner class HomeViewHolder(private val binding: BankRatesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToCurrencyModel) {

            binding.textName.text = item.name

                binding.textBuy.text = String.format("%.2f", item.buyPrice)


                binding.textSell.text = String.format("%.2f", item.sellPrice)

            Glide.with(context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)

        }
    }
}