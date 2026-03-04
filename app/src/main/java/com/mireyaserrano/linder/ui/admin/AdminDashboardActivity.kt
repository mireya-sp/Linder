package com.mireyaserrano.linder.ui.admin

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.AppMetrics
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.UserMetrics
import com.mireyaserrano.linder.data.Intent as UserIntent

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnBack = findViewById<ImageButton>(R.id.btn_back_to_login)
        val tvStats = findViewById<TextView>(R.id.tv_stats)
        val btnReset = findViewById<Button>(R.id.btn_reset_data)

        pieChart = findViewById(R.id.pieChartIntents)
        barChart = findViewById(R.id.barChartInterests)

        btnBack.setOnClickListener { finish() }
        btnReset.setOnClickListener { showResetConfirmationDialog(tvStats) }

        calculateAndDisplayStats(tvStats)
    }

    private fun showResetConfirmationDialog(tvStats: TextView) {
        AlertDialog.Builder(this)
            .setTitle("¿Reiniciar métricas?")
            .setMessage("Esto borrará todos los contadores globales y los intereses de todos los usuarios. ¿Estás seguro?")
            .setPositiveButton("Sí, borrar todo") { _, _ ->
                resetAllData()
                calculateAndDisplayStats(tvStats)
                Toast.makeText(this, "Datos reiniciados con éxito", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun resetAllData() {
        LocalDatabase.globalMetrics = AppMetrics()
        LocalDatabase.saveMetrics(this)

        val allUsers = LocalDatabase.getAllUsers().values
        for (user in allUsers) {
            user.metrics = UserMetrics()
            LocalDatabase.updateUser(user)
        }
    }

    private fun calculateAndDisplayStats(tvStats: TextView) {
        val allUsers = LocalDatabase.getAllUsers().values

        val passOk = LocalDatabase.globalMetrics.passwordOk
        val passWrong = LocalDatabase.globalMetrics.passwordWrong
        val logComplete = LocalDatabase.globalMetrics.loginComplete
        val logIncomplete = LocalDatabase.globalMetrics.loginIncomplete
        val logGoogle = LocalDatabase.globalMetrics.loginGoogle
        val logFacebook = LocalDatabase.globalMetrics.loginFacebook
        val logPhone = LocalDatabase.globalMetrics.loginPhone

        var subWeek = 0; var subMonth = 0; var subYear = 0
        var intSeria = 0; var intNoche = 0; var intAmigas = 0
        var intTravel = 0; var intEvents = 0; var intNature = 0; var intBeach = 0

        for (user in allUsers) {
            subWeek += user.metrics.subWeeklyPurchases
            subMonth += user.metrics.subMonthlyPurchases
            subYear += user.metrics.subYearlyPurchases

            if (user.metrics.interestTravel) intTravel++
            if (user.metrics.interestEvents) intEvents++
            if (user.metrics.interestNature) intNature++
            if (user.metrics.interestBeach) intBeach++

            when (user.intent) {
                UserIntent.RELACION_SERIA -> intSeria++
                UserIntent.ROLLO_UNA_NOCHE -> intNoche++
                UserIntent.HACER_AMIGAS -> intAmigas++
                else -> {}
            }
        }

        // TEXTO RESUMEN
        val report = """
            ** RENDIMIENTO LOGIN **
            Completos: $logComplete | Incompletos: $logIncomplete
            Pass OK: $passOk | Pass Falladas: $passWrong
            Métodos: Tel ($logPhone) | Ggl ($logGoogle) | Fb ($logFacebook)
            
            ** VENTAS **
            Semanal: $subWeek | Mensual: $subMonth | Anual: $subYear
        """.trimIndent()
        tvStats.text = report

        // DIBUJAR GRÁFICAS
        setupPieChart(intSeria, intNoche, intAmigas)
        setupBarChart(intTravel, intEvents, intNature, intBeach)
    }

    private fun setupPieChart(seria: Int, noche: Int, amigas: Int) {
        val entries = ArrayList<PieEntry>()
        if (seria > 0) entries.add(PieEntry(seria.toFloat(), "Estable"))
        if (noche > 0) entries.add(PieEntry(noche.toFloat(), "1 Noche"))
        if (amigas > 0) entries.add(PieEntry(amigas.toFloat(), "Amistad"))

        // Si no hay datos, mostramos algo por defecto
        if (entries.isEmpty()) entries.add(PieEntry(1f, "Sin datos"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.parseColor("#CC99FF"), Color.parseColor("#4A2A6A"), Color.parseColor("#D4FB5D"))
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        pieChart.data = PieData(dataSet)
        pieChart.description.isEnabled = false
        pieChart.legend.textColor = Color.WHITE
        pieChart.setHoleColor(Color.parseColor("#121212")) // Centro oscuro
        pieChart.invalidate() // Refrescar gráfica
    }

    private fun setupBarChart(travel: Int, events: Int, nature: Int, beach: Int) {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, travel.toFloat()))
        entries.add(BarEntry(1f, events.toFloat()))
        entries.add(BarEntry(2f, nature.toFloat()))
        entries.add(BarEntry(3f, beach.toFloat()))

        val dataSet = BarDataSet(entries, "Clics por categoría")
        dataSet.colors = ColorTemplate.PASTEL_COLORS.toList()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        barChart.data = BarData(dataSet)

        // Nombres en el eje X
        val labels = arrayOf("Viajes", "Eventos", "Naturaleza", "Playa")
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.textColor = Color.WHITE
        barChart.xAxis.granularity = 1f

        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.isEnabled = false // Quitamos el eje derecho
        barChart.description.isEnabled = false
        barChart.legend.textColor = Color.WHITE
        barChart.invalidate() // Refrescar gráfica
    }
}