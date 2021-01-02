package com.example.trafficaerien

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
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

        viewModel.flightListLiveData.observe(this, androidx.lifecycle.Observer {
            if (it == null) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Aucune réponse de l'API")
                builder.setMessage("Veuillez recommencez plus tard ou avec des valeurs différentes")
                builder.setIcon(R.drawable.sad)

                builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    this.finish()
                })

                val alertDialog = builder.create()
                alertDialog.show()
            }
        })

        viewModel.isLoadingLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.getSelectedFlightNameLiveData().observe(this, androidx.lifecycle.Observer {
            //switch fragment
            val newFragment: MapsFragment = MapsFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            if (isMobile) {
                transaction.add(R.id.activityContainer, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            } else {
                transaction.add(R.id.detail_container, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
        })

    }
}