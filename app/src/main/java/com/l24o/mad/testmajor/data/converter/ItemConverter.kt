package com.l24o.mad.testmajor.data.converter

import com.l24o.mad.testmajor.data.entity.NWImageItem
import com.l24o.mad.testmajor.domain.entity.ImageItem

object ItemConverter {

    fun toDomain(source: NWImageItem): ImageItem {
        return ImageItem(
                id = source.id,
                title = source.title,
                imageUrl = source.imageUrl
        )
    }

}