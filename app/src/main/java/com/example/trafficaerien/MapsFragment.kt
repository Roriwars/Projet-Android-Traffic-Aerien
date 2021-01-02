package com.example.trafficaerien

import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.trafficaerien.aircraft.AircraftActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: FlightListViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var flight: FlightModel

    companion object {
        var mapFragment : SupportMapFragment?=null
        val TAG: String = MapsFragment::class.java.simpleName
        fun newInstance() = MapsFragment().apply {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)

        viewModel.getSelectedFlightNameLiveData().observe(this, androidx.lifecycle.Observer {
            flight = it
        })

        var rootView = inflater.inflate(R.layout.fragment_maps, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonDetailAvion.setOnClickListener { rootView ->
            search()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            mMap = googleMap
        }

        var titleArrival = "Trajet Indisponible"
        val coordinateDefault = LatLng(0.0,0.0)
        val markerDefault = MarkerOptions()
                .position(coordinateDefault)
                .title(titleArrival)
                .icon(fromResource(R.drawable.not_found))
        //si le nom des aéroports est différent de null alors on recherche leurs informations (en particulier leur coordonnées pour les placer sur la map)
        if(flight.estArrivalAirport!=null && flight.estDepartureAirport!=null) {
            var airportArrival = Utils.getAirportByName(flight.estArrivalAirport)
            var airportDeparture = Utils.getAirportByName(flight.estDepartureAirport)

            var lonArrival = 1000.0
            var latArrival = 1000.0
            if(airportArrival.lon!="" && airportArrival.lat!="" && airportArrival.city!="") {
                lonArrival = airportArrival.lon.toDouble()
                latArrival = airportArrival.lat.toDouble()
                titleArrival = "Aéroport de "+airportArrival.city
            }
            var lonDeparture = 1000.0
            var latDeparture = 1000.0
            var titleDeparture = "Aéroport inexistant"
            if(airportDeparture.lon!="" && airportDeparture.lat!="" && airportDeparture.city!="") {
                lonDeparture = airportDeparture.lon.toDouble()
                latDeparture = airportDeparture.lat.toDouble()
                titleDeparture = "Aéroport de "+airportDeparture.city
            }

            //si les aéroports ont été trouvé dans notre fichier airport.json alors on place les points et on trace le trajet sur la map
            if(lonArrival<=180 && lonDeparture<=180 && latArrival<=90 && latDeparture<=90){
                val coordinateArrival = LatLng(latArrival,lonArrival)
                val coordinateDeparture = LatLng(latDeparture,lonDeparture)
                val markerArrival = MarkerOptions()
                    .position(coordinateArrival)
                    .title(titleArrival)
                    .icon(fromResource(R.drawable.planelanding))
                val markerDeparture = MarkerOptions()
                    .position(coordinateDeparture)
                    .title(titleDeparture)
                    .icon(fromResource(R.drawable.planetakeoff))
                val polylineFlight = googleMap!!.addPolyline(PolylineOptions()
                    .add(coordinateArrival)
                    .add(coordinateDeparture))
                stylePolyline(polylineFlight)

                mMap.addMarker(markerArrival)
                mMap.addMarker(markerDeparture)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinateArrival))
                return
            }
        }
        //si on arrive à cette étape nous n'avons pas récupérer les informations nécessaires, on affiche quand même la map avec un marker montrant que les aéroports n'ont pas été trouvé
        mMap.addMarker(markerDefault)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinateDefault))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.maxZoomLevel))
    }

    //fonction permettant de styliser notre polyline (représentant notre trajet sur la map)
    private fun stylePolyline(polyline: Polyline) {
        polyline.startCap=CustomCap(fromResource(R.drawable.arrow_cyan_v2), 10F)
        polyline.endCap = RoundCap()
        polyline.width = 12.toFloat()
        polyline.color = Color.CYAN
        polyline.jointType = JointType.ROUND
    }

    //création de la nouvelle activité avec les informations de l'avion
    private fun search(){
        activity?.let{
            val intent = Intent (it, AircraftActivity::class.java)
            intent.putExtra("icao", flight.icao24)
            it.startActivity(intent)
        }
    }

}