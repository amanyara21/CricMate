package com.aman.cricmate.components

import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.aman.cricmate.model.TestResult
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
fun LineChartView(
    testResult: TestResult,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
) {
    val sortedResults = testResult.results.reversed()

    val entries = sortedResults.mapIndexed { index, result ->
        Entry(index.toFloat(), result.result.toFloat())
    }

    val dateLabels = sortedResults.map { result ->
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(result.testDate)
    }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                if (entries.isEmpty()) {
                    clear()
                    setNoDataText("No data available")
                    setNoDataTextColor(Color.GRAY)
                    setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    val dataSet = LineDataSet(entries, testResult.fieldName).apply {
                        color = Color.parseColor("#2196F3")
                        valueTextColor = Color.BLACK
                        lineWidth = 2f
                        circleRadius = 5f
                        setCircleColor(Color.parseColor("#1976D2"))
                        setDrawCircleHole(false)
                        setDrawValues(false)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawFilled(true)
                        fillColor = Color.parseColor("#BBDEFB")
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
                                return if (index in dateLabels.indices) dateLabels[index] else ""
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
    )
}



