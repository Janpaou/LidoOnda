package com.example.lidoonda

import ImageAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleriaFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<ImageData>
    lateinit var imageList : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_galleria, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageList = arrayOf(
            R.drawable.foto1,
            R.drawable.foto2,
            R.drawable.foto3,
            R.drawable.foto4,
            R.drawable.foto5,
            R.drawable.foto6,
            R.drawable.foto7
        )
        recyclerView = view.findViewById(R.id.recyclerViewGalleria)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf<ImageData>()
        getData()
    }

    private fun getData(){
        for (i in imageList.indices){
            val imageClass = ImageData(imageList[i])
            dataList.add(imageClass)
        }
        recyclerView.adapter = ImageAdapter(dataList)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
        dataList.clear()
    }


}
