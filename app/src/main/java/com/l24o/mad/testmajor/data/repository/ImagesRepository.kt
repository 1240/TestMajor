package com.l24o.mad.testmajor.data.repository

import com.l24o.mad.testmajor.data.converter.ItemConverter
import com.l24o.mad.testmajor.data.network.AppApiHolder
import com.l24o.mad.testmajor.data.network.AppApiProvider
import com.l24o.mad.testmajor.domain.entity.ImageItem
import io.reactivex.Single

object ImagesRepository {
    private val apiProvider: AppApiProvider = AppApiHolder()

    fun getImageList(): Single<List<ImageItem>> {
        return apiProvider
                .provide()
                .imageList()
                .map { it.map { ItemConverter.toDomain(it) } }

    }
}