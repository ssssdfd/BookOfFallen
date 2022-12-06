package com.lmmobi.lereade.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.lmmobi.lereade.RefViewModel
import com.lmmobi.lereade.databinding.OctivityBrowserBinding
import com.lmmobi.lereade.green.Ref

class BrowserOctivity : AppCompatActivity() {
    private lateinit var binding: OctivityBrowserBinding
    private lateinit var browserView:WebView
    private lateinit var dsds: ValueCallback<Array<Uri?>>
    val sdsd = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){dsds.onReceiveValue(it.toTypedArray())}
    private val refViewModel:RefViewModel by viewModels()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OctivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        browserView = binding.browserView
        browserView.loadUrl(intent.getStringExtra("EXTRA_REF")!!)

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(browserView, true)

        browserView.settings.userAgentString  = WebView(this).settings.userAgentString.replace("wv","")
        browserView.settings.loadWithOverviewMode = false
        browserView.settings.domStorageEnabled = true
        browserView.settings.javaScriptEnabled = true




        browserView.webChromeClient = object: WebChromeClient(){
            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                val window = WebView(this@BrowserOctivity)
                window.webChromeClient  = this
                window.settings.javaScriptEnabled  =true
                window.settings.domStorageEnabled = true
                window.settings.javaScriptCanOpenWindowsAutomatically = true
                window.settings.setSupportMultipleWindows(true)


                val tran = resultMsg.obj as WebView.WebViewTransport
                tran.webView = window
                resultMsg.sendToTarget()
                return true
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>>, fileChooserParams: FileChooserParams?): Boolean {
                dsds = filePathCallback
                val mime = "image/*"
                sdsd.launch(mime)
                return true
            }
        }

        browserView.webViewClient = object: WebViewClient(){
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()
                if (url=="https://cookiesorwolf.fun/"){
                    startActivity(Intent(this@BrowserOctivity, IgraOctivity::class.java))
                }else{
                    val x = refViewModel.refSource.reference
                    if (x==null && !url!!.contains("https://cookiesorwolf.fun/fallen.php")){
                        Log.d("MyLog", "put to db")
                        refViewModel.putToDb(Ref(1L,url))
                    }
                }
            }
        }

        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (browserView.canGoBack())
                    browserView.goBack()
            }
        })
    }
}