package com.yunext.twins.base

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface Store<State, Action, Effect> {
    val state: StateFlow<State>
    val effect: SharedFlow<Effect>
    fun dispatch(action: Action)

    fun onClear()
}

typealias Reducer<State, Action> = suspend (State, Action) -> State

typealias PostProcessor<State, Action, Effect> = (State, Action, Effect) -> Action?

abstract class AbsStore<State, Action, Effect>(
    initializer: State,
    private val reducer: Reducer<State, Action>? = null,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineName("AbsStore")),
) : Store<State, Action, Effect>, CoroutineScope by scope {

    private val mutableState: MutableStateFlow<State> = MutableStateFlow(initializer)

    private val mutableEffect: MutableSharedFlow<Effect> = MutableSharedFlow()

    override val state: StateFlow<State>
        get() = mutableState

    override val effect: SharedFlow<Effect>
        get() = mutableEffect

    suspend fun effect(effect: Effect) {
        mutableEffect.emit(effect)
    }

    suspend fun state(newState: State) {
        mutableState.update {
            newState
        }
    }

    suspend fun state(stateBlock: (State) -> State) {
        mutableState.update {
            stateBlock(this.state.value)
        }
    }


    suspend fun state(action: Action, reducer: Reducer<State, Action>) {
        mutableState.update {
            reducer.invoke(state.value, action)
        }
    }

//    override fun dispatch(action: Action) {
//        launch {
//            try {
//
//                mutableState.update {
//                    WrapperReducer(reducer).invoke(state.value, action)
//                }
//            } catch (e: Throwable) {
//            }finally {
//
//            }
//
//        }
//    }

    private class WrapperReducer<State, Action>(private val reducer: Reducer<State, Action>) :
        Reducer<State, Action> {
        override suspend fun invoke(p1: State, p2: Action): State {
            return reducer.invoke(p1, p2)
        }
    }

    override fun onClear() {
        cancel()
    }
}
//
//class MyStore(init: MyState = MyState(listOf()), reducer: MyReducer = MyReducer()) :
//    AbsStore<MyStore.MyState, MyStore.MyAction, MyStore.MyEffect>(
//        initializer = init,
//        reducer = reducer
//    ) {
//    data class MyState(val list: List<Int>)
//    sealed class MyAction {
//        object Add : MyAction()
//        object Delete : MyAction()
//    }
//
//    sealed class MyEffect {
//        object Loading : MyEffect()
//        object Toast : MyEffect()
//    }
//
//    class MyReducer : Reducer<MyState, MyAction> {
//        override suspend fun invoke(state: MyState, action: MyAction): MyState {
//            return when (action) {
//                MyAction.Add -> state.copy(list = state.list + 1)
//                MyAction.Delete -> state.copy(list = state.list - state.list[0])
//            }
//        }
//
//    }
//}


sealed interface UiState<out REQ, out RESP> {
    val req: REQ

    class Start<REQ>(override val req: REQ) : UiState<REQ, Nothing>
    class Processing<REQ>(override val req: REQ, val progress: Int = 0, val max: Int = 100) :
        UiState<REQ, Nothing>

    class Fail<REQ>(override val req: REQ, val error: Throwable) : UiState<REQ, Nothing>
    class Success<REQ, RESP>(override val req: REQ, val resp: RESP) : UiState<REQ, RESP>
}

fun UiState<*, *>.processing() = this is UiState.Processing<*>


//sealed class End<REQ, RESP>(override val req: REQ, val resp: RESP) : UiState<REQ,RESP> {
//    class Success<REQ, RESP>(req: REQ, resp: RESP) : End<REQ, RESP>(req, resp)
//    class Fail<REQ>(req: REQ, error: Throwable) : End<REQ, Throwable>(req, error)
//}
