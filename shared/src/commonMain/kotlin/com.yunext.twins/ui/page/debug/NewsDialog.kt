package com.yunext.twins.ui.page.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunext.twins.base.AbsStore
import com.yunext.twins.base.UiState
import com.yunext.twins.module.db.datasource.NewsDatasource
import com.yunext.twins.module.repository.NewsRepository
import com.yunext.twins.module.repository.RootComponent
import com.yunext.twins.module.repository.impl.NewsRepositoryImpl
import com.yunext.twins.shared.cache.News
import com.yunext.twins.ui.compoents.China
import com.yunext.twins.ui.compoents.Debug
import com.yunext.twins.ui.compoents.dialog.CHDialog
import com.yunext.twins.util.randomText
import kotlinx.coroutines.launch
import kotlin.random.Random

class NewsDialogStore(private val newsRepository: NewsRepository) :
    AbsStore<NewsDialogStore.State, NewsDialogStore.Action, NewsDialogStore.Effect>(State(listOf())) {
    data class State(val news: List<News>, val loading: Boolean = false)
    sealed class Action {
        object Clear : Action()
        data class Add(val news: News) : Action()
        data class Init(val news: List<News>) : Action()
        object Select : Action()
    }

    class Effect(val ui: UiState<*, *>)


    override fun dispatch(action: Action) {
        launch {
            when (action) {
                is Action.Add -> {
                    try {
                        debugPrintln("add ...")
                        effect(Effect(UiState.Start(Unit)))
                        state {
                            it.copy(loading = true)
                        }
                        newsRepository.add(action.news)
                        debugPrintln("add success")
                        val list = newsRepository.list()
                        state {
                            it.copy(news = list)
                        }
                        effect(Effect(UiState.Success(Unit, list)))
                    } catch (e: Exception) {
                        debugPrintln("add error $e")
                        effect(Effect(UiState.Fail(Unit, e)))
                    } finally {
                        state {
                            it.copy(loading = false)
                        }
                    }
                }

                Action.Clear -> {
                    try {
                        debugPrintln("clear ...")
                        effect(Effect(UiState.Start(Unit)))
                        state {
                            it.copy(loading = true)
                        }
                        newsRepository.clear()
                        debugPrintln("clear success")
                        val list = newsRepository.list()
                        state {
                            it.copy(news = list)
                        }
                        effect(Effect(UiState.Success(Unit, list)))
                    } catch (e: Exception) {
                        effect(Effect(UiState.Fail(Unit, e)))
                        debugPrintln("clear error $e")
                    } finally {
                        state {
                            it.copy(loading = false)
                        }
                    }
                }

                is Action.Init -> {
                    try {
                        effect(Effect(UiState.Start(Unit)))
                        state {
                            it.copy(loading = true)
                        }
                        debugPrintln("init ...")
                        newsRepository.addAll(action.news)
                        debugPrintln("init success")
                        val list = newsRepository.list()
                        state {
                            it.copy(news = list)
                        }
                        effect(Effect(UiState.Success(Unit, list)))
                    } catch (e: Exception) {
                        debugPrintln("clear error $e")
                        effect(Effect(UiState.Fail(Unit, e)))
                    } finally {
                        state {
                            it.copy(loading = false)
                        }
                    }
                }

                Action.Select -> {
                    try {
                        debugPrintln("list ...")
                        effect(Effect(UiState.Start(Unit)))
                        state {
                            it.copy(loading = true)
                        }


                        val list = newsRepository.list()
                        debugPrintln("list success ${list.size}")
                        state {
                            it.copy(news = list)
                        }
                        effect(Effect(UiState.Success(Unit, list)))
                    } catch (e: Exception) {
                        debugPrintln("list error $e ")
                        effect(Effect(UiState.Fail(Unit, e)))
                    } finally {
                        state {
                            it.copy(loading = false)
                        }
                    }
                }
            }
        }

    }
}

private fun debugPrintln(msg: String) {
    println("[xpl]$msg")
}


private val newsDialogStore = NewsDialogStore(RootComponent.rootComponent.newsRepository)

private fun randomNews() = News(
    id = Random.nextLong(), title = randomText(4), summary = randomText(6),
    date = randomText(5), imageUrl = randomText(7)
)


@Composable
fun NewsDialog(
    dimAmount: Float = .1f,
    onDismissRequest: () -> Unit = {},
) {
    Debug("TwinsHomePage-内容-弹窗1")
    CHDialog(dimAmount = dimAmount, onDismissRequest = onDismissRequest) {


        val state: NewsDialogStore.State by newsDialogStore.state.collectAsState()

        val actions by remember {
            mutableStateOf(
                listOf(
                    DebugAction("查询") { newsDialogStore.dispatch(NewsDialogStore.Action.Select) },
                    DebugAction("添加") {
                        newsDialogStore.dispatch(
                            NewsDialogStore.Action.Add(
                                randomNews()
                            )
                        )
                    },
                    DebugAction("清空") { newsDialogStore.dispatch(NewsDialogStore.Action.Clear) },
                )
            )
        }

        Column(modifier = Modifier.padding(16.dp).heightIn(min = 200.dp, max = 400.dp).fillMaxWidth().background(China.hui_xiao_hui)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(actions, key = { it.toString() }) {
                    Button({ it.action() }) {
                        Text(it.name)
                    }
                }
            }
            Text(
                state.news.joinToString("\n") { it.toString() },
                color = China.r_yan_zhi_hong,
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )

                    .padding(12.dp).background(China.g_zhu_lv)
            )

        }
    }

}

private class DebugAction(val name: String, val action: () -> Unit)