package com.smic.conjugadorit

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.smic.conjugadorit.databinding.ActivityMainBinding
import com.smic.conjugadorit.fragment.FavoriteFragment
import com.smic.conjugadorit.fragment.SettingsFragment
import com.smic.conjugadorit.fragment.VerbsFragment
import com.smic.conjugadorit.rxelements.RxSearchView
import com.smic.conjugadorit.utils.gone
import com.smic.conjugadorit.utils.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var disposeSearchView: Disposable
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var currentFragment: Fragment? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            currentFragment = f
            updateUI()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.fragment_container)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        val searchView = binding.searchView

        val searchEditText = searchView.findViewById<EditText>(
            resources.getIdentifier(
                "android:id/search_src_text",
                null,
                null
            )
        )


        if (searchEditText != null) {
            searchEditText.gravity = Gravity.CENTER;
        }

        disposeSearchView = RxSearchView().fromView(searchView)
            .subscribeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { quest ->
                setQuest(quest)
            }

    }

    private fun setQuest(quest: String) {
        sharedViewModel.setQuest(quest)
        when {
            (quest.isEmpty() && currentFragment !is FavoriteFragment) ->
                navController.navigate(R.id.favoriteFragment)
            (quest.isEmpty() && currentFragment is FavoriteFragment) ->
                return
            (currentFragment !is VerbsFragment) ->
                navController.navigate(R.id.verbsFragment)

        }

    }

    private fun updateUI() {
        binding.searchView.show
        if (currentFragment is SettingsFragment) {
            binding.toolbar.menu.clear()
            binding.searchView.gone
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val iconDrawable =
            DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.menu_button)!!)
        val menuItem = menu.add(R.string.setings)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            navController.navigate(R.id.settingsFragment)
            return@setOnMenuItemClickListener true
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        binding.searchView.show
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeSearchView.dispose()
    }
}