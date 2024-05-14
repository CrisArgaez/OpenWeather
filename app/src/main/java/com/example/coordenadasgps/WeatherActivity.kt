package com.example.coordenadasgps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import kotlinx.coroutines.*
import org.json.JSONObject

class WeatherActivity : AppCompatActivity() {

    private var latitudActual = 0.0 // Ubicación predeterminada
    private var longitudActual = 0.0 // Ubicación predeterminada
    val apiKey = "87348746bfbaef27ae027273dfa25063"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        latitudActual = intent.getDoubleExtra("latitudActual", 0.0)
        longitudActual = intent.getDoubleExtra("longitudActual", 0.0)

        GlobalScope.launch(Dispatchers.IO) {
            // Crea un objeto URL a partir de la cadena de texto.
            val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitudActual&lon=$longitudActual&appid=$apiKey&lang=es&units=metric")

            // Abre una conexión a la URL.
            val connection = url.openConnection()

            // Crea un BufferedReader para leer la respuesta de la conexión.
            // InputStreamReader convierte los bytes de la respuesta en caracteres.
            // El método use garantiza que el BufferedReader se cierre cuando ya no sea necesario.
            BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->

                // Declara una variable para almacenar cada línea de la respuesta.
                var line: String?

                // Lee cada línea de la respuesta hasta que no haya más líneas (es decir, readLine() devuelve null).
                while (inp.readLine().also { line = it } != null) {

                    // Imprime la línea actual.
                    println(line)

                    // Convierte la línea en un objeto JSON
                    val respuestaClima = JSONObject(line)

                    // Obtiene el estado del clima de la respuesta
                    val estadoClima = respuestaClima.getJSONArray("weather").getJSONObject(0).getString("main")

                    // Selecciona el recurso de fondo apropiado basándote en el estado del clima
                    val idImagenFondo = when (estadoClima) {
                        "Rain" -> R.drawable.fondo_lluvioso_animado
                        "Clear" -> R.drawable.fondo_soleado_animado
                        "Clouds" -> R.drawable.fondo_nublado_animado
                        // Agrega más condiciones climáticas aquí
                        else -> R.drawable.fondo_predeterminado_animado
                    }

                    val nombreCiudad = respuestaClima.getString("name")
                    val codigoPais = respuestaClima.getJSONObject("sys").getString("country")
                    val temp = respuestaClima.getJSONObject("main").getDouble("temp").toInt()
                    val tempMin = respuestaClima.getJSONObject("main").getDouble("temp_min").toInt()
                    val tempMax = respuestaClima.getJSONObject("main").getDouble("temp_max").toInt()
                    val descripcionClima = respuestaClima.getJSONArray("weather").getJSONObject(0).getString("description")
                    val descripcionClimaMayuscula = descripcionClima.capitalize()
                    val humedad = respuestaClima.getJSONObject("main").getInt("humidity")
                    val presion = respuestaClima.getJSONObject("main").getInt("pressure")
                    val velocidadViento = respuestaClima.getJSONObject("wind").getDouble("speed")
                    val porcentajeNubes = respuestaClima.getJSONObject("clouds").getInt("all")
                    val sensacionTermica = respuestaClima.getJSONObject("main").getDouble("feels_like").toInt()

                    withContext(Dispatchers.Main) {
                        // Encuentra los TextViews en el layout
                        val imageViewFondo: ImageView = findViewById(R.id.imageViewFondo)
                        val textViewCiudad: TextView = findViewById(R.id.textViewLugar)
                        val textViewTemp: TextView = findViewById(R.id.textViewTemp)
                        val textViewTempMin: TextView = findViewById(R.id.textViewTempMin)
                        val textViewTempMax: TextView = findViewById(R.id.textViewTempMax)
                        val textViewDescripcion: TextView = findViewById(R.id.textViewDescripcion)
                        val textViewHumedad: TextView = findViewById(R.id.textViewHumedadDato)
                        val textViewPresion: TextView = findViewById(R.id.textViewPresionDato)
                        val textViewVelocidadViento: TextView = findViewById(R.id.textViewVientoDato)
                        val textViewNubes: TextView = findViewById(R.id.textViewNubesDato)
                        val textViewSensacion: TextView = findViewById(R.id.textViewSensacionDato)

                        Glide.with(this@WeatherActivity).asGif().load(idImagenFondo).into(imageViewFondo)
                        textViewCiudad.text = "$nombreCiudad, $codigoPais"
                        textViewTemp.text = "$temp°C"
                        textViewTempMin.text = "$tempMin°C"
                        textViewTempMax.text = "$tempMax°C"
                        textViewDescripcion.text = descripcionClimaMayuscula
                        textViewHumedad.text = "$humedad%"
                        textViewPresion.text = "$presion mb"
                        textViewVelocidadViento.text = "$velocidadViento km/h"
                        textViewNubes.text = "$porcentajeNubes%"
                        textViewSensacion.text = "$sensacionTermica°C"
                    }
                }
            }
        }
    }
}

