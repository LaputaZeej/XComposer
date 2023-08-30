//package com.yunext.twins.module.repository
//
//
//@Deprecated("见数据库实现",replaceWith = ReplaceWith("DbLogRepositoryImpl"))
//class LogRepositoryImpl @Inject constructor() : LogRepository,
//    ILogger by DefaultLogger("LogRepository") {
//
//    private val mList: CopyOnWriteArrayList<Log> = CopyOnWriteArrayList()
//
//    override suspend fun add(log: Log): Boolean {
//        return coroutineScope {
//            mList.add(log).apply {
//                ld("#${this@LogRepositoryImpl}# add ${log.display} $this size=${mList.size}")
//            }
//        }
//    }
//
//    override suspend fun findAll(): List<Log> {
//        return coroutineScope {
//            mList.toList().apply {
//                ld("findAll size = ${this.size}")
//            }
//        }
//    }
//
//    override suspend fun deleteALl(deviceId: String): Boolean {
//        return coroutineScope {
//            mList.clear()
//            ld("deleteAll")
//            true
//        }
//    }
//
//    override suspend fun findSearch(
//        deviceId: String,
//        sign: DbDatasource.Sign,
//        search: String?,
//        start: Long,
//        end: Long,
//        pageNumber: Int,
//        pageSize: Int
//    ): List<Log> {
//        return coroutineScope {
//            mList.toList().asSequence().filter {
//                deviceId == it.deviceId
//            }
//                .filter {
//                    when {
//                        start > 0 && end > 0 -> it.timestamp in start..end
//                        start > 0 && end <= 0 -> it.timestamp >= start
//                        start <= 0 && end > 0 -> it.timestamp <= end
//                        else -> true
//                    }
//                }
//                .filter {
//                    if (search.isNullOrEmpty()) true else {
//                        when (it) {
//                            is DownLog -> {
//                                it.topic.contains(search, true) or
//                                        it.cmd.contains(search, true) or
//                                        it.clientId.contains(search, true) or
//                                        it.payload.contains(search, true)
//                            }
//                            is OnlineLog -> {
//                                true
//                            }
//                            is UpLog -> {
//                                it.topic.contains(search, true) or
//                                        it.cmd.contains(search, true) or
//                                        it.clientId.contains(search, true) or
//                                        it.payload.contains(search, true)
//                            }
//                        }
//                    }
//                }.toList()
//        }.apply {
//            ld("find size = ${this.size}")
//        }
//
//    }
//}
