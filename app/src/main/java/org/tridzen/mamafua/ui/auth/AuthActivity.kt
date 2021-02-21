package org.tridzen.mamafua.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.ui.home.HomeActivity
import org.tridzen.mamafua.utils.coroutines.Coroutines
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        Coroutines.main {
            val authKey = preferences.getValue(AppPreferences.KEY_AUTH).first()
            if (!authKey.isNullOrBlank()) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}