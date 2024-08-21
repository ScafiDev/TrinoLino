package com.example.trinolino.viewModel

import com.example.trinolino.Classi.OrderItem
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

class OrderViewModel : ViewModel() {

    private val _orderItems = MutableLiveData<List<OrderItem>>()
    val orderItems: LiveData<List<OrderItem>> get() = _orderItems

    fun fetchOrderItems(userId: Int, ticketId: Int, context: Context) {
        Client.retrofit.getOrder(userId, ticketId).enqueue(object : Callback<List<OrderItem>> {
            override fun onResponse(call: Call<List<OrderItem>>, response: Response<List<OrderItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _orderItems.postValue(it)
                    } ?: run {
                        Toast.makeText(context, "Nessun ordine trovato", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nel caricamento degli ordini", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderItem>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
