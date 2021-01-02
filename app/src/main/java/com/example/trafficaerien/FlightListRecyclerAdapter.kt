package com.example.trafficaerien

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FlightListRecyclerAdapter : RecyclerView.Adapter<FlightListRecyclerAdapter.MyViewHolder>() {

    var flightList : List<FlightModel>?  = null
    var onItemClickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClicked(flight : FlightModel)
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FlightInfoCell(parent.context))
    }

    override fun getItemCount(): Int {
        return flightList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myCell = holder.itemView as FlightInfoCell
        myCell.bindData(flightList!![position])
        myCell.setOnClickListener { onItemClickListener?.onItemClicked(flightList!![position]) }
    }
}