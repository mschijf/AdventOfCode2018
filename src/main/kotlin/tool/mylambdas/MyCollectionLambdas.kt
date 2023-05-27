package com.tool.mylambdas

fun <T> Iterable<T>.splitByCondition(predicate: (T) -> Boolean): List<List<T>> {
    val result = ArrayList<List<T>>()
    var tmp = mutableListOf<T>()
    this.forEach {
        if (predicate(it)) {
            result.add(tmp)
            tmp = mutableListOf()
        } else {
            tmp.add(it)
        }
    }
    result.add(tmp)
    return result
}

inline fun <S, T> List<T>.mapCombinedAll(combineOperation: (T, T) -> S): List<S> {
    var result = mutableListOf<S>()
    for (i in 0 until this.size-1) {
        for (j in i+1 until this.size) {
            result += combineOperation(this[i], this[j])
        }
    }
    return result
}
