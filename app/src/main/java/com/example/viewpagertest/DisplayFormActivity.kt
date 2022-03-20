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
import android.graphics.pdf.PdfRenderer

import android.os.ParcelFileDescriptor
import java.io.FileOutputStream
import android.graphics.Bitmap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.viewpagertest.database.MyDatabaseHelper
import com.example.viewpagertest.helper.Constant
import com.example.viewpagertest.models.FieldSpecifier
import com.example.viewpagertest.models.Form
import com.example.viewpagertest.models.FormField


class DisplayFormActivity : AppCompatActivity() {
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    private lateinit var currentPage: PdfRenderer.Page
    private lateinit var imageViewPdf: ImageView
    private lateinit var displayText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_form)
        displayText = findViewById(R.id.displayText)

        val form = intent.getStringExtra("File")
        val formId = intent.getIntExtra("FormId", -1)
        var filename = ""

        if(form == "Aadhar Card Form") {
            filename = "adharCard.pdf"
            if(formId == 0) return

            val fields = Constant.Fields.AADHAR_FIELD_NAMES
            for (field in fields) {
                val fieldName = field.split("@")[0]
                val x = field.split(":")[1].toString().toFloat()
                val y = field.split(":")[2].toString().toFloat()

                val dbHelper = MyDatabaseHelper(this)
                val fieldSpecifier = FieldSpecifier(
                    null,
                    Form(formId, "", "", 0, 0),
                    dbHelper.getFormFieldData(null, fieldName)[0],
                    x,
                    y,
                    ""
                )

                dbHelper.close()
                dbHelper.insertFieldSpecifier(fieldSpecifier)
                dbHelper.close()
            }
        }
        else if(form == "Pan Card Form")
            filename = "panCard.pdf"
        else
            filename = "votingCard.pdf"

        val uri = FileProvider.getUriForFile(this, "be.MyApplication", File(cacheDir, filename))

        val back = findViewById<Button>(R.id.back)
        val forward = findViewById<Button>(R.id.forward)

        back.isEnabled = false
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frgCurrent, SelectFormDetailsFragment.INSTANCE)
            commit()
        }

        forward.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frgCurrent, ActionPdfOpenFragment.newInstance(uri, formId))
                commit()
            }
            displayText.text = "Preview Details"
            it.isEnabled = false
            back.isEnabled = true
        }

        back.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frgCurrent, SelectFormDetailsFragment.INSTANCE)
                commit()
            }
            displayText.text = "Add Details"
            it.isEnabled = false
            forward.isEnabled = true
        }
    }
}