package com.example.openin_app.view.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openin_app.R
import com.example.openin_app.databinding.FragmentFirstBinding
import com.example.openin_app.model.MainResponse
import com.example.openin_app.model.TopClicksResponse
import com.example.openin_app.view.adapters.TopClicksAdapter
import com.example.openin_app.view.adapters.TopLinksAdapter
import com.example.openin_app.viewmodel.MainViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.io.path.CopyActionContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(),OnClickListener, TopLinksAdapter.OnItemClickListener {
    private var _binding: FragmentFirstBinding? = null
    private lateinit var viewModel: MainViewModel
    private var response: MainResponse? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTopLinks.setOnClickListener(this)
        binding.tvRecentLinks.setOnClickListener(this)
        binding.talkWithUs.setOnClickListener(this)
        initViewModel()
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.data.observe(viewLifecycleOwner, Observer { data ->
            // Update UI with data
            Log.d("FirstFragment", "Data received: $data")
            response = data
            setData()
        })

        // Fetch data when activity is created
        viewModel.fetchData()
    }
    private fun setData(){
        setTopClicksAdapter()
        setTopLinksAdapter()
        setLineChart()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setTopClicksAdapter() {
        val tempList = ArrayList<TopClicksResponse>()
        if (response != null) {
            tempList.add(TopClicksResponse(R.drawable.ic_clicks,
                response?.today_clicks.toString(), "Today's clicks"))
            tempList.add(TopClicksResponse(R.drawable.ic_location, response?.top_location, "Top Location"))
            tempList.add(TopClicksResponse(R.drawable.ic_globe, response?.top_source, "Top Source"))
            tempList.add(TopClicksResponse(R.drawable.ic_clicks, "11:00 - 12:00", "Best Time"))
        }
        val adapter = TopClicksAdapter(tempList)
        binding.rvClicks.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvClicks.adapter = adapter
    }
    private fun setTopLinksAdapter() {
        if (response == null || response?.data?.top_links == null) {
            return
        }
        val adapter = TopLinksAdapter(response?.data?.top_links ?: emptyList(),this)
        binding.rvLinksDetails.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvLinksDetails.adapter = adapter
    }
    private fun setRecentLinksAdapter() {
        if (response == null || response?.data?.recent_links == null) {
            return
        }
        val adapter = TopLinksAdapter(response?.data?.recent_links ?: emptyList(),this)
        binding.rvLinksDetails.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvLinksDetails.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_top_links -> {
                binding.tvTopLinks.setTextColor(resources.getColor(R.color.white))
                binding.tvTopLinks.setBackgroundResource(R.drawable.bg_links_selected)
                binding.tvRecentLinks.setTextColor(resources.getColor(R.color.light_grey))
                binding.tvRecentLinks.setBackgroundResource(0)
                setTopLinksAdapter()
            }
            R.id.tv_recent_links -> {
                binding.tvRecentLinks.setTextColor(resources.getColor(R.color.white))
                binding.tvRecentLinks.setBackgroundResource(R.drawable.bg_links_selected)
                binding.tvTopLinks.setTextColor(resources.getColor(R.color.light_grey))
                binding.tvTopLinks.setBackgroundResource(0)
                setRecentLinksAdapter()
            }
            R.id.talk_with_us -> {
                openWhatsApp(response?.support_whatsapp_number ?: "")
            }
        }
    }
    private fun setLineChart() {
        val lineChart: LineChart = binding.lineChart

        // Create data entries
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 50f))
        entries.add(Entry(1f, 70f))
        entries.add(Entry(2f, 30f))
        entries.add(Entry(3f, 90f))
        entries.add(Entry(4f, 60f))
        entries.add(Entry(5f, 80f))
        entries.add(Entry(6f, 40f))

        // Create a dataset and set its properties
        val lineDataSet = LineDataSet(entries, "Mountain Data").apply {
            color = ContextCompat.getColor(requireContext(), R.color.background_blue)
            lineWidth = 2f
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.light_grey)
            valueTextSize = 12f
            setDrawFilled(true)
            fillAlpha = 100
            setDrawCircles(false) // Optional, remove if you want circles
            setDrawValues(false) // Optional, remove if you want values to be displayed
        }
        // Set the gradient drawable
        val gradientDrawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_fill)
        lineDataSet.fillDrawable = gradientDrawable

        // Create line data and set it to the chart
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // Customize the XAxis
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {
            private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul")
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return months.getOrElse(value.toInt()) { "" }
            }
        }
        xAxis.granularity = 1f

        // Customize the YAxis
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 100f
        yAxisLeft.granularity = 25f

        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        // Customize the chart
        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        lineChart.invalidate()
    }

    override fun onItemClick(link: String) {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Link", link)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(requireContext(), "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }
    private fun openWhatsApp(phoneNumber: String) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), "WhatsApp number is not available", Toast.LENGTH_SHORT).show()
            return
        }
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")

            // Check if WhatsApp is installed
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                // Show a message if WhatsApp is not installed
                Toast.makeText(requireContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "An error occurred while trying to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}