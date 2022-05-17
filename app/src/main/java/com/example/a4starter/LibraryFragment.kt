package com.example.a4starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.ListView

// Acknowledgement: The GestureAdapter code is based closely on the "09.Android/8.ListView" sample code from the cs349 public repo

class LibraryFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null
    private var listView: ListView? = null
    private var adapter: GestureAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_library, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arrayOfGestures = mViewModel!!.gestures
        adapter = GestureAdapter(this.context, arrayOfGestures, mViewModel, this)
        listView = view.findViewById(R.id.listView)
        listView?.adapter = adapter
    }

    fun updateListView() {
        listView?.adapter = adapter
    }
}