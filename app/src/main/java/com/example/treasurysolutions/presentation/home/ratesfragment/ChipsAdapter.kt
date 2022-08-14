package com.example.treasurysolutions.presentation.home.ratesfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedpreference.Constant
import com.example.sharedpreference.model.ChipModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.ChipAdapterItemBinding

class ChipsAdapter(
    private val context: Context,
    private val onClick: (ChipModel) -> Unit
) : RecyclerView.Adapter<ChipsAdapter.ChipsViewHolder>() {

    var list = mutableListOf(
        ChipModel(1, Constant.WEEK, true),
        ChipModel(2, Constant.MONTH, false),
        ChipModel(3, Constant.YEAR, false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsViewHolder {
        return ChipsViewHolder(
            ChipAdapterItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChipsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int) = position

    inner class ChipsViewHolder(private val binding: ChipAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chip: ChipModel) {

            binding.chip.text = chip.name
            setSelectedStatus(binding, chip.isSelected)

            binding.chip.setOnClickListener {

                onClick.invoke(chip)
            }
        }
    }

    private fun setSelectedStatus(binding: ChipAdapterItemBinding, isSelected: Boolean) {
        binding.chip.isSelected = isSelected

        if (isSelected) {
            binding.chip.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.chip.chipBackgroundColor =
                context.getColorStateList(R.color.light_green)
        } else {
            binding.chip.setTextColor(ContextCompat.getColor(context, R.color.light_black))
            binding.chip.chipBackgroundColor =
                context.getColorStateList(R.color.white)
        }
    }

    fun updateAdapter() {
//        this.list.clear()
//        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
