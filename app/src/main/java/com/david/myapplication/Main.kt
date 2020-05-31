package com.david.myapplication

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.david.myapplication.chat.ChatFragment
import com.david.myapplication.chat.ChatUsers
import com.david.myapplication.covid19.Covid
import com.david.myapplication.news.News
import com.david.myapplication.note.NoteFragment
import com.david.myapplication.register_login.Login
import com.david.myapplication.weather.WeatherFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_latest_message.*
import kotlin.system.exitProcess


class Main: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)
        checkConnectivity()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChatFragment()).commit()
        bottomNavigationViews()

    }

    private fun checkConnectivity(){
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if(netInfo != null && netInfo.isConnected){
            verifyIfUserIsLoggedIn()
        }else{
            AlertDialog.Builder(this@Main)
                .setTitle("No Internet Connection")
                .setMessage("Check you network setting and try again")
                .setCancelable(false)
                .setPositiveButton("Ok") { _, _ ->
                    finishAffinity();
                    exitProcess(0)
                }.show()
        }
    }

    private fun verifyIfUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun bottomNavigationViews() {
        bot_nav.setOnNavigationItemSelectedListener {
            var selectedFragment: Fragment? = null

            when(it.itemId){
                R.id.bot_nav_chat -> selectedFragment =
                    ChatFragment()
                R.id.bot_nav_news -> selectedFragment =
                    News()
                R.id.bot_nav_weather -> selectedFragment =
                    WeatherFragment()
                R.id.bot_nav_note -> selectedFragment =
                    NoteFragment()
                R.id.bot_nav_covid -> selectedFragment =
                    Covid()

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
