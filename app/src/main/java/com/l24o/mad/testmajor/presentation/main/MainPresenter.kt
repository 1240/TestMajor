package com.l24o.mad.testmajor.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.l24o.mad.testmajor.domain.usecase.ImagesUseCase
import com.l24o.mad.testmajor.extension.schedulersIoToMain

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    private val items = mutableListOf<ImageListItem>()

    init {
        viewState.setLoadingVisible(true)
        ImagesUseCase.getImages()
                .map { images ->
                    images.map { ImageListItem(it, ::onItemClicked) }
                }
                .schedulersIoToMain()
                .subscribe({
                    items.clear()
                    items.addAll(it)
                    viewState.setLoadingVisible(false)
                    viewState.showImages(items)
                }, {
                    viewState.setLoadingVisible(false)
                    viewState.showError(it.localizedMessage)
                })
    }

    private fun onItemClicked(item: ImageListItem, posititon: Int) {
        item.image?.let { image ->
            ImagesUseCase.setViews(items.mapNotNull { it.image })
            ImagesUseCase.enterPosition = posititon
            viewState.navigateToImage(
                    items = items,
                    position = posititon,
                    image = image)
            viewState.setCallback(
                    exitPosition = ImagesUseCase.exitPosition,
                    enterPosition = ImagesUseCase.enterPosition,
                    sharedViews = ImagesUseCase.sharedViews
            )
        }
    }

    fun onItemExit(exitPosition: Int) {
        ImagesUseCase.exitPosition = exitPosition

    }

    fun onMapSharedElements() {
        ImagesUseCase.sharedViews.clear()
    }

}
