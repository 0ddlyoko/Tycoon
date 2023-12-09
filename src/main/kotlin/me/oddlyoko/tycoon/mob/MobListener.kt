package me.oddlyoko.tycoon.mob

import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityHumanType
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageModifier
import cn.nukkit.event.entity.EntityDeathEvent
import cn.nukkit.event.entity.EntitySpawnEvent
import cn.nukkit.inventory.EntityInventoryHolder
import cn.nukkit.item.Item
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.util.CustomBigNumber

object MobListener: Listener {

    private fun getBaseDamageOfEntity(entity: Entity): CustomBigNumber {
        if (entity.namedTag == null || !entity.namedTag.contains("tycoon"))
            return CustomBigNumber(1, 1)
        val baseDamage = entity.namedTag.getInt("tycoon_entity_base_damage")
        val baseMultiplicator = entity.namedTag.getInt("tycoon_entity_base_multiplicator")
        return CustomBigNumber(baseDamage, baseMultiplicator)
    }

    private fun getDamageOfItem(item: Item): CustomBigNumber {
        if (!item.hasCompoundTag() || !item.namedTag.contains("tycoon"))
            return CustomBigNumber(1, 1)
        val itemDamage = item.namedTag.getInt("tycoon_item_damage")
        val itemMultiplicator = item.namedTag.getInt("tycoon_item_multiplicator")
        return CustomBigNumber(itemDamage, itemMultiplicator)
    }

    private fun getHealthOfEntity(entity: Entity): CustomBigNumber {
        if (entity.namedTag == null || !entity.namedTag.contains("tycoon"))
            return CustomBigNumber(1, 1)
        val health = entity.namedTag.getInt("tycoon_entity_health")
        val multiplicator = entity.namedTag.getInt("tycoon_entity_health_multiplicator")
        return CustomBigNumber(health, multiplicator)
    }

    private fun setHealthOfEntity(entity: Entity, newHealth: CustomBigNumber) {
        entity.namedTag.putInt("tycoon_entity_health", newHealth.number)
        entity.namedTag.putInt("tycoon_entity_health_multiplicator", newHealth.multiplicator)
    }

    @EventHandler
    fun onMobHit(e: EntityDamageByEntityEvent) {
        if (e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.isCancelled = true
            return
        }
        val entity = e.entity
        if (!entity.namedTag.getBoolean("tycoon"))
            return
        DamageModifier.entries.forEach { e.setDamage(0f, it) }
        entity.health = 20f

        val damager = e.damager
        // Calculate damage
        var item = if (damager is EntityInventoryHolder) damager.itemInHand else Item.AIR_ITEM
        if (item.id == 0 && damager is EntityHumanType)
            item = damager.inventory.getItemInHand() ?: Item.AIR_ITEM
        val baseDamage = getBaseDamageOfEntity(damager)
        val itemDamage = getDamageOfItem(item)
        val finalDamage = baseDamage.clone().add(itemDamage)

        // Calculate health
        val entityHealth = getHealthOfEntity(entity)
        val newHealth = entityHealth.clone().remove(finalDamage)
        if (newHealth.number == 0) {
            // Entity is dead
            e.damage = 21_07_1998f
            return
        }
        // Save health to entity
        setHealthOfEntity(entity, newHealth)

        // Update the name of the mob
        val mobId = entity.namedTag.getString("tycoon_mob")
        if (mobId.isEmpty())
            return
        entity.nameTag = "${MobManager.getMob(mobId)?.name} ($newHealth ❤)"
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

        e.drops = arrayOf(mob.chooseItemToDrop())

        // Spawn again
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
