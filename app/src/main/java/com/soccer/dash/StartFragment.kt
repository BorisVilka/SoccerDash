package com.soccer.dash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.soccer.dash.databinding.FragmentStartBinding


class StartFragment : Fragment() {


    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater,container,false)
        binding.cardView.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.navigate(R.id.scoreFragment)
        }
        binding.cardView2.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.navigate(R.id.settingsFragment)
        }
        binding.card3.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.navigate(R.id.gameFragment)
        }
        return binding.root
    }


}