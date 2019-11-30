package org.nullpointerexception.sampler.entity

import android.content.Context
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import com.huhx0015.hxaudio.audio.HXMusic
import com.huhx0015.hxaudio.audio.HXSound

class SoundEntity(
    val activeColorResId: Int, val icon: Int,
    val resource: Int, private val context: Context?
) {

    var array = arrayOf(
        false /* 1/16 */, false /* 1/8 */, false, false, //1/4
        false, false, false, false, //1/2
        false, false, false, false,
        false, false, false, false
    )

    fun play() {
        HXSound.sound().load(resource).play(context)
    }
}