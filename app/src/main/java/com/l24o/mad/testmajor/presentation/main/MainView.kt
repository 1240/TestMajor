package com.l24o.mad.testmajor.presentation.main

import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.xwray.groupie.Group

interface MainView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showImages(items: List<Group>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setLoadingVisible(isVisible: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToImage(items: List<ImageListItem>, position: Int, image: ImageView)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(errorText: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setCallback(enterPosition: Int, sharedViews: List<View>)
}