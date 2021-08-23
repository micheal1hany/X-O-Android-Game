package com.micheal.xogame


import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.micheal.xogame.databinding.FragmentGameBinding
import java.util.*
import kotlin.collections.ArrayList

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var mInterstitialAd: InterstitialAd? = null

    private var player1 = ArrayList<Int>()
    private var player2 = ArrayList<Int>()
    private var activePlayer = 1
    private val happyUnicode = 0x1F973
    private val ohohUnicode = 0x1F62E
    private val dialogEmoji = String(Character.toChars(happyUnicode))
    private val tieDialogEmoji = String(Character.toChars(ohohUnicode))
    private var gameCounter = 0
    private var winner = -1

    private val timer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.timerTxt.text = (getString(R.string.timer_txt, millisUntilFinished / 1000))

        }

        override fun onFinish() {
            binding.timerTxt.setTextColor(Color.RED)
            timeOutDialog()
            cancel()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater,container,false)

        //load banner add
        val adRequest = AdRequest.Builder().build()
        binding.gameAdView.loadAd(adRequest)

        //load interstitial ad
        loadInterstitialAd()

        binding.turnTxt.text = getString(R.string.player_turn, "X")
        timer.start()

            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameFragment = this

    }

    fun btnSelect(view: View) {
        val btnChoice = view as Button
        var cellID = 0
        when (btnChoice.id) {
            R.id.btn_1 -> cellID = 1
            R.id.btn_2 -> cellID = 2
            R.id.btn_3 -> cellID = 3
            R.id.btn_4 -> cellID = 4
            R.id.btn_5 -> cellID = 5
            R.id.btn_6 -> cellID = 6
            R.id.btn_7 -> cellID = 7
            R.id.btn_8 -> cellID = 8
            R.id.btn_9 -> cellID = 9
        }
        playerGame(cellID, btnChoice)
    }

    private fun playerGame(cellID: Int, btnChoice: Button) {
        if (activePlayer == 1) {
            btnChoice.setBackgroundResource(R.drawable.xicon)
            player1.add(cellID)
            binding.turnTxt.text = getString(R.string.player_turn, "O")
            activePlayer = 2
            if (!twoPlayerBtnClicked) {
                Handler(Looper.getMainLooper()).postDelayed({
                    autoPlay()
                }, 500)
            }
        } else {
            btnChoice.setBackgroundResource(R.drawable.oicon)
            player2.add(cellID)
            binding.turnTxt.text = getString(R.string.player_turn, "X")
            activePlayer = 1
        }
        btnChoice.isEnabled = false
        checkWinner()
    }

    private fun checkWinner() {


        //row1
        if (winner == -1) {
            if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
                winner = 1
            }
            if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
                winner = 2
            }

            //row2
            if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
                winner = 1
            }
            if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
                winner = 2
            }

            //row3
            if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
                winner = 1
            }
            if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
                winner = 2
            }

            //col1
            if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
                winner = 1
            }
            if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
                winner = 2
            }

            //col2
            if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
                winner = 1
            }
            if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
                winner = 2
            }

            //col3
            if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
                winner = 1
            }
            if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
                winner = 2
            }

            //diagLR
            if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
                winner = 1
            }
            if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
                winner = 2
            }

            //diagRL
            if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
                winner = 1
            }
            if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
                winner = 2
            }
            gameCounter++
        }

        if (gameCounter == 9 && winner == -1) {
            winner = 3
        }

        if (winner != -1) {
            when (winner) {
                1 -> {
                    winnerDialog(1)
                    timer.cancel()
                }
                2 -> {
                    winnerDialog(2)
                    timer.cancel()
                }
                else -> {
                    tieDialog()
                    timer.cancel()
                }
            }
        }

    }

    private fun winnerDialog(winnerNum: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title, dialogEmoji))
            .setMessage(getString(R.string.dialog_message, winnerNum))
            .setCancelable(false)
            .setNegativeButton(R.string.back) { _, _ ->
                findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
                twoPlayerBtnClicked = false
                showInterstitialAd()
            }
            .setPositiveButton(R.string.play_again) { _, _ ->
                restartGame()
            }
            .show()

        binding.turnTxt.visibility = View.GONE
    }

    private fun tieDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.tie_dialog_title, tieDialogEmoji))
            .setMessage(R.string.tie_dialog_message)
            .setCancelable(false)
            .setNegativeButton(R.string.back) { _, _ ->
                findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
                twoPlayerBtnClicked = false
                showInterstitialAd()
            }
            .setPositiveButton(R.string.play_again) { _, _ ->
                restartGame()
            }
            .show()

        binding.turnTxt.visibility = View.GONE
    }

    private fun restartGame() {
        findNavController().navigate(R.id.action_gameFragment_self)

    }

    private fun timeOutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.time_out_title)
            .setMessage(R.string.time_out_message)
            .setCancelable(false)
            .setNegativeButton(R.string.back) { _, _ ->
                findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
                twoPlayerBtnClicked = false
                showInterstitialAd()
            }
            .setPositiveButton(R.string.play_again) { _, _ ->
                restartGame()
            }
            .show()

        binding.turnTxt.visibility = View.GONE

    }

    private fun autoPlay() {

        //scan empty cells
        val emptyCells = ArrayList<Int>()
        for (cellID in 1..9) {
            if (!(player1.contains(cellID) || player2.contains(cellID))) {
                emptyCells.add(cellID)
            }
        }

        //select random cells
        if (emptyCells.size != 0) {
            val random = Random().nextInt((emptyCells.size))
            val compCellID = emptyCells[random]

            //assign the cells
            val btnSelect: Button
            when (compCellID) {
                1 -> btnSelect = binding.btn1
                2 -> btnSelect = binding.btn2
                3 -> btnSelect = binding.btn3
                4 -> btnSelect = binding.btn4
                5 -> btnSelect = binding.btn5
                6 -> btnSelect = binding.btn6
                7 -> btnSelect = binding.btn7
                8 -> btnSelect = binding.btn8
                9 -> btnSelect = binding.btn9
                else -> {
                    btnSelect = binding.btn1
                }
            }

            if (winner == -1 && gameCounter < 10) {
                playerGame(compCellID, btnSelect)
            }
        }

    }

    override fun onStop() {
        timer.cancel()
        super.onStop()
    }

    private fun loadInterstitialAd(){
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this.requireContext(),
            "ca-app-pub-7003901998192619/2081569671",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(addError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Toast.makeText(activity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun showInterstitialAd(){
        if(mInterstitialAd != null){
            mInterstitialAd?.show(requireActivity())
        }
    }

    override fun onPause() {
        binding.gameAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        binding.gameAdView.resume()
        super.onResume()
    }

    override fun onDestroyView() {
        binding.gameAdView.destroy()
        super.onDestroyView()
        _binding = null
    }
}