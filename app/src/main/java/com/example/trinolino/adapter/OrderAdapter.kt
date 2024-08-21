package com.example.trinolino.adapter

import com.example.trinolino.Classi.OrderItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.R

class OrderAdapter(private var items: List<OrderItem>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemQuantity: TextView = view.findViewById(R.id.itemQuantity)
        val itemTotalPrice: TextView = view.findViewById(R.id.itemTotalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.item_name
        holder.itemQuantity.text = "Quantità: ${item.quantity}"
        holder.itemTotalPrice.text = "Prezzo: ${item.item_price * item.quantity} €"
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<OrderItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}