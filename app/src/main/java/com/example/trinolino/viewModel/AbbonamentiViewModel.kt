package com.example.trinolino.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.example.trinolino.Classi.Abbonamenti
import com.example.trinolino.Object.UsersSession
import com.google.gson.JsonObject

class AbbonamentiViewModel : ViewModel() {

    private val _abbonamenti = MutableLiveData<List<Abbonamenti>>()
    val abbonamenti: LiveData<List<Abbonamenti>> get() = _abbonamenti

    fun fetchAbbonamenti(context: Context) {
        Client.retrofit.getAbbonamenti().enqueue(object : Callback<List<Abbonamenti>> {
            override fun onResponse(call: Call<List<Abbonamenti>>, response: Response<List<Abbonamenti>>) {
                if (response.isSuccessful) {
                    _abbonamenti.postValue(response.body())
                } else {
                    Toast.makeText(context, "Errore nel caricamento degli abbonamenti", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Abbonamenti>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSaldo(context: Context, amount: Int, onSuccess: () -> Unit) {
        if (UsersSession.userId == -1) {
            Toast.makeText(context, "Effettua il login", Toast.LENGTH_SHORT).show()
            return
        }
        if (UsersSession.saldo < amount) {
            Toast.makeText(context, "Saldo insufficiente", Toast.LENGTH_SHORT).show()
            return
        }

        val requestBody = JsonObject().apply {
            addProperty("user_id", UsersSession.userId)
            addProperty("amount", -amount)
        }

        Client.retrofit.updateSaldo(requestBody).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val saldoAggiornato = response.body()?.get("saldo")?.asInt ?: UsersSession.saldo
                    UsersSession.saldo = saldoAggiornato
                    onSuccess() // esegue l'add delle corse solo se è andato a buon fine l'update
                    Toast.makeText(context, "Saldo aggiornato", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Errore durante l'aggiornamento del saldo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete durante l'aggiornamento del saldo", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addCorseGratuite(context: Context, corseGratuite: Int) {
        if (UsersSession.userId == -1) {
            Toast.makeText(context, "Effettua il login", Toast.LENGTH_SHORT).show()
            return
        }

        val requestBody = JsonObject().apply {
            addProperty("user_id", UsersSession.userId)
            addProperty("corse_gratuite", corseGratuite)
        }



        Client.retrofit.addCorseGratuite(requestBody).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val corseGratuiteAggiornate = response.body()?.get("corse_gratuite")?.asInt ?: UsersSession.corseGratuite
                    UsersSession.corseGratuite = corseGratuiteAggiornate
                    Toast.makeText(context, "Corse gratuite aggiunte", Toast.LENGTH_SHORT).show()

                } else {


                    Toast.makeText(context, "Errore durante l'aggiunta delle corse gratuite", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Toast.makeText(context, "Errore di rete durante l'aggiunta delle corse gratuite", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun purchaseAbbonamento(context: Context, abbonamento: Abbonamenti) {
        updateSaldo(context, abbonamento.price) {
            addCorseGratuite(context, abbonamento.number_of_rides)
        }
    }
}