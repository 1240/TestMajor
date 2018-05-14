package com.l24o.mad.testmajor.presentation.main

import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.l24o.mad.testmajor.R
import com.l24o.mad.testmajor.domain.entity.ImageItem
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.li_main_image.view.*

class ImageListItem(
        val imageItem: ImageItem,
        private val onClickListener: (ImageListItem, Int) -> Unit
) : Item<ViewHolder>() {
    var image: ImageView? = null
    override fun getLayout() = R.layout.li_main_image

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder.itemView) {
//            li_main_image_thumbnail.tag = position
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                li_main_image_thumbnail?.transitionName = context.getString(R.string.transition_name, position, position)
            }
            Glide.clear(li_main_image_thumbnail)
            Glide.with(context)
                    .load(imageItem.imageUrl)
                    .dontAnimate()
                    .placeholder(R.drawable.ic_image_black_48dp)
                    .error(R.drawable.ic_broken_image_black_48dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(li_main_image_thumbnail)
            li_main_image_title.text = imageItem.title
            setOnClickListener { onClickListener.invoke(this@ImageListItem, position) }
            image = li_main_image_thumbnail
        }
    }

}