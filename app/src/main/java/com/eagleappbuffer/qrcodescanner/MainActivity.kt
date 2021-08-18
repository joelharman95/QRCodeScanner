package com.eagleappbuffer.qrcodescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startActivity(Intent(this, ScannerActivity::class.java))
                } else {
                    showAlert("Camera permission is needed for this app to operate")
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        llScan.setOnClickListener {
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

}