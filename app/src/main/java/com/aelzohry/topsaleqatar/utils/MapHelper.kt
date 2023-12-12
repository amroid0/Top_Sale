package com.aelzohry.topsaleqatar.utils


import android.content.Context
import androidx.core.content.ContextCompat
import com.aelzohry.topsaleqatar.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import java.util.ArrayList
import com.google.android.gms.maps.model.CircleOptions





internal object MapHelper {
    val LA_LOCATION: LatLng = LatLng(34.052235, -118.243683)

    /**
     * In kilometers.
     */
    private const val EARTH_RADIUS = 6371
    fun createPolygonWithCircle(context: Context, center: LatLng, radius: Int): PolygonOptions {
        return PolygonOptions()
            .fillColor(ContextCompat.getColor(context, R.color.circle_color))
            .addAll(createOuterBounds())
            .addHole(createHole(center, radius))
            .strokeColor(ContextCompat.getColor(context, R.color.border_circle_color))
            .strokeWidth(2f)
    }

     fun drawCircle(context:Context,googleMap:GoogleMap,point: LatLng,meter:Double) : Circle {

         googleMap.clear()
        // Instantiating CircleOptions to draw a circle around the marker
        val circleOptions = CircleOptions()

        // Specifying the center of the circle
        circleOptions.center(point)

        // Radius of the circle
        circleOptions.radius(meter)

        // Border color of the circle
        circleOptions.strokeColor(ContextCompat.getColor(context, R.color.border_circle_color))

        // Fill color of the circle
        circleOptions.fillColor(ContextCompat.getColor(context, R.color.circle_color))

        // Border width of the circle
        circleOptions.strokeWidth(4f)

        // Adding the circle to the GoogleMap
        return googleMap.addCircle(circleOptions)
    }

    private fun createOuterBounds(): List<LatLng?> {
        val delta = 0.01f
        return object : ArrayList<LatLng?>() {
            init {
                add(LatLng((90 - delta).toDouble(), (-180 + delta).toDouble()))
                add(LatLng(0.0, (-180 + delta).toDouble()))
                add(LatLng((-90 + delta).toDouble(), (-180 + delta).toDouble()))
                add(LatLng((-90 + delta).toDouble(), 0.0))
                add(LatLng((-90 + delta).toDouble(), (180 - delta).toDouble()))
                add(LatLng(0.0, (180 - delta).toDouble()))
                add(LatLng((90 - delta).toDouble(), (180 - delta).toDouble()))
                add(LatLng((90 - delta).toDouble(), 0.0))
                add(LatLng((90 - delta).toDouble(), (-180 + delta).toDouble()))
            }
        }
    }

    private fun createHole(center: LatLng, radius: Int):List<LatLng> {
        val points = 50 // number of corners of inscribed polygon
        val radiusLatitude = Math.toDegrees((radius / EARTH_RADIUS.toFloat()).toDouble())
        val radiusLongitude = radiusLatitude / Math.cos(Math.toRadians(center.latitude))
        val result: MutableList<LatLng> = ArrayList<LatLng>(points)
        val anglePerCircleRegion = 2 * Math.PI / points
        for (i in 0 until points) {
            val theta = i * anglePerCircleRegion
            val latitude: Double = center.latitude + radiusLatitude * Math.sin(theta)
            val longitude: Double = center.longitude + radiusLongitude * Math.cos(theta)
            result.add(LatLng(latitude, longitude))
        }
        return result
    }
}