package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.Classi.Offer
import com.example.trinolino.R

class OffersAdapter(
    private var offers: List<Offer>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(offer: Offer)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.description)

        val validFrom: TextView = view.findViewById(R.id.validFrom)
        val validTo: TextView = view.findViewById(R.id.validTo)
        val price: TextView = view.findViewById(R.id.price)
        val startStation: TextView = view.findViewById(R.id.startStation)
        val endStation: TextView = view.findViewById(R.id.endStation)
        val redeemButton: Button = view.findViewById(R.id.redeemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer = offers[position]
        holder.description.text = offer.description

        holder.validFrom.text = offer.valid_from
        holder.validTo.text = offer.valid_to
        holder.price.text = "${offer.price} €"
        holder.startStation.text = offer.start_station
        holder.endStation.text = offer.end_station

        holder.redeemButton.setOnClickListener {
            itemClickListener.onItemClick(offer)
        }
    }

    override fun getItemCount() = offers.size

    fun updateList(newOffers: List<Offer>) {
        offers = newOffers
        notifyDataSetChanged()
    }
}
