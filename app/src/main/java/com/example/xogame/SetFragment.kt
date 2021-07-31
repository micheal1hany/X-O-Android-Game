package com.example.xogame

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xogame.databinding.FragmentSetBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class SetFragment : Fragment() {

    private var _binding: FragmentSetBinding? = null
    private val binding get() = _binding!!

    private var langRadioIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSetBinding.inflate(inflater, container, false)


        if (getTheLocal() == "en") {
            langRadioIndex = 0
        } else {
            langRadioIndex = 1
        }


        binding.btnLanguage.setOnClickListener {
            langDialog()
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
                activity?.recreate()

            }

            .setNeutralButton(getString(R.string.back)) { dialog, _ ->
                dialog.cancel()
            }

            .show()
    }

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

    private fun getTheLocal(): String {
        val langSettings =
            activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE) ?: return ""
        val langIndex = langSettings.getString("my_lang", "en")

        return langIndex!!
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}