package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trinolino.Classi.CartItem
import com.example.trinolino.R

class CartAdapter(
    private var items: List<CartItem>,
    private val itemRemoveClickListener: OnItemRemoveClickListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    interface OnItemRemoveClickListener {
        fun onItemRemoveClick(cartItem: CartItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.cartItemName)
        val itemQuantity: TextView = view.findViewById(R.id.cartItemQuantity)
        val itemTotalPrice: TextView = view.findViewById(R.id.cartItemTotalPrice)
        val removeItemTextView: TextView = view.findViewById(R.id.removeItemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.item.name
        holder.itemQuantity.text = "Quantità: ${item.quantity}"
        holder.itemTotalPrice.text = "Prezzo totale: ${item.totalPrice} €"

        holder.removeItemTextView.setOnClickListener {
            itemRemoveClickListener.onItemRemoveClick(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}