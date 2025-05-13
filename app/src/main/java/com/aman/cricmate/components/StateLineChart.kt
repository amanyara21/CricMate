package com.aman.cricmate.components

import android.graphics.Color
import android.view.ViewGroup
import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.aman.cricmate.model.DateStatsData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun StatsLineChart(
    modifier: Modifier = Modifier,
    statsData: List<DateStatsData>,
    label: String,
    getValueForEntry: (DateStatsData) -> Float
) {
    val entries = statsData.mapIndexed { index, data ->
        Entry(index.toFloat(), getValueForEntry(data))
    }
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM", Locale.getDefault())

    val labels = statsData.map {
        try {
            val date = inputFormat.parse(it.date)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            it.date
        }
    }


    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                if (entries.isEmpty()) {
                    clear()
                    setNoDataText("No data available")
                    setNoDataTextColor(Color.GRAY)
                    setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    val dataSet = LineDataSet(entries, label).apply {
                        color = Color.parseColor("#4CAF50")
                        valueTextColor = Color.BLACK
                        lineWidth = 2f
                        circleRadius = 5f
                        setCircleColor(Color.parseColor("#388E3C"))
                        setDrawCircleHole(false)
                        setDrawValues(false)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawFilled(true)
                        fillColor = Color.parseColor("#C8E6C9")
                    }

                    data = LineData(dataSet)

                    description = Description().apply { text = "" }

                    axisRight.isEnabled = false
                    axisLeft.apply {
                        textColor = Color.DKGRAY
                        textSize = 12f
                        gridColor = Color.LTGRAY
                    }

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        textColor = Color.DKGRAY
                        textSize = 12f
                        granularity = 1f
                        setDrawGridLines(false)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val index = value.toInt()
                                return if (index in labels.indices) labels[index] else ""
                            }
                        }
                    }

                    legend.apply {
                        form = Legend.LegendForm.LINE
                        textColor = Color.BLACK
                        textSize = 12f
                    }

                    animateX(1000)
                    invalidate()
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}



