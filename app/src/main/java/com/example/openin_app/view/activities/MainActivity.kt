package com.example.openin_app.view.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.openin_app.view.fragments.FirstFragment
import com.example.openin_app.R
import com.example.openin_app.databinding.ActivityMainBinding
import com.example.openin_app.utils.Constants
import com.example.openin_app.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        // Removing the shadow effect on the BottomNavigationView
        binding.bottomNavigationView.background = null

        // Making the placeholder menu item unclickable
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        storeSession(Constants.TOKEN)
        // Load the initial fragment
        if (savedInstanceState == null) {
            loadFragment(FirstFragment())
        }
    }

    private fun loadFragment(fragment: FirstFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    private fun storeSession(token: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("access_token", token)
            apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
