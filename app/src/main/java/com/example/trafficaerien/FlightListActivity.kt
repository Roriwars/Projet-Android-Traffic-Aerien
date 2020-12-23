package com.example.trafficaerien

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_flight_list.*

class FlightListActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val isMobile = detail_container == null

        viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)
        viewModel.search(
                intent.getStringExtra("icao")!!,
                intent.getBooleanExtra("isArrival", false),
                intent.getLongExtra("begin", 0),
                intent.getLongExtra("end", 0)
        )

        viewModel.getSelectedFlightNameLiveData().observe(this, androidx.lifecycle.Observer{
            //switch fragment
            val newFragment: FlightDetailFragment = FlightDetailFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            if (isMobile) {
                transaction.add(R.id.activityContainer, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
            else{
                transaction.add(R.id.detail_container, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
        })

    }
}