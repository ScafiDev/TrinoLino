package com.example.trinolino.Fragment

import com.example.trinolino.viewModel.StationsViewModel
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.viewModels
import com.example.trinolino.MainActivity
import com.example.trinolino.Classi.Stazioni
import com.example.trinolino.adapter.CustomAdapter
import com.example.trinolino.databinding.FragmentDestinazioneBinding

class DestinazioneFragment : Fragment(), CustomAdapter.OnItemClickListener {
    private lateinit var binding: FragmentDestinazioneBinding
    private var callback: OnImageButtonClickListener? = null
    private val viewModel: StationsViewModel by viewModels()
    private lateinit var adapter: CustomAdapter

    companion object {
        const val TAG = "DestinazioneFragment"
    }

    interface OnImageButtonClickListener {
        fun onImageButtonClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnImageButtonClickListener) {
            callback = context
        } else {
            throw RuntimeException("$context must implement OnImageButtonClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDestinazioneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creazione e impostazione dell'adapter per la RecyclerView
        adapter = CustomAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Osserva i dati del ViewModel
        viewModel.filteredItems.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateList(items)
        })

        // Configura il SearchView
        val searchEditText: EditText = binding.cerca
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterList(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Recupera le stazioni dal backend
        viewModel.fetchStations()

        // Gestione del click sull'ImageButton
        binding.imageButton.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@DestinazioneFragment).commit()
            callback?.onImageButtonClick()
        }
    }

        override fun onItemClick(station: Stazioni) {
            (activity as? MainActivity)?.setDestinazioneCity(station.stationName)
            parentFragmentManager.beginTransaction().remove(this@DestinazioneFragment).commit()
            callback?.onImageButtonClick()
            Toast.makeText(context, "Clicked: ${station.stationName}", Toast.LENGTH_SHORT).show()
        }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
