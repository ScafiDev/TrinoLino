package com.example.trinolino.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trinolino.Classi.Notizia
import com.example.trinolino.R

class NotizieAdapter(
    private var notizie: List<Notizia>
) : RecyclerView.Adapter<NotizieAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foto: ImageView = view.findViewById(R.id.notiziaFoto)
        val titolo: TextView = view.findViewById(R.id.notiziaTitolo)
        val descrizione: TextView = view.findViewById(R.id.notiziaDescrizione)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notizia_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notizia = notizie[position]
        holder.titolo.text = notizia.titolo
        holder.descrizione.text = notizia.descrizione
        Glide.with(holder.itemView.context).load(notizia.foto).into(holder.foto)
    }

    override fun getItemCount() = notizie.size

    fun updateList(newNotizie: List<Notizia>) {
        notizie = newNotizie
        notifyDataSetChanged()
    }
}