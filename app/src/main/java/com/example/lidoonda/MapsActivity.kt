package com.example.lidoonda

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

public class MapsActivity : FragmentActivity(), OnMapReadyCallback{
    lateinit var gMap : GoogleMap
    lateinit var map : FrameLayout
    override fun onMapReady(p0: GoogleMap) {
        this.gMap = p0
        var position : LatLng = LatLng(38.1869486, 13.3203805)
        this.gMap.addMarker(MarkerOptions().position(position).title("Lido Onda"))
        this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 11F))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_mappa)

        map = findViewById(R.id.map)
        var mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

}