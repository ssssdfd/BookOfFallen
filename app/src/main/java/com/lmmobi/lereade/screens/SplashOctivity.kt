package com.lmmobi.lereade.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

@SuppressLint("CustomSplashScreen")
class SplashOctivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LoadOctivity::class.java))
    }
}