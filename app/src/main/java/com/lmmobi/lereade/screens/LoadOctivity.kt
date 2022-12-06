package com.lmmobi.lereade.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.lmmobi.lereade.MyWorker
import com.lmmobi.lereade.R
import com.lmmobi.lereade.RefViewModel

class LoadOctivity : AppCompatActivity() {
    private val refViewModel:RefViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.octivity_load)
        startWork()
    }

    private fun observeData(){
        val y = refViewModel.refSource.reference
        if (y == null){
            refViewModel.buildAndGetRef(this)
            Log.d("MyLog","DB empty")
            refViewModel.url.observe(this){
                launch(it)
            }
        }else{
            Log.d("MyLog","DB not empty")
            launch(y.url)
        }
    }

    private fun launch(reference:String){
        val intent = Intent(this, BrowserOctivity::class.java)
        intent.putExtra("EXTRA_REF",reference)
        startActivity(intent)
    }

    private fun startWork(){
        val task = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(task)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(task.id).observe(this){workInfos->
            val x = workInfos.outputData.getString("adb_status")
            if(x!=null){
                if (x.equals("adb is true")){
                    startActivity(Intent(this, IgraOctivity::class.java))
                }else if(x.equals("adb is false")){
                    observeData()
                }
            }
        }
    }
}