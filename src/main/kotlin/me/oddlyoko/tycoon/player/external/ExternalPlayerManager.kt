package me.oddlyoko.tycoon.player.external

import me.oddlyoko.tycoon.player.Player
import java.util.*
import java.util.concurrent.CompletableFuture

interface ExternalPlayerManager {
    /**
     * Save specific player into an external resource
     */
    fun savePlayer(player: Player): CompletableFuture<Void>

    /**
     * Retrieve specific player from an external resource
     */
    fun getPlayer(playerUuid: UUID): CompletableFuture<Player>

    companion object {
        val INSTANCE: ExternalPlayerManager = ExternalMemoryPlayerManager
    }
}
