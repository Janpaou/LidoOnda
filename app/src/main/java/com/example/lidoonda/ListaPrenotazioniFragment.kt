package com.example.lidoonda

import PrenotazioniClass
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaPrenotazioniFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClassPrenotazioni>
    lateinit var imageList : Array<Int>
    lateinit var titleList : Array<String>
    lateinit var buttonList : Array<Int>
    lateinit var annullaList : Array<Int>
    lateinit var rimuoviList : Array<Int>
    lateinit var pagamentoList : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_prenotazioni, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var jsonArray = makeQueryPrenotazioni("*", "prenotazioni", "idutente", "$idUtente")
        var listaPrenotazioni : MutableList<Prenotazioni> = mutableListOf()
        var i = 0
        while (i < jsonArray!!.size()){
            var jsonObject = jsonArray.get(i).asJsonObject
            var prenotazioni = Prenotazioni(jsonObject.get("idutente").toString(), jsonObject.get("tipo").asInt, jsonObject.get("ora").toString(), jsonObject.get("data").toString(), jsonObject.get("prenotato").asInt)
            Log.v("Prenotazione",prenotazioni.toString())
            listaPrenotazioni.add(prenotazioni)
            i++
        }

        imageList = arrayOf(
        )
        titleList = arrayOf(
        )
        buttonList = arrayOf(
        )
        annullaList = arrayOf(
        )
        rimuoviList = arrayOf(
        )
        pagamentoList = arrayOf(
        )
        var j = 0
        for (i in listaPrenotazioni){
            when(i.tipo){
                0 -> imageList += R.drawable.ombrellone
                1 -> imageList += R.drawable.lettino
            }
            titleList += ("Data: ${i.data}\nOre: ${i.ora}")
            buttonList += (i.prenotato)
            annullaList += (0)
            rimuoviList += (0)
            pagamentoList += (0)
            j++
        }
        Log.v("Image", imageList.size.toString())

        recyclerView = view.findViewById(R.id.recyclerViewPrenotazioni)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf<DataClassPrenotazioni>()
        getData()
    }

    private fun getData(){
        for (i in imageList.indices){
            val dataClassPrenotazioni = DataClassPrenotazioni(imageList[i], titleList[i], buttonList[i], annullaList[i], rimuoviList[i], pagamentoList[i])
            dataList.add(dataClassPrenotazioni)
        }
        var adapter = PrenotazioniClass(dataList)
        recyclerView.adapter = adapter

    }
    fun getPosition(index : Int, listData : ArrayList<DataClassPrenotazioni>){
        Log.v("Index", listData[index].toString())
        var data = listData[index].dataTitle.substring(6,18)
        var ora = listData[index].dataTitle.substring(25,27)
        Log.v("Index", data)
        Log.v("Index", ora)
        var query = "delete from prenotazioni where data=$data and ora = $ora and idutente=$idUtente;"
        makeRemove(query)
    }


}

