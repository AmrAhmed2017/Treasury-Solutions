package com.example.treasurysolutions.presentation.home.metalfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedpreference.model.MetalResponse
import com.example.sharedpreference.model.ToCurrencyModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.FragmentMetalBinding
import com.example.treasurysolutions.presentation.home.BankAdapter
import com.example.treasurysolutions.presentation.home.HomeAdapter
import com.example.treasurysolutions.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MetalFragment : Fragment() {

    private lateinit var binding: FragmentMetalBinding
    private val viewModel: MetalViewModel by viewModels()
    private var metals: MetalResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMetalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        viewModel.getMetalPrices()
        onObserveData()
        onClickListeners()
    }

    private fun onObserveData() {

        viewModel.metalsMutableLiveData.observe(viewLifecycleOwner) {

            hideProgress()
            metals = it.data
            onGoldButtonClicked()
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            hideProgress()
            initAlertDialog()
        }
    }

    private fun onClickListeners() {

        binding.goldBtn.setOnClickListener {
            onGoldButtonClicked()
        }

        binding.silverBtn.setOnClickListener {
            onSilverButtonClicked()
        }
    }

    private fun onGoldButtonClicked() {
        binding.goldBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green
            )
        )
        binding.goldBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.silverBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.silverBtn.setStrokeColorResource(R.color.light_green)
        binding.silverBtn.strokeWidth = 2
        binding.silverBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_black
            )
        )

        metals?.goldPrices?.let {
            val adapter =
                GoldAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun onSilverButtonClicked() {
        binding.silverBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green
            )
        )
        binding.silverBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        binding.goldBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.goldBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_black
            )
        )
        binding.goldBtn.setStrokeColorResource(R.color.light_green)
        binding.goldBtn.strokeWidth = 2

        metals?.silverPrices?.let {
            val adapter =
                SilverAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.something_went_wrong))
            .setPositiveButton(getString(R.string.try_again)) { dialogInterface, _ ->
                viewModel.getMetalPrices()
                dialogInterface.dismiss()
                showProgress()
            }.create().show()
    }
}