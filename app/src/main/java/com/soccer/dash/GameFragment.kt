package com.soccer.dash

import android.content.Context
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.soccer.dash.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater,container,false)
        var vibr = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("vibr",true)
        binding.imageView9.setOnClickListener {
         binding.game.togglePause()
            var pause = PauseDialog {
                binding.game.togglePause()
            }
            pause.show(requireActivity().supportFragmentManager,"TAG")
        }
        binding.game.setEndListener(object : GameView.Companion.EndListener {
            override fun end() {
                var set = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getStringSet("score",HashSet<String>())!!
                var set1 = HashSet<String>()
                set1.addAll(set)
                if(!set1.contains(binding.game.score.toString()))set1.add(binding.game.score.toString())
                requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putStringSet("score",set1).apply()
                var dialog = EndDialog(binding.game.score)
                dialog.show(requireActivity().supportFragmentManager,"END")
            }

            override fun score(score: Int) {
                if(vibr) {
                    if(Build.VERSION.SDK_INT>=31) {
                        (requireActivity().getSystemService(AppCompatActivity.VIBRATOR_MANAGER_SERVICE) as VibratorManager).vibrate(
                            CombinedVibration.createParallel(
                                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)))
                    } else if(Build.VERSION.SDK_INT>=26){
                        (requireActivity().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator).vibrate(
                            VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                }
            }

        })
        return binding.root
    }


}