package com.example.trafficaerien.aircraft

import android.app.AlertDialog
import android.content.DialogInterface
import com.example.trafficaerien.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_aircraft_flight_list.*

/**
 * A simple [Fragment] subclass.
 * Use the [FlightListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//fragment concernant l'affichage des vols des trois dernier jours de l'avion
class AircraftFlightListFragment : Fragment(){
    // TODO: Rename and change types of parameters

    private lateinit var viewModel: AircraftViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(AircraftViewModel::class.java)
        viewModel.getFlightAircraftListLiveData().observe(this, androidx.lifecycle.Observer{
            //si l'on reçoit aucune info de l'api alors on prévient l'utilisateur et on retourne à l'activity précédente
            if (it == null || it.isEmpty()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                builder.setTitle("Aucune réponse de l'API")
                builder.setMessage("Veuillez recommencez plus tard ou avec des valeurs différentes")
                builder.setIcon(R.drawable.sad)

                builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    activity?.finish()
                })

                val alertDialog = builder.create()
                alertDialog.show()
            } else {
                //sinon on peut remplis notre recycler adapter de ces infos
                val adapter = AircraftFlightListRecyclerAdapter()
                adapter.flightList = it
                recyclerViewFlightAircraft.adapter = adapter
                recyclerViewFlightAircraft.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aircraft_flight_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlightListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = AircraftFlightListFragment().apply {}
    }
}