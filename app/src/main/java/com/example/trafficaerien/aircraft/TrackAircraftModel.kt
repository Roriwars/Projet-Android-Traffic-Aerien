package com.example.trafficaerien.aircraft

//model de ce que l'api nous renvoie lorsqu'on lui demande les états de l'avion
data class TrackAircraftModel(
        val time :Int,
        val state: List<State>
)

data class State(
        val icao24 :String,
        val callsign: String,
        val origin_country: String,
        val time_position: Int,
        val last_contact: Int,
        val longitude: Double,
        val latitude: Double,
        val baro_altitude: Double,
        val on_ground: Boolean,
        val velocity: Double,
        val true_track: Double,
        val vertical_rate: Double,
        val geo_altitude: Double,
        val squawk: String,
        val spi: Boolean,
        val position_source: Int
)