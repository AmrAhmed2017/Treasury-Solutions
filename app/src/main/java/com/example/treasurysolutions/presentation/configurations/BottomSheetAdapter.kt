package com.example.treasurysolutions.presentation.configurations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurysolutions.databinding.BottomSheetItemBinding
import com.example.treasurysolutions.datalayer.model.BankSelectionModel

class BottomSheetAdapter(
    private val context: Context,
    private var banks: List<BankSelectionModel>,
    private val onClick: (BankSelectionModel) -> Unit
) :
    RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(banks[position])
    }

    override fun getItemCount() = banks.size

    override fun getItemViewType(position: Int) = position

    fun updateAdapterList(banks: List<BankSelectionModel>){
        this.banks = banks
        notifyDataSetChanged()
    }

    inner class BottomSheetViewHolder(private val binding: BottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BankSelectionModel) {

            binding.textAbb.text = item.abbreviation
            binding.textName.text = item.name
            binding.radioBtn.isChecked = item.selected

            binding.radioBtn.setOnClickListener {

                onClick.invoke(item)

            }
        }
    }
}