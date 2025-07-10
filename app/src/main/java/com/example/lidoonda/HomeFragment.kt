package com.example.lidoonda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeVis(loggato)


        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    private fun changeVis(state : Boolean){
        if (state){
            login.isVisible = false
            logout.isVisible = true
            profile.isVisible = true
            prenotazioni.isVisible = true
            signup.isVisible = false

        }else{
            login.isVisible = true
            logout.isVisible = false
            profile.isVisible = false
            prenotazioni.isVisible = false
            signup.isVisible = true
        }

    }

}