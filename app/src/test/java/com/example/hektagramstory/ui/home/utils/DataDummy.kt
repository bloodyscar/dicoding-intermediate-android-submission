package com.example.hektagramstory.ui.home.utils

import com.example.hektagramstory.data.remote.response.ListStoryItem


object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "photoUrl + $i",
                "createdAt $i",
                "Name $i",
                "description $i",
                1,
                "ID $i",
                1
            )
            items.add(quote)
        }
        return items
    }
}