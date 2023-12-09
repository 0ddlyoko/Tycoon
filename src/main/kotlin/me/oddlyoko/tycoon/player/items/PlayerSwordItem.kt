package me.oddlyoko.tycoon.player.items

import me.oddlyoko.tycoon.item.items.SwordItem

data class PlayerSwordItem(
    private val item: SwordItem,
): PlayerItem {
    override fun item(): SwordItem = item
}
