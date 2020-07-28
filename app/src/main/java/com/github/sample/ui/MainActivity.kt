package com.github.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.sample.R
import com.github.sample.databinding.ActivityMainBinding
import com.github.sample.domain.auth.AuthInteractor
import com.github.sample.ui.auth.AuthFragment
import com.github.sample.ui.history.HistoryFragment
import com.github.sample.ui.search.SearchFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Navigator {

    @Inject
    lateinit var interactor: AuthInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component().inject(this)
        val b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (savedInstanceState == null) {
            if (!interactor.isAuthorized()) {
                replaceFragment(AuthFragment(), false)
            } else {
                replaceFragment(SearchFragment())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            supportFragmentManager.fragments.forEach {
                (it as? IntentInterceptor)?.onNewIntent(intent)
            }
        }
    }

    override fun toSearchScreen() {
        replaceFragment(SearchFragment())
    }

    override fun toHistoryScreen() {
        replaceFragment(HistoryFragment())
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .apply {
                if (addToBackStack) {
                    addToBackStack("")
                }
            }
            .commit()
    }
}
