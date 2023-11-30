package me.oddlyoko.tycoon.entities.entities

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityAngryable
import cn.nukkit.entity.EntityIntelligent
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.FluctuateController
import cn.nukkit.entity.ai.controller.LookController
import cn.nukkit.entity.ai.controller.WalkController
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.ProbabilityEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.LookAtTargetExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.custom.CustomEntity
import cn.nukkit.entity.custom.CustomEntityDefinition
import cn.nukkit.entity.passive.EntityChicken
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import me.oddlyoko.tycoon.entities.MemoryTypes
import me.oddlyoko.tycoon.entities.executors.MeleeAttackExecutorFixMaxSense
import me.oddlyoko.tycoon.entities.sensors.TargetAttackNearestEntitiesSensor
import java.util.function.Function

class EntityGoldChicken(chunk: FullChunk, nbt: CompoundTag?) : EntityIntelligent(chunk, nbt), CustomEntity, EntityWalkable, EntityAngryable {

    override fun getNetworkId(): Int {
        return Entity.NETWORK_ID
    }

    override fun getOriginalName(): String = "Gold Chicken"

    override fun getWidth(): Float {
        return 1.6f
    }

    override fun getHeight(): Float {
        return 1.8f
    }

    override fun initEntity() {
        super.initEntity()
        this.maxHealth = 400
    }

    override fun getDefinition(): CustomEntityDefinition = DEF

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf(
                Behavior(
                    { _: EntityIntelligent? ->
                        val storage = memoryStorage
                        var attackTarget: Entity? = null
                        val attackEvent = storage.get(CoreMemoryTypes.BE_ATTACKED_EVENT)
                        var attackByEntityEvent: EntityDamageByEntityEvent? = null
                        if (attackEvent is EntityDamageByEntityEvent)
                            attackByEntityEvent = attackEvent
                        val validAttacker = attackByEntityEvent != null && attackByEntityEvent.damager.isAlive && (attackByEntityEvent.damager !is Player || (attackByEntityEvent.damager as Player).isSurvival)
                        if (validAttacker) {
                            attackTarget = attackByEntityEvent!!.damager
                            storage.clear(CoreMemoryTypes.BE_ATTACKED_EVENT)
                        } else if (storage.notEmpty(MemoryTypes.NEAR_ENTITY_BE_ATTACKED) && storage.get(MemoryTypes.NEAR_ENTITY_BE_ATTACKED).isAlive) {
                            attackTarget = storage.get(MemoryTypes.NEAR_ENTITY_BE_ATTACKED)
                            storage.clear(MemoryTypes.NEAR_ENTITY_BE_ATTACKED)
                        }
                        storage.put(CoreMemoryTypes.ATTACK_TARGET, attackTarget)
                        false
                    },
                    { _: EntityIntelligent ->
                        this.memoryStorage.isEmpty(CoreMemoryTypes.ATTACK_TARGET)
                    },
                    1
                )
            ),
            setOf(
                // Attack
                Behavior(
                    MeleeAttackExecutorFixMaxSense(CoreMemoryTypes.ATTACK_TARGET, 0.7f, 33, true, 20),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.ATTACK_TARGET),
                    6, 1
                ),
                // Look at nearest player
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1, 1, 100
                ),
                // Random walk
                Behavior(
                    FlatRandomRoamExecutor(0.22f, 12, 100, false, -1, true, 10),
                    { true },
                    1, 1)
            ),
            setOf(
                // Nearest player
                NearestPlayerSensor(8.0, 0.0, 20),
                // Entity nearest who attacked
                TargetAttackNearestEntitiesSensor(
                    0.0,
                    20.0,
                    20,
                    listOf(
                        MemoryTypes.NEAR_ENTITY_BE_ATTACKED,
                    ),
                    arrayOf(
                        Function { entity: Entity -> entity is EntityChicken || entity is EntityGoldChicken },
                    )
                ),
            ),
            setOf(
                // Walk controller
                WalkController(),
                // Look controller
                LookController(true, true),
                // Swim in water
                FluctuateController(),
            ),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this,
        )
    }

    companion object {
        val DEF = CustomEntityDefinition.builder().identifier("tycoon:gold_chicken")
            .summonable(true)
            .spawnEgg(false)
            .build()
    }
}
