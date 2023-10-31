package com.yunext.twins.base.mvi

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface Store {
    val storeScope: CoroutineScope
    fun onCleared()
}

abstract class AbsMVIStore(final override val storeScope: CoroutineScope) : Store {
    override fun onCleared() {
        storeScope.cancel()
    }
}

/**
 *
 *
 * 这种方法的思想是，我们将拥有一组中间件，每个中间件负责应用程序的一部分业务逻辑；
 * 每个中间件将观察来自MVI管道的操作，当它负责的操作被发射时，它将启动异步操作。
 * 在一个大型应用程序中，我们可以将屏幕分成几个部分，由各自独立的中间件处理，或者我们可以根据它们执行的业务逻辑来分离中间件。
 * 思想是拥有小而精细的中间件，每个中间件只执行一个或一小组操作，而不是一个处理所有异步工作的大型中间件。
 *
 * @param reducer 减速器
 * @param middlewares MviViewModel接收一个中间件列表，默认为空列表，因为并不是所有屏幕都有异步工作。
 * @param initialState 初始状态
 *
 */
open class MVIStore<S : MVIState, A : MVIAction>(
    private  val reducer: MVIReducer<S, A>,
    private  val middlewares: List<MVIMiddleware<S, A>> = emptyList(),
    storeScope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineName("store")),
    initialState: S,
) : AbsMVIStore(storeScope), MVIDispatcher<A> {

    /**
     * 定义了一个包装器类，它包装了当前状态和操作，我们将其推送到管道上，以便在接收操作时拥有状态的副本。
     */
    private data class ActionImpl<S : MVIState, A : MVIAction>(val state: S, val action: A)

    private val actions = MutableSharedFlow<ActionImpl<S, A>>(extraBufferCapacity = BufferSize)

    var state: MutableStateFlow<S> = MutableStateFlow(initialState)
        private set

    init {
        // 循环遍历中间件，并为每个中间件设置调度器，即ViewModel本身
        middlewares.forEach { middlewares ->
            middlewares.setDispatcher(this)
        }

        // 启动一个协程来观察MutableSharedFlow的Action和State的发射
        storeScope.launch {
            actions
                .onEach { actionImpl ->

                    // 对于每个发射，我们遍历所有中间件
                    middlewares.forEach { middleware ->
                        // reducer更新状态
                        val newState =
                            reducer.reduce(state = state.value, action = actionImpl.action)
                        if (newState != null) {
                            state.value = newState
                        }
                        // 对于每个中间件，我们调用其process方法来处理操作。
                        try {
                            if (actionImpl.action.type == MVIAction.Type.Async) {
                                middleware.process(actionImpl.state, actionImpl.action, storeScope)
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }

                    if (actionImpl.action.type == MVIAction.Type.Sync) {
                        val newState = reducer.reduce(state = state.value, action = actionImpl.action)
                            ?: return@onEach
                        state.value = newState
                    }
                }
                .collect()
        }

//        viewModelScope.launch {
//            actions.collect { actionImpl ->
//                val newState = reducer.reduce(state = state.value, action = actionImpl.action)?:return@collect
//                state.value = newState
//            }
//        }
    }

    /**
     * 发射操作
     */
    override fun dispatch(action: A) {
        val success = actions.tryEmit(ActionImpl(state.value, action))
        println("dispatch : $action")
        if (!success) error("MVI action buffer overflow >$BufferSize")
    }

    override fun onCleared() {
        super.onCleared()
        middlewares.forEach {
            it.onCleared()
        }
    }

    companion object {
        internal const val BufferSize = 64
    }
}

