package org.tridzen.mamafua.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.ui.home.HomeActivity
import org.tridzen.mamafua.utils.data.Prefs
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(){

    @Inject lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

//        prefs.getValue(AppPreferences.KEY_AUTH).asLiveData().observe(this) {
//            if (it != null) {
//                startActivity(Intent(this, HomeActivity::class.java))
//                finish()
//            }
//        }

//        Coroutines.main {
//            prefs.getValue(AppPreferences.KEY_AUTH).collect {
//                if (it != null) {
//                    startActivity(Intent(this, HomeActivity::class.java))
//                    finish()
//                }
//            }
//        }

        val auth = prefs.getString(Prefs.KEY_AUTH)
        if (auth.isNotEmpty()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}