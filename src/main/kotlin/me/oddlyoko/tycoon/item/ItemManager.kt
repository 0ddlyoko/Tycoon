package me.oddlyoko.tycoon.item

import me.oddlyoko.tycoon.item.types.ItemType

object ItemManager {
    val itemTypes: MutableMap<String, ItemType> = HashMap()
    val items: MutableMap<String, Item> = HashMap()

    fun registerItemType(type: String, itemType: ItemType) {
        itemTypes[type] = itemType
    }

    fun getItemType(type: String): ItemType? {
        return itemTypes[type]
    }

    fun registerItem(item: Item) {
        items[item.id] = item
    }

    fun getItem(id: String): Item? {
        return items[id]
    }
}
