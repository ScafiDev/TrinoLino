package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trinolino.R
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.databinding.FragmentProfileBinding
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

   private lateinit var binding: FragmentProfileBinding

    companion object {
        const val TAG = "ProfileFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = UsersSession.userId
        if (userId != -1) {
            Client.retrofit.getUser(userId).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val userJson = response.body()
                        if (userJson != null) {
                            val userName = userJson.get("nome").asString
                            val userEmail = userJson.get("email").asString
                            val userCognome = userJson.get("cognome").asString
                            val userSesso = userJson.get("sesso").asString
                            val userSaldo = userJson.get("saldo")?.asInt ?: 0
                            val freeRides = userJson.get("corse_gratuite")?.asInt ?: 0

                            binding.nomeprofilo.text = "Nome:$userName"
                            binding.emailprofilo.text = "Email:$userEmail"
                            binding.sessoprofilo.text = "Sesso:$userSesso"
                            binding.cognomeprofilo.text = "Cognome:$userCognome"
                            binding.saldo.text = "Saldo: €$userSaldo"
                            binding.freeRides.text = "Corse gratuite: $freeRides"
                        } else {
                            Toast.makeText(context, "Errore nel recupero dei dati: utente non trovato", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Errore nel recupero dei dati: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Errore di rete: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            binding.nomeprofilo.visibility = View.GONE
            binding.cognomeprofilo.visibility = View.GONE
            binding.emailprofilo.visibility = View.GONE
            binding.sessoprofilo.visibility = View.GONE
            binding.modifica.visibility = View.GONE
            binding.imageView2.visibility = View.GONE
            binding.saldo.visibility = View.GONE
            binding.freeRides.visibility = View.GONE
            binding.layoutpay.visibility = View.GONE
            binding.creditplus.visibility = View.GONE
            binding.box.visibility= View.GONE
            binding.textView11.visibility = View.VISIBLE
            binding.accessoprof.visibility = View.VISIBLE

            Toast.makeText(context, "Non sei loggato", Toast.LENGTH_SHORT).show()
        }

        binding.modifica.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(ModificaFragment.TAG) == null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fra, ModificaFragment(), ModificaFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.accessoprof.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(LoginFragment.TAG) == null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fra, LoginFragment(), LoginFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.pay.setOnClickListener {
            val amount = binding.money.text.toString().toIntOrNull()
            val userId = UsersSession.userId

            if (amount == null || userId == -1) {
                Toast.makeText(context, "Invalid amount or user not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updateRequest = JsonObject().apply {
                addProperty("user_id", userId)
                addProperty("amount", amount)
            }

            Client.retrofit.updateSaldo(updateRequest).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val saldoAggiornato = response.body()?.get("saldo")?.asInt ?: UsersSession.saldo
                        UsersSession.saldo = saldoAggiornato
                        binding.saldo.text = "Saldo: €$saldoAggiornato"
                        Toast.makeText(context, "Saldo aggiornato con successo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update saldo: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        var i = 2
        binding.creditplus.setOnClickListener {
            if (i % 2 == 0) {
                binding.layoutpay.visibility = View.VISIBLE

            } else {
                binding.layoutpay.visibility = View.GONE
            }
            i++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}