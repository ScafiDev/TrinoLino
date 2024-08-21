package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trinolino.MainActivity
import com.example.trinolino.Classi.Schedule
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.adapter.ScheduleAdapter
import com.example.trinolino.databinding.FragmentCercaBinding
import com.example.trinolino.retrofit.Client
import com.example.trinolino.viewModel.SchedulesViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CercaFragment : Fragment(), ScheduleAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCercaBinding
    private val viewModel: SchedulesViewModel by viewModels()
    private lateinit var adapter: ScheduleAdapter

    private var returnDate: String? = null
    private var endStation: String? = null
    private var startStation: String? = null
    private var isReturnTrip: Boolean = false
    private var adultTickets: Int = 0
    private var childTickets: Int = 0
    private lateinit var useFreeRidesCheckbox: CheckBox

    private val ticketsToPurchase = mutableListOf<Triple<Schedule, Int, Boolean>>() // Lista di (Schedule, Cost, IsReturn)

    companion object {
        const val TAG = "CercaFragment"

        fun newInstance(
            startStation: String?,
            endStation: String?,
            date: String?,
            returnDate: String?,
            adultTickets: Int,
            childTickets: Int
        ): CercaFragment {
            val fragment = CercaFragment()
            val args = Bundle()
            args.putString("startStation", startStation)
            args.putString("endStation", endStation)
            args.putString("date", date)
            args.putString("returnDate", returnDate)
            args.putInt("adultTickets", adultTickets)
            args.putInt("childTickets", childTickets)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCercaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isReturnTrip = false

        startStation = arguments?.getString("startStation")
        endStation = arguments?.getString("endStation")
        val date = arguments?.getString("date")
        returnDate = arguments?.getString("returnDate")
        adultTickets = arguments?.getInt("adultTickets") ?: 0
        childTickets = arguments?.getInt("childTickets") ?: 0

        useFreeRidesCheckbox = binding.useFreeRidesCheckbox

        adapter = ScheduleAdapter(emptyList(), this, adultTickets, childTickets)
        binding.recycleroute.layoutManager = LinearLayoutManager(context)
        binding.recycleroute.adapter = adapter

        viewModel.schedules.observe(viewLifecycleOwner, Observer { schedules ->
            adapter.updateList(schedules)
        })

        viewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            adapter.setOffers(offers)
        })

        binding.back5.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        if (startStation != null && endStation != null && date != null) {
            viewModel.fetchSchedules(startStation!!, endStation!!, date, requireContext())
        }

        val userId = UsersSession.userId
        if (userId != -1) {
            viewModel.fetchUserOffers(userId, requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ticketsToPurchase.clear() // Pulisce la lista quando la vista viene distrutta
    }

    override fun onItemClick(schedule: Schedule, totalCost: Int) {
        val userId = UsersSession.userId
        if (userId == -1) {
            Toast.makeText(requireContext(), "Devi effettuare il login per acquistare un biglietto", Toast.LENGTH_SHORT).show()
            return
        }

        Client.retrofit.getUser(userId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userJson = response.body()
                    val saldo = userJson?.get("saldo")?.asInt ?: 0
                    val freeRides = userJson?.get("corse_gratuite")?.asInt ?: 0
                    val offers = viewModel.offers.value ?: emptyList()

                    if (useFreeRidesCheckbox.isChecked && freeRides >= (adultTickets + childTickets)) {
                        viewModel.updateFreeRidesAndPurchaseTickets(
                            context = requireContext(),
                            userId = userId,
                            schedule = schedule,
                            ticketsToBuy = adultTickets + childTickets,
                            adultTickets = adultTickets,
                            childTickets = childTickets
                        ) {
                            ticketsToPurchase.add(Triple(schedule, 0, isReturnTrip))
                            processReturnTrip()
                        }
                    } else {
                        val applicableOffer = offers.find { it.start_station == schedule.start_station && it.end_station == schedule.end_station }
                        val finalPrice = if (applicableOffer != null) applicableOffer.price else totalCost

                        if (saldo < finalPrice) {
                            Toast.makeText(requireContext(), "Saldo insufficiente", Toast.LENGTH_SHORT).show()
                            return
                        }

                        viewModel.updateSaldo(requireContext(), -finalPrice) {
                            ticketsToPurchase.add(Triple(schedule, finalPrice, isReturnTrip))
                            processReturnTrip()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Errore nel recupero del saldo: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(requireContext(), "Errore di rete: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun processReturnTrip() {
        if (returnDate != "Ritorno" && !isReturnTrip) {
            isReturnTrip = true
            val newStartStation = endStation
            val newEndStation = startStation
            val newDate = returnDate
            binding.and.text = "Ritorno"
            if (newStartStation != null && newEndStation != null && newDate != null) {
                viewModel.fetchSchedules(newStartStation, newEndStation, newDate, requireContext())
            }
        } else {
            completeTicketPurchase()
        }
    }

    private fun completeTicketPurchase() {
        val userId = UsersSession.userId
        if (userId == -1) {
            Toast.makeText(requireContext(), "Devi effettuare il login per acquistare un biglietto", Toast.LENGTH_SHORT).show()
            return
        }

        ticketsToPurchase.forEach { (schedule, cost, isReturn) ->
            viewModel.purchaseTickets(requireContext(), userId, schedule, adultTickets, childTickets) {
                if (isReturn) {
                    (activity as? MainActivity)?.goToHomePage()
                }
            }
        }
        ticketsToPurchase.clear() // Pulisce la lista dopo l'acquisto

        // Naviga alla homepage dopo aver completato l'acquisto
        (activity as? MainActivity)?.goToHomePage()
    }
}
