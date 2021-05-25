package com.justkrated.trivialquotes

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AboutFragment() : DialogFragment() {

    private lateinit var buttonRate: Button
    private lateinit var buttonFeedback: Button
    private lateinit var buttonClose: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.fragment_about, null)

        buttonRate = view?.findViewById(R.id.rateButton) as Button
        buttonRate.setOnClickListener { rate() }

        buttonFeedback = view.findViewById(R.id.feedbackButton) as Button
        buttonFeedback.setOnClickListener { feedback() }

        buttonClose = view.findViewById(R.id.closeButton) as Button
        buttonClose.setOnClickListener { close() }

        return AlertDialog.Builder(activity as Activity)
            .setView(view)
            .create()
    }

    private fun rate() {
        val uri = Uri.parse("market://details?id=com.getkrated.trivialquotes")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                getString(R.string.warning_rate_not_found),
                Toast.LENGTH_LONG
            ).show()
        }
        close()
    }

    private fun feedback() {

        val subject = resources.getString(R.string.app_name) + " - Feedback"
        val body = resources.getString(R.string.feedback_body)
        val title = resources.getString(R.string.feedback_title)

        val i = Intent(Intent.ACTION_SENDTO)
        i.type = "message/rfc822"
        i.data = Uri.parse("mailto:")
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@justkrated.com"))
        i.putExtra(Intent.EXTRA_SUBJECT, subject)
        i.putExtra(Intent.EXTRA_TEXT, body)
        try {
            startActivity(Intent.createChooser(i, title))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                getString(R.string.warning_email_not_found),
                Toast.LENGTH_LONG
            ).show()
        }
        close()
    }

    private fun close() {
        dismiss()
    }


}