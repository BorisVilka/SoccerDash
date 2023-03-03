package com.soccer.dash

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.soccer.dash.databinding.EndDialogBinding

class EndDialog(val score: Int): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        var binding = EndDialogBinding.inflate(layoutInflater,null,false)
        binding.button.setOnClickListener {
            dismiss()
        }
        binding.card3.setOnClickListener {
            dismiss()
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.navigate(R.id.gameFragment)
        }
        binding.textView8.text = "${score}"
        var list = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getStringSet("score",HashSet<String>())!!.map { it.toInt() }.toMutableList()
        if(list.isEmpty()) list.add(0)
        binding.textView9.text =  list.max().toString()
        adb = adb.setView(binding.root)
        return adb.create().apply {
            window!!.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    override fun dismiss() {
        super.dismiss()
        val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
        controller.popBackStack()
    }
}