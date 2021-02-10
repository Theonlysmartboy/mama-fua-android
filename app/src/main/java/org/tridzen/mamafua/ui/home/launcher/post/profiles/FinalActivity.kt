package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R

@AndroidEntryPoint
class FinalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profiles)

        //removing the shadow from the action bar
        supportActionBar?.elevation = 0f
    }
}