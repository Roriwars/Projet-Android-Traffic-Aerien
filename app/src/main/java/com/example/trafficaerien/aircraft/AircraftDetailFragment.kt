package com.example.trafficaerien

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.trafficaerien.aircraft.AircraftViewModel
import com.example.trafficaerien.aircraft.TrackAircraftModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AircraftDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AircraftDetailFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mMap: GoogleMap
    private lateinit var track : TrackAircraftModel
    private lateinit var viewModel: AircraftViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(AircraftViewModel::class.java)

        viewModel.getTrackLiveData().observe(this, androidx.lifecycle.Observer {
            track=it
            Log.d("trackLiveData",track.toString())
            val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync(this)
        })
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aircraft_detail, container, false)
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
            var firstState = track.state[0]
            title = firstState.callsign
            var snippet = "Dernière position connue de l'avion"
            var lon = firstState.longitude
            var lat = firstState.latitude
            var rotation = firstState.true_track
            Log.d("rotationTypeFloat?", "${rotation.toFloat()}")
            Log.d("rotationTypeFloat?", "${rotation.toFloat()::class.simpleName}")
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.maxZoomLevel))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AircraftDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AircraftDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}