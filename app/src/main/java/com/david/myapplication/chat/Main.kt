package com.david.myapplication.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.david.myapplication.R
import com.david.myapplication.bottom_nav.ChatFragment
import com.david.myapplication.bottom_nav.NewsFragment
import com.david.myapplication.bottom_nav.NoteFragment
import com.david.myapplication.bottom_nav.WeatherFragment
import com.david.myapplication.register_login.Login
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_latest_message.*

class Main: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChatFragment()).commit()

        bottomNavigationViews()
    }

    private fun bottomNavigationViews() {
        bot_nav.setOnNavigationItemSelectedListener {
            var selectedFragment: Fragment? = null

            when(it.itemId){
                R.id.bot_nav_chat -> selectedFragment = ChatFragment()
                R.id.bot_nav_news -> selectedFragment = NewsFragment()
                R.id.bot_nav_weather -> selectedFragment = WeatherFragment()
                R.id.bot_nav_note -> selectedFragment = NoteFragment()
            }
            if(selectedFragment != null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit()
            }

            return@setOnNavigationItemSelectedListener true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_new_messages -> {
                val intent = Intent(this, ChatUsers::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
