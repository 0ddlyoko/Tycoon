package me.oddlyoko.tycoon.item.items

import cn.nukkit.entity.item.EntityItem
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.item.ItemRarity
import me.oddlyoko.tycoon.player.items.PlayerItem

interface Item {
    fun id(): String
    fun name(): String
    fun type(): String
    fun rarity(): ItemRarity

    fun get(): cn.nukkit.item.Item {
        val nItem = cn.nukkit.item.Item.fromString(id())
        nItem.customName = name()
        val namedTag = nItem.orCreateNamedTag
        namedTag.putBoolean("tycoon", true)
        namedTag.putString("tycoon_item", id())
        namedTag.putString("tycoon_uuid", Tycoon.RANDOM_UUID)
        nItem.namedTag = namedTag
        return nItem
    }

    fun spawn(location: Location, pickupDelay: Int = 10): EntityItem? {
        val theItem = get()
        return location.level.dropAndGetItem(location, theItem, location.directionVector, pickupDelay)
    }

    fun toPlayerItem(): PlayerItem
}
