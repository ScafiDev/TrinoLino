package com.example.trinolino.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.Classi.Offer
import com.example.trinolino.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OffersViewModel : ViewModel() {

    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> get() = _offers

    fun fetchOffers(context: Context) {
        Client.retrofit.getOffers().enqueue(object : Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                if (response.isSuccessful) {
                    _offers.postValue(response.body())
                } else {
                    // Gestisci l'errore
                }
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                // Gestisci l'errore di rete
            }
        })
    }
}
