package ru.delivery.system.android

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.delivery.system.android.utils.HttpHelper

class MainActivity : AppCompatActivity() {
    private val httpHelpler = HttpHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        ordersButton.setOnClickListener { navigateTo(this, OrderActivity::class.java) }
        mapButton.setOnClickListener { navigateTo(this, MapActivity::class.java) }
        settingsButton.setOnClickListener { navigateTo(this, MapActivity::class.java) }
        exitButton.setOnClickListener { finish(); System.exit(0) }

        fab.setOnClickListener { view ->
            var message: String
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
            ) {
                message =  "Permission is not granted"
            } else {
                message =  "Permission is granted"
            }

            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }



    private fun navigateTo(context: Context, cls: Class<*>) {
        val randomIntent = Intent(context, cls)
        startActivity(randomIntent)
    }

    private fun orderButtonAction() {
        val randomIntent = Intent(this, OrdersActivity::class.java)
        randomIntent.putExtra(OrdersActivity.orderId, "10")
        startActivity(randomIntent)
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
