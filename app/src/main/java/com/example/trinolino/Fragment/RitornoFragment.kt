package com.example.trinolino.Fragment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trinolino.MainActivity

import com.example.trinolino.databinding.FragmentRitornoBinding
import java.util.Locale


class RitornoFragment : Fragment() {
    private lateinit var binding: FragmentRitornoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRitornoBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButton5.setOnClickListener{
            parentFragmentManager.popBackStack()

        }
        binding.calendarView2.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = format.format(calendar.time)
            (activity as? MainActivity)?.setDataRitorno(selectedDate)

            parentFragmentManager.popBackStack()
        }
    }



        companion object {
        const val TAG = "RitornoFragment"

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


}