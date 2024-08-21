package com.example.trinolino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trinolino.Classi.MenuItem
import com.example.trinolino.R

class MenuAdapter(
    private var items: List<MenuItem>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: MenuItem, quantity: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemDescription: TextView = view.findViewById(R.id.itemDescription)
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val quantity: TextView = view.findViewById(R.id.quantity)
        val addButton: Button = view.findViewById(R.id.add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemDescription.text = item.description
        holder.itemPrice.text = "${item.price} €"
        Glide.with(holder.itemView.context).load(item.image_path).into(holder.itemImage)

        holder.addButton.setOnClickListener {
            val quantity = holder.quantity.text.toString().toIntOrNull() ?: 1
            itemClickListener.onItemClick(item, quantity)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}