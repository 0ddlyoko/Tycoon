package me.oddlyoko.tycoon.mob

import me.oddlyoko.tycoon.mob.types.MobType

object MobManager {
    val mobTypes: MutableMap<String, MobType> = HashMap()
    val mobs: MutableMap<String, Mob> = HashMap()

    fun registerMobType(type: String, mobType: MobType) {
        mobTypes[type] = mobType
    }

    fun getMobType(type: String): MobType? {
        return mobTypes[type]
    }

    fun registerMob(mob: Mob) {
        mobs[mob.id] = mob
        mob.zone.mobs.add(mob)
    }

    fun getMob(id: String): Mob? {
        return mobs[id]
    }
}
