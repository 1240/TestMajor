package com.l24o.mad.testmajor.presentation.item

import android.annotation.TargetApi
import android.os.Build
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.l24o.mad.testmajor.R
import com.l24o.mad.testmajor.domain.entity.ImageItem
import kotlinx.android.synthetic.main.li_image.view.*

class PhotoAdapter(
        private var items: List<ImageItem>,
        private val current: Int,
        private val onStartPostTransition: (View) -> Unit
) : PagerAdapter() {

    override fun getCount() = items.size

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getPageTitle(position: Int) = items[position].title

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = LayoutInflater.from(container.context).inflate(R.layout.li_image, container, false)
        with(layout) {
            Glide.clear(li_image)
            Glide.with(container.context)
                    .load(items[position].imageUrl)
                    .asBitmap()
                    .dontAnimate()
                    .error(R.drawable.ic_image_black_48dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(li_image)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val name = "transition_name:$position"
                li_image.transitionName = name
                li_image.tag = name
                if (position == current) {
                    onStartPostTransition.invoke(li_image)
                }
            }
            container.addView(layout)
        }
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

}