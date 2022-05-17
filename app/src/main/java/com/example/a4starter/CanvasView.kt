package com.example.a4starter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
//import android.graphics.PathMeasure
//import java.lang.Math.toRadians
//import kotlin.math.*

// Acknowledgement: This CanvasView code is based on the "09.Android/2.Canvas" and "09.Android/5.PanZoom" sample code from the cs349 public repo

@SuppressLint("AppCompatCustomView")
class CanvasView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    // drawing
    var path = Path()
    private var paintbrush = Paint(Color.BLACK)

    // constructor
    init {
        this.setBackgroundColor(Color.rgb(120, 197, 239))
        paintbrush.style = Paint.Style.STROKE
        paintbrush.strokeWidth = 20f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("CanvasView", "Action touch down " + event.x + "," + event.y)
                path = Path()
                path.moveTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("CanvasView", "Action touch move " + event.x + "," + event.y)
                path.lineTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                Log.d("CanvasView", "Action touch up " + event.x + "," + event.y)
                invalidate()

                /*Algorithm Visual Test
                val newPath = ArrayList<Pair<Float, Float>>()
                val pathMeasure = PathMeasure(path, false)
                val point = FloatArray(2)
                var percentage = 0f
                while (percentage <= 100) {
                    pathMeasure.getPosTan(pathMeasure.length * percentage/100f, point, null)
                    newPath.add(Pair(point[0], point[1]))
                    percentage += 100f/128
                }
                Log.d("SharedViewModel", "Number of points in new path: ${newPath.size} $newPath")

                var totalX = 0f
                var totalY = 0f
                for (point in newPath) {
                    totalX += point.first
                    totalY += point.second
                }
                val centroid = Pair(totalX/newPath.size, totalY/newPath.size)
                Log.d("SharedViewModel", "Centroid: $centroid")
                var angle = atan2(centroid.second-newPath[0].second, centroid.first-newPath[0].first)*180/PI
                if (angle > 0) angle = 180 - angle
                else angle = angle * -1 + 180
                Log.d("SharedViewModel", "Angle: ${angle}")
                val newNewPath = ArrayList<Pair<Float, Float>>()
                for(point in newPath) {
                    newNewPath.add(Pair(
                        (cos(toRadians(angle)) * (point.first-centroid.first) - sin(toRadians(angle)) * (point.second-centroid.second) + centroid.first).toFloat(),
                        (sin(toRadians(angle)) * (point.first-centroid.first) + cos(toRadians(angle)) * (point.second-centroid.second) + centroid.second).toFloat()
                    ))
                }

                var totalX2 = 0f
                var totalY2 = 0f
                var minX = 9999f
                var maxX = 0f
                var minY = 9999f
                var maxY = 0f
                for (point in newNewPath) {
                    totalX2 += point.first
                    totalY2 += point.second
                    if (point.first < minX) minX = point.first
                    if (point.first > maxX) maxX = point.first
                    if (point.second < minY) minY = point.second
                    if (point.second > maxY) maxY = point.second
                }
                val centroid2 = Pair(totalX2/newNewPath.size, totalY2/newNewPath.size)
                Log.d("SharedViewModel", "Centroid: $centroid2")
                val width = maxX - minX
                val height = maxY - minY
                val scaleFactor = max(width, height) / 800f
                val newNewNewPath = ArrayList<Pair<Float, Float>>()
                for(point in newNewPath) {
                    newNewNewPath.add(Pair(
                        (point.first-centroid2.first) / scaleFactor + centroid2.first,
                        (point.second-centroid2.second) / scaleFactor + centroid2.second
                    ))
                }

                path = Path()
                path.moveTo(newNewNewPath[0].first, newNewNewPath[0].second)
                for (point in newNewNewPath) {
                    path.lineTo(point.first, point.second)
                }
                invalidate()
                */
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paintbrush)
    }
}
