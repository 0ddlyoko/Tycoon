package me.oddlyoko.tycoon.item.items

import me.oddlyoko.tycoon.item.ItemRarity
import me.oddlyoko.tycoon.player.items.PlayerSwordItem
import me.oddlyoko.tycoon.util.CustomBigNumber

data class SwordItem(
    private val id: String,
    private val name: String,
    private val type: String,
    private val rarity: ItemRarity,
    private val damage: CustomBigNumber,
): Item {

    override fun id(): String = id

    override fun name(): String = name

    override fun type(): String = type

    override fun rarity(): ItemRarity = rarity

    override fun get(): cn.nukkit.item.Item {
        val nItem = super.get()
        nItem.customName = "${name()} ($damage ❤)"
        val namedTag = nItem.orCreateNamedTag
        namedTag.putInt("tycoon_item_damage", damage.number)
        namedTag.putInt("tycoon_item_multiplicator", damage.multiplicator)
        return nItem
    }

    override fun toPlayerItem(): PlayerSwordItem = PlayerSwordItem(this)
}
