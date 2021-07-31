package com.example.xogame

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadLocal()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        twoPlayerBtnClicked = false
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun loadLocal(){
        val sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE)
        val langPrefs = sharedPref.getString("my_lang","en")
        setLocal(langPrefs!!)
    }

    private fun setLocal(lang: String){
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        resources.updateConfiguration(config,resources.displayMetrics)
        val sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE)?:return
        with(sharedPref.edit()){
            putString("my_lang",lang)
            apply()
        }

    }


}