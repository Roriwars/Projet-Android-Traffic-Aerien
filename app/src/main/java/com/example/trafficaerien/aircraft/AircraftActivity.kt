package com.example.trafficaerien.aircraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.trafficaerien.R
import kotlinx.android.synthetic.main.activity_aircraft.*

//Activity sur les détails de l'avion, sa position et ses trois dernier jours
class AircraftActivity : AppCompatActivity() {

    private lateinit var viewModel: AircraftViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aircraft)

        //récupération de icao de l'avion dont on désire voir les détails provenant de l'activity précédente
        //et appels à l'api pour récupérer toute les informations nécessaires
        viewModel = ViewModelProvider(this).get(AircraftViewModel::class.java)
        viewModel.search(
                intent.getStringExtra("icao")!!
        )

        viewModel.isLoadingLiveData.observe(this, androidx.lifecycle.Observer{
            if (it) {
                progressBarAicraft.visibility = View.VISIBLE
            } else {
                progressBarAicraft.visibility = View.INVISIBLE
            }
        })
    }
}