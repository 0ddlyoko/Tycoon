package me.oddlyoko.tycoon.player

import me.oddlyoko.tycoon.player.external.ExternalPlayerManager
import java.util.*
import java.util.concurrent.CompletableFuture

object PlayerManager {
    val players: HashMap<UUID, Player> = hashMapOf()

    fun registerPlayer(playerUuid: UUID): CompletableFuture<Player> {
        return ExternalPlayerManager.INSTANCE.getPlayer(playerUuid).thenApply {
            players[it.uuid] = it
            it
        }
    }

    fun unregisterPlayer(playerUuid: UUID) {
        val player = players[playerUuid] ?: return
        ExternalPlayerManager.INSTANCE.savePlayer(player)
        players.remove(playerUuid)
    }

    fun save(player: Player): CompletableFuture<Void> = ExternalPlayerManager.INSTANCE.savePlayer(player)

    operator fun get(playerUuid: UUID): Player? = players[playerUuid]
}
