package com.eagleappbuffer.qrcodescanner

import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
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
}