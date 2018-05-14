package com.l24o.mad.testmajor.presentation.item

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.view.View
import android.view.ViewTreeObserver
import com.arellomobile.mvp.MvpAppCompatActivity
import com.l24o.mad.testmajor.R
import com.l24o.mad.testmajor.domain.entity.ImageItem
import com.l24o.mad.testmajor.extension.extras
import kotlinx.android.synthetic.main.activity_image_item.*

class ImageItemViewPagerActivity : MvpAppCompatActivity() {

    companion object {
        const val EXTRA_ITEMS = "EXTRA_ITEMS"
        const val EXTRA_CURRENT = "EXTRA_CURRENT"
        const val EXTRA_EXIT_POSITION = "EXTRA_EXIT_POSITION"
    }

    private val items: ArrayList<ImageItem> by extras(EXTRA_ITEMS)
    private val current: Int by extras(EXTRA_CURRENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
        }
        setContentView(R.layout.activity_image_item)
        initViews()
    }

    private fun initViews() {
        supportActionBar?.title = items[current].title
        with(viewPager) {
            adapter = PhotoAdapter(
                    items = items,
                    current = current,
                    onStartPostTransition = ::setStartPostTransition)

            clearOnPageChangeListeners()
            addOnPageChangeListener(object : SimpleOnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    supportActionBar?.title = items[position].title
                }

            })
            currentItem = current
        }
    }


    override fun finishAfterTransition() {
        val position = viewPager.currentItem
        val intent = Intent()
                .putExtra(EXTRA_EXIT_POSITION, position)
        setResult(Activity.RESULT_OK, intent)
        if (current != position) {
            val view = viewPager.findViewWithTag<View>("transition_name:$position")
            setSharedElementCallback(view)
        }
        super.finishAfterTransition()
    }

    @TargetApi(21)
    private fun setSharedElementCallback(view: View) {
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                names?.clear()
                sharedElements?.clear()
                names?.add(view.transitionName)
                sharedElements?.put(view.transitionName, view)
            }
        })
    }

    @TargetApi(21)
    private fun setStartPostTransition(sharedView: View) {
        sharedView.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        sharedView.viewTreeObserver.removeOnPreDrawListener(this)
                        startPostponedEnterTransition()
                        return false
                    }
                })
    }

}

