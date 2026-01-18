package com.kucingoyen.dashboard.screen

import com.kucingoyen.core.base.BaseViewModel
import com.kucingoyen.core.utils.ViewModelUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    viewModelUtils: ViewModelUtils,
) : BaseViewModel(viewModelUtils) {

    private val _bottomBarSelected = MutableStateFlow(0)
    val bottomBarSelected: StateFlow<Int> = _bottomBarSelected.asStateFlow()

    fun updateBottomBarSelected(value: Int) {
        _bottomBarSelected.value = value
    }

}