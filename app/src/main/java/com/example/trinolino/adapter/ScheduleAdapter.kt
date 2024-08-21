package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.Classi.Offer
import com.example.trinolino.R
import com.example.trinolino.Classi.Schedule

class ScheduleAdapter(
    private var schedules: List<Schedule>,
    private val itemClickListener: OnItemClickListener,
    private val adultTickets: Int,
    private val childTickets: Int
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private var offers: List<Offer> = emptyList()

    interface OnItemClickListener {
        fun onItemClick(schedule: Schedule, totalCost: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val startStation: TextView = view.findViewById(R.id.startStation)
        val endStation: TextView = view.findViewById(R.id.endStation)
        val departureDate: TextView = view.findViewById(R.id.departureDate)
        val arrivalDate: TextView = view.findViewById(R.id.arrivalDate)
        val departureTime: TextView = view.findViewById(R.id.arrivalTime)
        val arrivalTime: TextView = view.findViewById(R.id.departureTime)
        val prezzo: TextView = view.findViewById(R.id.prezzo)
        val buyButton: Button = view.findViewById(R.id.acquista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.startStation.text = schedule.start_station
        holder.endStation.text = schedule.end_station
        holder.departureDate.text = schedule.departure_date
        holder.arrivalDate.text = schedule.arrival_date
        holder.departureTime.text = "Orario di arrivo: " + schedule.departure_time
        holder.arrivalTime.text = "Orario di partenza:"+ schedule.arrival_time

        val adultPrice = 15
        val childPrice = 10

        // Calcola il prezzo totale
        var totalCost = (adultTickets * adultPrice) + (childTickets * childPrice)

        // Applica l'offerta se esiste
        offers.forEach { offer ->
            if (offer.start_station == schedule.start_station && offer.end_station == schedule.end_station) {
                totalCost = (adultTickets + childTickets) * offer.price
            }
        }

        holder.prezzo.text = "Prezzo totale: €$totalCost"

        holder.buyButton.setOnClickListener {
            itemClickListener.onItemClick(schedule, totalCost)
        }
    }

    override fun getItemCount() = schedules.size

    fun updateList(newSchedules: List<Schedule>) {
        schedules = newSchedules
        notifyDataSetChanged()
    }

    fun setOffers(newOffers: List<Offer>) {
        offers = newOffers
        notifyDataSetChanged()
    }
}
