package com.example.treasurysolutions.presentation.home.convertfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.sharedpreference.model.BankModel
import com.example.sharedpreference.model.ConvertRatesResponse
import com.example.sharedpreference.model.CurrencyModel
import com.example.treasurysolutions.ConvertAdapter
import com.example.treasurysolutions.ConvertViewModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.FragmentConvertBinding
import com.example.treasurysolutions.datalayer.model.BankSelectionModel
import com.example.treasurysolutions.datalayer.model.CurrencySelectionModel
import com.example.treasurysolutions.presentation.configurations.ConfigurationsBanksBottomSheet
import com.example.treasurysolutions.presentation.configurations.ConfigurationsCurrenciesBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertFragment : Fragment() {

    private lateinit var binding: FragmentConvertBinding
    private val viewModel: ConvertViewModel by viewModels()
    private var banks: List<BankSelectionModel>? = null
    private var currencies: List<CurrencySelectionModel>? = null
    private var bankId: Int? = null
    private var currencyId: Int? = null
    private var fromCurrencyId: Int? = null
    private var toCurrencyId: Int? = null
    private var isBuy = true
    private var convertResponse: ConvertRatesResponse? = null
    private var fromCurrency: CurrencyModel? = null
    private var toCurrency: CurrencyModel? = null
    private var tempCurrency: CurrencyModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentConvertBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedBank()
        viewModel.getSelectedCurrency()
        viewModel.getDollarCurrency()
        viewModel.getSavedBanks()
        viewModel.getSavedCurrencies()
        onBuyButtonClicked()
        onDataObserved()
        onClickListeners()
    }

    private fun onDataObserved() {

        viewModel.savedBankMutableLiveData.observe(viewLifecycleOwner) {
            it?.let { bank ->
                bankId = bank.id
                setSelectedBank(bank)
            }
        }

        viewModel.selectedCurrencyMutableLiveData.observe(viewLifecycleOwner) {
            it?.let { currency ->
                fromCurrency = currency
                fromCurrencyId = currency.id
                setSelectedCurrency(currency)
            }
        }

        viewModel.dollarCurrencyMutableLiveData.observe(viewLifecycleOwner) {
            it?.let { dollar ->
                toCurrency = dollar
                toCurrencyId = dollar.id
                setDollarCurrency(dollar)
            }
        }

        viewModel.convertRatesMutableLiveData.observe(viewLifecycleOwner) {

            hideProgress()
            it.data?.let { convertRate ->
                this.convertResponse = convertRate
                updateConvertResponse()
            }
        }

        viewModel.banksMutableLiveData.observe(viewLifecycleOwner) {
            banks = it
        }

        viewModel.currenciesMutableLiveData.observe(viewLifecycleOwner) {
            currencies = it
        }
    }

    private fun onClickListeners() {
        binding.optionsImage.setOnClickListener {
            banks?.let {
                initBankBottomSheet(it)
            }
        }

        binding.convertToOptionsImage.setOnClickListener {
            currencies?.let {
                initCurrencyBottomSheet(it)
            }
        }

        binding.convertFromOptionsImage.setOnClickListener {
            currencies?.let {
                initCurrencyBottomSheet(it)
            }
        }

        binding.editDollarUnits.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                getConvertRates()
            }
            false
        }

        binding.buyBtn.setOnClickListener {
            onBuyButtonClicked()
            updateConvertResponse()
        }

        binding.sellBtn.setOnClickListener {
            onSellButtonClicked()
            updateConvertResponse()

        }

        binding.swapImage.setOnClickListener {
            toCurrency?.let {
                setSelectedCurrency(it)
            }

            fromCurrency?.let {
                setDollarCurrency(it)
            }

            tempCurrency = toCurrency
            toCurrency = fromCurrency
            fromCurrency = tempCurrency

            getConvertRates()
        }
    }

    private fun initBankBottomSheet(banks: List<BankSelectionModel>) {

        val bottomSheetDialog = ConfigurationsBanksBottomSheet(banks) {

            val bank = viewModel.getBankMapper(it)
            bankId = bank.id
            setSelectedBank(bank)
            showProgress()
            getConvertRates()
        }
        bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)

    }

    private fun initCurrencyBottomSheet(currencies: List<CurrencySelectionModel>) {

        val bottomSheetDialog = ConfigurationsCurrenciesBottomSheet(currencies) {

            val currency = viewModel.getCurrencyMapper(it)
            currencyId = currency.id
            setSelectedCurrency(currency)
            showProgress()
            getConvertRates()
        }
        bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)

    }

    private fun setSelectedBank(bank: BankModel) {
        binding.textChange.text = bank.name
    }

    private fun setSelectedCurrency(currency: CurrencyModel) {
        binding.textFromAbb.text = currency.abbreviation
        binding.textFromName.text = currency.name

        Glide.with(this)
            .load(currency.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_global)
            .into(binding.imageFromLogo)
    }

    private fun setDollarCurrency(currency: CurrencyModel) {
        binding.textToAbb.text = currency.abbreviation
        binding.textToName.text = currency.name

        Glide.with(this)
            .load(currency.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_global)
            .into(binding.imageToLogo)
    }

    private fun updateConvertResponse() {
        binding.generalItem.textAbb.text = convertResponse?.convertedBank?.bank?.abbreviation
        binding.generalItem.textName.text = convertResponse?.convertedBank?.bank?.name
        binding.generalItem.textPrice.text =
            if (isBuy)
                convertResponse?.convertedBank?.buyPrice.toString()
            else
                convertResponse?.convertedBank?.sellPrice.toString()

        Glide.with(this)
            .load(convertResponse?.convertedBank?.bank?.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_global)
            .into(binding.generalItem.imageLogo)

        convertResponse?.topBanks?.let {

            binding.recycler.adapter = ConvertAdapter(requireContext(), it, isBuy)
        }
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    private fun onBuyButtonClicked() {
        isBuy = true
        binding.buyBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green
            )
        )
        binding.buyBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.sellBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.sellBtn.setStrokeColorResource(R.color.light_green)
        binding.sellBtn.strokeWidth = 2
        binding.sellBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_black))

    }

    private fun onSellButtonClicked() {
        isBuy = false
        binding.sellBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green
            )
        )
        binding.sellBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.buyBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.buyBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_black
            )
        )
        binding.buyBtn.setStrokeColorResource(R.color.light_green)
        binding.buyBtn.strokeWidth = 2

    }

    private fun getConvertRates() {
        fromCurrency?.id?.let { fromCurrencyId ->
            toCurrency?.id?.let { toCurrencyId ->
                bankId?.let { bankId ->
                    binding.editDollarUnits.text?.let { value ->
                        if (value.toString().isNotEmpty() && value.toString()
                                .isNotBlank()
                        ) {
                            showProgress()
                            viewModel.getConvertRates(
                                fromCurrencyId,
                                toCurrencyId,
                                bankId,
                                value.toString().toInt()
                            )
                        }
                    }
                }
            }
        }
    }
}