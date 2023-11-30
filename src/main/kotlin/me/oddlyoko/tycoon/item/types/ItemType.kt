package me.oddlyoko.tycoon.item.types

import cn.nukkit.entity.item.EntityItem
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.item.Item

interface ItemType {
    fun id(): String

    fun name(): String

    fun get(item: Item): cn.nukkit.item.Item {
        val nItem = cn.nukkit.item.Item.fromString(id())
        nItem.customName = "${name()} (${item.damage} ❤)"
        nItem.namedTag.putBoolean("tycoon", true)
        nItem.namedTag.putInt("tycoon_damage_multiplicator", item.damage.multiplicator)
        nItem.namedTag.putString("tycoon_item", item.id)
        nItem.namedTag.putString("tycoon_uuid", Tycoon.RANDOM_UUID)
        return nItem
    }

    fun spawn(location: Location, item: Item, pickupDelay: Int = 10): EntityItem? {
        val theItem = get(item)
        return location.level.dropAndGetItem(location, theItem, location.directionVector, pickupDelay)
    }
}
