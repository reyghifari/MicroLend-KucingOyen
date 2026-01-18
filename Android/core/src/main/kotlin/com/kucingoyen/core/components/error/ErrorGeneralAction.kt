package com.kucingoyen.core.components.error

import com.kucingoyen.entity.model.ErrorModelData

object ErrorGeneralAction {
    private var showError: (Boolean, ErrorModelData?, (ErrorModelData) -> Unit) -> Unit =
        { _, _, _ -> }

    fun init(
        showHandler: (Boolean, ErrorModelData?, (ErrorModelData) -> Unit) -> Unit,
    ) {
        showError = showHandler
    }

    fun show(
        error: ErrorModelData? = null,
        handleOnError: (ErrorModelData) -> Unit = {},
    ) {
        showError(true, error) { errorData ->
            handleOnError(errorData)
        }
    }
}
