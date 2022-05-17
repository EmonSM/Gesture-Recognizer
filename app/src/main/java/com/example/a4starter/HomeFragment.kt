package com.example.a4starter

import android.graphics.Path
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.lang.Float.MAX_VALUE
import kotlin.math.hypot
import kotlin.math.sqrt

class HomeFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    private fun calculateMatchingScore(strokePath: ArrayList<Pair<Float, Float>>, gesturePath: ArrayList<Pair<Float, Float>>): Float {
        var total = 0f
        for(i in 0..126) {
            total += sqrt(hypot(strokePath[i].first-gesturePath[i].first,
                strokePath[i].second-gesturePath[i].second))
        }
        return total/127
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        mViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val canvasView = root.findViewById<CanvasView>(R.id.canvasView)
        val recognizeButton = root.findViewById<Button>(R.id.recognizeButton)
        val clearButton = root.findViewById<Button>(R.id.clearButton)
        val firstGestureThumbnail = root.findViewById<ImageView>(R.id.firstGestureThumbnail)
        val firstGestureName = root.findViewById<TextView>(R.id.firstGestureName)
        val secondGestureThumbnail = root.findViewById<ImageView>(R.id.secondGestureThumbnail)
        val secondGestureName = root.findViewById<TextView>(R.id.secondGestureName)
        val thirdGestureThumbnail = root.findViewById<ImageView>(R.id.thirdGestureThumbnail)
        val thirdGestureName = root.findViewById<TextView>(R.id.thirdGestureName)

        recognizeButton.setOnClickListener {
            var standardizedPath = mViewModel!!.resamplePath(canvasView.path)
            standardizedPath = mViewModel!!.rotatePath(standardizedPath)
            standardizedPath = mViewModel!!.scaleAndTranslatePath(standardizedPath)

            var firstMatch: Gesture? = null
            var firstScore: Float = MAX_VALUE
            var secondMatch: Gesture? = null
            var secondScore: Float = MAX_VALUE
            var thirdMatch: Gesture? = null
            var thirdScore: Float = MAX_VALUE
            for (gesture in mViewModel!!.gestures) {
                val score = calculateMatchingScore(standardizedPath, gesture.gesturePath)
                if (score < firstScore) {
                    thirdMatch = secondMatch
                    thirdScore = secondScore
                    secondMatch = firstMatch
                    secondScore = firstScore
                    firstMatch = gesture
                    firstScore = score
                } else if (score < secondScore) {
                    thirdMatch = secondMatch
                    thirdScore = secondScore
                    secondMatch = gesture
                    secondScore = score
                } else if (score < thirdScore) {
                    thirdMatch = gesture
                    thirdScore = score
                }
            }

            Log.d("HomeFragment", "Matches in order: ${firstMatch?.gestureName}, ${secondMatch?.gestureName}, ${thirdMatch?.gestureName}")
            firstGestureThumbnail.setImageBitmap(firstMatch?.gestureThumbnail)
            firstGestureName.text = firstMatch?.gestureName
            firstGestureName.typeface = Typeface.DEFAULT_BOLD
            secondGestureThumbnail.setImageBitmap(secondMatch?.gestureThumbnail)
            secondGestureName.text = secondMatch?.gestureName
            thirdGestureThumbnail.setImageBitmap(thirdMatch?.gestureThumbnail)
            thirdGestureName.text = thirdMatch?.gestureName
        }

        clearButton.setOnClickListener {
            canvasView.path = Path()
            firstGestureThumbnail.setImageBitmap(null)
            firstGestureName.text = null
            secondGestureThumbnail.setImageBitmap(null)
            secondGestureName.text = null
            thirdGestureThumbnail.setImageBitmap(null)
            thirdGestureName.text = null
            canvasView.invalidate()
        }

        return root
    }
}