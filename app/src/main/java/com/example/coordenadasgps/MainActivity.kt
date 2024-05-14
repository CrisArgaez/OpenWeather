package com.example.coordenadasgps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationListenerCompat


class MainActivity : AppCompatActivity(), LocationListenerCompat {

    private lateinit var locationManager: LocationManager
    private lateinit var latitud: TextView
    private lateinit var longitud: TextView
    var latitudActual = 0.0
    var longitudActual = 0.0

    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"

        val button : Button = findViewById(R.id.getLocation)
        button.setOnClickListener{
            getLocation()
        }

        val buttonMapa : Button = findViewById(R.id.visualizarMapa)
        buttonMapa.setOnClickListener{
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("latitudActual", latitudActual);
            intent.putExtra("longitudActual", longitudActual);
            startActivity(intent)
        }

        val buttonClima: Button = findViewById(R.id.visualizarClima)
        buttonClima.setOnClickListener {
            // Crea un Intent para iniciar WeatherActivity
            val intent = Intent(applicationContext, WeatherActivity::class.java)

            // Pone la latitud y longitud actuales como extras
            intent.putExtra("latitudActual", latitudActual)
            intent.putExtra("longitudActual", longitudActual)

            // Inicia WeatherActivity
            startActivity(intent)
        }
    }

    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,5f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        latitud = findViewById(R.id.latitud)
        longitud = findViewById(R.id.longitud)
        latitud.text = "" + location.latitude
        longitud.text = "" + location.longitude
        latitudActual = location.latitude
        longitudActual = location.longitude
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso no ha sido concedido, solo lo mantenemos en la pagina principal de la app
                    return
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,5f, this)
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}