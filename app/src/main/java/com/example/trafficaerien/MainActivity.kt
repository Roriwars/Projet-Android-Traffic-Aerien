package com.example.trafficaerien

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val DATE_FORMAT = "dd MMM yyy"

    val fromCalendar = Calendar.getInstance()
    val toCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView     = findViewById<TextView>(R.id.editDate)
        val textView2     = findViewById<TextView>(R.id.editDate2)
        val spinner: Spinner = findViewById(R.id.spinner)

        var airports = Utils.generateAirportList()

        // Create an ArrayAdapter using the string array and a default spinner layout
        val listAirportName: ArrayList<String> = ArrayList<String>()

        //add name airport in listAirportName
        for(airport in airports){
            listAirportName.add(airport.getFormattedName())
        }

        val adapter : ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listAirportName
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter=adapter

        displaySelectedDate(textView, fromCalendar)
        displaySelectedDate(textView2, toCalendar)

        textView.setOnClickListener {
            showDatePicker(textView, fromCalendar)
        }

        textView2.setOnClickListener {
            showDatePicker(textView2, toCalendar)
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
}