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
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.adapter.OrderAdapter
import com.example.trinolino.databinding.FragmentOrderBinding
import com.example.trinolino.viewModel.OrderViewModel

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var adapter: OrderAdapter
    private var ticketId: Int? = null

    companion object {
        const val TAG = "OrderFragment"

        fun newInstance(ticketId: Int): OrderFragment {
            val fragment = OrderFragment()
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
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketId = arguments?.getInt("ticket_id")

        adapter = OrderAdapter(emptyList())
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewOrders.adapter = adapter

        viewModel.orderItems.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateList(items)
        })

        val userId = UsersSession.userId
        if (userId != -1 && ticketId != null) {
            viewModel.fetchOrderItems(userId, ticketId!!, requireContext())
        } else {
            Toast.makeText(context, "Dati utente o ticket mancanti", Toast.LENGTH_SHORT).show()
        }

        binding.imageButton7.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
