package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.R
import com.example.trinolino.Classi.Stazioni

class CustomAdapter(
    private var stations: List<Stazioni>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(station: Stazioni)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stationName: TextView = view.findViewById(R.id.nomestazione)
        val city: TextView = view.findViewById(R.id.nomecitta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = stations[position]
        holder.stationName.text = station.stationName
        holder.city.text = station.city
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(station)
        }
    }

    override fun getItemCount() = stations.size

    fun updateList(newStations: List<Stazioni>) {
        stations = newStations
        notifyDataSetChanged()
    }
}
