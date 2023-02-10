package com.semba.pixabayimages.core.common.pagination

import com.semba.pixabayimages.core.common.Result

class DefaultPaginator<Key, Item> (
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend(items: List<Item>, newKey: Key) -> Unit
    ): Paginator<Item> {

    private var currentKey = initialKey
    private var isBusy = false

    override suspend fun loadNextItems() {
        if (isBusy) return

        isBusy = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isBusy = false

        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }

}