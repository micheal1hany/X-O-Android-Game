package com.micheal.xogame

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.micheal.xogame.databinding.FragmentSetBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class SetFragment : Fragment() {

    private var _binding: FragmentSetBinding? = null
    private val binding get() = _binding!!

    private var langRadioIndex = 0

    private lateinit var btnClickSound : MediaPlayer

    private var soundState = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSetBinding.inflate(inflater, container, false)

        //load banner ad
        val adRequest = AdRequest.Builder().build()
        binding.SettingsAdView.loadAd(adRequest)

        // initial btn click sound mediaPlayer
        btnClickSound = MediaPlayer.create(activity,R.raw.btnclick)

        // set language radio button state
        if (getTheLocal() == "en") {
            langRadioIndex = 0
        } else {
            langRadioIndex = 1
        }

        if(getSoundState() == 1){
            binding.btnSound.text = getString(R.string.sound_btn)
        }else{
            binding.btnSound.text = getString(R.string.mute_btn)
        }

        // language button listener
        binding.btnLanguage.setOnClickListener {
            btnClickSound.start()
            langDialog()
        }

        // sound button listener
        binding.btnSound.setOnClickListener {
            btnClickSound.start()
            if(binding.btnSound.text.contentEquals(getString(R.string.sound_btn))){
                binding.btnSound.text = getString(R.string.mute_btn)
                (activity as MainActivity).stopBackgroundMusic()
            }else if(binding.btnSound.text.contentEquals(getString(R.string.mute_btn))){
                binding.btnSound.text = getString(R.string.sound_btn)
                (activity as MainActivity).startBackgroundMusic()
            }

            /**
             * check/set the background music state for shared preferences
             */
            setSoundState()
        }

        return binding.root
    }


    private fun langDialog() {
        val langRadioBtns =
            arrayOf("English", "عربي")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.popup_title_txt))
            .setSingleChoiceItems(langRadioBtns, langRadioIndex) { _, which ->
                if (which == 0) {
                    setLocal("en")


                } else {
                    setLocal("ar")

                }
            }
            .setPositiveButton(getString(R.string.done)) { _, _ ->
                //nothing to do
                activity?.finish()
                activity?.overridePendingTransition(R.anim.from_left,R.anim.to_right)
                activity?.startActivity(activity?.intent)

            }

            .setNeutralButton(getString(R.string.back)) { dialog, _ ->
                dialog.cancel()

            }

            .show()
    }

    /**
     * set the local instant with the radio button select
     */
    private fun setLocal(lang: String) {
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        resources.updateConfiguration(config, resources.displayMetrics)
        val sharedPref = activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("my_lang", lang)
            apply()
        }

    }

    /**
     * get the local state to present the right radio button
     */
    private fun getTheLocal(): String {
        val langSettings =
            activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE) ?: return ""
        val langIndex = langSettings.getString("my_lang", "en")

        return langIndex!!
    }


    /**
     * check/set the background state for shared preferences
     */
    private fun setSoundState(){
        if(binding.btnSound.text.contentEquals(getString(R.string.sound_btn))){
            soundState = 1
            val sharedPref = activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putInt("sound", soundState)
                apply()
            }
        }else if (binding.btnSound.text.contentEquals(getString(R.string.mute_btn))) {
            soundState = 2
            val sharedPref =
                activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putInt("sound", soundState)
                apply()
            }
        }
    }


    /**
     * get the sound state to make the button show the right text
     */
    private fun getSoundState(): Int {
        val soundSettings =
            activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE) ?: return 1

        return soundSettings.getInt("sound", 1)
    }

    override fun onPause() {
        binding.SettingsAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        binding.SettingsAdView.resume()
        super.onResume()
    }


    override fun onDestroyView() {
        binding.SettingsAdView.destroy()
        super.onDestroyView()
        _binding = null
    }


}