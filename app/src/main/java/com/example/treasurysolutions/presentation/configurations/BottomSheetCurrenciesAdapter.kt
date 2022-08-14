package com.example.treasurysolutions.presentation.configurations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurysolutions.databinding.BottomSheetItemBinding
import com.example.treasurysolutions.datalayer.model.CurrencySelectionModel

class BottomSheetCurrenciesAdapter(
    private val context: Context,
    private var currencies: List<CurrencySelectionModel>,
    private val onClick: (CurrencySelectionModel) -> Unit
) :
    RecyclerView.Adapter<BottomSheetCurrenciesAdapter.BottomSheetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount() = currencies.size

    override fun getItemViewType(position: Int) = position

    fun updateAdapterList(currencies: List<CurrencySelectionModel>){
        this.currencies = currencies
        notifyDataSetChanged()
    }

    inner class BottomSheetViewHolder(private val binding: BottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencySelectionModel) {

            binding.textAbb.text = item.abbreviation
            binding.textName.text = item.name
            binding.radioBtn.isChecked = item.selected

            binding.radioBtn.setOnClickListener {

                onClick.invoke(item)
            }
        }
    }
}