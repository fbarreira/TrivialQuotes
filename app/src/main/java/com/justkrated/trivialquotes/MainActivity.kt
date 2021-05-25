package com.justkrated.trivialquotes

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var count = 0
    private var limit = 10
    private var listSize = 0
    private var blocksSize = 0
    private var blockCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        setSupportActionBar(toolbar)
        setupActivity()
        getQuote()
    }

    private fun setupActivity() {
        listSize = resources.getStringArray(R.array.quotes).size - 1
        blocksSize = resources.getStringArray(R.array.quotes_block).size - 1

        getButton.setOnClickListener { getQuote() }
    }

    private fun getQuote() {
        if (count <= limit) {
            count++
            val random: Int = Random.nextInt(0, listSize)
            val quote = resources.getStringArray(R.array.quotes)[random]
            quoteLabel.text = quote
        } else {
            showBlockStoryline()
        }
    }

    private fun showBlockStoryline() {
        if (blockCount > blocksSize) {
            finishAndRemoveTask()
        } else {
            if (blockCount < 2)
                vibrate()

            val quote = resources.getStringArray(R.array.quotes_block)[blockCount]
            quoteLabel.text = quote
            blockCount++
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //R.id.action_settings -> showSettings()
            R.id.action_about -> showAbout()
            R.id.action_share -> tryShare()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String): Boolean {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        return true
    }

    private fun tryShare(): Boolean {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val quote = quoteLabel.text.toString()
        intent.putExtra(Intent.EXTRA_TEXT, "$quote\n\n Got from Trivial Quotes")
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, getString(R.string.share_to)))

        return true
    }

    private fun showAbout(): Boolean {
        AboutFragment().show(supportFragmentManager, "about")
        return true
    }

    private fun showSettings(): Boolean{
        showToast("SETTINGS")
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun vibrate() {
        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(500)
            }
        }
    }

}