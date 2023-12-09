package me.oddlyoko.tycoon.player.external

import me.oddlyoko.tycoon.item.ItemManager
import me.oddlyoko.tycoon.player.Player
import me.oddlyoko.tycoon.player.external.items.ExternalSwordItem
import me.oddlyoko.tycoon.player.items.PlayerSwordItem
import java.util.*
import java.util.concurrent.CompletableFuture

object ExternalMemoryPlayerManager: ExternalPlayerManager {
    val players: HashMap<UUID, ExternalPlayer> = hashMapOf()

    override fun savePlayer(player: Player): CompletableFuture<Void> {
        val externalPlayer = ExternalPlayer(player.uuid)
        player.swords.forEach { sword ->
            externalPlayer.swords.add(ExternalSwordItem(sword.item().id()))
        }
        players[player.uuid] = externalPlayer
        return CompletableFuture.completedFuture(null)
    }

    override fun getPlayer(playerUuid: UUID): CompletableFuture<Player> {
        val player = Player(playerUuid)
        val externalPlayer = players[playerUuid] ?: return CompletableFuture.completedFuture(player)
        externalPlayer.swords.forEach { sword ->
            val theSword = ItemManager.getSword(sword.item) ?: return CompletableFuture.completedFuture(player)
            player.items.add(PlayerSwordItem(theSword))
        }
        return CompletableFuture.completedFuture(player)
    }
}
