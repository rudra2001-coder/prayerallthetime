package com.rudra.prayerallthetime.notifications

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.rudra.prayerallthetime.R

class AthanService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playAthan()
        return START_NOT_STICKY
    }

    private fun playAthan() {
        try {
            // Note: You need to add an 'athan.mp3' file to your res/raw folder
            // For now, we use a placeholder logic. If the file exists, it plays.
            val resId = resources.getIdentifier("athan", "raw", packageName)
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(this, resId)
                mediaPlayer?.setOnCompletionListener { stopSelf() }
                mediaPlayer?.start()
            } else {
                Log.e("AthanService", "Athan file not found in res/raw. Please add athan.mp3")
                stopSelf()
            }
        } catch (e: Exception) {
            Log.e("AthanService", "Error playing Athan", e)
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
