package com.comfortablesporttools.yourfitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ProcessLifecycleOwner
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds

class MainTitleActivity : AppCompatActivity() {

    private var appOpenAd: AppOpenAd? = null
    private var firstShowAd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_title)

        // Configure the user privacy data policy before init sdk
        MobileAds.initialize(this) {
            // now you can use ads
            loadAppOpenAd()
            val processLifecycleObserver = DefaultProcessLifecycleObserver(
                onProcessCameForeground = ::showAppOpenAd
            )
            ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
        }

        val btnStart = findViewById<Button>(R.id.nextButton1)
        btnStart.setOnClickListener {
            startActivity(Intent(this@MainTitleActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun loadAppOpenAd() {
        val appOpenAdLoader: AppOpenAdLoader = AppOpenAdLoader(this)
        val appOpenAdLoadListener = object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                // The ad was loaded successfully. Now you can show loaded ad.
                this@MainTitleActivity.appOpenAd = appOpenAd
                if (!firstShowAd) {
                    showAppOpenAd()
                    firstShowAd = true
                }
            }

            override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                // Ad failed to load with AdRequestError.
                // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
            }
        }
        appOpenAdLoader.setAdLoadListener(appOpenAdLoadListener)
        // R-M-5273887-1 // моя реклама
        val AD_UNIT_ID = "R-M-5273887-1" // для отладки можно использовать "demo-appopenad-yandex"
        val adRequestConfiguration = AdRequestConfiguration.Builder(AD_UNIT_ID).build()
        appOpenAdLoader.loadAd(adRequestConfiguration)
    }

    private fun showAppOpenAd() {
        val appOpenAdEventListener = AdEventListener()
        appOpenAd?.setAdEventListener(appOpenAdEventListener)
        appOpenAd?.show(this)
    }

    private inner class AdEventListener : AppOpenAdEventListener {
        override fun onAdShown() {
            // Called when ad is shown.
            Log.e("yandex_ads", "Called when ad is shown.")
        }

        override fun onAdFailedToShow(adError: AdError) {
            // Called when ad failed to show.
            clearAppOpenAd()
            loadAppOpenAd()
        }

        override fun onAdDismissed() {
            // Called when ad is dismissed.
            // Clean resources after dismiss and preload new ad.
            clearAppOpenAd()
            loadAppOpenAd()
        }

        override fun onAdClicked() {
            // Called when a click is recorded for an ad.
        }

        override fun onAdImpression(impressionData: ImpressionData?) {
            // Called when an impression is recorded for an ad.
            // Get Impression Level Revenue Data in argument.
        }
    }

    private fun clearAppOpenAd() {
        appOpenAd?.setAdEventListener(null)
        appOpenAd = null
    }

}