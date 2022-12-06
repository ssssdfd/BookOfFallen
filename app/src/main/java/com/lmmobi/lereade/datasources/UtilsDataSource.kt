package com.lmmobi.lereade.datasources

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.lmmobi.lereade.R
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UtilsDataSource {
    private val myScope = CoroutineScope(Dispatchers.IO)
    suspend fun returnAF(context: Context, app_id:String):MutableMap<String, Any>?= suspendCoroutine {
        Log.d("MyLog","start AppsFlyer")
        val key = "movdKJULdszfYkTuBsEk6A"
        AppsFlyerLib.getInstance().init(key, object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(result: MutableMap<String, Any>?) {
                Log.d("MyLog","onConversionDataSuccess")
                val mockAppsData: MutableMap<String, Any> = mutableMapOf()
                mockAppsData["af_status"] = "Non-organic"
                mockAppsData["media_source"] = "testSource"
                mockAppsData["campaign"] = "test1_test2_test3_test4_test5"
                mockAppsData["adset"] = "testAdset"
                mockAppsData["adset_id"] = "testAdsetId"
                mockAppsData["campaign_id"] = "testCampaignId"
                mockAppsData["orig_cost"] = "1.22"
                mockAppsData["af_site_id"] = "testSiteID"
                mockAppsData["adgroup"] = "testAdgroup"
                it.resume(mockAppsData)
            }
            override fun onConversionDataFail(result: String?) {
                Log.d("MyLog","onConversionDataFail")
                it.resume(null)
            }
            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {}
            override fun onAttributionFailure(data: String?) {}
        }, context)
        AppsFlyerLib.getInstance().start(context)
    }

    suspend fun returnDP(context: Context):String = suspendCoroutine {
        Log.d("MyLog","returnDP")
        AppLinkData.fetchDeferredAppLinkData(context){result->
            val deep = "myapp://test11/test22/test33/test44/test55"
           // it.resume(result?.targetUri.toString())
            it.resume(deep)
        }
    }

    fun sendReport(fb_data: String = "null", af_data: MutableMap<String, Any>? = null) {
        myScope.launch {
            when {
                af_data?.get("campaign").toString() == "null" && fb_data == "null" -> {
                    Log.d("MyLog","organic")
                    OneSignal.sendTag("key2", "organic")
                }
                fb_data != "null" -> {
                    Log.d("MyLog","deeplink install")
                    OneSignal.sendTag("key2", fb_data.replace("myapp://", "").substringBefore("/"))
                }
                af_data?.get("campaign").toString() != "null" -> {
                    Log.d("MyLog","apps install")
                    OneSignal.sendTag(
                        "key2", af_data?.get("campaign").toString().substringBefore("_")
                    )
                }
            }
        }

    }

    suspend fun getGoogleId(context: Context):String = suspendCoroutine {
        val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        it.resume(advertisingIdInfo.id.toString())
    }

    fun startOneS(context: Context){
        OneSignal.initWithContext(context)
        OneSignal.setAppId(context.resources.getString(R.string.odinZignal))
        myScope.launch {
            OneSignal.setExternalUserId(getGoogleId(context))
        }
    }

}