package com.example.treasurysolutions.presentation.home.currenciesratesfragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sharedpreference.Constant
import com.example.sharedpreference.model.GraphResponse
import com.example.treasurysolutions.R
import com.example.treasurysolutions.databinding.FragmentCurrenciesRatesBinding
import com.example.treasurysolutions.presentation.home.CurrenciesRatesAdapter
import com.example.treasurysolutions.presentation.home.HomeViewModel
import com.example.treasurysolutions.presentation.home.ratesfragment.ChipsAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class CurrenciesRatesFragment : Fragment() {

    private lateinit var binding: FragmentCurrenciesRatesBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var chipsAdapter: ChipsAdapter
    private var selectedChip = Constant.WEEK
    private var graphResponse: GraphResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrenciesRatesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        viewModel.getCurrenciesRatesToEGP()
        showProgress()
//        viewModel.getGraphData()
        onObserveData()
    }

    private fun onObserveData() {

        viewModel.egpCurrenciesRatesMLD.observe(viewLifecycleOwner) {

            hideProgress()
            it?.data?.exchangeRates?.let { exchangeRates ->
                val adapter = CurrenciesRatesAdapter(requireContext(), exchangeRates)
                binding.currenciesRatesRecyclerView.adapter = adapter
            }

        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            hideProgress()
            error.message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        }

        viewModel.graphRatesMutableLiveData.observe(viewLifecycleOwner) {

            it?.let { graphResponse ->
                this.graphResponse = graphResponse
                updateChart()
            }
        }
    }

    private fun initView() {
        viewModel.selectedCurrency?.let {

            binding.textName.text = it.name
            Glide.with(requireContext())
                .load(it.imageUrl)
                .placeholder(R.drawable.ic_global)
                .into(binding.imageLogo)

            binding.textUpdatedAt.text = String.format(
                getString(R.string.according_to_central_bank_of_egypt_updated_at_s),
                it.lastUpdate
            )
        }
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
    private fun showProgress(){
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress(){
        binding.progress.visibility = View.GONE
    }
}