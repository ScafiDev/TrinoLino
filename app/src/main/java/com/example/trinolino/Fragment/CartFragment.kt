package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trinolino.Object.Cart
import com.example.trinolino.Classi.CartItem
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.adapter.CartAdapter
import com.example.trinolino.databinding.FragmentCartBinding
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), CartAdapter.OnItemRemoveClickListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartAdapter
    private var ticketId: Int? = null

    companion object {
        const val TAG = "CartFragment"

        fun newInstance(ticketId: Int): CartFragment {
            val fragment = CartFragment()
            val args = Bundle()
            args.putInt("ticket_id", ticketId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketId = arguments?.getInt("ticket_id")

        adapter = CartAdapter(Cart.getItems(), this)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.cartRecyclerView.adapter = adapter

        updateTotalPrice()

        binding.checkoutButton.setOnClickListener {
            val totalPrice = Cart.getTotalPrice()
            if (totalPrice == 0) {
                Toast.makeText(context, "Aggiungi qualcosa al carrello prima di procedere al checkout", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ticketId?.let {

                createOrder(it, totalPrice)
            } ?: Toast.makeText(context, "Ticket ID mancante", Toast.LENGTH_SHORT).show()
        }

        binding.svuota.setOnClickListener {
            Cart.clear()
            adapter.updateList(Cart.getItems())
            updateTotalPrice()
        }

        binding.imageButton9.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateTotalPrice() {
        val totalPrice = Cart.getTotalPrice()
        binding.totalPriceTextView.text = "Totale: €$totalPrice"
    }

    override fun onItemRemoveClick(cartItem: CartItem) {
        Cart.removeItem(cartItem)
        adapter.updateList(Cart.getItems())
        updateTotalPrice()
        Toast.makeText(context, "Elemento rimosso dal carrello", Toast.LENGTH_SHORT).show()
    }

    private fun createOrder(ticketId: Int, totalPrice: Int) {
        val userId = UsersSession.userId
        if (userId == -1) {
            Toast.makeText(context, "Effettua il login", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica il saldo dell'utente
        Client.retrofit.getUser(userId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    val currentSaldo = user?.get("saldo")?.asInt ?: 0
                    if (currentSaldo < totalPrice) {
                        Toast.makeText(context, "Saldo insufficiente", Toast.LENGTH_SHORT).show()
                    } else {
                        val orderItems = JsonArray().apply {
                            Cart.getItems().forEach {
                                add(JsonObject().apply {
                                    addProperty("menu_item_id", it.item.id)
                                    addProperty("quantity", it.quantity)
                                })
                            }
                        }

                        val order = JsonObject().apply {
                            addProperty("user_id", userId)
                            addProperty("ticket_id", ticketId)
                            addProperty("total_price", totalPrice)
                            add("items", orderItems)
                        }

                        Client.retrofit.createOrder(order).enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Ordine creato con successo", Toast.LENGTH_SHORT).show()
                                    Cart.clear()
                                    // Aggiorna il saldo dell'utente
                                    UsersSession.saldo -= totalPrice
                                    parentFragmentManager.popBackStack()
                                } else {
                                    Toast.makeText(context, "Errore durante la creazione dell'ordine", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Toast.makeText(context, "Errore di rete durante la creazione dell'ordine", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    Toast.makeText(context, "Errore durante il recupero del saldo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete durante il recupero del saldo", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}