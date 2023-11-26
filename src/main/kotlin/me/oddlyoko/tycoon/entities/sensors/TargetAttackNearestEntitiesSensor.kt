package me.oddlyoko.tycoon.entities.sensors

import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityIntelligent
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.utils.SortedList
import java.util.function.Function

/**
 * Sensor that detect when an entity attacks another entity
 */
class TargetAttackNearestEntitiesSensor<T: Entity>(
    private val minRange: Double,
    private val maxRange: Double,
    private val period: Int = 1,
    private val memories: List<MemoryType<Entity>>,
    private val allTargetFunction: Array<Function<T, Boolean>> = emptyArray(),
): ISensor {
    override fun sense(entity: EntityIntelligent) {
        val minRangeSquared = minRange * minRange
        val maxRangeSquared = maxRange * maxRange

        if (allTargetFunction.isEmpty()) {
            if (memories.size != 1)
                return
            // Save all in first memory
            val currentMemory = memories[0]
            val current = entity.memoryStorage.get(currentMemory)
            // If there is already a target, he is still alive, and he is still in range, do not continue
            if (current != null && current.isAlive && current.distanceSquared(entity) in minRangeSquared..maxRangeSquared)
                return
            // Search for a new target
            val entities = entity.level.entities.filter {
                it.lastDamageCause != null
                        && it.lastDamageCause is EntityDamageByEntityEvent
                        && entity.distanceSquared(it) in minRangeSquared..maxRangeSquared
                        && (it.lastDamageCause as EntityDamageByEntityEvent).damager.isAlive
                        && entity.distanceSquared((it.lastDamageCause as EntityDamageByEntityEvent).damager) in minRangeSquared..maxRangeSquared
            }.map { (it.lastDamageCause as EntityDamageByEntityEvent).damager }
            if (entities.isEmpty())
                entity.memoryStorage.clear(currentMemory)
            else {
                entity.memoryStorage.put(currentMemory, entities.minBy { it.distanceSquared(entity) })
            }
            return
        }
        // One target function = one memory
        val sortEntities = mutableListOf<MutableList<Entity>>()
        for (i in memories.indices)
            sortEntities.add(SortedList(Comparator.comparingDouble { e: Entity -> entity.distanceSquared(e) }))
        for (p in entity.level.entities) {
            if (p.lastDamageCause !is EntityDamageByEntityEvent)
                continue
            if (entity.distanceSquared(p) !in minRangeSquared..maxRangeSquared)
                continue
            val damager = (p.lastDamageCause as EntityDamageByEntityEvent).damager
            if (!damager.isAlive)
                continue
            if (entity.distanceSquared(damager) !in minRangeSquared..maxRangeSquared)
                continue
            for (i in allTargetFunction.indices) {
                if (allTargetFunction[i].apply(p as T)) {
                    // Add to the list
                    sortEntities[i].add(damager)
                }
            }
        }
        for (i in sortEntities.indices) {
            val currentMemory = memories[i]
            val current = entity.memoryStorage.get(currentMemory)
            if (current != null && current.isAlive)
                continue
            // Search for a new target
            if (sortEntities[i].isEmpty())
                entity.memoryStorage.clear(currentMemory)
            else
                entity.memoryStorage.put(currentMemory, sortEntities[i][0])
        }
    }

    override fun getPeriod(): Int = period
}
