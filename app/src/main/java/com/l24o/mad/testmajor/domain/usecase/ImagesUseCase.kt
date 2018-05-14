package com.l24o.mad.testmajor.domain.usecase

import android.view.View
import com.l24o.mad.testmajor.data.repository.ImagesRepository

object ImagesUseCase {


    var exitPosition: Int = 0
    var enterPosition: Int = 0
    var sharedViews = mutableListOf<View>()

    fun setViews(views: List<View>) {
        sharedViews.clear()
        sharedViews.addAll(views)
    }

    fun getImages() = ImagesRepository.getImageList()


}