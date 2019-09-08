package com.elevin.printpdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.print.PrintAttributes
import android.content.Context
import android.print.PrintDocumentAdapter
import android.content.Context.PRINT_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.print.PrintManager
import android.util.AttributeSet
import android.view.View
import com.webviewtopdf.PdfView
import android.app.ProgressDialog
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.getExternalStoragePublicDirectory
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import java.io.File
import android.widget.Toast
import android.os.StrictMode

class MainActivity : AppCompatActivity() {


    // basic function to convert web view to pdf
    fun printWebviewAsPdf(){
        val printManager = this
            .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter("koko.pdf")

        val jobName = getString(R.string.app_name) + " Print Test"

        printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )

    }

    // basic function to open email composer
    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_VIEW)
        val data = Uri.parse("mailto:?subject=test&body=body")
        intent.data = data
        startActivity(intent)
    }


    // send email with attachment
    private fun sendEmailAttachment(attachment: File){

        val uri = Uri.fromFile(attachment)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Repair Quote")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "Send email..."))

    }


    fun convertWebviewToPdf() {

        // set directory
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/DisappearDents/");

        // set converted file name
        val fileName = "Quote.pdf"

        // we don't need this, but just to make app look better
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()


        // main 3rd library that will do all the converting hard work
        PdfView.createWebPrintJob(
            this@MainActivity,
            webView,
            directory,
            fileName,
            object : PdfView.Callback {

                override fun success(path: String) {
                    progressDialog.dismiss()

                    // this is the converted file
                    var file = File("$directory/$fileName")

                    // send it as an attachment
                    sendEmailAttachment(file)

                }

                override fun failure() {
                    progressDialog.dismiss()

                    // do some error handling here

                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // we need this cause of some errors with exposing the URI that we will use
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // button trigger
        button.setOnClickListener {

            // this our function
            convertWebviewToPdf()
        }

        // this will be the webview content
        val customHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"content-type\">\n" +
                "<title>A simple, clean, and responsive HTML invoice template</title>\n" +
                "<style>\n" +
                ".invoice-box{\n" +
                "    max-width:800px;\n" +
                "    margin:auto;\n" +
                "    padding:30px;\n" +
                "    /*border:1px solid #eee;*/\n" +
                "    /*box-shadow:0 0 10px rgba(0, 0, 0, .15); */\n" +
                "    font-size:16px;\n" +
                "    line-height:24px;\n" +
                "    font-family:'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" +
                "    color:#555;\n" +
                "}\n" +
                "\n" +
                ".invoice-box table{\n" +
                "    width:100%;\n" +
                "    line-height:inherit;\n" +
                "    text-align:left;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table td{\n" +
                "        padding:5px;\n" +
                "        vertical-align:top;\n" +
                "        }\n" +
                "        \n" +
                "        .invoice-box table tr td:nth-child(2){\n" +
                "            text-align:right;\n" +
                "}\n" +
                "\n" +
                ".invoice-box table tr.top table td{\n" +
                "    padding-bottom:20px;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.top table td.title{\n" +
                "        font-size:45px;\n" +
                "        line-height:45px;\n" +
                "        color:#333;\n" +
                "        }\n" +
                "        \n" +
                "        .invoice-box table tr.information table td{\n" +
                "            padding-bottom:40px;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.heading td{\n" +
                "                background:#eee;\n" +
                "                border-bottom:1px solid #ddd;\n" +
                "                font-weight:bold;\n" +
                "                }\n" +
                "                \n" +
                "                .invoice-box table tr.details td{\n" +
                "                    padding-bottom:5px;\n" +
                "                    border-bottom:1px solid #eee;\n" +
                "                    }\n" +
                "                    \n" +
                "                    .invoice-box table tr.item td{\n" +
                "                        border-bottom:1px solid #eee;\n" +
                "                        }\n" +
                "                        \n" +
                "                        .invoice-box table tr.item.last td{\n" +
                "                            border-bottom:none;\n" +
                "                            }\n" +
                "                            \n" +
                "                            .invoice-box table tr.total td:nth-child(2){\n" +
                "                                border-top:2px solid #eee;\n" +
                "                                font-weight:bold;\n" +
                "}\n" +
                "\n" +
                "@media only screen and (max-width: 600px) {\n" +
                "    .invoice-box table tr.top table td{\n" +
                "        width:100%;\n" +
                "        display:block;\n" +
                "        text-align:center;\n" +
                "        }\n" +
                "        \n" +
                "        .invoice-box table tr.information table td{\n" +
                "            width:100%;\n" +
                "            display:block;\n" +
                "            text-align:center;\n" +
                "    }\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"invoice-box\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "<tbody>\n" +
                "<tr class=\"top\">\n" +
                "<td colspan=\"2\">\n" +
                "<table>\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<h1> Estimate </h1>\n" +
                "<td class=\"title\"> <img src=\"#LOGO_IMAGE#\" style=\"width:100%; max-width:300px;>\n" +
                "</td>\n" +
                "<td> <h1>Quote #: #INVOICE_NUMBER#</h1><br>\n" +
                "<h2>#INVOICE_DATE#</h2><br>\n" +
                "#DUE_DATE# </td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"information\">\n" +
                "<td colspan=\"2\">\n" +
                "<table>\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td> #SENDER_INFO# </td>\n" +
                "<td> #RECIPIENT_INFO# </td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"heading\">\n" +
                "<td> Car Info </td>\n" +
                "<td> <br>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"details\">\n" +
                "<td> #VIN NUMBER# </td>\n" +
                "\n" +
                "<td>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"details\">\n" +
                "<td> #MAKE# </td>\n" +
                "\n" +
                "<td>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"details\">\n" +
                "<td> #MODEL# </td>\n" +
                "\n" +
                "<td>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"details\">\n" +
                "<td> #TYPE# </td>\n" +
                "\n" +
                "<td>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr class=\"details\">\n" +
                "<td> #YEAR# </td>\n" +
                "\n" +
                "<td>\n" +
                "</td>\n" +
                "</tr>\n" +
                "\n" +
                "<tr class=\"heading\">\n" +
                "<td> Item </td>\n" +
                "<td> Price </td>\n" +
                "</tr>\n" +
                "#ITEMS#\n" +
                "<tr class=\"total\">\n" +
                "<td><br>\n" +
                "</td>\n" +
                "<td> Total: #TOTAL_AMOUNT# </td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>"

        // set our webview content here
        webView.loadData(customHtml, "text/html", "UTF-8")



    }


}
