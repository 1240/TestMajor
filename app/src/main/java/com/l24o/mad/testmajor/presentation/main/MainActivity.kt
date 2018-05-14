package com.l24o.mad.testmajor.presentation.main

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.l24o.mad.testmajor.R
import com.l24o.mad.testmajor.extension.makeVisibleOrGone
import com.l24o.mad.testmajor.presentation.item.ImageItemViewPagerActivity
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    private var exitPosition: Int = 0

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        with(swipe_refresh_layout) {
            setOnRefreshListener {
                presenter.refresh()
            }
            setColorSchemeColors(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
        }
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.setHasFixedSize(true)
    }

    override fun showImages(items: List<Group>) {
        adapter.clear()
        adapter.addAll(items)
    }

    override fun setLoadingVisible(isVisible: Boolean) {
        if (!swipe_refresh_layout.isRefreshing) {
            progress_bar_container.makeVisibleOrGone(isVisible)
        } else {
            swipe_refresh_layout.isRefreshing = isVisible
        }
    }

    override fun navigateToImage(items: List<ImageListItem>, position: Int, image: ImageView) {
        val intent = Intent(this, ImageItemViewPagerActivity::class.java)
                .putParcelableArrayListExtra(ImageItemViewPagerActivity.EXTRA_ITEMS, ArrayList(items.map { it.imageItem }))
                .putExtra(ImageItemViewPagerActivity.EXTRA_CURRENT, position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, image, image.transitionName)
            startActivityForResult(intent, 0, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun showError(errorText: String) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            exitPosition = data.getIntExtra(ImageItemViewPagerActivity.EXTRA_EXIT_POSITION, 0)
        }
    }

    override fun setCallback(enterPosition: Int, sharedViews: List<View>) {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                if (exitPosition != enterPosition &&
                        (names?.size ?: 0) > 0 && exitPosition < sharedViews.size) {
                    names?.clear()
                    sharedElements?.clear()
                    sharedViews.getOrNull(exitPosition)?.let { view ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            names?.add(view.transitionName)
                            sharedElements?.put(view.transitionName, view)
                        }
                    }
                }
                setExitSharedElementCallback(null as SharedElementCallback?)
                presenter.onMapSharedElements()
            }
        })
    }

}