package com.lmmobi.lereade

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val result = Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED) == "1"
        if (Settings.Secure.getInt(context.contentResolver, Settings.Secure.ADB_ENABLED, 0) == 1) {
            val adb_status = Data.Builder().putString("adb_status", "adb is true").build()
            return Result.failure(adb_status)
        } else {
            val adb_status = Data.Builder().putString("adb_status", "adb is false").build()
            return Result.failure(adb_status)
        }
    }
}