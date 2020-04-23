package ink.ptms.cronus.internal.task.listener

import ink.ptms.cronus.Cronus
import ink.ptms.cronus.CronusAPI
import ink.ptms.cronus.internal.task.player.*
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerAttack
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDamaged
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDeath
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerKill
import ink.ptms.cronus.internal.task.player.total.TaskPlayerExp
import io.izzel.taboolib.common.event.PlayerJumpEvent
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Servers
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.EntityBlockFormEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.event.vehicle.VehicleMoveEvent

/**
 * @Author 坏黑
 * @Since 2019-05-31 14:50
 */
@TListener
class ListenerPlayer : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerTeleportEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerTeleport::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerFishEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerFish::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerEditBookEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerBook::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerBucketEmptyEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerBucketEmpty::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerBucketFillEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerBucketFill::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: AsyncPlayerChatEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerChat::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerCommandPreprocessEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerCommand::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerShearEntityEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerShear::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerJumpEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerJump::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerExpChangeEvent) {
        CronusAPI.stageHandle(e.player, e, TaskPlayerExp::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PotionSplashEvent) {
        if (e.potion.shooter is Player) {
            CronusAPI.stageHandle(e.potion.shooter as Player, e, TaskPlayerThrowPotion::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: ExpBottleEvent) {
        if (e.entity.shooter is Player) {
            CronusAPI.stageHandle(e.entity.shooter as Player, e, TaskPlayerThrowXpBottle::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityTameEvent) {
        if (e.owner is Player) {
            CronusAPI.stageHandle(e.owner as Player, e, TaskPlayerTame::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityShootBowEvent) {
        if (e.entity is Player) {
            CronusAPI.stageHandle(e.entity as Player, e, TaskPlayerShoot::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: ProjectileLaunchEvent) {
        if (e.entity.shooter is Player) {
            CronusAPI.stageHandle(e.entity.shooter as Player, e, TaskPlayerThrow::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityDamageEvent) {
        if (e.entity is Player) {
            CronusAPI.stageHandle(e.entity as Player, e, TaskPlayerDamaged::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityDeathEvent) {
        if (e.entity.killer is Player) {
            CronusAPI.stageHandle(e.entity.killer, e, TaskPlayerKill::class.java)
        }
        // 宠物击杀
        if (e.entity.killer is Tameable && (e.entity.killer as Tameable).owner is Player) {
            CronusAPI.stageHandle((e.entity.killer as Tameable).owner as Player, e, TaskPlayerKill::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerDeathEvent) {
        CronusAPI.stageHandle(e.entity, e, TaskPlayerDeath::class.java)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityPortalEnterEvent) {
        if (e.entity is Player) {
            CronusAPI.stageHandle(e.entity as Player, e, TaskPlayerPortalEnter::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityPortalExitEvent) {
        if (e.entity is Player) {
            CronusAPI.stageHandle(e.entity as Player, e, TaskPlayerPortalExit::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: VehicleEnterEvent) {
        if (e.entered is Player) {
            CronusAPI.stageHandle(e.entered as Player, e, TaskPlayerVehicleEnter::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: VehicleExitEvent) {
        if (e.exited is Player) {
            CronusAPI.stageHandle(e.exited as Player, e, TaskPlayerVehicleExit::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: InventoryClickEvent) {
        if (e.inventory.type == InventoryType.MERCHANT && e.rawSlot == 2 && !Items.isNull(e.currentItem)) {
            CronusAPI.stageHandle(e.whoClicked as Player, e, TaskPlayerTrade::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerInteractEvent) {
        if (e.action == Action.PHYSICAL && e.clickedBlock?.type?.name?.endsWith("_PLATE") == true) {
            CronusAPI.stageHandle(e.player, e, TaskPlayerPressurePlate::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityBlockFormEvent) {
        if (e.entity is Player && e.block.type == Material.ICE) {
            CronusAPI.stageHandle(e.entity as Player, e, TaskPlayerWalkFrost::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: HorseJumpEvent) {
        for (entity in e.entity.passengers.filter { it is Player }) {
            CronusAPI.stageHandle(entity as Player, e, TaskPlayerJumpHorse::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: EntityDamageByEntityEvent) {
        val attacker = Servers.getLivingAttackerInDamageEvent(e)
        if (attacker is Player) {
            CronusAPI.stageHandle(attacker, e, TaskPlayerAttack::class.java)
        }
        if (e.entity is Player) {
            CronusAPI.stageHandle(e.entity as Player, e, TaskPlayerDamaged::class.java)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerMoveEvent) {
        if (e.from.block != e.to!!.block) {
            // 异步计算
            Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), Runnable { CronusAPI.stageHandle(e.player, e, TaskPlayerWalk::class.java, TaskPlayerSwim::class.java, TaskPlayerRide::class.java, TaskPlayerElytra::class.java, TaskPlayerLeash::class.java) })
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: VehicleMoveEvent) {
        if (e.from.block != e.to.block) {
            for (passenger in e.vehicle.passengers.filter { it is Player }) {
                // 异步计算
                Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), Runnable { CronusAPI.stageHandle(passenger as Player, e, TaskPlayerVehicle::class.java) })
            }
        }
    }
}
