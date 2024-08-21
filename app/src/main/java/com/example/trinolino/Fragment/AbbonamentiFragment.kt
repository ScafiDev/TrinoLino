package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trinolino.Classi.Abbonamenti
import com.example.trinolino.adapter.AbbonamentiAdapter
import com.example.trinolino.databinding.FragmentAbbonamentiBinding
import com.example.trinolino.viewModel.AbbonamentiViewModel

class AbbonamentiFragment : Fragment(), AbbonamentiAdapter.OnItemClickListener {

    private lateinit var binding: FragmentAbbonamentiBinding
    private val viewModel: AbbonamentiViewModel by viewModels()
    private lateinit var adapter: AbbonamentiAdapter

    companion object {
        const val TAG = "AbbonamentiFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAbbonamentiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageButton8.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        adapter = AbbonamentiAdapter(emptyList(), this)
        binding.recyclerViewAbbonamenti.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAbbonamenti.adapter = adapter

        viewModel.abbonamenti.observe(viewLifecycleOwner, Observer { abbonamenti ->
                adapter.updateList(abbonamenti)
            })


        viewModel.fetchAbbonamenti(requireContext())
    }

    override fun onItemClick(abbonamento: Abbonamenti) {
        viewModel.purchaseAbbonamento(requireContext(), abbonamento)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}