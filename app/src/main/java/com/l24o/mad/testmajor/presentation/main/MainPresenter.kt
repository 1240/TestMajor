package com.l24o.mad.testmajor.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.l24o.mad.testmajor.domain.usecase.ImagesUseCase
import com.l24o.mad.testmajor.extension.schedulersIoToMain
import com.l24o.mad.testmajor.utils.ErrorHandler

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    private val items = mutableListOf<ImageListItem>()

    init {
        viewState.setLoadingVisible(true)
        fetch()
    }

    private fun fetch() {
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
                    processError(it)
                })
    }

    private fun processError(error: Throwable) {
        ErrorHandler.processError(
                throwable = error,
                onErrorText = { errorText ->
                    viewState.showError(errorText)
                }
        )
    }

    private fun onItemClicked(item: ImageListItem, position: Int) {
        item.image?.let { image ->
            ImagesUseCase.setViews(items.mapNotNull { it.image })
            ImagesUseCase.enterPosition = position
            viewState.navigateToImage(
                    items = items,
                    position = position,
                    image = image
            )
            viewState.setCallback(
                    enterPosition = ImagesUseCase.enterPosition,
                    sharedViews = ImagesUseCase.sharedViews
            )
        }
    }

    fun onMapSharedElements() {
        ImagesUseCase.sharedViews.clear()
    }

    fun refresh() {
        fetch()
    }

}
