package com.emberwing.mergeassistant

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var status: TextView
    private lateinit var queueInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        status = TextView(this).apply {
            textSize = 16f
            gravity = Gravity.CENTER
        }

        val enableButton = Button(this).apply {
            text = "1. Buka pengaturan Accessibility"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        queueInput = EditText(this).apply {
            setText("4,2,4,3")
            hint = "sourceRow,sourceCol,destRow,destCol"
            minLines = 4
            gravity = Gravity.TOP
        }

        val sendButton = Button(this).apply {
            text = "2. Kirim queue (lalu buka Hatchery)"
            setOnClickListener { sendQueue() }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            setBackgroundColor(Color.rgb(245, 242, 232))
            addView(status)
            addView(enableButton)
            addView(queueInput)
            addView(sendButton)
        }

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        status.text = if (DragService.instance != null) {
            "Accessibility: AKTIF"
        } else {
            "Accessibility: BELUM AKTIF"
        }
    }

    private fun sendQueue() {
        val service = DragService.instance
        if (service == null) {
            toast("Aktifkan accessibility dulu.")
            return
        }

        val moves = mutableListOf<IntArray>()
        val lines = queueInput.text.toString().lines().filter { it.isNotBlank() }

        for ((index, line) in lines.withIndex()) {
            val parts = line.split(",").map { it.trim().toIntOrNull() }
            if (parts.size != 4 || parts.any { it == null || it !in 1..7 }) {
                toast("Format salah di baris ${index + 1}. Contoh: 4,2,4,3")
                return
            }
            moves.add(IntArray(4) { parts[it]!! })
        }

        if (moves.isEmpty()) {
            toast("Queue kosong.")
            return
        }

        service.runQueue(moves)
        toast("Queue dikirim. Buka Hatchery dalam 5 detik.")
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
