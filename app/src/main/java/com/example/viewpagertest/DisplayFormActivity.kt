package com.example.viewpagertest

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


class DisplayFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_form)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        openPdfDocument()
    }

    private fun openPdfDocument() {
        val assets = assets
        val pdfViewIntent = Intent(Intent.ACTION_VIEW);
        pdfViewIntent.setDataAndType(Uri.parse("file://${getFilesDir()}/adhar_card.pdf"),"application/pdf")
        val intent = Intent.createChooser(pdfViewIntent, "Open File")
        startActivity(intent)
    }
}