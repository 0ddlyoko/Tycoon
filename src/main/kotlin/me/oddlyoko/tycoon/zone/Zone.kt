package me.oddlyoko.tycoon.zone

import cn.nukkit.entity.Entity
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.mob.Mob
import kotlin.random.Random

data class Zone(
    val id: String,
    val name: String,
    val maxMobCount: Int,
    val startPos: Location,
    val endPos: Location,
    val mobs: MutableList<Mob> = mutableListOf(),
    var currentMobCount: Int = 0,
) {

    fun isInZone(location: Location): Boolean {
        if (location.level == null && startPos.level != null)
            return false
        if (location.level != startPos.level)
            return false
        return location.x >= startPos.x && location.x <= endPos.x &&
                location.y >= startPos.y && location.y <= endPos.y &&
                location.z >= startPos.z && location.z <= endPos.z
    }

    fun spawn(): Entity? {
        val location = Location(
            startPos.x + (endPos.x - startPos.x) * Math.random(),
            startPos.y + (endPos.y - startPos.y) * Math.random(),
            startPos.z + (endPos.z - startPos.z) * Math.random(),
            startPos.level
        )
        val mobToSpawn = chooseMobToSpawn() ?: return null
        val entity = mobToSpawn.spawn(location) ?: return null
        currentMobCount++
        return entity
    }

    private fun chooseMobToSpawn(): Mob? {
        val mobs = mobs.filter { it.currentMobCount < it.maxAmount }
        if (mobs.isEmpty())
            return null
        val maxValue = mobs.sumOf { it.probability }
        val randomValue = Random.nextInt(maxValue + 1)

        var acc = 0
        var mob: Mob?

        val remainingMobs = mobs.map { Pair(it.probability, it) }.sortedBy { -it.first }
        val itr = remainingMobs.iterator()
        do {
            val pair = itr.next()
            mob = pair.second
            acc += pair.first
        } while (randomValue > acc)
        return mob
    }
}
