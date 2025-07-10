package com.example.lidoonda

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

var flagDate: Boolean = false

class PrenotazioneFragment : Fragment() {
    var hour: Int = 0
    var minute: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_prenotazione, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var textView: TextView = view.findViewById(R.id.textPrenotazione)
        when (numServizio) {
            0 -> textView.text = "Prenota Ombrellone"
            1 -> textView.text = "Prenota Lettino"
        }
        var calendarView: CalendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            popTimePicker(view, year, month, dayOfMonth)

        }
    }

    fun popTimePicker(view: View, year: Int, month: Int, dayOfMonth: Int) {
        val onTimeSetListener =
            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                Log.d("console", "$dayOfMonth/${month + 1}/$year\n$hour:$minute")
                if (hour >= 8 && hour <= 20 && loggato) {
                    flagDate = true
                    if (hour < 10){
                        var query = "INSERT INTO prenotazioni VALUES ($idUtente, $numServizio, '0$hour', '$year-${month+1}-$dayOfMonth', 0);"
                        Log.v("Query", query)
                        makeInsert(query)
                        Toast.makeText(this.context,"Prenotazione effettuata", Toast.LENGTH_SHORT).show()
                    }else{
                        var query = "INSERT INTO prenotazioni VALUES ($idUtente, $numServizio, $hour, '$year-${month+1}-$dayOfMonth', 0);"
                        Log.v("Query", query)
                        makeInsert(query)
                        Toast.makeText(this.context,"Prenotazione effettuata", Toast.LENGTH_SHORT).show()
                    }
                } else if (!loggato){
                    flagDate = false
                    Toast.makeText(
                        this.context,
                        "Devi effettuare l'accesso per poter prenotare",
                        Toast.LENGTH_SHORT
                    ).show()
                }else {
                    flagDate = false
                    Toast.makeText(
                        this.context,
                        "Il lido non è aperto in questo orario",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        val timePickerDialog =
            TimePickerDialog(this.context,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Seleziona Orario")
        timePickerDialog.show()
    }

}