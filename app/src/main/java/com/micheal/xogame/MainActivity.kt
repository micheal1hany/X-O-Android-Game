package com.micheal.xogame

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.ads.MobileAds
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    private var backGroundMusic = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize ads
        MobileAds.initialize(this@MainActivity)

        // load the settings saved within shared preferences file
        loadSettings()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        twoPlayerBtnClicked = false
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun loadSettings(){
        val sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE)
        val langPrefs = sharedPref.getString("my_lang","en")
        val soundPref = sharedPref.getInt("sound",1)
        setSettings(langPrefs!!,soundPref)
    }

    private fun setSettings(lang: String, sound: Int){

        //set local
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        resources.updateConfiguration(config,resources.displayMetrics)

        //set sound state
        if(sound == 1){
            //initialize the background music media player
            backGroundMusic = MediaPlayer.create(this, R.raw.backgroundmusic)
            backGroundMusic.isLooping = true
            backGroundMusic.start()
        }

        //write my settings on shared preferences
        val sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE)?:return
        with(sharedPref.edit()){
            putString("my_lang",lang)
            putInt("sound",sound)
            apply()
        }

    }

    fun stopBackgroundMusic(){
        backGroundMusic.stop()
        backGroundMusic.release()
    }
    fun startBackgroundMusic(){
        backGroundMusic = MediaPlayer.create(this, R.raw.backgroundmusic)
        backGroundMusic.isLooping = true
        backGroundMusic.start()

    }


    override fun onPause() {
        backGroundMusic.pause()
        super.onPause()

    }

    override fun onResume() {
        backGroundMusic.start()
        super.onResume()
    }
}