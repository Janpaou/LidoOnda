package com.example.lidoonda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged


class PagamentoFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pagamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            var jsonArray = makeQueryPrenotazioni("*", "pagamento", "idutente", "$idUtente")
            Log.v("metodo",jsonArray.toString())
            var jsonObject = jsonArray!!.get(0).asJsonObject
            var pagamento = Pagamento(jsonObject.get("numero").asString, jsonObject.get("data").asString, jsonObject.get("cvv").asString)

            inserisciDati(pagamento, view)
            view.findViewById<TextView>(R.id.inserisciButton).visibility = View.INVISIBLE
            view.findViewById<TextView>(R.id.modificaButton).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.rimuoviButton).visibility = View.VISIBLE
        }catch (e : Exception){
            Log.v("metodo", e.toString())
        }


        var editText =view.findViewById<EditText>(R.id.numeroPagamento)
        editText.doAfterTextChanged { text ->
            val formattedText = text.toString().replace(" ", "").chunked(4).joinToString(" ")

            if (formattedText != text.toString()) {
                editText.setText(formattedText)
                editText.setSelection(editText.length())
            }
        }
        var datepicker =view.findViewById<EditText>(R.id.pickDate)
        datepicker.doAfterTextChanged { text ->
            val formattedText = text.toString().replace("/", "").chunked(2).joinToString("/")

            if (formattedText != text.toString()) {
                datepicker.setText(formattedText)
                datepicker.setSelection(datepicker.length())
            }
        }
    }
    private fun inserisciDati(pagamento: Pagamento, view: View) {
        view.findViewById<TextView>(R.id.numeroPagamento).text = pagamento.numero
        view.findViewById<TextView>(R.id.pickDate).text = pagamento.data
        view.findViewById<TextView>(R.id.cvvpick).text = pagamento.cvv
    }



}


