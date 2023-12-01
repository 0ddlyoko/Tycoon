package me.oddlyoko.tycoon.item

import cn.nukkit.entity.item.EntityItem
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.util.CustomBigNumber

data class Item(
    val id: String,
    val name: String,
    val type: String,
    val damage: CustomBigNumber,
    val rarity: ItemRarity,
) {

    fun get(): cn.nukkit.item.Item {
        val nItem = cn.nukkit.item.Item.fromString(id)
        nItem.customName = "$name ($damage ❤)"
        val namedTag = nItem.orCreateNamedTag
        namedTag.putBoolean("tycoon", true)
        namedTag.putInt("tycoon_item_damage", damage.number)
        namedTag.putInt("tycoon_item_multiplicator", damage.multiplicator)
        namedTag.putString("tycoon_item", id)
        namedTag.putString("tycoon_uuid", Tycoon.RANDOM_UUID)
        nItem.namedTag = namedTag
        return nItem
    }

    fun spawn(location: Location, pickupDelay: Int = 10): EntityItem? {
        val theItem = get()
        return location.level.dropAndGetItem(location, theItem, location.directionVector, pickupDelay)
    }
}
