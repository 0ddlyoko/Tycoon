package me.oddlyoko.tycoon.mob

import cn.nukkit.entity.Entity
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.util.CustomBigNumber
import me.oddlyoko.tycoon.zone.Zone

data class Mob(
    val id: String,
    val zone: Zone,
    val name: String,
    val type: String,
    val probability: Int,
    val maxAmount: Int,
    val health: CustomBigNumber,
    var currentMobCount: Int = 0,
) {

    fun spawn(location: Location): Entity? {
        val entity = Entity.createEntity(type, location) ?: return null
        entity.nameTag = "$name ($health ❤)"
        entity.isNameTagVisible = true
        entity.isNameTagAlwaysVisible = true
        entity.maxHealth = 20
        entity.health = 20f
        entity.namedTag.putBoolean("tycoon", true)
        entity.namedTag.putInt("tycoon_entity_health", health.number)
        entity.namedTag.putInt("tycoon_entity_health_multiplicator", health.multiplicator)
        entity.namedTag.putString("tycoon_mob", id)
        entity.namedTag.putString("tycoon_uuid", Tycoon.RANDOM_UUID)
        entity.spawnToAll()

        currentMobCount++
        return entity
    }
}
