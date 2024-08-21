package com.example.trinolino.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.Classi.Stazioni
import com.example.trinolino.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class StationsViewModel : ViewModel() {

    private val _items = MutableLiveData<List<Stazioni>>()
    val items: LiveData<List<Stazioni>> get() = _items

    private val _filteredItems = MutableLiveData<List<Stazioni>>()
    val filteredItems: LiveData<List<Stazioni>> get() = _filteredItems

    // Funzione per recuperare le stazioni dal backend
    fun fetchStations() {
        Client.retrofit.getStations().enqueue(object : Callback<List<Stazioni>> {
            override fun onResponse(call: Call<List<Stazioni>>, response: Response<List<Stazioni>>) {
                if (response.isSuccessful) {
                    _items.value = response.body() ?: emptyList()
                    _filteredItems.value = _items.value
                } else {
                    // Gestisci errori di risposta
                }
            }

            override fun onFailure(call: Call<List<Stazioni>>, t: Throwable) {
                // Gestisci errori di rete
            }
        })
    }

    // Funzione per filtrare la lista in base alla query
    fun filterList(query: String) {
        _items.value?.let { itemsList ->
            val filteredList = itemsList.filter {
                it.stationName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) ||
                        it.city.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            }
            _filteredItems.value = filteredList
        }
    }
}
