package com.example.treasurysolutions.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.EGPCurrenciesRatesModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.BankRatesItemBinding
import kotlin.math.round

class CurrenciesRatesAdapter(
    private val context: Context,
    private val currencies: List<EGPCurrenciesRatesModel>,
) :
    RecyclerView.Adapter<CurrenciesRatesAdapter.HomeViewHolder>() {


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

        fun bind(item: EGPCurrenciesRatesModel) {

            binding.textAbb.text = item.bank?.abbreviation
            binding.textName.text = item.bank?.name
            item.buyPrice?.let {

                binding.textBuy.text = String.format("%.2f", it)
            }

            item.sellPrice?.let {

                binding.textSell.text = String.format("%.2f", it)
            }

            Glide.with(context)
                .load(item.bank?.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)

        }
    }
}