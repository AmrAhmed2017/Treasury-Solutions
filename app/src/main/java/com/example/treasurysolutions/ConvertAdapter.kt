package com.example.treasurysolutions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.ConvertRatesResponse
import com.example.sharedpreference.model.ConvertedBankExchangeRateModel
import com.example.sharedpreference.model.EGPCurrenciesRatesModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.BankRatesItemBinding
import com.example.treasurysolutions.databinding.GeneralItemBinding
import kotlin.math.round

class ConvertAdapter(
    private val context: Context,
    private val convertRates: List<ConvertedBankExchangeRateModel>,
    private val isBuy: Boolean
) :
    RecyclerView.Adapter<ConvertAdapter.ConvertViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertViewHolder {
        return ConvertViewHolder(
            GeneralItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ConvertViewHolder, position: Int) {
        holder.bind(convertRates[position])
    }

    override fun getItemCount() = convertRates.size

    override fun getItemViewType(position: Int) = position

    inner class ConvertViewHolder(private val binding: GeneralItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConvertedBankExchangeRateModel) {

            binding.textAbb.text = item.bank?.abbreviation
            binding.textName.text = item.bank?.name

            if (isBuy) {

                item.buyPrice?.let {
                    binding.textPrice.text = String.format("%.2f", it)
                }
            } else
                item.sellPrice?.let {
                    binding.textPrice.text = String.format("%.2f", it)
                }

            Glide.with(context)
                .load(item.bank?.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)

        }
    }
}