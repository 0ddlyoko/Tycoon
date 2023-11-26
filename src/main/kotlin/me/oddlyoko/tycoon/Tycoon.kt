package me.oddlyoko.tycoon

import cn.nukkit.entity.Entity
import cn.nukkit.entity.provider.CustomClassEntityProvider
import cn.nukkit.level.Location
import cn.nukkit.plugin.PluginBase
import me.oddlyoko.tycoon.config.Config
import me.oddlyoko.tycoon.mob.Mob
import me.oddlyoko.tycoon.mob.MobListener
import me.oddlyoko.tycoon.mob.MobManager
import me.oddlyoko.tycoon.entities.entities.EntityGoldChicken
import me.oddlyoko.tycoon.mob.types.ChickenMobType
import me.oddlyoko.tycoon.mob.types.GoldChickenMobType
import me.oddlyoko.tycoon.util.CustomBigNumber
import me.oddlyoko.tycoon.zone.Zone
import me.oddlyoko.tycoon.zone.ZoneListener
import me.oddlyoko.tycoon.zone.ZoneManager
import java.util.*

class Tycoon: PluginBase() {

    override fun onEnable() {
        logger.info("Tycoon loading...")
        INSTANCE = this
        logger.info("Loading configuration")
        Config.loadConfig(dataFolder)
        logger.info("Configuration loaded")
        loadRegions()
        logger.info("${ZoneManager.zones.size} regions loaded")
        logger.info("Loading mob entities")
        loadCustomMobEntities()
        logger.info("Loading mob types")
        loadMobTypes()
        logger.info("${MobManager.mobTypes.size} mob types loaded")
        logger.info("Loading mobs")
        loadMobs()
        logger.info("${MobManager.mobs.size} mobs loaded")
        loadListeners()
        logger.info("Spawning mobs")
        ZoneManager.spawnMobs()
        logger.info("Tycoon enabled")
    }

    private fun loadRegions() {
        Config.worldsConfig.worlds.forEach { world ->
            val level = server.getLevelByName(world.id)
            if (level == null) {
                logger.error("World ${world.id} not found")
                server.shutdown()
                return
            }
            world.zones.forEach { zone ->
                ZoneManager.registerZone(Zone(
                    id = zone.id,
                    name = zone.name,
                    maxMobCount = zone.maxMobCount,
                    startPos = Location(zone.start.x.toDouble(), zone.start.y.toDouble(), zone.start.z.toDouble(), level),
                    endPos = Location(zone.end.x.toDouble(), zone.end.y.toDouble(), zone.end.z.toDouble(), level),
                ))
            }
        }
    }

    private fun loadCustomMobEntities() {
        val a = CustomClassEntityProvider(EntityGoldChicken::class.java)
        Entity.registerCustomEntity(a)
    }

    private fun loadMobTypes() {
        MobManager.registerMobType("chicken", ChickenMobType)
        MobManager.registerMobType("gold_chicken", GoldChickenMobType)
    }

    private fun loadMobs() {
        Config.mobsConfig.mobs.forEach { mobConfig ->
            val zone = ZoneManager.getZone(mobConfig.zone)
            if (zone == null) {
                logger.error("Zone ${mobConfig.zone} not found")
                server.shutdown()
                return
            }
            val type = MobManager.getMobType(mobConfig.type)
            val health = CustomBigNumber.fromString(mobConfig.health)
            if (health == null) {
                logger.error("Invalid health for mob ${mobConfig.id}: ${mobConfig.health}")
                server.shutdown()
                return
            }
            val mob = Mob(
                id = mobConfig.id,
                zone = zone,
                name = mobConfig.name,
                type = type!!,
                probability = mobConfig.probability,
                maxAmount = mobConfig.maxAmount,
                health = health,
            )
            MobManager.registerMob(mob)
        }
    }

    private fun loadListeners() {
        server.pluginManager.registerEvents(ZoneListener, this)
        server.pluginManager.registerEvents(MobListener, this)
    }

    override fun onDisable() {
        logger.info("Tycoon disabled")
    }

    companion object {
        lateinit var INSTANCE: Tycoon
            private set

        val MOB_UUID = UUID.randomUUID().toString()
    }
}
