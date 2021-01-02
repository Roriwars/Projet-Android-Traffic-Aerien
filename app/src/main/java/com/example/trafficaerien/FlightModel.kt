package com.example.trafficaerien

data class FlightModel (val icao24: String,
                        val firstSeen: Long,
                        val estDepartureAirport: String,
                        val lastSeen: Long,
                        val estArrivalAirport: String,
                        val callsign: String,
                        val estDepartureAirportHorizDistance: Int,
                        val estDepartureAirportVertDistance: Int,
                        val estArrivalAirportHorizDistance: Int,
                        val estArrivalAirportVertDistance: Int,
                        val departureAirportCandidatesCount: Int,
                        val arrivalAirportCandidatesCount: Int,
                        val longAirportDeparture: Long,
                        val latAirportDeparture: Long,
                        val longAirportArrival: Long,
                        val latAirportArrival: Long)