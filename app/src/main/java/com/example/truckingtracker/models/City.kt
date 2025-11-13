package com.example.truckingtracker.models

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class City(
    val name: String,
    val province: String,
    val latitude: Double,
    val longitude: Double
)

object PakistanCities {
    val SAMPLE: List<City> = listOf(
        City("Karachi", "Sindh", 24.8607, 67.0011),
        City("Lahore", "Punjab", 31.5820, 74.3294),
        City("Islamabad", "Islamabad Capital Territory", 33.6844, 73.0479),
        City("Rawalpindi", "Punjab", 33.6007, 73.0679),
        City("Faisalabad", "Punjab", 31.4180, 73.0790),
        City("Multan", "Punjab", 30.1575, 71.5249),
        City("Peshawar", "Khyber Pakhtunkhwa", 34.0151, 71.5250),
        City("Quetta", "Balochistan", 30.1798, 66.9750),
        City("Sialkot", "Punjab", 32.4927, 74.5319),
        City("Hyderabad", "Sindh", 25.3960, 68.3578),
        City("Gujranwala", "Punjab", 32.1877, 74.1945),
        City("Sargodha", "Punjab", 32.0833, 72.6715),
        City("Bahawalpur", "Punjab", 29.3956, 71.6836),
        City("Sukkur", "Sindh", 27.7052, 68.8573),
        City("Larkana", "Sindh", 27.5600, 68.2264),
        City("Gujrat", "Punjab", 32.5736, 74.0789),
        City("Sheikhupura", "Punjab", 31.7131, 73.9850),
        City("Abbottabad", "Khyber Pakhtunkhwa", 34.1466, 73.2115),
        City("Muzaffarabad", "Azad Kashmir", 34.3770, 73.4718),
        City("Gilgit", "Gilgit-Baltistan", 35.9187, 74.3090)
    )

    fun findNearestCity(lat: Double, lng: Double, cities: List<City>): City? {
        if (cities.isEmpty()) return null
        var best: City? = null
        var bestDist = Double.MAX_VALUE
        for (c in cities) {
            val d = haversine(lat, lng, c.latitude, c.longitude)
            if (d < bestDist) {
                bestDist = d
                best = c
            }
        }
        return best
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371e3 // metres
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val dphi = Math.toRadians(lat2 - lat1)
        val dlambda = Math.toRadians(lon2 - lon1)
        val a = sin(dphi / 2) * sin(dphi / 2) +
                cos(phi1) * cos(phi2) *
                sin(dlambda / 2) * sin(dlambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

