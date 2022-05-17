package com.example.a4starter

import android.app.AlertDialog
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.text.InputType
import android.widget.EditText
import androidx.core.view.drawToBitmap

class AdditionFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        mViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val root: View = inflater.inflate(R.layout.fragment_addition, container, false)

        val canvasView = root.findViewById<CanvasView>(R.id.canvasView)
        val addButton = root.findViewById<Button>(R.id.addButton)
        val clearButton = root.findViewById<Button>(R.id.clearButton)

        addButton.setOnClickListener {
            if (canvasView.path.isEmpty) {
                AlertDialog.Builder(context).setTitle("Empty gesture cannot be added.").setCancelable(false)
                    .setPositiveButton("OK") { _, _ -> }.show()
            } else {
                var gestureName: String
                val input = EditText(context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                AlertDialog.Builder(context).setTitle("Gesture Name").setView(input)
                    .setPositiveButton("OK") { _, _ ->
                        gestureName = input.text.toString()
                        if (gestureName == "") {
                            AlertDialog.Builder(context).setTitle("Gesture without a name cannot be added.").setCancelable(false)
                                .setPositiveButton("OK") { _, _ -> }.show()
                        } else {
                            mViewModel!!.addStroke(canvasView.drawToBitmap(), gestureName, canvasView.path)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }.show()
            }
        }

        clearButton.setOnClickListener {
            canvasView.path = Path()
            canvasView.invalidate()
        }

        return root
    }
}
