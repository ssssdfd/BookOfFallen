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
        val key = "movdKJULdszfYkTuBsEk6A"
        AppsFlyerLib.getInstance().init(key, object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(result: MutableMap<String, Any>?) {
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
                it.resume(result)
            }
            override fun onConversionDataFail(result: String?) {
                it.resume(null)
            }
            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {}
            override fun onAttributionFailure(data: String?) {}
        }, context)
        AppsFlyerLib.getInstance().start(context)
    }

    suspend fun returnDP(context: Context):String = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(context){result->
            val deep = "myapp://test1/test2/test3/test4/test5"
              it.resume(result?.targetUri.toString())
            //it.resume(deep)
        }
    }

    fun sendReport(fb_data: String = "null", af_data: MutableMap<String, Any>? = null) {
        myScope.launch {
            when {
                af_data?.get("campaign").toString() == "null" && fb_data == "null" -> {
                    OneSignal.sendTag("key2", "organic")
                }
                fb_data != "null" -> {
                    OneSignal.sendTag("key2", fb_data.replace("myapp://", "").substringBefore("/"))
                }
                af_data?.get("campaign").toString() != "null" -> {
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