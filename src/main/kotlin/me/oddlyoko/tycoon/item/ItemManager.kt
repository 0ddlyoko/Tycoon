package me.oddlyoko.tycoon.item

import me.oddlyoko.tycoon.item.items.Item
import me.oddlyoko.tycoon.item.items.SwordItem

object ItemManager {
    val items: MutableMap<String, Item> = mutableMapOf()
    val swords: MutableMap<String, SwordItem> = mutableMapOf()

    fun registerItem(item: Item) {
        items[item.id()] = item
        when (item) {
            is SwordItem -> swords[item.id()] = item
        }
    }

    fun getItem(id: String): Item? {
        return items[id]
    }

    fun getSwords(): List<SwordItem> = swords.values.toList()

    fun getSword(id: String): SwordItem? = swords[id]
}
