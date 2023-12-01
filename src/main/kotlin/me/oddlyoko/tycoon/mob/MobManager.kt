package me.oddlyoko.tycoon.mob

object MobManager {
    val mobs: MutableMap<String, Mob> = HashMap()

    fun registerMob(mob: Mob) {
        mobs[mob.id] = mob
        mob.zone.mobs.add(mob)
    }

    fun getMob(id: String): Mob? {
        return mobs[id]
    }
}
