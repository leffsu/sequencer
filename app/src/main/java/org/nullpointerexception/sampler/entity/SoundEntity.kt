package org.nullpointerexception.sampler.entity

import android.content.Context
import android.media.SoundPool

class SoundEntity(val icon: Int, resource: Int, private val context: Context?) {

    // ошибка -12
    private val soundPool = SoundPool(1, 3, 0)

    private val soundId = soundPool.load(context, resource, 1)
    val array = arrayOf(
        false /* 1/16 */, false /* 1/8 */, false, false, //1/4
        false, false, false, false, //1/2
        false, false, false, false,
        false, false, false, false
    )

    fun play() {
        soundPool.play(soundId, 1f, 1f, 10, 0, 1f)
    }
}