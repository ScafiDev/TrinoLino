package com.example.trinolino.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trinolino.R
import com.example.trinolino.adapter.NotizieAdapter
import com.example.trinolino.databinding.FragmentOccasioniBinding
import com.example.trinolino.viewModel.NotiziaViewModel

class OccasioniFragment : Fragment() {

    companion object {
        const val TAG = "OccasioniFragment"
    }
    private lateinit var binding: FragmentOccasioniBinding

    private val viewModel: NotiziaViewModel by viewModels()
    private lateinit var adapter: NotizieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOccasioniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.OfferteButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fra, OfferteFragment(), OfferteFragment.TAG)
                .addToBackStack(null)
                .commit()
        }

        binding.abbonamentibutton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fra, AbbonamentiFragment(), AbbonamentiFragment.TAG)
                .addToBackStack(null)
                .commit()
        }

        adapter = NotizieAdapter(emptyList())
        binding.recyclerViewNotizia.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewNotizia.adapter = adapter

        viewModel.notizie.observe(viewLifecycleOwner, Observer { notizia ->
            adapter.updateList(notizia)
        })

        viewModel.fetchNotizie(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
