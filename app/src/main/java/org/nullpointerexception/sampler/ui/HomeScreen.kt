package org.nullpointerexception.sampler.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.nullpointerexception.sampler.R

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
    }

}