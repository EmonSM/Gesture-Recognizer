package com.example.a4starter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView

// Acknowledgement: This GestureAdapter code is based  closely on the "09.Android/8.ListView" sample code from the cs349 public repo

class GestureAdapter(context: Context?, gestures: ArrayList<Gesture>?, private val model: SharedViewModel?, private val libraryFragment: LibraryFragment) :
    ArrayAdapter<Gesture?>(context!!, 0, gestures!! as List<Gesture?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var view: View? = convertView
        val gesture = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gesture_item, parent, false)
        }

        // Lookup view for data population
        val gestureThumbnail = view?.findViewById<ImageView>(R.id.gestureThumbnail)
        val gestureName = view?.findViewById<TextView>(R.id.gestureName)
        val deleteButton = view?.findViewById<Button>(R.id.deleteButton)

        deleteButton?.setOnClickListener {
            if (gesture != null) {
                model?.removeStroke(gesture)
                libraryFragment.updateListView()
            }
        }

        // Populate the data into the template view using the data object
        gestureThumbnail?.setImageBitmap(gesture?.gestureThumbnail)
        gestureName?.text = gesture?.gestureName

        // Return the completed view to render on screen
        return view!!
    }
}