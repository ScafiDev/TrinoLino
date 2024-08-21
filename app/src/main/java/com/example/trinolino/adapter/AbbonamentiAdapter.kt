package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.Classi.Abbonamenti
import com.example.trinolino.R

class AbbonamentiAdapter(
    private var abbonamenti: List<Abbonamenti>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AbbonamentiAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(abbonamento: Abbonamenti)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.abbDescription)
        val price: TextView = view.findViewById(R.id.abbPrice)
        val acquistaButton: Button = view.findViewById(R.id.abbAcquista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.abbonamento_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val abbonamento = abbonamenti[position]
        holder.description.text = abbonamento.description
        holder.price.text = "Prezzo: ${abbonamento.price} €"

        holder.acquistaButton.setOnClickListener {
            itemClickListener.onItemClick(abbonamento)
        }
    }

    override fun getItemCount() = abbonamenti.size

    fun updateList(newAbbonamenti: List<Abbonamenti>) {
        abbonamenti = newAbbonamenti
        notifyDataSetChanged()
    }
}
