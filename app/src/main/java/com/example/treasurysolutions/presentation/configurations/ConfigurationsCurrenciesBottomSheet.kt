package com.example.treasurysolutions.presentation.configurations

import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.base.BaseBottomSheet
import com.example.treasurysolutions.databinding.BottomSheetConfigBinding
import com.example.treasurysolutions.datalayer.model.CurrencySelectionModel

class ConfigurationsCurrenciesBottomSheet(
    private val currencies: List<CurrencySelectionModel>,
    private val onClick: (CurrencySelectionModel) -> Unit
) : BaseBottomSheet<BottomSheetConfigBinding>() {

    private var selectedCurrencyId: CurrencySelectionModel? = null
    private var adapter: BottomSheetCurrenciesAdapter? = null

    override fun initBinding(): BottomSheetConfigBinding {
        return BottomSheetConfigBinding.inflate(layoutInflater)
    }

    override fun onBottomSheetCreated() {

        onClickListeners()

        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = BottomSheetCurrenciesAdapter(requireContext(), currencies) { item ->
            selectedCurrencyId = item
            currencies.forEach {

                it.selected = it.id == item.id
            }
            adapter?.updateAdapterList(currencies)
        }
        binding.recycler.adapter = adapter

    }

    override fun getHeight(displayMetrics: DisplayMetrics) =
        (displayMetrics.heightPixels * 0.9).toInt()

    private fun onClickListeners() {
        binding.confirmBtn.setOnClickListener {
            selectedCurrencyId?.let {

                onClick.invoke(it)
                dismiss()
            }
        }
    }
}