package me.oddlyoko.tycoon.mob

import cn.nukkit.entity.Entity
import cn.nukkit.level.Location
import me.oddlyoko.tycoon.Tycoon
import me.oddlyoko.tycoon.item.items.Item
import me.oddlyoko.tycoon.util.CustomBigNumber
import me.oddlyoko.tycoon.zone.Zone
import kotlin.random.Random

data class Mob(
    val id: String,
    val zone: Zone,
    val name: String,
    val type: String,
    val probability: Int,
    val maxAmount: Int,
    val health: CustomBigNumber,
    val exp: Int,
    val drops: List<MobDrop>,
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

    fun chooseItemToDrop(): cn.nukkit.item.Item {
        val items = drops
        if (drops.isEmpty())
            return cn.nukkit.item.Item.AIR_ITEM
        val maxValue = drops.sumOf { it.probability }
        val randomValue = Random.nextInt(maxValue + 1)

        var acc = 0
        var item: MobDrop?

        val remainingItems = items.map { Pair(it.probability, it) }.sortedBy { -it.first }
        val itr = remainingItems.iterator()
        do {
            val pair = itr.next()
            item = pair.second
            acc += pair.first
        } while (randomValue > acc)
        return item?.item?.get() ?: cn.nukkit.item.Item.AIR_ITEM
    }
}

data class MobDrop(
    val item: Item?,
    val probability: Int,
)
