package me.oddlyoko.tycoon.mob

import cn.nukkit.entity.Entity
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.mob.types.MobType
import me.oddlyoko.tycoon.util.CustomBigNumber
import me.oddlyoko.tycoon.zone.Zone

data class Mob(
    val id: String,
    val zone: Zone,
    val name: String,
    val type: MobType,
    val probability: Int,
    val maxAmount: Int,
    val health: CustomBigNumber,
    var currentMobCount: Int = 0,
) {
    fun spawn(location: Location): Entity? {
        val entity = type.spawn(location, this) ?: return null
        currentMobCount++
        return entity
    }
}
