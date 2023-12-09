package me.oddlyoko.tycoon.player

import me.oddlyoko.tycoon.player.items.PlayerItem
import me.oddlyoko.tycoon.player.items.PlayerSwordItem
import java.util.*
import java.util.concurrent.CompletableFuture

class Player(
    val uuid: UUID,
    val items: MutableList<PlayerItem> = mutableListOf()
) {
    val swords: List<PlayerSwordItem>
        get() = items.filterIsInstance<PlayerSwordItem>()

    fun addItem(playerItem: PlayerItem) {
        items.add(playerItem)
        save()
    }

    fun save(): CompletableFuture<Void> = PlayerManager.save(this)
}
