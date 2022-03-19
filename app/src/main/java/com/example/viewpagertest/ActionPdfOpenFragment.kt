package com.example.viewpagertest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.viewpagertest.database.MyDatabaseHelper
import com.example.viewpagertest.models.FieldSpecifier
import java.io.IOException

class ActionPdfOpenFragment: Fragment() {
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var currentPage: PdfRenderer.Page
    private var currentPageNumber: Int = INITIAL_PAGE_INDEX
    private lateinit var pdfPageView: ImageView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var fragment: View

    val pageCount get() = pdfRenderer.pageCount

    companion object {
        private var formId: Int = -1
        private const val DOCUMENT_URI_ARGUMENT =
            "com.example.viewpagertest.DOCUMENT_URI_ARGUMENT"

        fun newInstance(documentUri: Uri, id: Int): ActionPdfOpenFragment {
            formId = id
            return ActionPdfOpenFragment().apply {
                arguments = Bundle().apply {
                    putString(DOCUMENT_URI_ARGUMENT, documentUri.toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragment = inflater.inflate(R.layout.fragment_action_open_pdf, container, false)
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pdfPageView = view.findViewById(R.id.image)
        previousButton = view.findViewById<Button>(R.id.previous).apply {
            setOnClickListener {
                showPage(currentPage.index - 1)
            }
        }
        nextButton = view.findViewById<Button>(R.id.next).apply {
            setOnClickListener {
                showPage(currentPage.index + 1)
            }
        }

        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        currentPageNumber = savedInstanceState?.getInt(CURRENT_PAGE_INDEX_KEY, INITIAL_PAGE_INDEX)
            ?: INITIAL_PAGE_INDEX
    }

    override fun onStart() {
        super.onStart()

        val documentUri = arguments?.getString(DOCUMENT_URI_ARGUMENT)?.toUri() ?: return
        try {
            openRenderer(activity, documentUri)
            showPage(currentPageNumber)
        } catch (ioException: IOException) {
            Log.d(TAG, "Exception opening document", ioException)
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            closeRenderer()
        } catch (ioException: IOException) {
            Log.d(TAG, "Exception closing document", ioException)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_PAGE_INDEX_KEY, currentPage.index)
        super.onSaveInstanceState(outState)
    }

    /**
     * Sets up a [PdfRenderer] and related resources.
     */
    @Throws(IOException::class)
    private fun openRenderer(context: Context?, documentUri: Uri) {
        if (context == null) return

        val fileDescriptor = context.contentResolver.openFileDescriptor(documentUri, "r") ?: return

        // This is the PdfRenderer we use to render the PDF.
        pdfRenderer = PdfRenderer(fileDescriptor)
        currentPage = pdfRenderer.openPage(currentPageNumber)
    }

    /**
     * Closes the [PdfRenderer] and related resources.
     *
     * @throws IOException When the PDF file cannot be closed.
     */
    @Throws(IOException::class)
    private fun closeRenderer() {
        currentPage.close()
        pdfRenderer.close()
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * The way [PdfRenderer] works is that it allows for "opening" a page with the method
     * [PdfRenderer.openPage], which takes a (0 based) page number to open. This returns
     * a [PdfRenderer.Page] object, which represents the content of this page.
     *
     * There are two ways to render the content of a [PdfRenderer.Page].
     * [PdfRenderer.Page.RENDER_MODE_FOR_PRINT] and [PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY].
     * Since we're displaying the data on the screen of the device, we'll use the later.
     *
     * @param index The page index.
     */
    @SuppressLint("StringFormatInvalid")
    private fun showPage(index: Int) {
        if (index < 0 || index >= pdfRenderer.pageCount) return

        currentPage.close()
        currentPage = pdfRenderer.openPage(index)

        // Important: the destination bitmap must be ARGB (not RGB).
        val bitmap = createBitmap(currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)

        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        pdfPageView.setImageBitmap(bitmap)

        val pageCount = pdfRenderer.pageCount
        previousButton.isEnabled = (0 != index)
        nextButton.isEnabled = (index + 1 < pageCount)
        activity?.title = String.format(getString(R.string.app_name, index + 1, pageCount))

        loadFields()
    }

    @SuppressLint("ResourceAsColor")
    private fun loadFields() {
        val fields = MyDatabaseHelper(fragment.context).getFieldSpecifierData(formId)
        for (field in fields) {
            when(field.field?.type) {
                "TEXTBOX" -> {
                    val textView = TextView(fragment.context)
                    textView.text = field.field?.fieldname
                    textView.x = field.xaxis!!
                    textView.y = field.yaxis!!
                    textView.setTextColor(R.color.black)
                    textView.textSize = 6.0f
                    textView.setTypeface(null, Typeface.BOLD)
                    fragment.findViewById<FrameLayout>(R.id.tvGenerator).addView(textView)
                }
                "CHECKBOX" -> {

                }
                "RADIOBUTTON" -> {

                }
            }
        }
    }
}

private const val CURRENT_PAGE_INDEX_KEY =
    "com.example.viewpagertest.state.CURRENT_PAGE_INDEX_KEY"

private const val TAG = "ActionOpenDocumentFragment"
private const val INITIAL_PAGE_INDEX = 0