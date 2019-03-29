package com.example.hackatonproject

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.hackatonproject.data.JejuService
import com.example.hackatonproject.data.Repo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val array =
        arrayListOf<LatLng>(
            LatLng(33.2375458, 126.4769981),
            LatLng(33.254121, 126.3315148),
            LatLng(33.25129639, 126.5703212),
            LatLng(33.247217, 126.554658),
            LatLng(33.286521, 126.60817),
            LatLng(33.242239, 126.558918)
        )
    private val jejuLocation = LatLng(33.35, 126.53)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseUrl = "https://416c3a01.ngrok.io"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: JejuService = retrofit.create(JejuService::class.java)
        val jsonObject = JsonObject()
        jsonObject.addProperty("row", 500)
        jsonObject.addProperty("column", 700)

        val sends = service.sendRow(jsonObject)
        Log.d("jsonObject", jsonObject.toString())

        sendDataButton.setOnClickListener {
            sends.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("fail", "send data fail")
                    Log.d("fail - throw", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("succeed - data", "succeed")
                    Log.d("return - data", response.body().toString())
                }

            })
        }

        val repos = service.listRepose()

        getDataButton.setOnClickListener {
            repos.enqueue(object : Callback<List<Repo>> {
                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    Log.d("Succeed", "Succeed")
                    Log.d("data", response.body().toString())
                }

                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    Log.d("Error", "Error")
                }

            })
        }

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap : GoogleMap) {
        map = googleMap

        for(index in array){
            makeMarker(index)
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jejuLocation, 10F))
        map.setMinZoomPreference(10F)
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.isMyLocationEnabled = true


//        val permissionListener: PermissionListener = object : PermissionListener {
//            override fun onPermissionGranted() {
//            }
//
//            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
//
//            }
//        }
//
//        TedPermission.with(this)
//            .setPermissionListener(permissionListener)
//            .setDeniedMessage("위치 권한을 허용하지 않을 경우, GPS 기능을 사용할 수 없습니다.")
//            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//            .check()
    }

    private fun makeMarker(locate: LatLng) {
        val smallMarker =
            Bitmap.createScaledBitmap(
                (ContextCompat.getDrawable(this, R.drawable.ic_camera) as BitmapDrawable).bitmap, 80, 80, false
            )

        val makerOptions = MarkerOptions()
        makerOptions
            .position(locate)
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        map.addMarker(makerOptions).showInfoWindow()
    }

}
