package com.example.trinolino.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.Classi.Ticket
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketsViewModel : ViewModel() {

    private val _tickets = MutableLiveData<List<Ticket>>()
    val tickets: LiveData<List<Ticket>> get() = _tickets

    fun fetchTickets(userId: Int, context: Context) {
        Client.retrofit.getTickets(userId).enqueue(object : Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: Response<List<Ticket>>) {
                if (response.isSuccessful) {
                    _tickets.value = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Failed to fetch tickets: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun validateTicket(ticketId: Int, context: Context) {
        val validateRequest = JsonObject().apply {
            addProperty("isValidated", 1) // Imposta a 1 per indicare che il biglietto è convalidato
        }



        Client.retrofit.validateTicket(ticketId, validateRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    _tickets.value = _tickets.value?.map {
                        if (it.ticket_id == ticketId) it.copy(isValidated = 1) else it
                    }
                    Toast.makeText(context, "Biglietto convalidato con successo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Errore nella convalida del biglietto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })
    }
}