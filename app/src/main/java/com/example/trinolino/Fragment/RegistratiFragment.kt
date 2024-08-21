package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trinolino.R
import com.example.trinolino.Classi.Users
import com.example.trinolino.databinding.FragmentRegistratiBinding
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegistratiFragment : Fragment() {

    private lateinit var binding: FragmentRegistratiBinding
    private var callback: OnImageButtonClickListener? = null

    interface OnImageButtonClickListener {
        fun onImageButtonClick()
    }

    companion object {
        const val TAG = "RegistratiFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistratiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
            callback?.onImageButtonClick()
        }

        binding.button.setOnClickListener {
            val nome = binding.nomereg.text.toString()
            val cognome = binding.CognomeReg.text.toString()
            val email = binding.EmailReg.text.toString()
            val password = binding.Password.text.toString()
            val confermaPassword = binding.confermaPassword.text.toString()

            val sesso = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.radioMaschio -> "Maschio"
                R.id.radioFemmina -> "Femmina"
                else -> ""
            }
            if (!isValidEmail(email)) {
                Toast.makeText(context, "Formato email non valido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confermaPassword) {
                Toast.makeText(context, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            } else {
                val emailJson = JsonObject().apply {
                    addProperty("email", email)
                }

                Client.retrofit.checkEmail(emailJson).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            val exists = response.body()?.get("exists")?.asBoolean ?: false
                            if (exists) {
                                Toast.makeText(context, "Email già esistente", Toast.LENGTH_SHORT).show()
                            } else {
                                val user = Users(0, nome, cognome, email, password, sesso, 0, 0)

                                val userJson = JsonObject().apply {
                                    addProperty("nome", user.nome)
                                    addProperty("cognome", user.cognome)
                                    addProperty("email", user.email)
                                    addProperty("password", user.password)
                                    addProperty("sesso", user.sesso)
                                    addProperty("saldo", user.saldo)
                                    addProperty("corse_gratuite", user.corse_gratuite)
                                }

                                Client.retrofit.createUser(userJson).enqueue(object : Callback<JsonObject> {
                                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Registrazione effettuata", Toast.LENGTH_SHORT).show()
                                            parentFragmentManager.popBackStack()
                                            callback?.onImageButtonClick()
                                        } else {
                                            Toast.makeText(context, "Registrazione NON effettuata", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        } else {
                            Toast.makeText(context, "Errore durante il controllo dell'email", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[^@]+@[^@]+\\.[^@]+$"
        val pattern = Pattern.compile(emailPattern)
        return pattern.matcher(email).matches()
    }
}
