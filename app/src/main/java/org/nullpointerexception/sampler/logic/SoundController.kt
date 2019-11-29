package org.nullpointerexception.sampler.logic

import android.os.Handler
import android.os.Looper
import org.greenrobot.eventbus.EventBus
import android.content.Context
import org.nullpointerexception.sampler.R
import org.nullpointerexception.sampler.entity.SoundEntity
import org.nullpointerexception.sampler.event.PeriodChangeEvent

class SoundController(private val context: Context?) { // контекст нужен для soundpool'a

    // Количество ударов в секунду
    var bpm: Int = 140

    // Счетчик текущего элемента
    private var period = 0

    // Максимальный индекс элемента
    private val PERIOD_MAX = 15

    // наш бог и спаситель, который управляет временем и пространством
    private val handler: Handler = Handler(Looper.getMainLooper())

    // внешний статус
    var playing: Boolean = false

    // Сущности
    val soundEntities: List<SoundEntity> = listOf(
        // Создаем сущности инструментов, которые внутри себя все загрузят
        SoundEntity(
            R.color.color1,
            R.drawable.ic_note,
            R.raw.bass,
            context
        ),
        SoundEntity(
            R.color.color2,
            R.drawable.ic_note,
            R.raw.clap,
            context
        ),
        SoundEntity(
            R.color.color3,
            R.drawable.ic_note,
            R.raw.claves,
            context
        ),
        SoundEntity(
            R.color.color4,
            R.drawable.ic_note,
            R.raw.click,
            context
        ),
        SoundEntity(
            R.color.color5,
            R.drawable.ic_note,
            R.raw.hat,
            context
        ),
        SoundEntity(
            R.color.color6,
            R.drawable.ic_note,
            R.raw.moog,
            context
        ),
        SoundEntity(
            R.color.color7,
            R.drawable.ic_note,
            R.raw.perc,
            context
        ),
        SoundEntity(
            R.color.color8,
            R.drawable.ic_note,
            R.raw.snare,
            context
        ),
        SoundEntity(
            R.color.color9,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color10,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color11,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color12,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color13,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color14,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color15,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color16,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color17,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color18,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        ),
        SoundEntity(
            R.color.color19,
            R.drawable.ic_note,
            R.raw.synthtwo,
            context
        )
    )

    fun defaultSoundboard() {

    }

    fun resetSoundboard() {
        for (entity in soundEntities) {
            for (x in entity.array.indices) {
                entity.array[x] = false
            }
        }
    }

    fun pause() {
        handler.removeCallbacksAndMessages(processBeat)
        playing = false
        period = 0
    }

    fun play() {
        handler.post(processBeat)
        playing = true
    }

    // функционал проигрывания
    private val processBeat: Runnable = object : Runnable {
        override fun run() {
            if (playing) {
                period++
                if (period > PERIOD_MAX) {
                    period = 0
                }
                // Кидаем в MainActivity ивент о проигрывании
                EventBus.getDefault().post(PeriodChangeEvent(period))

                val booleanArrayToSendToNetwork = arrayListOf<Boolean>()

                for (se in soundEntities) {
                    booleanArrayToSendToNetwork.add(se.array[period])
                    if (se.array[period]) {
                        se.play()
                    }
                }

                NetworkController.sendToServer(
                    booleanArrayToSendToNetwork, true
                )

                // Кидаем в очередь
                handler.postDelayed(this, (60000.toFloat() / (bpm.toFloat() * 4)).toLong())
            }
        }
    }
}