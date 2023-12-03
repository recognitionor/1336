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
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ham.onettsix.constant.ActivityResultKey.LOGIN_RESULT_OK
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityMainBinding
import com.ham.onettsix.databinding.NavHeaderMainBinding
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.fragment.HomeFragment
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.view.ToolbarView
import com.ham.onettsix.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHeaderBinding: NavHeaderMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.getInstance(supportFragmentManager)
    }

    val mainViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[MainViewModel::class.java]
    }

    private val loginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            mainViewModel.updateUserInfo()
            selectedItem(0)
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
            if (currentFragment is HomeFragment) {
                currentFragment.refresh()
            }
        }

    private val myProfileResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                LOGIN_RESULT_OK -> {
                    mainViewModel.updateUserInfo()
                    selectedItem(1)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navHeaderBinding = NavHeaderMainBinding.inflate(layoutInflater)
        binding.navView.addHeaderView(navHeaderBinding.root)
        setContentView(binding.root)

        progressDialog.show()

        setupObserve()
        binding.appBarMain.toolbar.setTitleTextColor(Color.TRANSPARENT)
        setSupportActionBar(binding.appBarMain.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_get_ticket,
                R.id.nav_typing_game,
                R.id.nav_history,
                R.id.nav_youtube
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (mainViewModel.userInfo.value?.data == null) {
                mainViewModel.updateUserInfo()
            }
        }
        navHeaderBinding.navHeaderImg.setOnClickListener(this)
        navHeaderBinding.navHeaderNickname.setOnClickListener(this)
    }

    private fun setupObserve() {
        mainViewModel.userInfo.observe(this) {
            progressDialog.dismiss()
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        it.data.let { data ->
                            navHeaderBinding.navHeaderNickname.text = "${data.nickName}#${data.uid}"
                            navHeaderBinding.navHeaderImg.setImageDrawable(
                                getDrawable(
                                    ProfileImageUtil.getImageId(data.profileImageId ?: -1)
                                )
                            )
                        }
                    } else {
                        navHeaderBinding.navHeaderNickname.setText(R.string.login_do)
                        navHeaderBinding.navHeaderImg.setImageResource(R.drawable.ic_account_circle)
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
            R.id.action_settings -> {
                myProfileResult.launch(Intent(this, NoticeActivity::class.java))
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
            R.id.nav_typing_game -> {
                updateTypingGameToolBar()
            }

            R.id.nav_profile -> {
                if (!mainViewModel.isLogin()) {
                    login()
                    return false
                }
                binding.appBarMain.toolbar.setTitle("")
                binding.appBarMain.toolbar.removeMenu()
            }

            else -> {
                binding.appBarMain.toolbar.setTitle("")
                binding.appBarMain.toolbar.removeMenu()
            }
        }
        NavigationUI.onNavDestinationSelected(item, navController)
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {
        when (v) {
            navHeaderBinding.navHeaderImg, navHeaderBinding.navHeaderNickname -> {
                if (!mainViewModel.isLogin()) {
                    login()
                }
            }
        }
    }

    private fun updateTypingGameToolBar() {
        binding.appBarMain.toolbar.setTitle(getString(R.string.menu_typing_game))
        binding.appBarMain.toolbar.addMenu(AppCompatImageView(this).apply {
            background = getDrawable(R.drawable.ic_add)
            this.setOnClickListener {
                startActivity(Intent(this@MainActivity, TypingGameRegisterActivity::class.java))
            }
        })
    }

    fun selectedItem(selectedIndex: Int, doClose: Boolean = true) {
        if (selectedIndex == 2) {
            updateTypingGameToolBar()
        } else {
            binding.appBarMain.toolbar.removeMenu()
        }
        val menu = binding.navView.menu.getItem(selectedIndex)
        menu.isChecked = true
        NavigationUI.onNavDestinationSelected(menu, navController)
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (doClose) {
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    fun login() {
        loginResult.launch(
            Intent(
                this@MainActivity, LoginActivity::class.java
            )
        )
    }
}