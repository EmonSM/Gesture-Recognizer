package com.example.a4starter

import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.math.*

class SharedViewModel : ViewModel() {
    val gestures: ArrayList<Gesture> = ArrayList()

    fun resamplePath(gesturePath: Path): ArrayList<Pair<Float, Float>> {
        val newPath = ArrayList<Pair<Float, Float>>()
        val pathMeasure = PathMeasure(gesturePath, false)
        val point = FloatArray(2)
        var percentage = 0f
        while (percentage <= 100) {
            pathMeasure.getPosTan(pathMeasure.length * percentage/100f, point, null)
            newPath.add(Pair(point[0], point[1]))
            percentage += 100f/128
        }
        Log.d("SharedViewModel", "Number of points in resampled path: ${newPath.size}")
        return newPath
    }

    fun rotatePath(currentPath: ArrayList<Pair<Float, Float>>): ArrayList<Pair<Float, Float>> {
        var totalX = 0f
        var totalY = 0f
        for (point in currentPath) {
            totalX += point.first
            totalY += point.second
        }
        val centroid = Pair(totalX/currentPath.size, totalY/currentPath.size)
        Log.d("SharedViewModel", "Centroid: $centroid")

        var angle = atan2(centroid.second-currentPath[0].second, centroid.first-currentPath[0].first) *180 / PI
        angle = if (angle > 0) 180 - angle
        else angle * -1 + 180
        Log.d("SharedViewModel", "Angle: $angle")

        val newPath = ArrayList<Pair<Float, Float>>()
        for(point in currentPath) {
            newPath.add(Pair(
                (cos(Math.toRadians(angle)) * (point.first-centroid.first) - sin(Math.toRadians(angle)) * (point.second-centroid.second) + centroid.first).toFloat(),
                (sin(Math.toRadians(angle)) * (point.first-centroid.first) + cos(Math.toRadians(angle)) * (point.second-centroid.second) + centroid.second).toFloat()
            ))
        }
        return newPath
    }

    fun scaleAndTranslatePath(currentPath: ArrayList<Pair<Float, Float>>): ArrayList<Pair<Float, Float>> {
        var totalX = 0f
        var totalY = 0f
        var minX = 9999f
        var maxX = 0f
        var minY = 9999f
        var maxY = 0f
        for (point in currentPath) {
            totalX += point.first
            totalY += point.second
            if (point.first < minX) minX = point.first
            if (point.first > maxX) maxX = point.first
            if (point.second < minY) minY = point.second
            if (point.second > maxY) maxY = point.second
        }
        val centroid = Pair(totalX/currentPath.size, totalY/currentPath.size)
        Log.d("SharedViewModel", "Centroid: $centroid")

        val width = maxX - minX
        val height = maxY - minY
        val scaleFactor = max(width, height) / 800f
        Log.d("SharedViewModel", "Scale Factor: $scaleFactor")

        val newPath = ArrayList<Pair<Float, Float>>()
        for(point in currentPath) {
            newPath.add(Pair(
                (point.first-centroid.first) / scaleFactor + centroid.first,
                (point.second-centroid.second) / scaleFactor + centroid.second
            ))
        }
        return newPath
    }

    fun addStroke(gestureThumbnail: Bitmap, gestureName: String, gesturePath: Path) {
        for (gesture in gestures) {
            if (gesture.gestureName == gestureName) gestures.remove(gesture)
        }
        gestures.add(Gesture(gestureThumbnail, gestureName,
            scaleAndTranslatePath(rotatePath(resamplePath(gesturePath)))))
        Log.d("SharedViewModel", "Added gesture $gestureName, Current number of gestures: ${gestures.size}")
    }

    fun removeStroke(gesture: Gesture) {
        gestures.remove(gesture)
        Log.d("SharedViewModel", "Removed gesture ${gesture.gestureName}, Current number of gestures: ${gestures.size}")
    }
}