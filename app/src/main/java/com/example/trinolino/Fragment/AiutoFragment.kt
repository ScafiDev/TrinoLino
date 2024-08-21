package com.example.trinolino.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trinolino.databinding.FragmentAiutoBinding


class AiutoFragment : Fragment() {

    private lateinit var binding: FragmentAiutoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAiutoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back7.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    companion object {
    const val TAG = "AiutoFragment"
    }
}