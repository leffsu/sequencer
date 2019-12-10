package su.leffsu.sequencer.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.huhx0015.hxaudio.audio.HXSound
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.overlay_bpm.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.leffsu.sequencer.R
import su.leffsu.sequencer.event.PeriodChangeEvent
import su.leffsu.sequencer.logic.NetworkController
import su.leffsu.sequencer.logic.SoundController
import java.util.*
import android.util.DisplayMetrics
import org.leffsu.sequencer.BuildConfig
import su.leffsu.sequencer.cellMargin
import su.leffsu.sequencer.cellSize
import su.leffsu.sequencer.cellSpaceParam


class HomeScreen : AppCompatActivity() {

    private var soundController: SoundController? = null

    private var timers = arrayOf<View>()

    private var currentApiVersion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_home)

        setupTimerViews()

        // регаемся на ивенты таймера
        EventBus.getDefault().register(this)

        calculateCellSize()
        setupSound()
        setupRecycler()
        setupButtons()
        updateBPM()
    }

    private fun setupTimerViews() {

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
    }

    private fun calculateCellSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        /*
        From 1920:1080
        113 - space
        105 - available for cell
        8 - margin
         */
        val cellSizeWithoutMargins = screenWidth / 17
        cellSize = (cellSizeWithoutMargins * cellSpaceParam).toInt()
        cellMargin = (cellSizeWithoutMargins * (1f - cellSpaceParam)).toInt()
    }

    private fun setupSound() {
        HXSound.engines(20)
        if (BuildConfig.DEBUG)
            HXSound.logging(true)
        soundController = SoundController(this)
    }

    private fun setupRecycler() {

        // Убираем анимацию про notifyitemchanged
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        recycler.apply {
            layoutManager = LinearLayoutManager(this@HomeScreen)
            adapter = soundController?.soundEntities?.let {
                HomeAdapter(
                    it
                )
            }
        }
    }

    fun setupButtons() {
        btnShuffle.setOnClickListener {
            shuffle()
        }

        btnSettings.setOnClickListener {
            //            val dialog = FullscreenDialog.newInstance();
//            dialog.setCallback(object : FullscreenDialog.Callback {
//                override fun onActionClick(name: String) {
//                }
//
//            });
//            val fm = supportFragmentManager.beginTransaction()
//            fm.add(R.id.rltDialogContainer, dialog)
//            fm.commit()
        }

        btnSwitchMode.setOnClickListener {

        }

        // кнопка плюс
        btnPlus.setOnClickListener {
            soundController?.bpm?.let { bpm ->
                soundController?.bpm = bpm + 5
            }
            updateBPM()
        }

        // кнопка минус
        btnMinus.setOnClickListener {
            soundController?.bpm?.let { bpm ->
                soundController?.bpm = bpm - 5
            }
            updateBPM()
        }
        btnPlay.setOnClickListener {
            // если играет - стоп, если на паузе - играть
            if (soundController?.playing == true) {
                btnPlay.setImageResource(R.drawable.ic_newplay)
                soundController?.pause()
                removeTimers()
            } else {
                btnPlay.setImageResource(R.drawable.ic_newpause)
                soundController?.play()
            }
        }
        btnReset.setOnClickListener {
            soundController?.resetSoundboard()
            recycler.adapter?.notifyDataSetChanged()
        }
    }

    fun hideNavigationBar() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT

        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            window.decorView.systemUiVisibility = flags

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val decorView = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN === 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
    }

    fun shuffle() {
        for (s in 0..6) {
            if (Random().nextBoolean()) {

                if (Random().nextBoolean()) {
                    soundController?.soundEntities?.get(s)!!.array = arrayOf<Boolean>(
                        true, false, true, false, true, false, true, false,
                        true, false, true, false, true, false, true, false
                    )
                } else {

                    if (Random().nextBoolean()) {
                        soundController?.soundEntities?.get(s)!!.array = arrayOf<Boolean>(
                            false, true, false, true, false, true, false, true,
                            false, true, false, true, false, true, false, true
                        )
                    } else {
                        soundController?.soundEntities?.get(s)!!.array = arrayOf<Boolean>(
                            false, true, true, false, false, true, true, false,
                            false, true, true, false, false, true, true, false
                        )
                    }
                }
            } else {
                soundController?.soundEntities?.get(s)!!.array = arrayOf<Boolean>(
                    false, false, false, false, false, false, false, false,
                    false, false, false, false, false, false, false, false
                )
            }
        }
        recycler.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        NetworkController.init()
    }

    private fun updateBPM() {
        // Обновляем TextView из SoundController'a
        txvBpm.text = soundController?.bpm?.toString()
    }

    private fun removeTimers() {
        // Очищаем все таймеры-вьюшки
        removeTimers(
            timers = timers
        )
    }

    private fun removeTimers(timers: Array<View>) {
        for (view in timers) {
            view.setBackgroundResource(android.R.color.transparent)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(periodChangedEvent: PeriodChangeEvent) {
        val transparent = android.R.color.transparent
        val active = R.color.colorPrimary

        val activeIndex = periodChangedEvent.period
        // формула для вычисления предыдущего значения
        val inactiveIndex = (activeIndex + timers.size - 1) % 16

        timers[activeIndex].setBackgroundResource(active)
        timers[inactiveIndex].setBackgroundResource(transparent)
    }

    @SuppressLint("NewApi")
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}