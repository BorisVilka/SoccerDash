package com.soccer.dash

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.soccer.dash.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private var music = true
    private var vibr = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        binding.imageButton2.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.popBackStack()
        }
        vibr = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("vibr",true)
        music = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("music",true)
        binding.textView1.text = if(music) "ON" else "OFF"
        binding.textView111.text = if(vibr) "ON" else "OFF"
          binding.imageView3.setOnClickListener {
            music = true
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("music",true).apply()
            binding.textView1.text = if(music) "ON" else "OFF"
        }
        binding.imageView2.setOnClickListener {
            music = false
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("music",false).apply()
            binding.textView1.text = if(music) "ON" else "OFF"
        }
        binding.imageView31.setOnClickListener {
            vibr = true
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("vibr",true).apply()
            binding.textView111.text = if(vibr) "ON" else "OFF"
        }
        binding.imageView211.setOnClickListener {
            vibr = false
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("vibr",false).apply()
            binding.textView111.text = if(vibr) "ON" else "OFF"
        }
        return binding.root
    }

}