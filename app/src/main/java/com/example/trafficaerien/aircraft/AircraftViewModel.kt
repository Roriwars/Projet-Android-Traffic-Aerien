package com.example.trafficaerien.aircraft

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trafficaerien.FlightModel
import com.example.trafficaerien.RequestsManager
import com.example.trafficaerien.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

//class permettant les requête url à l'api servant l'activity des avions ainsi que ses deux fragments
class AircraftViewModel : ViewModel(), RequestsManager.RequestListener {

    private val flightAircraftListLiveData: MutableLiveData<List<FlightModel>> = MutableLiveData()
    val trackLiveData: MutableLiveData<TrackAircraftModel> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getFlightAircraftListLiveData(): LiveData<List<FlightModel>> {
        return flightAircraftListLiveData
    }

    //fonction appelé dans l'activity pour ensuite fournir les fragments
    fun search(icao: String) {
        val c = Calendar.getInstance()
        val cBis = Calendar.getInstance()
        cBis.add(Calendar.DATE,-3)
        val end = c.timeInMillis/1000
        val begin = cBis.timeInMillis/1000

        val searchLastFlightAircraft = SearchLastFlightAircraft(
            begin,
            end,
            icao
        )
        val baseUrlLastFlight = "https://opensky-network.org/api/flights/aircraft"
        //on fait d'abord un appel a l'api sur les dernier vols des trois dernier jours de l'avion
        viewModelScope.launch {
            //start loading
            isLoadingLiveData.value = true
            val result = withContext(Dispatchers.IO) {
                RequestsManager.getSuspended(baseUrlLastFlight, getRequestParams(searchLastFlightAircraft))
            }
            if (result == null) {
                isLoadingLiveData.value = false
                Log.e("Request", "problem")
            } else {
                //si l'api nous renvoie les bonnes informations on lui fait un deuxième appel
                //on lui demande les états de l'avion la dernière fois qu'on la vu (temps récupérer de l'appel précédent)
                val flightList = Utils.getFlightListFromString(result)
                flightAircraftListLiveData.value = flightList
                var time = flightAircraftListLiveData.value!![0].lastSeen
                val searchDataAircraft = SearchDataAircraft(
                        time,
                        icao
                )
                val baseUrl = "https://opensky-network.org/api/states/all"
                viewModelScope.launch {
                    //start loading
                    val result = withContext(Dispatchers.IO) {
                        RequestsManager.getSuspended(baseUrl, getRequestParams(searchDataAircraft))
                    }
                    if (result == null) {
                        isLoadingLiveData.value = false
                        trackLiveData.value = null
                        Log.e("Request", "problem")
                    } else {
                        val trackList = Utils.getTrackAircraftFromString("$result")
                        //end loading
                        isLoadingLiveData.value = false
                        trackLiveData.value = trackList
                    }
                }
            }
        }
    }

    private fun getRequestParams(searchModel: SearchDataAircraft?): Map<String, String>? {
        val params = HashMap<String, String>()
        if (searchModel != null) {
            params["icao24"] = searchModel.icao
            params["time"] = searchModel.time.toString()
        }
        return params
    }
    private fun getRequestParams(searchModel: SearchLastFlightAircraft?): Map<String, String>? {
        val params = HashMap<String, String>()
        if (searchModel != null) {
            params["icao24"] = searchModel.icao
            params["begin"] = searchModel.begin.toString()
            params["end"] = searchModel.end.toString()
        }
        return params
    }

    override fun onRequestSuccess(result: String?) {
        TODO("Not yet implemented")
    }

    override fun onRequestFailed() {
        TODO("Not yet implemented")
    }
}