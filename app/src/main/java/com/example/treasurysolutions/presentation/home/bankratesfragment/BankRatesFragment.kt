package com.example.treasurysolutions.presentation.home.bankratesfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.FragmentBankRatesBinding
import com.example.treasurysolutions.presentation.home.BankAdapter
import com.example.treasurysolutions.presentation.home.BankRatesAdapter
import com.example.treasurysolutions.presentation.home.CurrenciesRatesAdapter
import com.example.treasurysolutions.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankRatesFragment : Fragment() {

    private lateinit var binding: FragmentBankRatesBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBankRatesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        viewModel.selectedBank?.id?.let {

            viewModel.getBankExchangeRates(it)
        }
        onObserveData()
    }

    private fun onObserveData() {

        hideProgress()
        viewModel.bankRatesMutableLiveData.observe(viewLifecycleOwner) {

            binding.textAbbreviation.text = viewModel.selectedBank?.abbreviation
            binding.textName.text = viewModel.selectedBank?.name
            binding.textUpdatedAt.text = it.data?.exchangeRates?.get(0)?.lastUpdate

            viewModel.selectedBank?.let {

                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_global)
                    .into(binding.imageLogo)
            }


            it?.data?.exchangeRates?.let {
                binding.bankRatesRecyclerView.layoutManager = LinearLayoutManager(context)
                val adapter = BankRatesAdapter(requireContext(), it)
                binding.bankRatesRecyclerView.adapter = adapter
            }

        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            it.message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

}