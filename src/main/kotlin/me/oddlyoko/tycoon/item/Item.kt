package me.oddlyoko.tycoon.item

import cn.nukkit.entity.item.EntityItem
import cn.nukkit.item.Item
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.item.types.ItemType
import me.oddlyoko.tycoon.util.CustomBigNumber

data class Item(
    val id: String,
    val name: String,
    val type: ItemType,
    val damage: CustomBigNumber,
    val rarity: ItemRarity,
) {
    fun get(): Item = type.get(this)

    fun spawn(location: Location, pickupDelay: Int = 10): EntityItem? = type.spawn(location, this, pickupDelay)
}
