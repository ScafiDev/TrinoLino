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
import com.example.trinolino.Object.Cart
import com.example.trinolino.Classi.MenuItem
import com.example.trinolino.R
import com.example.trinolino.adapter.MenuAdapter
import com.example.trinolino.databinding.FragmentAcquistaPastoBinding
import com.example.trinolino.viewModel.MenuViewModel

class AcquistaPastoFragment : Fragment(), MenuAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAcquistaPastoBinding
    private val viewModel: MenuViewModel by viewModels()
    private lateinit var adapter: MenuAdapter
    private var ticketId: Int? = null

    companion object {
        const val TAG = "AcquistaPastoFragment"

        fun newInstance(ticketId: Int): AcquistaPastoFragment {
            val fragment = AcquistaPastoFragment()
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
        binding = FragmentAcquistaPastoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketId = arguments?.getInt("ticket_id")

        adapter = MenuAdapter(emptyList(), this)
        binding.recyclerViewmenu.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewmenu.adapter = adapter

        viewModel.menuItems.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateList(items)
        })

        viewModel.fetchMenuItems(requireContext())

        // listner pulsante carrello
        binding.cart.setOnClickListener {
            ticketId?.let { id ->
                val fragment = CartFragment.newInstance(id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fra, fragment, CartFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            } ?: Toast.makeText(context, "Ticket ID mancante", Toast.LENGTH_SHORT).show()
        }

        binding.imageButton4.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onItemClick(item: MenuItem, quantity: Int) {
        ticketId?.let {
            Cart.addItem(item, quantity, it)
            Toast.makeText(context, "Item aggiunto al carrello", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}