package com.mikelike11.kinopoiskapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikelike11.kinopoiskapp.screens.favoritefilms.FavoriteFilmsFragment
import com.mikelike11.kinopoiskapp.screens.searchfilms.SearchFilmsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    loadFragment(FavoriteFilmsFragment())
                    true
                }
                R.id.search -> {
                    loadFragment(SearchFilmsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
        chooseFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("screen_index", bottomNavigationView.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        bottomNavigationView.selectedItemId = savedInstanceState.getInt("screen_index")
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun chooseFragment() {
        when (bottomNavigationView.selectedItemId) {
            R.id.favorites -> {
                loadFragment(FavoriteFilmsFragment())
            }
            R.id.search -> {
                loadFragment(SearchFilmsFragment())
            }
            else -> {}
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, fragment)
            transaction.commit()
        } else {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view3, fragment)
            transaction.commit()
        }
    }
}