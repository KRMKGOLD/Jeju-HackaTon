package com.example.hackatonproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
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
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var service: JejuService

    private val dataArray: ArrayList<LatLng> = arrayListOf<LatLng>()
    private val jejuLocation = LatLng(33.35, 126.53)

    private var myLatitude: Double = 0.0
    private var myLongitude: Double = 0.0
    // 내 경도와 위도

    private lateinit var locationManager: LocationManager
    private lateinit var locate: Location

    private val jsonObject = JsonObject()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseUrl = "http://2ed43a78.ngrok.io/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(JejuService::class.java)

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var sendsCCTV : Call<JsonObject>
        var sendsLight : Call<JsonObject>
        val repos = service.listRepose()

        getCCTVData.setOnClickListener {
            sendsCCTV = service.cctvData(jsonObject)
            sendsCCTV.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("fail", "send data fail")
                    Log.d("fail - throw", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("succeed - data", "succeed")
                    Log.d("return - data", response.body().toString())

                    repos.enqueue(object : Callback<List<Repo>> {
                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                            Log.d("fail", "get data fail")
                            Log.d("fail - throw", t.toString())
                        }

                        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                            Log.d("succeed - data", "succeed")
                            Log.d("return - data", response.body().toString())

                            dataArray.clear()

                            if(response.body() != null){
                                for (index in response.body()!!) {
                                    makeCCTVMarker(LatLng(index.latitude.toDouble(), index.longitude.toDouble()))

                                }

                                Toast.makeText(this@MainActivity, "데이터를 불러왔습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(this@MainActivity, "주변에 CCTV가 없습니다.", Toast.LENGTH_SHORT).show()
                            }

                        }
                    })
                }
            })
        }

        getLightData.setOnClickListener {
            sendsLight = service.lightData(jsonObject)
            sendsLight.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("fail", "send data fail")
                    Log.d("fail - throw", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("succeed - data", "succeed")
                    Log.d("return - data", response.body().toString())

                    repos.enqueue(object : Callback<List<Repo>> {
                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                            Log.d("fail", "get data fail")
                            Log.d("fail - throw", t.toString())
                        }

                        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                            Log.d("succeed - data", "succeed")
                            Log.d("return - data", response.body().toString())

                            dataArray.clear()

                            if(response.body() != null){
                                for (index in response.body()!!) {
                                    makeLightMarker(LatLng(index.latitude.toDouble(), index.longitude.toDouble()))

                                }

                                Toast.makeText(this@MainActivity, "데이터를 불러왔습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(this@MainActivity, "주변에 가로등이 없습니다.", Toast.LENGTH_SHORT).show()
                            }

                        }
                    })
                }
            })
        }

        getAllData.setOnClickListener {
            sendsCCTV = service.cctvData(jsonObject)
            sendsCCTV.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("fail", "send data fail")
                    Log.d("fail - throw", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("succeed - data", "succeed")
                    Log.d("return - data", response.body().toString())

                    repos.enqueue(object : Callback<List<Repo>> {
                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                            Log.d("fail", "get data fail")
                            Log.d("fail - throw", t.toString())
                        }

                        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                            Log.d("succeed - data", "succeed")
                            Log.d("return - data", response.body().toString())

                            dataArray.clear()

                            if(response.body() != null){
                                for (index in response.body()!!) {
                                    makeCCTVMarker(LatLng(index.latitude.toDouble(), index.longitude.toDouble()))

                                }

                                Toast.makeText(this@MainActivity, "데이터를 불러왔습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(this@MainActivity, "주변에 CCTV가 없습니다.", Toast.LENGTH_SHORT).show()
                            }

                        }
                    })
                }
            })

            sendsLight = service.lightData(jsonObject)
            sendsLight.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("fail", "send data fail")
                    Log.d("fail - throw", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("succeed - data", "succeed")
                    Log.d("return - data", response.body().toString())

                    repos.enqueue(object : Callback<List<Repo>> {
                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                            Log.d("fail", "get data fail")
                            Log.d("fail - throw", t.toString())
                        }

                        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                            Log.d("succeed - data", "succeed")
                            Log.d("return - data", response.body().toString())

                            dataArray.clear()

                            if(response.body() != null){
                                for (index in response.body()!!) {
                                    makeLightMarker(LatLng(index.latitude.toDouble(), index.longitude.toDouble()))

                                }

                                Toast.makeText(this@MainActivity, "데이터를 불러왔습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(this@MainActivity, "주변에 가로등이 없습니다.", Toast.LENGTH_SHORT).show()
                            }

                        }
                    })
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        for (index in dataArray) {
            makeCCTVMarker(index)
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jejuLocation, 10F))
        map.setMinZoomPreference(10F)
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomControlsEnabled = true

        val permissionListener: PermissionListener = object : PermissionListener {
            @SuppressLint("MissingPermission")
            override fun onPermissionGranted() {
                map.isMyLocationEnabled = true
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {

            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("위치 권한을 허용하지 않을 경우, GPS 기능을 사용할 수 없습니다.")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()

        map.setOnMyLocationButtonClickListener {
            val criteria = Criteria()

            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locate = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false))

            myLatitude = locate.latitude
            myLongitude = locate.longitude

            //        val myRow: Int = Math.round( (  ) / 0.04363).toInt()
//        val myColumn: Int = Math.round( (  ) / 0.05681).toInt()

            var myRow = (myLatitude.minus(33.2536182)).div(0.04363)
            var myColumn = (myLongitude.minus(126.1632263)).div(0.05681)

            jsonObject.addProperty("row", Math.round(myRow))
            jsonObject.addProperty("column", Math.round(myColumn))

            Log.d("lati-long", "lati : $myLatitude long : $myLongitude")

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(myLatitude, myLongitude), 15F))
            true
        }
    }

    private fun makeCCTVMarker(locate: LatLng) {
        val smallMarker =
            Bitmap.createScaledBitmap(
                (ContextCompat.getDrawable(this, R.drawable.ic_camera) as BitmapDrawable).bitmap, 100, 100, false
            )

        val makerOptions = MarkerOptions()
        makerOptions
            .position(locate)
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        map.addMarker(makerOptions).showInfoWindow()
    }

    private fun makeLightMarker(locate: LatLng) {
        val smallMarker =
            Bitmap.createScaledBitmap(
                (ContextCompat.getDrawable(this, R.drawable.ic_streetlamp) as BitmapDrawable).bitmap, 100, 100, false
            )

        val makerOptions = MarkerOptions()
        makerOptions
            .position(locate)
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        map.addMarker(makerOptions).showInfoWindow()
    }
}