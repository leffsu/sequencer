package org.nullpointerexception.sampler.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.overlay_bpm.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import org.greenrobot.eventbus.EventBus
import org.nullpointerexception.sampler.R

class HomeScreen : AppCompatActivity() {

    private var timers = arrayOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        timers = arrayOf(
            imgTimerBeat1, imgTimerBeat2,
            imgTimerBeat3, imgTimerBeat4,
            imgTimerBeat5, imgTimerBeat6,
            imgTimerBeat7, imgTimerBeat8,
            imgTimerBeat9, imgTimerBeat10,
            imgTimerBeat11, imgTimerBeat12,
            imgTimerBeat13, imgTimerBeat14,
            imgTimerBeat15, imgTimerBeat16
        )

        // регаемся на ивенты таймера
        EventBus.getDefault().register(this)

        // Баунс-эффект
        OverScrollDecoratorHelper.setUpOverScroll(
            scrollview
        )

        // Убираем анимацию про notifyitemchanged
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        // кнопка плюс
        btnPlus.setOnClickListener {
        }

        // кнопка минус
        btnMinus.setOnClickListener {
        }
        btnPlay.setOnClickListener {
        }
        btnReset.setOnClickListener {
        }


        updateBPM()
    }

    private fun updateBPM() {
        // Обновляем TextView из SoundController'a
        txvBpm.text = "140"
    }

}