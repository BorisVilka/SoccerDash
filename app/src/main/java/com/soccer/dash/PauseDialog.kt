package com.soccer.dash

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.soccer.dash.databinding.EndDialogBinding
import com.soccer.dash.databinding.PauseDialogBinding

class PauseDialog(val v: ()->Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        var binding = PauseDialogBinding.inflate(layoutInflater,null,false)
        binding.textView6.setOnClickListener {
            dismiss()
            v()
        }
        binding.textView10.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.popBackStack()
            dismiss()
            controller.navigate(R.id.gameFragment)
        }
        binding.textView11.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.popBackStack()
            dismiss()
        }
        adb = adb.setView(binding.root)
        return adb.create().apply {
            window!!.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    override fun dismiss() {
        super.dismiss()
    }
}