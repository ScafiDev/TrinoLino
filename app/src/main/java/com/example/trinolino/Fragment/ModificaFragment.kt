package com.example.trinolino.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.databinding.FragmentModificaBinding
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModificaFragment : Fragment() {

    private lateinit var binding: FragmentModificaBinding

    companion object {
        const val TAG = "ModificaFragment"
    }
    interface OnImageButtonClickListner{
        fun onImageButtonClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModificaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = UsersSession.userId
        binding.imageButton3.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        Client.retrofit.getUser(userId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userJson = response.body()
                    if (userJson != null) {
                        val userName = userJson.get("nome").asString
                        val userEmail = userJson.get("email").asString
                        val userCognome = userJson.get("cognome").asString
                        val userSesso = userJson.get("sesso").asString
                        binding.nomemodifica.setText(userName)
                        binding.emailmodifica.setText(userEmail)
                        binding.cognomemodifica.setText(userCognome)
                        if (userSesso == "Maschio") {
                            binding.maschiomodifca.isChecked = true
                            binding.femminamodifica.isChecked = false
                        } else {
                            binding.femminamodifica.isChecked = true
                            binding.maschiomodifca.isChecked = false
                        }
                    }
                } else {
                    Toast.makeText(context, "Errore nel recupero dei dati", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })

        binding.modificadati.setOnClickListener {
            val selectedSessoId = binding.radioGroup2.checkedRadioButtonId
            val selectedSesso = view.findViewById<RadioButton>(selectedSessoId).text.toString()
            val updatedUser = JsonObject().apply {
                addProperty("nome", binding.nomemodifica.text.toString())
                addProperty("cognome", binding.cognomemodifica.text.toString())
                addProperty("email", binding.emailmodifica.text.toString())
                addProperty("sesso", selectedSesso)
            }

            Client.retrofit.updateUser(userId, updatedUser).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Dati aggiornati con successo", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Errore nell'aggiornamento dei dati", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
