package me.oddlyoko.tycoon.zone

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerMoveEvent
import cn.nukkit.event.player.PlayerTeleportEvent
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.zone.events.ZoneEnterEvent
import me.oddlyoko.tycoon.zone.events.ZoneExitEvent

object ZoneListener: Listener {

    private fun onPlayerMove(player: Player, from: Location, to: Location) {
        val worldId = player.level?.id ?: return
        for (zone in ZoneManager.getZones(worldId)) {
            if (zone.isInZone(to)) {
                // Player entered zone
                if (!zone.isInZone(from)) {
                    // Player wasn't in zone
                    val event = ZoneEnterEvent(player, zone)
                    Tycoon.INSTANCE.server.pluginManager.callEvent(event)
                }
            } else if (zone.isInZone(from)) {
                // Player left zone
                val event = ZoneExitEvent(player, zone)
                Tycoon.INSTANCE.server.pluginManager.callEvent(event)
            }
            player.location.level?.id
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        onPlayerMove(event.player, event.from, event.to)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        onPlayerMove(event.player, event.from, event.to)
    }
}
