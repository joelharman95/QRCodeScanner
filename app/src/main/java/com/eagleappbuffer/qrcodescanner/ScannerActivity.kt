package com.eagleappbuffer.qrcodescanner

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_scanner.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerActivity : AppCompatActivity() {

    companion object {
        var BARCODE_VALUE = "Barcode Value"
    }

    private val format: BarcodeScannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val barcodeScanner = BarcodeScanning.getClient(format)
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private val targetResolution = Size(480, 640)
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var readed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        lifecycle.addObserver(barcodeScanner)
//        cameraExecutor = Executors.newSingleThreadExecutor()
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetResolution(targetResolution)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalyzer?.setAnalyzer(cameraExecutor, { image: ImageProxy ->
            val mediaImage = image.image
            if (mediaImage != null) {
                val inputImage =
                    InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                barcodeScanner.process(inputImage)
                    .addOnSuccessListener { barcodes: List<Barcode> ->
                        for (barcode in barcodes) {
                            val vibrator =
                                applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT > 28) vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    100,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            ) else vibrator.vibrate(100)
                            if (!readed) {
                                readed = true
                                val intent = Intent(this, ResultActivity::class.java)
                                intent.putExtra(BARCODE_VALUE, barcode.rawValue)
                                startActivity(intent)
                                finish()
                                break
                            }
                        }
                    }
                    .addOnFailureListener { e: Exception -> println("GET_______$e") }
                    .addOnCompleteListener { task: Task<List<Barcode?>?>? -> image.close() }
            }
        })
        startScanner()

    }

    private fun startScanner() {
        val processCameraProvider = ProcessCameraProvider.getInstance(this)
        processCameraProvider.addListener({
            try {
                val cameraProvider = processCameraProvider.get()
                val preview = Preview.Builder().build()
                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                cameraProvider.unbindAll()
                camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                preview.setSurfaceProvider(cameraPreview?.surfaceProvider)
            } catch (e: ExecutionException) {
                println("GET_______$e")
            } catch (e: InterruptedException) {
                println("GET_______$e")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        cameraExecutor?.shutdown()
        super.onDestroy()
    }

}