package com.android.skillsync

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.skillsync.databinding.ActivityMapViewBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import android.Manifest
import android.widget.Toast

class MapActivity : AppCompatActivity(), LocationListener {

    private lateinit var aMapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMapViewBinding
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use View Binding for cleaner UI code
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the map view
        initializeMapView()

        // Check and request location permissions if needed
        checkLocationPermissions()
    }

    private fun initializeMapView() {
        aMapView = binding.map
        aMapView.setTileSource(TileSourceFactory.MAPNIK)
        aMapView.controller.setZoom(17.0)

        // Load map configuration
        Configuration.getInstance().load(applicationContext, getSharedPreferences("locations", MODE_PRIVATE))
    }

    private fun checkLocationPermissions() {
        if (isLocationPermissionGranted()) requestLocationUpdates()
        else requestLocationPermissions()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            locationPermissionCode
        )
    }

    private fun addMarker(geoPoint: GeoPoint, title: String, snippet: String) {
        val markerIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.map_pin_icon)

        // Create an OverlayItem with the marker's GeoPoint, title, and snippet
        val overlayItem = OverlayItem(title, snippet, geoPoint)

        // Set the marker icon
        overlayItem.setMarker(markerIcon)

        // Create an ItemizedIconOverlay and add the OverlayItem to it
        val itemizedIconOverlay = ItemizedIconOverlay<OverlayItem>(
            applicationContext,
            listOf(overlayItem),
            null
        )

        // Add the ItemizedIconOverlay to the MapView
        aMapView.overlays.add(itemizedIconOverlay)
    }

    private fun requestLocationUpdates() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if GPS permission is granted
            // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000, // Minimum time interval between updates (in milliseconds)
            1f,   // Minimum distance between updates (in meters)
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        // Handle the updated location
        val geoPoint = GeoPoint(location.latitude, location.longitude)
        aMapView.controller.setCenter(geoPoint)
        addMarker(geoPoint, "I Am Here!", "OSMDroid Marker")

        // add hardcoded markers - TODO GET LOCATIONS FROM API
        val hardcodedGeoPoint = GeoPoint(31.9591, 33.8061)
        addMarker(hardcodedGeoPoint, "I Am Here!", "OSMDroid Marker")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            locationPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
