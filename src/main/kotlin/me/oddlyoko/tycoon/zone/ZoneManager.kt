package me.oddlyoko.tycoon.zone

import me.oddlyoko.tycoon.Tycoon

object ZoneManager {
    val zones = mutableMapOf<String, Zone>()
    val zoneByWorld = mutableMapOf<Int, List<Zone>>()

    fun registerZone(zone: Zone) {
        zones[zone.id] = zone
    }

    fun getZone(id: String): Zone? {
        return zones[id]
    }

    fun getZones(worldId: Int): List<Zone> {
        return zoneByWorld[worldId] ?: emptyList()
    }

    fun spawnMobs() {
        zones.values.forEach { zone ->
            for (i in 0 ..< zone.maxMobCount) {
                val entity = zone.spawn()
                Tycoon.INSTANCE.logger.info("Spawned entity: $entity")
            }
        }
    }
}
