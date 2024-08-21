package com.example.trinolino.Fragment

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trinolino.MainActivity
import com.example.trinolino.databinding.FragmentAndataBinding
import java.util.Locale


class AndataFragment : Fragment() {

    private lateinit var binding: FragmentAndataBinding
    private var callback: OnImageButtonClickListener? = null

    interface OnImageButtonClickListener {
        fun onImageButtonClick()
    }

    companion object {
        const val TAG = "AndataFragment"
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
        binding = FragmentAndataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back2.setOnClickListener {
            parentFragmentManager.popBackStack()
            callback?.onImageButtonClick()
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = format.format(calendar.time)
            (activity as? MainActivity)?.setDataAndata(selectedDate)

            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
