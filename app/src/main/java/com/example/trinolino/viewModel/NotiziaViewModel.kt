package com.example.trinolino.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.Classi.Notizia
import com.example.trinolino.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class NotiziaViewModel : ViewModel() {

    private val _notizie = MutableLiveData<List<Notizia>>()
    val notizie: LiveData<List<Notizia>> get() = _notizie

    fun fetchNotizie(context: Context) {
        Client.retrofit.getNotizie().enqueue(object : Callback<List<Notizia>> {
            override fun onResponse(call: Call<List<Notizia>>, response: Response<List<Notizia>>) {
                if (response.isSuccessful) {
                    _notizie.postValue(response.body())
                } else {
                    Toast.makeText(context, "Errore nel caricamento delle notizie", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Notizia>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
