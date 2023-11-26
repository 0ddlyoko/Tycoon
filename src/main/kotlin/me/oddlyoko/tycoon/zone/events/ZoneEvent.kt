package me.oddlyoko.tycoon.zone.events

import cn.nukkit.Player
import cn.nukkit.event.player.PlayerEvent
import me.oddlyoko.tycoon.zone.Zone

abstract class ZoneEvent(
    player: Player,
    val zone: Zone,
): PlayerEvent() {
    init {
        this.player = player
    }
}
