package me.oddlyoko.tycoon.item

object ItemManager {
    val items: MutableMap<String, Item> = HashMap()

    fun registerItem(item: Item) {
        items[item.id] = item
    }

    fun getItem(id: String): Item? {
        return items[id]
    }
}
