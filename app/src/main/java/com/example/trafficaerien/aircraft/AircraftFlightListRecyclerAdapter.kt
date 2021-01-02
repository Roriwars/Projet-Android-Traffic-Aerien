package com.example.trafficaerien.aircraft

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trafficaerien.FlightInfoCell
import com.example.trafficaerien.FlightModel

/**
 * Created by sergio on 15/12/2020
 * All rights reserved GoodBarber
 */
//class similaire à celle pour affiché les vols de l'aéroport sauf qu'ici pas besoin de savoir quel cellule est cliqué
class AircraftFlightListRecyclerAdapter : RecyclerView.Adapter<AircraftFlightListRecyclerAdapter.MyViewHolder>() {

    var flightList : List<FlightModel>?  = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.i("RECYCLER", "onCreateViewHolder")
        return MyViewHolder(FlightInfoCell(parent.context))
    }

    override fun getItemCount(): Int {
        return flightList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("RECYCLER", "onBindViewHolder")
        val myCell = holder.itemView as FlightInfoCell
        myCell.bindData(flightList!![position])
    }
}