package com.android.skillsync

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.skillsync.databinding.ActivityMapViewBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import androidx.core.content.ContextCompat

class MapActivity : AppCompatActivity() {

    lateinit var aMapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        aMapView = binding.map
        aMapView.setTileSource(TileSourceFactory.MAPNIK)

        Configuration.getInstance().load(applicationContext, getSharedPreferences("locations", MODE_PRIVATE))

        val latitude = 31.9755974
        val longitude = 34.7702864

        val controller = aMapView.controller

        val mapPoint = GeoPoint(latitude, longitude)

        controller.setCenter(mapPoint)
        aMapView.controller.animateTo(mapPoint, 18.0, 1000L) // Use animateTo to set zoom level

        addMarker(mapPoint, "Marker Title", "Marker Snippet")
    }

    private fun addMarker(geoPoint: GeoPoint, title: String, snippet: String) {
        val markerIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.map_pin_icon)

        // Create an OverlayItem with the marker's GeoPoint, title, and snippet
        val overlayItem = OverlayItem(title, snippet, geoPoint)

        // Create an ItemizedIconOverlay and add the OverlayItem to it
        val itemizedIconOverlay = ItemizedIconOverlay<OverlayItem>(
            applicationContext,
            listOf(overlayItem),
            null
        )

        // Set the marker icon
        overlayItem.setMarker(markerIcon)

        // Add the ItemizedIconOverlay to the MapView
        aMapView.overlays.add(itemizedIconOverlay)
    }
}
