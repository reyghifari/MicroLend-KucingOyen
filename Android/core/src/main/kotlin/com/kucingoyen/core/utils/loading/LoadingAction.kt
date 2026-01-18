package com.kucingoyen.core.utils.loading

object LoadingAction {
    private var showLoading: ((Boolean) -> Unit) = {}

    fun init(showHandler: (Boolean) -> Unit) {
        showLoading = showHandler
    }

    fun show(isShow: Boolean) {
        showLoading(isShow)
    }
}
