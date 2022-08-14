package com.example.treasurysolutions.presentation.configurations

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.ActivityConfiguratiosBinding
import com.example.treasurysolutions.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguratiosBinding
    private lateinit var viewModel: ConfigurationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguratiosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initView()
        viewModel.getConfigurations()
        onObserveData()
        onClickListeners()
    }

    private fun initView() {
        showProgress()
        binding.currencyCard.textLabel.text = getString(R.string.currency)
        binding.currencyCard.textChoice.text =
            viewModel.configurationsRepo.sharedPrefManager.getSelectedCurrency()?.name ?: getString(R.string.egypt_pound)

        binding.currencyCard.textChoice.setTextColor(getColor(R.color.light_green))
        binding.currencyCard.optionsImage.setImageDrawable(getDrawable(R.drawable.ic_options_green))

        binding.bankCard.textLabel.text = getString(R.string.bank)
        binding.bankCard.textChoice.text =
            viewModel.configurationsRepo.sharedPrefManager.getSelectedBank()?.name ?: getString(R.string.central_bank_of_egypt)

        binding.bankCard.textChoice.setTextColor(getColor(R.color.light_green))
        binding.bankCard.optionsImage.setImageDrawable(getDrawable(R.drawable.ic_options_green))

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ConfigurationsViewModel::class.java]
    }

    private fun onObserveData() {

        viewModel.configMutableLiveData.observe(this) {
            hideProgress()
            val configResponse = it.data

            viewModel.configurationsRepo.sharedPrefManager.let { sharedPref ->

                if (sharedPref.getSelectedBank() == null){

                    configResponse?.banks?.get(0)?.let { bankModel ->
                        sharedPref.setSelectedBank(bankModel)
                    }
                }

                if (sharedPref.getSelectedCurrency() == null){

                    configResponse?.currencies?.get(0)?.let { currencyModel ->
                        sharedPref.setSelectedCurrency(currencyModel)
                    }
                }
            }

            it.data?.let { config ->

                viewModel.setConfigurations(config)
            }
        }

        viewModel.errorLiveData.observe(this) {
            hideProgress()
            it.message?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun onClickListeners() {
        binding.apply {
            currencyCard.optionsImage.setOnClickListener {
                initCurrenciesBottomSheet()
            }

            bankCard.optionsImage.setOnClickListener {
                initBankBottomSheet()
            }

            getStartedBtn.setOnClickListener {
                startActivity(Intent(this@ConfigurationsActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun initBankBottomSheet() {

        val bottomSheetDialog = ConfigurationsBanksBottomSheet(viewModel.selectedBanksList) {

            val bank = viewModel.getBankMapper(it)
            viewModel.configurationsRepo.sharedPrefManager.setSelectedBank(bank)
            binding.bankCard.textChoice.text = bank.name
        }
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
    }

    private fun initCurrenciesBottomSheet() {

        val bottomSheetDialog =
            ConfigurationsCurrenciesBottomSheet(viewModel.selectedCurrenciesList) {

                val currency = viewModel.getCurrencyMapper(it)
                viewModel.configurationsRepo.sharedPrefManager.setSelectedCurrency(currency)
                binding.currencyCard.textChoice.text = currency.name
            }
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
    }

    private fun showProgress() {
        binding.progress.show()
    }

    private fun hideProgress() {
        binding.progress.hide()
    }
}