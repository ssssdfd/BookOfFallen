package com.lmmobi.lereade

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.lmmobi.lereade.datasources.RefSource
import com.lmmobi.lereade.datasources.UtilsDataSource
import com.lmmobi.lereade.green.Ref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.moznion.uribuildertiny.URIBuilderTiny
import java.net.URI
import java.util.*

class RefViewModel(application: Application) : AndroidViewModel(application) {
     val refSource = RefSource(application)
    private val utilsDataSource = UtilsDataSource()
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    fun buildAndGetRef(c: Context) {
        utilsDataSource.startOneS(c)
        viewModelScope.launch(Dispatchers.IO) {
            val id = utilsDataSource.getGoogleId(c)
            val dp = utilsDataSource.returnDP(c)
            val myUrl: String
            if (dp != "null") {
                myUrl = makeRef(c, dp, null, id)
                Log.d("MyLog", "URL DeepLink:$myUrl")
                utilsDataSource.sendReport(fb_data = dp)
            }else{
                val appF: MutableMap<String, Any>? = utilsDataSource.returnAF(c, "something")
                myUrl = makeRef(c, "null",appF,id)
                Log.d("MyLog", "URL AppsFlyer:$myUrl")
                utilsDataSource.sendReport(af_data = appF)
            }
            _url.postValue(myUrl)
        }
    }

    private fun resource(c: Context, id: Int): String {
        return c.resources.getString(id)
    }

    fun putToDb(ref: Ref){
        refSource.insertReference(ref)
    }

    private fun makeRef(c: Context, dataDP: String, dataAP: MutableMap<String, Any>?, googleId: String): String {
        val base = "https://cookiesorwolf.fun/fallen.php"
        val urlTiny = URIBuilderTiny(URI(base)).apply {
            addQueryParameter(resource(c, R.string.zecureHetPoramitr), resource(c, R.string.zecureKay))
            addQueryParameter(resource(c, R.string.diw_tmsKoy), TimeZone.getDefault().id)
            addQueryParameter(resource(c, R.string.Go_d_i_d_key), googleId)
            addQueryParameter(resource(c, R.string.d_i_p_LinkKey), dataDP)
            addQueryParameter(resource(c, R.string.zou_rce_keu), if (dataDP != "null") "deeplink" else dataAP?.get("media_source").toString())
            if (dataDP != "null") { addQueryParameter(resource(c, R.string.a_dZet_Id_keeey), "null") }
            else { addQueryParameter(resource(c, R.string.A_f_i_d_k), AppsFlyerLib.getInstance().getAppsFlyerUID(c)!!) }
            addQueryParameter(resource(c, R.string.a_dZet_Id_keeey), dataAP?.get("adset_id").toString())
            addQueryParameter(resource(c, R.string.CAM_p_a_i_g_n_id_key), dataAP?.get("campaign_id").toString())
            addQueryParameter(resource(c, R.string.Opp_coom_pai_gn_keeeey), dataAP?.get("campaign").toString())
            addQueryParameter(resource(c, R.string.adzeeet_keeyyy), dataAP?.get("adset").toString())
            addQueryParameter(resource(c, R.string.ADDgoop_key), dataAP?.get("adgroup").toString())
            addQueryParameter(resource(c, R.string.original_coost_kkkkLey), dataAP?.get("orig_cost").toString())
            addQueryParameter(resource(c, R.string.AFSitiedkeyiu), dataAP?.get("af_siteid").toString())
        }.build()
        return urlTiny.toString()
    }
}