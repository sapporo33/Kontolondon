package com.emberwing.mergeassistant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent

class DragService : AccessibilityService() {

    companion object {
        var instance: DragService? = null

        // Batas luar grid dalam piksel layar (perkiraan dari screenshot 1080 px).
        const val LEFT = 85f
        const val TOP = 858f
        const val RIGHT = 997f
        const val BOTTOM = 1768f
        const val ROWS = 7
        const val COLS = 7
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onServiceConnected() {
        instance = this
    }

    override fun onUnbind(intent: Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    // Jeda 5 detik dulu supaya Anda sempat membuka Telegram/game.
    fun runQueue(moves: List<IntArray>) {
        moves.forEachIndexed { i, move ->
            handler.postDelayed({ drag(move) }, 5000L + i * 1500L)
        }
    }

    private fun drag(move: IntArray) {
        val cellW = (RIGHT - LEFT) / COLS
        val cellH = (BOTTOM - TOP) / ROWS

        // Koordinat 1-based, dihitung ke tengah sel.
        fun cx(col: Int) = LEFT + (col - 0.5f) * cellW
        fun cy(row: Int) = TOP + (row - 0.5f) * cellH

        val path = Path().apply {
            moveTo(cx(move[1]), cy(move[0]))
            lineTo(cx(move[3]), cy(move[2]))
        }
        val stroke = GestureDescription.StrokeDescription(path, 0, 400)
        dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
    }
}
