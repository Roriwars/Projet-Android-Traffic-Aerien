package com.example.trafficaerien

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val DATE_FORMAT = "dd MMM yyy"

    val fromCalendar = Calendar.getInstance()
    val toCalendar = Calendar.getInstance()

    var airportList = Utils.generateAirportList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val listAirportName: ArrayList<String> = ArrayList<String>()

        //add name airport in listAirportName
        for(airport in airportList){
            listAirportName.add(airport.getFormattedName())
        }

        val adapter : ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listAirportName
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter=adapter

        displaySelectedDate(editDate, fromCalendar)
        displaySelectedDate(editDate2, toCalendar)

        editDate.setOnClickListener {
            showDatePicker(textView, fromCalendar)
        }

        editDate2.setOnClickListener {
            showDatePicker(editDate2, toCalendar)
        }

        searchButton.setOnClickListener {
            search()
        }
    }

    private fun showDatePicker(textView: TextView, c: Calendar){

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                c.set(year,monthOfYear,dayOfMonth)
                displaySelectedDate(textView, c)
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun displaySelectedDate(textView: TextView, c : Calendar){
        textView.text = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(c.time)
    }

    private fun search(){
        //récupérer aeroport
        val icao = airportList.get(spinner.selectedItemPosition)

        //récupérer isArrival
        val isArrival = radioButton2.isActivated

        //récupérer les 2 dates
        val begin = fromCalendar.timeInMillis / 1000
        val end = toCalendar.timeInMillis / 1000

        Log.d("MainActivity", "icao=$icao isArrival=$isArrival begin=$begin end=$end ")
    }
}