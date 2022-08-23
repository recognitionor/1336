package com.ham.onettsix

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.ham.onettsix.constant.ActivityResultKey.LOGIN_RESULT_OK
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.getInstance(supportFragmentManager)
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[MainViewModel::class.java]
    }

    private val myProfileResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                LOGIN_RESULT_OK -> {
                    mainViewModel.updateUserInfo()
                    val menu = nav_view.menu.getItem(1)
                    menu.isChecked = true
                    NavigationUI.onNavDestinationSelected(menu, navController)
                    val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog.show()
        setupObserve()
        app_bar_main.toolbar.setTitleTextColor(Color.TRANSPARENT)
        setSupportActionBar(app_bar_main.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_game
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> mainViewModel.updateUserInfo() }
    }

    private fun setupObserve() {
        mainViewModel.userInfo.observe(this) {
            progressDialog.dismiss()
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.nickName?.let { nickname ->
                        nav_header_nickname.text = nickname
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_login -> {
                myProfileResult.launch(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.action_eula -> {
                myProfileResult.launch(Intent(this, PermissionActivity::class.java))
                true
            }
            R.id.action_sign_up -> {
                myProfileResult.launch(Intent(this, SignUpActivity::class.java))
                true
            }
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                if (!mainViewModel.isLogin()) {
                    myProfileResult.launch(Intent(this@MainActivity, LoginActivity::class.java))
                    return false
                }
            }
        }
        NavigationUI.onNavDestinationSelected(item, navController)
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}