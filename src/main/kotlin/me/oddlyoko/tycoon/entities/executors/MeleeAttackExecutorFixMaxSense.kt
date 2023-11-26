package me.oddlyoko.tycoon.entities.executors

import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityIntelligent
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.memory.MemoryType

/**
 * Custom MeleeAttackExecutor class that fix bug where the entity still attack the target even if it's out of range
 */
class MeleeAttackExecutorFixMaxSense(
    memory: MemoryType<out Entity>?,
    speed: Float,
    maxSenseRange: Int,
    clearDataWhenLose: Boolean,
    coolDown: Int,
) : MeleeAttackExecutor(
    memory,
    speed,
    maxSenseRange,
    clearDataWhenLose,
    coolDown
) {

    override fun execute(entity: EntityIntelligent?): Boolean {
        if (entity!!.behaviorGroup.memoryStorage.isEmpty(memory)) {
            // As we return early and don't call super method, we need to increment the attackTick
            attackTick++
            return false
        }

        val newTarget = entity.behaviorGroup.memoryStorage[memory]
        if (target == null)
            target = newTarget
        if (lookTarget == null) {
            lookTarget = target.clone()
        }

        if (!target.isAlive) {
            attackTick++
            return false
        }
        // Fix: Check if target is still in range
        if (target.distanceSquared(entity) > maxSenseRangeSquared) {
            attackTick++
            return false
        }

        // Now, do the standard behavior
        return super.execute(entity)
    }
}