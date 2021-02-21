package org.tridzen.mamafua.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.ui.auth.AuthActivity
import org.tridzen.mamafua.ui.onboarding.OnBoardingActivity
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.startNewActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        Coroutines.main {
            preferences.getValue(AppPreferences.SHOW_ONBOARDING).collect {
                val activity =
                    if (it == null) OnBoardingActivity::class.java else AuthActivity::class.java

                startNewActivity(activity)
            }
        }
    }
}
