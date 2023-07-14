package com.yunext.twins.base

sealed interface UiState<out REQ> {
    val req: REQ
}

class Start<REQ>(override val req: REQ) : UiState<REQ>
class Processing<REQ>(override val req: REQ) : UiState<REQ>
sealed class End<REQ, RESP>(override val req: REQ, val resp: RESP) : UiState<REQ> {
    class Success<REQ, RESP>(req: REQ, resp: RESP) : End<REQ, RESP>(req, resp)
    class Fail<REQ>(req: REQ, error: Throwable) : End<REQ, Throwable>(req, error)
}
