package com.example.treasurysolutions.presentation.home.accountfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView(){
        binding.apply {
            accountItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            accountItem.textInfo.text = getString(R.string.account_info)

            favouriteItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            favouriteItem.textInfo.text = getString(R.string.favourite)

            countryItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            countryItem.textInfo.text = getString(R.string.country)

            currencyItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            currencyItem.textInfo.text = getString(R.string.currency)

            bankItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            bankItem.textInfo.text = getString(R.string.bank)

            languageItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            languageItem.textInfo.text = getString(R.string.language)

            settingsItem.imageLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account))
            settingsItem.textInfo.text = getString(R.string.settings)
        }
    }
}