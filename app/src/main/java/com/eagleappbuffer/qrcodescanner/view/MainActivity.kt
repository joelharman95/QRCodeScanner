package com.eagleappbuffer.qrcodescanner.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eagleappbuffer.qrcodescanner.R
import com.eagleappbuffer.qrcodescanner.showAlert
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

}