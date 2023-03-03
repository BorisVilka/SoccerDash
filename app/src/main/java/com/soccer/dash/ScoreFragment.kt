package com.soccer.dash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.soccer.dash.databinding.FragmentScoreBinding


class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScoreBinding.inflate(layoutInflater,container,false)
        binding.list.adapter = MyAdapter(requireContext())
        binding.imageButton.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.popBackStack()
        }

        return binding.root
    }


}