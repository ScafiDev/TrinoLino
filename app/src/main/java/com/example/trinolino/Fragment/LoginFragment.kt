package com.example.trinolino.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.trinolino.R
import com.example.trinolino.Classi.Users
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.databinding.FragmentLoginBinding
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var loginSuccessCallback: OnLoginSuccessListener? = null
    private var imageButtonClickCallback: OnImageButtonClickListener? = null

    companion object {
        const val TAG = "LoginFragment"
    }

    interface OnLoginSuccessListener {
        fun onLoginSuccess(user: Users)
    }

    interface OnImageButtonClickListener {
        fun onImageButtonClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginSuccessListener) {
            loginSuccessCallback = context
        } else {
            throw RuntimeException("$context must implement OnLoginSuccessListener")
        }
        if (context is OnImageButtonClickListener) {
            imageButtonClickCallback = context
        } else {
            throw RuntimeException("$context must implement OnImageButtonClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButton2.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@LoginFragment).commit()
            imageButtonClickCallback?.onImageButtonClick()
        }

        binding.accesso.setOnClickListener {
            val email = binding.emaillogin.text.toString()
            val password = binding.passwordlogin.text.toString()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            val credentials = JsonObject().apply {
                addProperty("email", email)
                addProperty("password", password)
            }

            Client.retrofit.loginUser(credentials).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val userJson = response.body()
                        val user = Users(
                            user_id = userJson?.get("user_id")?.asInt ?: 0,
                            nome = userJson?.get("nome")?.asString ?: "",
                            cognome = userJson?.get("cognome")?.asString ?: "",
                            email = userJson?.get("email")?.asString ?: "",
                            password = "", // Non salvare la password
                            sesso = userJson?.get("sesso")?.asString ?: "",
                            saldo = userJson?.get("saldo")?.asInt ?: 0, // Imposta a 0 se null
                            corse_gratuite = userJson?.get("corse_gratuite")?.asInt ?: 0 // Imposta a 0 se null
                        )

                        UsersSession.userId = user.user_id
                        UsersSession.userName = user.nome
                        UsersSession.userEmail = user.email
                        UsersSession.saldo = user.saldo
                        UsersSession.corseGratuite = user.corse_gratuite

                        Toast.makeText(context, "Login effettuato", Toast.LENGTH_SHORT).show()
                        loginSuccessCallback?.onLoginSuccess(user)
                        parentFragmentManager.popBackStack()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "Login fallito: ${response.code()} - $errorBody")
                        Toast.makeText(context, "Email o password errati", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e(TAG, "Errore di rete", t)
                    Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.registrati.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(RegistratiFragment.TAG) == null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fra, RegistratiFragment(), RegistratiFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
