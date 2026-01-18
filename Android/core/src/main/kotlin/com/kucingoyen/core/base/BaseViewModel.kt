package com.kucingoyen.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kucingoyen.core.utils.ErrorModel
import com.kucingoyen.core.utils.Event
import com.kucingoyen.core.utils.ViewModelUtils
import com.kucingoyen.core.utils.loading.LoadingAction
import kotlinx.coroutines.CoroutineExceptionHandler

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
abstract class BaseViewModel(
    private val utils: ViewModelUtils
) : ViewModel() {

    protected val dispatcher get() = utils.dispatcherProvider
    protected val errorParser get() = utils.errorParser

    protected val _errorLiveData = MutableLiveData<Event<ErrorModel>>()
    val errorLiveData = _errorLiveData.asLiveData()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    protected fun onException(throwable: Throwable) {
        throwable.printStackTrace()

        LoadingAction.show(false)

        errorParser.parseError(throwable).let {
            _errorLiveData.postValue(Event(it))
        }
    }

}

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>
