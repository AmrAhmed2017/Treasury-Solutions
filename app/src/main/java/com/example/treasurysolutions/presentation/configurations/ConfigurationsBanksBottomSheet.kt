package com.example.treasurysolutions.presentation.configurations

import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.base.BaseBottomSheet
import com.example.sharedpreference.model.BankModel
import com.example.treasurysolutions.databinding.BottomSheetConfigBinding
import com.example.treasurysolutions.datalayer.model.BankSelectionModel

class ConfigurationsBanksBottomSheet(
    private val banks: List<BankSelectionModel>,
    private val onClick: (BankSelectionModel) -> Unit
) : BaseBottomSheet<BottomSheetConfigBinding>() {

    private var selectedBank: BankSelectionModel? = null
    private var adapter: BottomSheetAdapter? = null

    override fun initBinding(): BottomSheetConfigBinding {
        return BottomSheetConfigBinding.inflate(layoutInflater)
    }

    override fun onBottomSheetCreated() {

        onClickListeners()

        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = BottomSheetAdapter(requireContext(), banks) { item ->
            selectedBank = item
            banks.forEach {

                it.selected = it.id == item.id
            }
            adapter?.updateAdapterList(banks)
        }
        binding.recycler.adapter = adapter

    }

    override fun getHeight(displayMetrics: DisplayMetrics) =
        (displayMetrics.heightPixels * 0.9).toInt()

    private fun onClickListeners() {
        binding.confirmBtn.setOnClickListener {
            selectedBank?.let {

                onClick.invoke(it)
                dismiss()
            }
        }
    }
}