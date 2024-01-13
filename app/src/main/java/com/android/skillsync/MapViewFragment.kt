package com.android.skillsync

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.skillsync.databinding.FragmentMapViewBinding
import com.android.skillsync.models.CompanyLocation
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

class MapViewFragment : Fragment(), LocationListener {

    private lateinit var aMapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var view: View
    private val LOCATIONS_PERMISSIONS_CODE = 2

    private var _binding: FragmentMapViewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapViewBinding.inflate(layoutInflater, container, false)
        view = binding.root

        initializeMapView()
        checkLocationPermissions()

        return view
    }

    private fun initializeMapView() {
        aMapView = binding.root.findViewById(R.id.map)
        aMapView.setTileSource(TileSourceFactory.MAPNIK)
        aMapView.controller.setZoom(17.0)

        // Load map configuration
        Configuration.getInstance().load(
            context?.applicationContext, context?.getSharedPreferences(
                "locations",
                AppCompatActivity.MODE_PRIVATE
            )
        )
    }

    private fun checkLocationPermissions() {
        if (isLocationPermissionGranted()) requestLocationUpdates()
        else requestLocationPermissions()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATIONS_PERMISSIONS_CODE
        )
    }

    private fun addMarker(geoPoint: GeoPoint, title: String, snippet: String) {
        val markerIcon: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.map_pin_icon)

        val overlayItem = OverlayItem(title, snippet, geoPoint)
        overlayItem.setMarker(markerIcon)

        // Create an ItemizedIconOverlay and add the OverlayItem to it
        val itemizedIconOverlay = ItemizedIconOverlay<OverlayItem>(
            context?.applicationContext,
            listOf(overlayItem),
            null
        )

        // Add the ItemizedIconOverlay to the MapView
        aMapView.overlays.add(itemizedIconOverlay)
    }

    private fun requestLocationUpdates() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if GPS permission is granted
        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
        val geoPoint = GeoPoint(location.latitude, location.longitude)
        aMapView.controller.setCenter(geoPoint)
        addMarker(geoPoint, "I Am Here!", "OSMDroid Marker")

        addLocationsMarks()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATIONS_PERMISSIONS_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun addLocationsMarks() {
        val database = Firebase.firestore
        val companiesReference = database.collection("companies")
        fetchAllCompanies(companiesReference)
    }

    private fun fetchAllCompanies(companiesReference: CollectionReference) {
        companiesReference.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val companyId = document.id
                    fetchLocationsForCompany(companiesReference, companyId)
                }
            }.addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting company documents: $exception")
            }
    }

    private fun fetchLocationsForCompany(
        companiesReference: CollectionReference,
        companyId: String
    ) {
        val locationsCollection = companiesReference.document(companyId)

        locationsCollection.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data?.get("location") as? Map<*, *>
                    if (data != null) {
                        val address = data["address"] as? String ?: "Unknown Address"
                        val latitude =
                            (data["location"] as com.google.firebase.firestore.GeoPoint).latitude
                        val longitude =
                            (data["location"] as com.google.firebase.firestore.GeoPoint).longitude

                        val locationData = CompanyLocation(
                            address,
                            com.google.firebase.firestore.GeoPoint(latitude, longitude)
                        )
                        processLocationDocument(locationData)
                    } else Log.e("Firestore", "Error getting location documents")
                } else Log.e("Firestore", "Error getting location documents")

            }
            .addOnFailureListener { locationException ->
                Log.e("Firestore", "Error getting location documents: $locationException")
            }
    }

    private fun processLocationDocument(locationDocument: CompanyLocation) {
        val latitude = locationDocument.location.latitude
        val longitude = locationDocument.location.longitude
        val address = locationDocument.address


        val geoPoint = GeoPoint(latitude, longitude)

        // Add a marker for each location
        addMarker(geoPoint, address, "OSMDroid Marker")
    }
}