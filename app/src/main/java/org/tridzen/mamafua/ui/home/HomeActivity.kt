package org.tridzen.mamafua.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.remote.AppPreferences
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        Coroutines.main {
//            val authKey = preferences.getValue(AppPreferences.KEY_AUTH).first()
//            if (authKey.isNullOrBlank()) {
//                startActivity(Intent(this, AuthActivity::class.java))
//                finish()
//            }
//        }
    }
}