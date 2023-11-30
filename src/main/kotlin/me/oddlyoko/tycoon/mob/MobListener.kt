package me.oddlyoko.tycoon.mob

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDeathEvent
import cn.nukkit.event.entity.EntitySpawnEvent
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.util.CustomBigNumber

object MobListener: Listener {

    @EventHandler
    fun onMobHit(e: EntityDamageByEntityEvent) {
        if (e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.isCancelled = true
            return
        }
        val entity = e.entity
        if (!entity.namedTag.getBoolean("tycoon"))
            return
        val damager = e.damager
        // TODO Calculate the amount of damage
        val damage = CustomBigNumber(100, 1)
        val multiplicator = entity.namedTag.getInt("tycoon_health_multiplicator")
        if (multiplicator == 0)
            return
        // TODO Care if the damage is too high, we could have an overflow
        damage.convertTo(multiplicator)
        e.damage = damage.number.toFloat()
        var health = entity.health - e.finalDamage
        if (health <= 0) {
            health = 0F
        }
        val newHealth = CustomBigNumber(health.toInt(), multiplicator).verify()
        // If the multiplicator is different, we need to update the current multiplicator and modify the amount of damage.
        // As we don't have an event if the health is modified, we need to modify the damage here.
        // We can modify the damage by multiplying by 1_000 * the difference between the old and the new multiplicator.
        // We also need to update the current health
        if (newHealth.multiplicator != multiplicator) {
            entity.namedTag.putInt("tycoon_health_multiplicator", newHealth.multiplicator)
            val differenceMultiplier = newHealth.multiplicator - multiplicator
            EntityDamageEvent.DamageModifier.entries.forEach {
                val damageModifier = e.getDamage(it)
                if (damageModifier.toInt() == 0)
                    return@forEach
                e.setDamage(damageModifier * differenceMultiplier * 1000, it)
            }
            entity.health *= 1000 * differenceMultiplier
        }

        // Update the name of the mob
        val mobId = entity.namedTag.getString("tycoon_mob")
        if (mobId.isEmpty())
            return
        entity.nameTag = "${MobManager.getMob(mobId)?.type?.name()} ($newHealth ❤)"
    }

    @EventHandler
    fun onMobDie(e: EntityDeathEvent) {
        val entity = e.entity
        if (!entity.namedTag.getBoolean("tycoon"))
            return
        // TODO Drop stuff
        val mobId = entity.namedTag.getString("tycoon_mob")
        if (mobId.isEmpty())
            return
        val mob = MobManager.getMob(mobId) ?: return
        val zone = mob.zone
        mob.currentMobCount--
        zone.currentMobCount--
        zone.spawn()
    }

    @EventHandler
    fun onMobSpawn(e: EntitySpawnEvent) {
        // Cancel event if entity is one of the old server
        val entity = e.entity
        if (!entity.namedTag.getBoolean("tycoon"))
            return
        val mobUuid = entity.namedTag.getString("tycoon_uuid")
        if (mobUuid != Tycoon.RANDOM_UUID) {
            // Mob from a different server instance
            // We need to remove it
            e.isCancelled = true
        }
    }
}
