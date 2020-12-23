package com.example.trafficaerien

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val DATE_FORMAT = "dd MMM yyy"

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val listAirportName: ArrayList<String> = ArrayList<String>()

        //add name airport in listAirportName
        for(airport in viewModel.getAirportListLiveData().value!!){
            listAirportName.add(airport.getFormattedName())
        }

        val adapter : ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listAirportName
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter=adapter

        viewModel.getBeginDateLiveData()
            .observe(this, androidx.lifecycle.Observer { displaySelectedDate(editDate, it) })

        viewModel.getEndDateLiveData()
            .observe(this, androidx.lifecycle.Observer { displaySelectedDate(editDate2, it) })

        editDate.setOnClickListener {
            showDatePicker(editDate)
        }

        editDate2.setOnClickListener {
            showDatePicker(editDate2)
        }

        searchButton.setOnClickListener {
            search()
        }

    }

    private fun showDatePicker(clickedView: View){

        val c = if(clickedView.id==editDate.id) {
            viewModel.getBeginDateLiveData().value
        }else{
            viewModel.getEndDateLiveData().value
        }

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                if(clickedView.id==editDate.id) {
                    viewModel.updateBeginCalendar(year, monthOfYear, dayOfMonth)
                }else{
                    viewModel.updateEndCalendar(year, monthOfYear, dayOfMonth)
                }
            },
            c!!.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DATE)
        )
        dpd.show()
    }

    private fun displaySelectedDate(textView: TextView, c : Calendar){
        textView.text = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(c.time)
    }

    private fun search() {
        /*// récupérer aéroport
        val icao =
            viewModel.getAirportListLiveData().value!![spinner.selectedItemPosition].icao

        // récupérer isArrival
        val isArrival = radioButton2.isChecked

        // récupérer les 2 dates
        //val begin = fromCalendar.timeInMillis / 1000
        //val end = toCalendar.timeInMillis / 1000

        //Log.d("MainActivity", "icao = $icao, isArrival = $isArrival, begin = $begin, end = $end")
        //démarrer une activité et y passer les infos

//après avor récupérer la data des vues, appeler une méthode process search
        val i = Intent(this, FlightListActivity::class.java)*/
        val i = Intent(this,FlightListActivity::class.java)
        i.putExtra("begin", viewModel.getBeginDateLiveData().value!!.timeInMillis / 1000)
        i.putExtra("end", viewModel.getEndDateLiveData().value!!.timeInMillis / 1000)
        i.putExtra("icao", viewModel?.getAirportListLiveData().value?.get(spinner.selectedItemPosition)?.icao)
        i.putExtra("isArrival", radioButton2.isChecked)
        startActivity(i)
    }
}