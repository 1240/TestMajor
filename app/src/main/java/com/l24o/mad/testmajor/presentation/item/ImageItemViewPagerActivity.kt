package com.l24o.mad.testmajor.presentation.item

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.l24o.mad.testmajor.R
import com.l24o.mad.testmajor.domain.entity.ImageItem
import com.l24o.mad.testmajor.extension.extras
import kotlinx.android.synthetic.main.activity_image_item.*

class ImageItemViewPagerActivity : MvpAppCompatActivity() {

    companion object {
        const val EXTRA_ITEMS = "EXTRA_ITEMS"
        const val EXTRA_POSITION = "EXTRA_POSITION"
        const val EXTRA_CURRENT = "EXTRA_CURRENT"
        const val EXTRA_EXIT_POSITION = "EXTRA_EXIT_POSITION"
    }

    private var colorDrawable: ColorDrawable? = null
    private val ALPHA_MAX = 0xFF
    private val items: ArrayList<ImageItem> by extras(EXTRA_ITEMS)
    private val adapterPosition: Int by extras(EXTRA_POSITION)
    private val current: Int by extras(EXTRA_CURRENT)

    private val onDismissListener = object : DismissFrameLayout.OnDismissListener {
        override fun onScaleProgress(scale: Float) {
            colorDrawable?.alpha = Math.min(ALPHA_MAX, (colorDrawable?.alpha
                    ?: ALPHA_MAX) - (scale * ALPHA_MAX).toInt())
        }

        override fun onDismiss() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }

        override fun onCancel() {
            colorDrawable?.alpha = ALPHA_MAX
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
        }
        setContentView(R.layout.activity_image_item)
        initViews()
    }

    private fun initViews() {
        viewPager.adapter = PhotoAdapter(items, onDismissListener)
        viewPager.currentItem = current
    }


    override fun finishAfterTransition() {
        val pos = viewPager.currentItem
        val intent = Intent()
                .putExtra(EXTRA_EXIT_POSITION, pos)
        setResult(Activity.RESULT_OK, intent)
        if (current != pos) {
            val view = viewPager.findViewWithTag<View>(
                    getString(R.string.transition_name, adapterPosition, pos))
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
                sharedElements!![view.transitionName] = view
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

    inner class PhotoAdapter(
            private var items: List<ImageItem>,
            private var onDismissListener: DismissFrameLayout.OnDismissListener
    ) : PagerAdapter() {

        override fun getCount(): Int {
            return items.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(container.context)
            imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            val layout = DismissFrameLayout(container.context)
            layout.setDismissListener(onDismissListener)
            layout.layoutParams = ViewPager.LayoutParams()
            layout.addView(imageView)
            Glide.clear(imageView)
            Glide.with(container.context)
                    .load(items[position].imageUrl)
                    .dontAnimate()
                    .placeholder(R.drawable.ic_image_black_48dp)
                    .error(R.drawable.ic_broken_image_black_48dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val name = container.context
                        .getString(R.string.transition_name, adapterPosition, position)
                imageView.transitionName = name
                imageView.tag = name
                if (position == current) {
                    setStartPostTransition(imageView)
                }
            }

            container.addView(layout)
            return layout
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }

}

