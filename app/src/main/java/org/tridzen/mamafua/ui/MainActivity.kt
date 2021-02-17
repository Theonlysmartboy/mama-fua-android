package org.tridzen.mamafua.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

//        prefs.getValue(AppPreferences.IS_FIRST_TIME_LAUNCH).asLiveData().observe(this) {
//            val activity =
//                if (it == null || !it) OnBoardingActivity::class.java else AuthActivity::class.java
//            startNewActivity(activity)
//        }

        Coroutines.main {
            preferences.getValue(AppPreferences.IS_FIRST_TIME_LAUNCH).collect {
                val activity =
                    if (it == null || !it) OnBoardingActivity::class.java else AuthActivity::class.java

                startNewActivity(activity)
            }
        }
    }
}