package com.example.trinolino.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trinolino.Classi.Ticket
import com.example.trinolino.R
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.adapter.TicketsAdapter
import com.example.trinolino.databinding.FragmentTicketsBinding
import com.example.trinolino.retrofit.Client
import com.example.trinolino.viewModel.TicketsViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketsFragment : Fragment(), TicketsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentTicketsBinding
    private val viewModel: TicketsViewModel by viewModels()
    private lateinit var adapter: TicketsAdapter

    companion object {
        const val TAG = "TicketsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TicketsAdapter(emptyList(), this)
        binding.Biglietti.layoutManager = LinearLayoutManager(context)
        binding.Biglietti.adapter = adapter
        if (UsersSession.userId != -1) {
            viewModel.tickets.observe(viewLifecycleOwner, Observer { tickets ->
                adapter.updateList(tickets)
            })

            // Recupera i biglietti per l'utente loggato
            viewModel.fetchTickets(UsersSession.userId, requireContext())
        } else {
            binding.bigliettitext.visibility = View.GONE
            binding.Biglietti.visibility = View.GONE
            binding.acceditickets.visibility = View.VISIBLE
            binding.nonlog.visibility = View.VISIBLE
        }

        binding.acceditickets.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(LoginFragment.TAG) == null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fra, LoginFragment(), LoginFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onAcquistaPastoClick(ticket: Ticket) {
        if (parentFragmentManager.findFragmentByTag(AcquistaPastoFragment.TAG) == null) {
            val fragment = AcquistaPastoFragment.newInstance(ticket.ticket_id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fra, fragment, AcquistaPastoFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onVisualizzaOrdineClick(ticket: Ticket) {
        if (parentFragmentManager.findFragmentByTag(OrderFragment.TAG) == null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fra, OrderFragment.newInstance(ticket.ticket_id), OrderFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCancellaClick(ticket: Ticket) {
        val ticketId = ticket.ticket_id
        val deleteRequest = JsonObject().apply {
            addProperty("ticket_id", ticketId)
        }

        Client.retrofit.deleteTicket(deleteRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Biglietto eliminato con successo", Toast.LENGTH_SHORT).show()
                    viewModel.fetchTickets(UsersSession.userId, requireContext()) // Ricarica i biglietti
                } else {
                    Toast.makeText(context, "Errore nell'eliminazione del biglietto: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onConvalidaClick(ticket: Ticket) {
        val userId = UsersSession.userId
        if (userId == -1) {
            Toast.makeText(context, "Devi effettuare il login per convalidare un biglietto", Toast.LENGTH_SHORT).show()
            return
        }

        // Invia la richiesta al ViewModel per aggiornare lo stato del biglietto
        viewModel.validateTicket(ticket.ticket_id, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}