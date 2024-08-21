package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.R
import com.example.trinolino.Classi.Ticket

class TicketsAdapter(
    private var tickets: List<Ticket>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onAcquistaPastoClick(ticket: Ticket)
        fun onVisualizzaOrdineClick(ticket: Ticket)
        fun onCancellaClick(ticket: Ticket)
        fun onConvalidaClick(ticket: Ticket)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ticketId: TextView = view.findViewById(R.id.ticketId)
        val startStation: TextView = view.findViewById(R.id.startStation)
        val endStation: TextView = view.findViewById(R.id.endStation)
        val departureDate: TextView = view.findViewById(R.id.departureDate)
        val arrivalDate: TextView = view.findViewById(R.id.arrivalDate)
        val departureTime: TextView = view.findViewById(R.id.arrivalTime)
        val arrivalTime : TextView = view.findViewById(R.id.departureTime)
        val acquistaPastoButton: Button = view.findViewById(R.id.acquistaP)
        val visualizzabutton: Button = view.findViewById(R.id.visualizzaB)
        val cancellabutton: Button = view.findViewById(R.id.eliminaB)
        val convalidaButton: Button = view.findViewById(R.id.Convalida)
        val checkImage : ImageView = view.findViewById(R.id.check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.ticketId.text = ticket.ticket_id.toString()
        holder.startStation.text = ticket.start_station
        holder.endStation.text = ticket.end_station
        holder.departureDate.text = ticket.departure_date
        holder.arrivalDate.text = ticket.arrival_date
        holder.departureTime.text = "Ora di arrivo ${ticket.departure_time}"
        holder.arrivalTime.text = "Ora di partenza ${ticket.arrival_time}"

        holder.acquistaPastoButton.setOnClickListener {
            itemClickListener.onAcquistaPastoClick(ticket)
        }
        holder.visualizzabutton.setOnClickListener {
            itemClickListener.onVisualizzaOrdineClick(ticket)
        }

        holder.cancellabutton.setOnClickListener {
            itemClickListener.onCancellaClick(ticket)
        }

        holder.convalidaButton.setOnClickListener {
            itemClickListener.onConvalidaClick(ticket)
        }

        if (ticket.isValidated == 1) {
            holder.convalidaButton.visibility = View.GONE
            holder.checkImage.visibility = View.VISIBLE
        } else {
            holder.convalidaButton.visibility = View.VISIBLE
            holder.checkImage.visibility = View.GONE
        }
    }

    override fun getItemCount() = tickets.size

    fun updateList(newTickets: List<Ticket>) {
        tickets = newTickets
        notifyDataSetChanged()
    }
}
