package me.oddlyoko.tycoon.mob.types

import cn.nukkit.entity.Entity
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.mob.Mob

interface MobType {
    fun id(): String

    fun name(): String

    fun spawn(location: Location, mob: Mob): Entity? {
        val entity = Entity.createEntity(id(), location) ?: return null
        entity.nameTag = "${name()} (${mob.health} ❤)"
        entity.isNameTagVisible = true
        entity.isNameTagAlwaysVisible = true
        entity.maxHealth = mob.health.number
        entity.health = mob.health.number.toFloat()
        entity.namedTag.putBoolean("tycoon", true)
        entity.namedTag.putInt("tycoon_health_multiplicator", mob.health.multiplicator)
        entity.namedTag.putString("tycoon_mob", mob.id)
        entity.namedTag.putString("tycoon_uuid", Tycoon.RANDOM_UUID)
        entity.spawnToAll()
        return entity
    }
}
