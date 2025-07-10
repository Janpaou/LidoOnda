package com.example.lidoonda

import AdapterClass
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
var numItem : Int = 0

class MenuFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList : Array<Int>
    lateinit var titleList : Array<String>
    lateinit var buttonList : Array<Int>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageList = arrayOf(
            R.drawable.cocktail,
            R.drawable.cocktail,
            R.drawable.cocktail,
            R.drawable.birra,
            R.drawable.birra,
            R.drawable.birra
        )
        titleList = arrayOf(
            "Mojito",
            "Piña Colada",
            "Cuba Libre",
            "Birra dello stretto",
            "Semedorato",
            "Forst"
        )
        buttonList = arrayOf(0,1,2,0,1,0
        )


        recyclerView = view.findViewById(R.id.recyclerViewMenu)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf<DataClass>()
        getData()
    }
    private fun getData(){
        for (i in imageList.indices){
            val dataClass = DataClass(imageList[i], titleList[i], buttonList[i])
            dataList.add(dataClass)
        }
        var adapter = AdapterClass(dataList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : AdapterClass.onItemClickListener{
            override fun onItemClick(position: Int) {

            }
        })
    }
}