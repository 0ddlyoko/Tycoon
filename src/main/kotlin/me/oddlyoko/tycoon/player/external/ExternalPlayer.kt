package me.oddlyoko.tycoon.player.external

import me.oddlyoko.tycoon.player.external.items.ExternalSwordItem
import java.util.*

data class ExternalPlayer(
    val uuid: UUID,
    val swords: MutableList<ExternalSwordItem> = mutableListOf()
)
