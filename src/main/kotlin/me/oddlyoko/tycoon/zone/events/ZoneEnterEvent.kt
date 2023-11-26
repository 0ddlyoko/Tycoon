package me.oddlyoko.tycoon.zone.events

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import me.oddlyoko.tycoon.zone.Zone

class ZoneEnterEvent(
    player: Player,
    zone: Zone,
) : ZoneEvent(player, zone), Cancellable {

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlers(): HandlerList {
            return handlers
        }
    }
}
