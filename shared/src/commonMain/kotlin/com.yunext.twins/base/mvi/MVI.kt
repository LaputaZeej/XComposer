package com.yunext.twins.base.mvi

import com.squareup.sqldelight.db.Closeable
import kotlinx.coroutines.CoroutineScope

// https://mp.weixin.qq.com/s/40nOdq_C2DYg0oVeb7mo5Q

/**
 * 状态
 */
interface MVIState

/**
 * 操作
 */
 interface MVIAction{
     val type:Type
        get() = Type.Sync
    enum class Type{
        Sync,Async;
    }
}



/**
 * 减速器将负责更新状态，它有一个单一的函数，该函数接受当前状态和操作，并生成一个新的状态。
 * 需要注意的是，减速器的reduce方法必须是纯函数——生成的状态只能依赖于输入的状态和操作，并
 * 且状态必须同步生成。
 */
interface MVIReducer<S : MVIState, A : MVIAction> {
    /**
     * @param state 当前状态
     * @param action 操作
     * @return 新的状态
     */
    fun reduce(state: S, action: A): S?
}

/**
 * 1. Middleware需要分发自己的Actions，因此我们定义了一个Dispatcher接口
 * 2. Middleware类是在State和Action上进行参数化的，与减速器类似。
 * 3. Middleware将接收Dispatcher以将操作推送到MVI管道
 * 4. 挂起的process方法是异步工作将要进行的地方。
 * 5. 这是一个将操作推送到MVI框架的实用方法，以便我们可以将Dispatcher保持私有。
 * 6. 最后，我们有一个方法用于初始化Middleware中使用的Dispatcher
 */
abstract class MVIMiddleware<S : MVIState, A : MVIAction>(
    vararg closeables: Closeable,

    ) {

    private lateinit var dispatcher: MVIDispatcher<A>

    private val closeables: Set<Closeable> = setOf(*closeables)

    //abstract suspend fun process(state: S, action: A)
    abstract fun process(state: S, action: A, coroutineScope: CoroutineScope)

    protected fun dispatch(action: A) = dispatcher.dispatch(action)

    internal fun setDispatcher(dispatcher: MVIDispatcher<A>) {
        this.dispatcher = dispatcher
    }

    internal fun onCleared() {
        closeables.forEach { closeable ->
            try {
                closeable.close()
            } catch (e: Exception) {
                e("[MVIMiddleware]closing Exception:[$closeable]")
            }
        }
    }
}

interface MVIDispatcher<A : MVIAction> {
    fun dispatch(action: A)
}

private fun d(msg: String) {
    println("[MVI] $msg")
}

private fun e(msg: String) {
    println("[MVI] $msg")
}