package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trinolino.Classi.Offer
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.adapter.OffersAdapter
import com.example.trinolino.databinding.FragmentOfferteBinding
import com.example.trinolino.retrofit.Client
import com.example.trinolino.viewModel.OffersViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfferteFragment : Fragment(), OffersAdapter.OnItemClickListener {
    companion object{
        const val TAG = "OfferteFragment"
    }

    private lateinit var binding: FragmentOfferteBinding
    private val viewModel: OffersViewModel by viewModels()
    private lateinit var adapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfferteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButton6.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        adapter = OffersAdapter(emptyList(), this)
        binding.recyclerOffers.layoutManager = LinearLayoutManager(context)
        binding.recyclerOffers.adapter = adapter

        viewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            adapter.updateList(offers)
        })

        viewModel.fetchOffers(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onItemClick(offer: Offer) {
        val userId = UsersSession.userId
        val redeemRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("offer_id", offer.offer_id)
        }
        if (userId == -1) {
            Toast.makeText(context, "Effettua il login", Toast.LENGTH_SHORT).show()
        } else {

            Client.retrofit.redeemOffer(redeemRequest).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Offerta riscattata con successo",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Errore nel riscattare l'offerta oppure offerta già riscattata",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
