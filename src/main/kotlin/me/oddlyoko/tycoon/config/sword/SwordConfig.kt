package me.oddlyoko.tycoon.config.sword

import me.oddlyoko.tycoon.item.items.SwordItem
import me.oddlyoko.tycoon.util.CustomBigNumber

data class SwordConfig(
    val id: String,
    val name: String,
    val type: String,
    val damage: String,
    val rarity: ItemRarityConfig,
) {
    fun toSword(): SwordItem = SwordItem(
        id,
        name,
        type,
        rarity.toRarity(),
        CustomBigNumber.fromString(damage)!!,
    )
}
