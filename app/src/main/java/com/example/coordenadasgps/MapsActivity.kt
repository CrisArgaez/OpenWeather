package com.example.coordenadasgps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.coordenadasgps.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var latitudActual = 0.0 // Ubicación predeterminada
    private var longitudActual = 0.0 // Ubicación predeterminada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Si el usuario ha proporcionado su ubicación, la usamos. Si no, usamos la ubicación predeterminada.
        latitudActual = intent.getDoubleExtra("latitudActual", latitudActual)
        longitudActual = intent.getDoubleExtra("longitudActual", longitudActual)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el SupportMapFragment y notificar cuando el mapa pueda usarse
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Marcador de la ubicacion actual
        val ubicacionActual = LatLng(latitudActual, longitudActual)
        mMap.addMarker(MarkerOptions().position(ubicacionActual).title("Ubicacion actual del dispositivo"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15f)) // 15 es el nivel de zoom
    }
}

