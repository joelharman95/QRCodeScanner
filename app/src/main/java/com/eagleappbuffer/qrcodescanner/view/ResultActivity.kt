package com.eagleappbuffer.qrcodescanner.view

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eagleappbuffer.qrcodescanner.R
import com.eagleappbuffer.qrcodescanner.copyValue
import com.eagleappbuffer.qrcodescanner.openBrowser
import com.eagleappbuffer.qrcodescanner.pref.PreferenceManager
import com.eagleappbuffer.qrcodescanner.shareCode
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.activity_result.adView
import java.util.concurrent.TimeUnit

class ResultActivity : AppCompatActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        etOutput.paintFlags = etOutput.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        etOutput.setText(intent.getStringExtra(ScannerActivity.BARCODE_VALUE))

        llCopy.setOnClickListener {
            copyValue(etOutput.text.toString())
        }
        llShare.setOnClickListener {
            shareCode(etOutput.text.toString())
        }
        llVisitNow.setOnClickListener {
            openBrowser(etOutput.text.toString())
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
                PreferenceManager(this@ResultActivity).apply {
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