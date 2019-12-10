package su.leffsu.sequencer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.leffsu.sequencer.R


class MainActivity : AppCompatActivity() {

    var currentApiVersion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
