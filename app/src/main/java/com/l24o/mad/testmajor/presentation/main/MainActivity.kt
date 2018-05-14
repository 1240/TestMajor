package com.l24o.mad.testmajor.presentation.main

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.l24o.mad.testmajor.R
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        recycler_view.adapter = adapter
        recycler_view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view.setHasFixedSize(true)
    }

    override fun showImages(items: List<Group>) {
        adapter.clear()
        adapter.addAll(items)
    }

    override fun setLoadingVisible(isVisible: Boolean) {
    }

    override fun navigateToImage(items: List<ImageListItem>, position: Int, image: ImageView) {
        val intent = Intent(this, ImageItemViewPagerActivity::class.java)
                .putParcelableArrayListExtra(ImageItemViewPagerActivity.EXTRA_ITEMS, ArrayList(items.map { it.imageItem }))
                .putExtra(ImageItemViewPagerActivity.EXTRA_POSITION, position)
                .putExtra(ImageItemViewPagerActivity.EXTRA_CURRENT, position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, image, image.transitionName)
            startActivityForResult(intent, 0, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun showError(errorText: String) {
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