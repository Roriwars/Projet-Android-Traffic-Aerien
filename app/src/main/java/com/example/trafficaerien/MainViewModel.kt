package com.example.trafficaerien

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel(){
    private val airportListLiveData : MutableLiveData<List<Airport>> = MutableLiveData()
    private val beginDateListLiveData : MutableLiveData<Calendar> = MutableLiveData()
    private val endDateListLiveData : MutableLiveData<Calendar> = MutableLiveData()
    init{
        airportListLiveData.value = Utils.generateAirportList()
        beginDateListLiveData.value = Calendar.getInstance()
        endDateListLiveData.value = Calendar.getInstance()
    }

    fun getAirportListLiveData(): LiveData<List<Airport>> {
        return airportListLiveData
    }

    fun getBeginDateLiveData(): LiveData<Calendar> {
        return beginDateListLiveData
    }

    fun getEndDateLiveData(): LiveData<Calendar> {
        return endDateListLiveData
    }

    fun updateBeginCalendar(year: Int, month: Int, day: Int){
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
        beginDateListLiveData.value=calendar
    }

    fun updateEndCalendar(year: Int, month: Int, day: Int){
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
        endDateListLiveData.value=calendar
    }
}