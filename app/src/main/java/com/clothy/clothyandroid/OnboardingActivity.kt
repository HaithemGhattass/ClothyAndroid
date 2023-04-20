package com.clothy.clothyandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.ui.tooling.preview.Preview
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.RiveArtboardRenderer
import app.rive.runtime.kotlin.core.Direction
import app.rive.runtime.kotlin.core.LinearAnimationInstance
import app.rive.runtime.kotlin.core.Loop
import app.rive.runtime.kotlin.core.PlayableInstance
import com.clothy.clothyandroid.onBoarding.LoginActivity
import com.clothy.clothyandroid.utils.RiveButton
import jp.wasabeef.blurry.Blurry

class OnboardingActivity : AppCompatActivity() {
    // val riveView: RiveAnimationView = findViewById(R.id.rive_button)

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_onboarding)
        val riveButton = findViewById<RiveButton>(R.id.rive_button)


        riveButton.setOnClickListener{
            riveButton.play("active")
            Handler().postDelayed({
                // yourMethod()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }, 1000)



        }
        }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val email = sharedPreferences?.getString("email", "")

        if(email != ""){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }


// Sync


}