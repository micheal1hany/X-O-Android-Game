package com.micheal.xogame

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.micheal.xogame.databinding.FragmentHomeBinding

var twoPlayerBtnClicked = false
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnClickSound : MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        val adRequest = AdRequest.Builder().build()
        binding.homeAdView.loadAd(adRequest)

        //initialize buttons click sound
        btnClickSound = MediaPlayer.create(activity,R.raw.btnclick)


        binding.btnOnePlayer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
            btnClickSound.start()
        }

        binding.btnTwoPlayer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
            twoPlayerBtnClicked = true
            btnClickSound.start()
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_setFragment)
            btnClickSound.start()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFragment = this
    }

    override fun onPause() {
        binding.homeAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        binding.homeAdView.resume()
        super.onResume()
    }

    override fun onDestroyView() {
        binding.homeAdView.destroy()
        super.onDestroyView()
        _binding = null
    }
}