package com.example.treasurysolutions.presentation.home.ratesfragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedpreference.Constant
import com.example.sharedpreference.model.CurrencyModel
import com.example.sharedpreference.model.GraphResponse
import com.example.sharedpreference.model.ToCurrencyModel
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.FragmentRatesBinding
import com.example.treasurysolutions.presentation.configurations.ConfigurationsBanksBottomSheet
import com.example.treasurysolutions.presentation.configurations.ConfigurationsCurrenciesBottomSheet
import com.example.treasurysolutions.presentation.configurations.ConfigurationsViewModel
import com.example.treasurysolutions.presentation.home.BankAdapter
import com.example.treasurysolutions.presentation.home.HomeAdapter
import com.example.treasurysolutions.presentation.home.HomeViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RatesFragment : Fragment() {

    private lateinit var binding: FragmentRatesBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val configViewModel: ConfigurationsViewModel by activityViewModels()
    private var currencies = mutableListOf<ToCurrencyModel>()
    private var selectedChip = Constant.WEEK
    private var graphResponse: GraphResponse? = null
    private lateinit var chipsAdapter: ChipsAdapter
    private var adapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRatesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        viewModel.getExchangeRates()
        configViewModel.getConfigurations()
        viewModel.getSelectedBank()
        viewModel.getSelectedCurrency()
        viewModel.getSavedBanks()
//        viewModel.getGraphData()
        onCurrencyButtonClicked()
        onObserveData()
        onClickListeners()
        initView()
        initAdapter()

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                    0
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    currencies.let {
                        Collections.swap(
                            it,
                            viewHolder.adapterPosition,
                            target.adapterPosition
                        )
                    }
                    binding.recyclerView.adapter?.notifyItemMoved(
                        viewHolder.adapterPosition,
                        target.adapterPosition
                    )
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    private fun initAdapter() {
        adapter = HomeAdapter(requireContext()) {
            viewModel.selectedCurrency = it
            findNavController().navigate(R.id.action_ratesFragment_to_currenciesRatesFragment)
        }
    }

    private fun initBottomSheet() {

        val bottomSheetDialog = ConfigurationsBanksBottomSheet(configViewModel.selectedBanksList) {

            val bank = configViewModel.getBankMapper(it)
            configViewModel.configurationsRepo.sharedPrefManager.setSelectedBank(bank)
            viewModel.getExchangeRates()
            showProgress()
            binding.textBankName.text = bank.name
        }
        bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
    }

    private fun initCurrenciesBottomSheet() {

        val bottomSheetDialog =
            ConfigurationsCurrenciesBottomSheet(configViewModel.selectedCurrenciesList) {

                val currency = configViewModel.getCurrencyMapper(it)
                configViewModel.configurationsRepo.sharedPrefManager.setSelectedCurrency(currency)
                viewModel.getExchangeRates()
                showProgress()
                updateCardUI(currency)
            }
        bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
    }

    private fun initChart() {
        binding.chart.apply {

            setViewPortOffsets(0f, 0f, 0f, 0f)
            setBackgroundColor(Color.rgb(0, 181, 167))
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)

            setPinchZoom(false)

            setDrawGridBackground(false)
            maxHighlightDistance = 300f

            val x: XAxis = getXAxis()
            x.isEnabled = false

            val y: YAxis = getAxisLeft()
            y.typeface = Typeface.SANS_SERIF
            y.setLabelCount(6, false)
            y.textColor = Color.WHITE
            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            y.setDrawGridLines(false)
            y.axisLineColor = Color.WHITE

            axisRight.isEnabled = false

            legend.isEnabled = false

            animateXY(2000, 2000)
        }
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.something_went_wrong))
            .setPositiveButton(getString(R.string.try_again)) { dialogInterface, _ ->
                viewModel.getExchangeRates()
                dialogInterface.dismiss()
                showProgress()
            }.create().show()
    }

    private fun initView() {
        initChart()
        chipsAdapter = ChipsAdapter(requireContext()) { clickedChip ->
            selectedChip = clickedChip.name

            if (!clickedChip.isSelected) {

                chipsAdapter.list.forEach { chipModel ->
                    chipModel.isSelected = chipModel.id == clickedChip.id
                }
                chipsAdapter.updateAdapter()
                updateChart()
            }

        }

        binding.rvChips.adapter = chipsAdapter
    }

    private fun onObserveData() {

        viewModel.ratesMutableLiveData.observe(viewLifecycleOwner) {

            hideProgress()
            it?.data?.let {
                currencies = it.exchangeRates.toMutableList()
                if (it.exchangeRates.isNotEmpty()) {

                    it.exchangeRates[0].lastUpdate?.let {

                        binding.textUpdatedAt.text =
                            String.format(getString(R.string.updated_at_s), it)
                    }
                    binding.textEgp.text = String.format(
                        getString(R.string.s_s_with),
                        it.fromCurrency.englishAbb, it.fromCurrency.name
                    )

                    adapter?.currencies = currencies
                    binding.recyclerView.adapter = adapter
                }
            }
        }

        viewModel.banksMutableLiveData.observe(viewLifecycleOwner) {

            it?.let {
                binding.bankRecyclerView.layoutManager = LinearLayoutManager(context)
                val adapter = BankAdapter(requireContext(), it) {
                    viewModel.selectedBank = it
                    findNavController().navigate(R.id.action_ratesFragment_to_bankRatesFragment)
                }
                binding.bankRecyclerView.adapter = adapter
            }

        }

        viewModel.graphRatesMutableLiveData.observe(viewLifecycleOwner) {

            it?.let { graphResponse ->
                this.graphResponse = graphResponse
//                updateChart()
            }
        }

        viewModel.savedBankMutableLiveData.observe(viewLifecycleOwner) {
            binding.textBankName.text = it?.name
        }

        viewModel.savedCurrencyMutableLiveData.observe(viewLifecycleOwner) {
            it?.let { updateCardUI(it) }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            hideProgress()
            initAlertDialog()
        }
    }

    private fun onClickListeners() {

        binding.currencyBtn.setOnClickListener {
            onCurrencyButtonClicked()
            binding.currencyView.isVisible = true
            binding.bankView.isVisible = false
        }

        binding.bankBtn.setOnClickListener {
            onBankButtonClicked()
            binding.currencyView.isVisible = false
            binding.bankView.isVisible = true
        }

        binding.optionsImage.setOnClickListener {
            initBottomSheet()
        }

        binding.cardOptionsImage.setOnClickListener {
            initCurrenciesBottomSheet()
        }

        binding.editDollarUnits.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                getValueRates()
            }
            false
        }
    }

    private fun onCurrencyButtonClicked() {
        binding.currencyBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green
            )
        )
        binding.currencyBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.bankBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.bankBtn.setStrokeColorResource(R.color.light_green)
        binding.bankBtn.strokeWidth = 2
        binding.bankBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_black))

    }

    private fun onBankButtonClicked() {
        binding.bankBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green
            )
        )
        binding.bankBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.currencyBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.currencyBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_black
            )
        )
        binding.currencyBtn.setStrokeColorResource(R.color.light_green)
        binding.currencyBtn.strokeWidth = 2

    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    private fun updateCardUI(currency: CurrencyModel) {
        binding.textName.text = currency.name
        binding.textAbb.text = currency.abbreviation

        Glide.with(requireContext())
            .load(currency.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_global)
            .into(binding.imageLogo)
    }

    private fun setData(values: List<Entry>) {

        val set1: LineDataSet
        if (binding.chart.data != null &&
            binding.chart.data.dataSetCount > 0
        ) {
            set1 = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            binding.chart.data.notifyDataChanged()
            binding.chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.WHITE
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter =
                IFillFormatter { _, _ -> binding.chart.axisLeft.axisMinimum }

            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTypeface(Typeface.SANS_SERIF)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            binding.chart.data = data
        }
    }

    private fun updateChart() {
        val list = when (selectedChip) {
            Constant.MONTH -> graphResponse?.monthValues
            Constant.YEAR -> graphResponse?.yearValues
            else -> graphResponse?.weekValues
        }
        var count = 0.0
        var counter = 1
        val values = ArrayList<Entry>()

        list?.let {
            if (it.isNotEmpty()) {

                val num = it.size
                for (i in it) {
                    count += i.buyPrice
                    values.add(
                        Entry(
                            counter.toFloat(),
                            (i.buyPrice.toFloat() * Math.random()).toFloat()
                        )
                    )
                    counter++
                }
                binding.textAvgRateValue.text = String.format(getString(R.string.f), count / num)
                binding.textDate.text = it[0].date
            }
        }


        setData(values)
        val sets: List<ILineDataSet> = binding.chart.data
            .dataSets

        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.setDrawFilled(!set.isDrawFilledEnabled)
        }
        binding.chart.invalidate()
    }

    private fun getValueRates() {
        val value = binding.editDollarUnits.text.toString()

        if (value.isNotEmpty() && value.toInt() > 1) {

            val mutableList = currencies.map { it.copy() }
            mutableList.forEachIndexed { index, element ->
                element.apply {
                    if (currencies[index].sellPrice > 1)
                        this.sellPrice = value.toInt() /
                            currencies[index].sellPrice
                    else
                        this.sellPrice =
                            currencies[index].sellPrice * value.toInt()
                }
            }
            adapter?.currencies = mutableList.toMutableList()
            binding.recyclerView.adapter = adapter
        } else {
            adapter?.currencies = currencies
            binding.recyclerView.adapter = adapter
        }
    }

}