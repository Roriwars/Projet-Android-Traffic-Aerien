package com.example.trafficaerien.aircraft

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.trafficaerien.R
import com.example.trafficaerien.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


/**
 * A simple [Fragment] subclass.
 * Use the [AircraftDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//fragment concernant l'affichage des détails de l'avion y compris sa position sur une map
class AircraftDetailFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters

    private lateinit var mMap: GoogleMap
    lateinit var track : TrackAircraftModel
    private lateinit var viewModel: AircraftViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity()).get(AircraftViewModel::class.java)

        val fragment = inflater.inflate(R.layout.fragment_aircraft_detail, container, false)

        viewModel.trackLiveData.observe(this, androidx.lifecycle.Observer {
            track = it
            mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment?.getMapAsync(this)

            if (track.state.isNotEmpty()) {
                //si on reçoit de l'api toutes les informations nécessaires, elles sont associées à la vue
                val state = track.state[0]
                fragment.findViewById<TextView>(R.id.textDetailAircraft).text =
                    "Détails de l'avion " + state.callsign
                fragment.findViewById<TextView>(R.id.textVitesseAircraft).text = "Vitesse : ${(state.velocity*3.6).toInt()} km/h"
                fragment.findViewById<TextView>(R.id.textAltitudeAircraft).text = "Altitude : " + state.geo_altitude + "m"
                when {
                       state.velocity == 0.0 -> {
                           fragment.findViewById<TextView>(R.id.textEtatVolAircraft).text = "Etat : à l'arrêt"
                       }
                       state.vertical_rate > 0 -> {
                           fragment.findViewById<TextView>(R.id.textEtatVolAircraft).text = "Etat : en monté"
                       }
                       state.vertical_rate < 0 -> {
                           fragment.findViewById<TextView>(R.id.textEtatVolAircraft).text = "Etat : en descente"
                       }
                       else -> {
                           fragment.findViewById<TextView>(R.id.textEtatVolAircraft).text = "Etat : en vol de croisière"
                       }
                }
                fragment.findViewById<TextView>(R.id.textLastSeenAircraft).text = "Dernier contact le " + Utils.timestampToString((track.time).toLong()*1000,false)
            }else{
                //si l'api renvoie des informations vide alors on avertis l'utilisateur sur l'absence cette abscence
                val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                builder.setTitle("Détail de l'avion non récupérer")
                builder.setMessage("Aucune réponse de l'API\nVeuillez recommencez plus tard ou avec des valeurs différentes")
                builder.setIcon(R.drawable.sad)

                builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })

                val alertDialog = builder.create()
                alertDialog.show()
            }
        })

        // Inflate the layout for this fragment
        return fragment
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }
        var title = "Trajet Indisponible"
        val coordinateDefault = LatLng(0.0, 0.0)
        val markerDefault = MarkerOptions()
                .position(coordinateDefault)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.not_found))

        if (track.state.isNotEmpty()) {
            val firstState = track.state[0]
            title = firstState.callsign
            val snippet = "Dernière position connue de l'avion"
            val lon = firstState.longitude
            val lat = firstState.latitude
            val rotation = firstState.true_track
            if (lon <= 180 && lat <= 90) {
                if(track.state.count()>1) {
                    val polylineOptions: PolylineOptions = PolylineOptions()
                    for (i in 0 until track.state.count()) {
                        polylineOptions.add(
                                LatLng(
                                        track.state[i].latitude,
                                        track.state[i].longitude
                                )
                        )
                    }
                    mMap.addPolyline(polylineOptions.color(Color.CYAN).width(5.toFloat()))
                }
                val coordinate = LatLng(lat, lon)
                val marker = MarkerOptions()
                        .position(coordinate)
                        .title(title)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane))
                        .rotation(firstState.true_track.toFloat())
                mMap.addMarker(marker)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate))
                return
            }
        }
        mMap.addMarker(markerDefault)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinateDefault))
    }

    companion object {
        var mapFragment : SupportMapFragment?=null
        val TAG: String = AircraftDetailFragment::class.java.simpleName
        fun newInstance() = AircraftDetailFragment().apply {

        }
    }
}