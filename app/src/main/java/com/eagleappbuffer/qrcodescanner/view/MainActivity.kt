package com.eagleappbuffer.qrcodescanner.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eagleappbuffer.qrcodescanner.R
import com.eagleappbuffer.qrcodescanner.pref.PreferenceManager
import com.eagleappbuffer.qrcodescanner.showAlert
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var counter = 0
    private var hasClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    if (hasClicked)
                        startActivity(Intent(this, ScannerActivity::class.java))
                } else {
                    showAlert("Camera permission is needed for this app to operate")
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        llScan.setOnClickListener {
            hasClicked = true
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startActivity(Intent(this, ScannerActivity::class.java))
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    }

    override fun onResume() {
        PreferenceManager(this).apply {
            if (getExpiryTime() > System.currentTimeMillis()) {
            } else
                clearAds()
            counter = getAdCount()
            if (counter >= 5)
                adView?.visibility = View.GONE
            else {
                adView?.visibility = View.VISIBLE
                loadAd()
            }
            adView?.resume()
        }
        super.onResume()
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }

    private fun loadAd() {
        MobileAds.initialize(this) { }
        val adRequest: AdRequest = AdManagerAdRequest.Builder().build()
        if (counter <= 4)
            adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdOpened() {
                counter++
                PreferenceManager(this@MainActivity).apply {
                    saveAdCount(counter)
                    saveExpiryTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2))  //  2 minutes
//                    saveExpiryTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1440))  //  24 hours
                }
                if (counter >= 5)
                    adView.visibility = View.GONE
            }
        }
    }

}