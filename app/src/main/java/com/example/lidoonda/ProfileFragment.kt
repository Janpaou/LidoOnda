package com.example.lidoonda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var jsonArray = makeQueryPrenotazioni("*", "utenti", "idutenti", "$idUtente")
        Log.v("Utente",jsonArray.toString())
        var jsonObject = jsonArray!!.get(0).asJsonObject
        var utente = User(jsonObject.get("nome").asString, jsonObject.get("cognome").asString, jsonObject.get("email").asString, jsonObject.get("password").asString)
        Log.v("Utente",utente.toString())
        inserisciDati(utente, view)
    }

    private fun inserisciDati(utente: User, view: View) {
        view.findViewById<TextView>(R.id.nomeProf).text = utente.name
        view.findViewById<TextView>(R.id.cognomeProf).text = utente.cognome
        view.findViewById<TextView>(R.id.emailProf).text = utente.email
        view.findViewById<TextView>(R.id.passwordProf).text = utente.password
    }
}